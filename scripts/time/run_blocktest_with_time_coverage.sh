#!/bin/bash
#
# Given a project, run block test and measure time
# Usage: bash run_blocktest_with_time_coverage.sh <block-url> <extension_dir> <output_dir>
#
SCRIPT_DIR=$( cd $( dirname $0 ) && pwd )

source ${SCRIPT_DIR}/../constants.sh

BLOCK_URL=$1
EXTENSION_DIR=$2
OUTPUT_DIR=$3

FILENAME=$(echo ${BLOCK_URL} | rev | cut -d '/' -f 1 | rev | cut -d '#' -f 1)
REPO=$(echo ${BLOCK_URL} | cut -d '/' -f 4-5)
SHA=$(echo ${BLOCK_URL} | cut -d '/' -f 7)
PROJECT_NAME=$(echo ${BLOCK_URL} | cut -d '/' -f 4-5 | tr / -)
BLOCKTEST_FILE="${SCRIPT_DIR}/../../data/test/${PROJECT_NAME}/${FILENAME}"

echo "====="
echo "FILENAME: ${FILENAME}"
echo "REPO: ${REPO}"
echo "SHA: ${SHA}"
echo "PROJECT_NAME: ${PROJECT_NAME}"
echo "BLOCKTEST_FILE: ${BLOCKTEST_FILE}"
echo "====="


check_status_code() {
	local error_code=$?
	if [[ ${error_code} -ne 0 ]]; then
		echo "Step $1 failed"
		echo "$1,${error_code}" > "${OUTPUT_DIR}/logs/status.log"
		exit 1
	fi
}

check_inputs() {
	if [[ ! -f ${BLOCKTEST_FILE} || ! -d ${EXTENSION_DIR} || -z ${OUTPUT_DIR} ]]; then
		echo "Usage: bash run_blocktest_with_time_coverage.sh <block-url> <extension_dir> <output_dir>"
		exit 1
	fi
	
	if [[ ! ${EXTENSION_DIR} =~ ^/.* ]]; then
		EXTENSION_DIR=${SCRIPT_DIR}/${EXTENSION_DIR}
	fi
	
	if [[ ! ${OUTPUT_DIR} =~ ^/.* ]]; then
		OUTPUT_DIR=${SCRIPT_DIR}/${OUTPUT_DIR}
	fi
	
	mkdir -p ${OUTPUT_DIR}/logs
}

install() {
	if [[ ! -f ${EXTENSION_DIR}/target/blocktest-extension-1.0.jar ]]; then
		echo "Extension is missing... Building extension..."
		pushd ${EXTENSION_DIR} &> /dev/null
		mvn -Dmaven.repo.local=${OUTPUT_DIR}/repo package &>> ${OUTPUT_DIR}/logs/setup.log
		check_status_code "install extension"
		popd &> /dev/null
	fi
	
	echo "Setting up BlockTest"
	pushd ${SCRIPT_DIR}/../../blocktest &> /dev/null
	mvn -Dmaven.repo.local=${OUTPUT_DIR}/repo install &>> ${OUTPUT_DIR}/logs/setup.log
	popd &> /dev/null
}

clone_project() {
	echo "Cloning project"
	pushd ${OUTPUT_DIR} &> /dev/null
	git clone https://github.com/${REPO} project &>> ${OUTPUT_DIR}/logs/setup.log
	check_status_code "clone_project clone"
	pushd project &> /dev/null
	git checkout ${SHA} &>> ${OUTPUT_DIR}/logs/setup.log
	if [[ $? -ne 0 ]]; then
		echo "Unable to checkout, try fetching again..."
		git fetch origin ${SHA} &>> ${OUTPUT_DIR}/logs/setup.log
		git checkout ${SHA} &>> ${OUTPUT_DIR}/logs/setup.log
		check_status_code "clone_project checkout"
	fi
	
	bash ${SCRIPT_DIR}/../treat_special.sh ${PROJECT_NAME} ${SHA} true
	popd &> /dev/null
	popd &> /dev/null
}

generate_test() {
	echo "Copy file to project then generate tests..."
	if [[ ! -d ${OUTPUT_DIR}/project/src/test && -z $(head -n 3 ${BLOCKTEST_FILE} | grep "TEST_DIR") ]]; then
		echo "Unable to find test directory"
		exit 1
	fi
	
	local relative_path=$(echo ${BLOCK_URL} | cut -d '/' -f 8- | cut -d '#' -f 1)
	TEST_FILENAME="$(basename ${relative_path} | cut -d '.' -f 1)BlockTest"
	pushd ${OUTPUT_DIR}/project &> /dev/null
	cp ${BLOCKTEST_FILE} ${relative_path}
	local absolute_path=$(realpath ${relative_path})
	
	if [[ ! -f ${OUTPUT_DIR}/deps.txt ]]; then
		# Find classpath
		mvn dependency:build-classpath -Dmdep.outputFile=deps.txt
		echo "$(cat deps.txt):$(pwd)/target/classes" > deps2.txt
		mv deps2.txt ${OUTPUT_DIR}/deps.txt
	fi
	popd &> /dev/null
	
	if [[ -z $(head -n 3 ${BLOCKTEST_FILE} | grep "TEST_DIR") ]]; then
		fullpath_dir=$(dirname $(echo "${absolute_path}" | sed 's/\/main\//\/test\//'))
	else
		fullpath_dir=${OUTPUT_DIR}/project/$(head -n 3 ${BLOCKTEST_FILE} | grep "TEST_DIR" | cut -d ':' -f 2 | cut -d ' ' -f 2)/
	fi
	echo "Generating test"
	
	local framework=""
	if [[ -n $(head -n 3 ${BLOCKTEST_FILE} | grep "FRAMEWORK: junit5") ]]; then
		framework=" --junit_version=junit5"
		echo "JUnit 5"
	elif [[ -n $(head -n 3 ${BLOCKTEST_FILE} | grep "FRAMEWORK: testng") ]]; then
		framework=" --junit_version=testng"
		echo "TestNG"
	elif [[ -n $(head -n 3 ${BLOCKTEST_FILE} | grep "FRAMEWORK: junit4") ]]; then
		echo "Adding JUnit"
		export ADD_JUNIT=1
	fi
	
	local deps_file=${OUTPUT_DIR}/deps.txt
	local app_src=${OUTPUT_DIR}/project/src/main/java
	if [[ ! -d ${app_src} ]]; then
		app_src=${OUTPUT_DIR}/project/$(echo ${BLOCK_URL} | cut -d '/' -f 8)
		echo "Changing app_src to ${app_src}"
	fi
	
	pushd ${SCRIPT_DIR}/../../blocktest &> /dev/null
	local start=$(date +%s%3N)
	# Actually generate tests
	(time mvn -Dmaven.repo.local=${OUTPUT_DIR}/repo exec:java -Dexec.mainClass="org.blocktest.BlockTestRunnerSourceCode" -Dexec.args="--input_file=${absolute_path} --output_dir=${fullpath_dir}${framework} --coverage=true --dep_file_path=${deps_file} --app_src_path=${app_src}") &>> ${OUTPUT_DIR}/logs/gen-timed.log
	check_status_code "test_generation_timed"
	local end=$(date +%s%3N)
	DURATION=$((end - start))
	popd &> /dev/null

	if [[ ! -f ${fullpath_dir}/${TEST_FILENAME}.java ]]; then
		echo "No test file is generated"
		exit 1
	fi
	
	if [[ -z $(grep "@Test" ${fullpath_dir}/${TEST_FILENAME}.java) ]]; then
		echo "No test case is generated"
		exit 1
	fi
	
	echo ">>> Time to generate tests: ${DURATION}"
	cp ${fullpath_dir}/${TEST_FILENAME}.java ${OUTPUT_DIR}/logs/${TEST_FILENAME}.java
}

run_blocktest() {
	echo "Running blocktest..."
	local tmp_dir="${TMP_DIR}/${PROJECT_NAME}"
	local extension_jar="${EXTENSION_DIR}/target/blocktest-extension-1.0.jar"
	rm -rf ${tmp_dir} && mkdir ${tmp_dir}
	
	if [[ -z ${TEST_FILENAME} ]]; then
		echo "Unable to find test file name"
		exit 1
	fi
	
	if [[ -n $(head -n 3 ${BLOCKTEST_FILE} | grep "WORKING_DIR") ]]; then
		pushd ${OUTPUT_DIR}/project/$(head -n 3 ${BLOCKTEST_FILE} | grep "WORKING_DIR" | cut -d ':' -f 2 | cut -d ' ' -f 2) &> /dev/null
	else
		pushd ${OUTPUT_DIR}/project &> /dev/null
	fi

	export ADD_JACOCO=1
	echo "Adding JaCoCo..."
	# Actually running tests
	local start=$(date +%s%3N)
	(time mvn ${SKIP_WITH_JACOCO} -Djava.io.tmpdir=${tmp_dir} -Dmaven.repo.local=${OUTPUT_DIR}/repo -Dmaven.ext.class.path=${extension_jar} test -Dtest="${TEST_FILENAME}") &> ${OUTPUT_DIR}/logs/run-timed.log
	check_status_code "run_blocktest_timed"
	local end=$(date +%s%3N)
	DURATION=$((end - start))

	if [[ -d target/site ]]; then
		cp -r target/site ${OUTPUT_DIR}/site
	fi
	
	echo ">>> Time to run tests: ${DURATION}"
	popd &> /dev/null
	
	unset ADD_AGENT
	unset MOP_AGENT_PATH
}


echo "BLOCKTEST version: ($(git rev-parse HEAD) - $(date +%s))"
check_inputs
clone_project
install
generate_test
run_blocktest
