#!/bin/bash
#
# Given a project, run projects with (modified) and without (original) block tests and measure time
# Usage: bash run_test_with_and_without_blocktests.sh <repo> <extension_dir> <output_dir>
#
SCRIPT_DIR=$( cd $( dirname $0 ) && pwd )

source ${SCRIPT_DIR}/../constants.sh

REPO=$1
EXTENSION_DIR=$2
OUTPUT_DIR=$3

PROJECT_NAME=$(echo ${REPO} | tr / -)

echo "====="
echo "REPO: ${REPO}"
echo "PROJECT_NAME: ${PROJECT_NAME}"
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
	if [[ ! -d ${EXTENSION_DIR} || -z ${OUTPUT_DIR} ]]; then
		echo "Usage: bash run_blocktest_with_time.sh <repo> <extension_dir> <output_dir>"
		exit 1
	fi
	
	if [[ ! ${EXTENSION_DIR} =~ ^/.* ]]; then
		EXTENSION_DIR=${SCRIPT_DIR}/${EXTENSION_DIR}
	fi
	
	if [[ ! ${OUTPUT_DIR} =~ ^/.* ]]; then
		OUTPUT_DIR=${SCRIPT_DIR}/${OUTPUT_DIR}
	fi
	
	if [[ ! -d ${SCRIPT_DIR}/../../paper/data/evaluations/blocks/${PROJECT_NAME}/ || -z $(ls ${SCRIPT_DIR}/../../paper/data/evaluations/blocks/${PROJECT_NAME}/) ]]; then
		echo "Cannot find any block test for ${PROJECT_NAME}"
		exit 1
	fi
	
	mkdir -p ${OUTPUT_DIR}/logs
	
	# Determine the SHA
	local error=false
	for filename in $(ls ${SCRIPT_DIR}/../../paper/data/evaluations/blocks/${PROJECT_NAME}/); do
		filename="${SCRIPT_DIR}/../../paper/data/evaluations/blocks/${PROJECT_NAME}/${filename}"
		if [[ -n $(grep "// BLOCKTEST EVAL: " ${filename} | head -n 1) ]]; then
			if [[ -n ${SHA} && ${SHA} != $(grep "// BLOCKTEST EVAL: " ${filename} | head -n 1 | xargs | cut -d ' ' -f 4 | cut -d '/' -f 7) ]]; then
				echo "Multiple SHAs in ${PROJECT_NAME}"
				error=true
			fi

			SHA=$(grep "// BLOCKTEST EVAL: " ${filename} | head -n 1 | xargs | cut -d ' ' -f 4 | cut -d '/' -f 7)
		else
			echo "Cannot find any BLOCKTEST EVAL in ${PROJECT_NAME}"
			error=true
		fi
	done

	if [[ ${error} == true ]]; then
		exit 1
	fi

}

install() {
	if [[ ! -f ${EXTENSION_DIR}/target/blocktest-extension-1.0.jar ]]; then
		echo "Extension is missing... Building extension..."
		pushd ${EXTENSION_DIR} &> /dev/null
		timeout ${TIMEOUT} mvn -Dmaven.repo.local=${OUTPUT_DIR}/repo package &>> ${OUTPUT_DIR}/logs/setup.log
		check_status_code "install extension"
		popd &> /dev/null
	fi
	
	echo "Setting up BlockTest"
	pushd ${SCRIPT_DIR}/../../blocktest &> /dev/null
	timeout ${TIMEOUT} mvn -Dmaven.repo.local=${OUTPUT_DIR}/repo install &>> ${OUTPUT_DIR}/logs/setup.log
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
		echo "Unable to checkout, try fetching agian..."
		git fetch origin ${SHA} &>> ${OUTPUT_DIR}/logs/setup.log
		git checkout ${SHA} &>> ${OUTPUT_DIR}/logs/setup.log
		check_status_code "clone_project checkout"
	fi

	bash ${SCRIPT_DIR}/../treat_special.sh ${PROJECT_NAME} ${SHA}
	popd &> /dev/null
	popd &> /dev/null
}

run_test_without_blocktests() {
	echo "Running test without block tests"
	
	pushd ${OUTPUT_DIR}/project &> /dev/null
	
	local tmp_dir="${TMP_DIR}/${PROJECT_NAME}"
	local extension_jar="${EXTENSION_DIR}/target/blocktest-extension-1.0.jar"
	rm -rf ${tmp_dir} && mkdir ${tmp_dir}
	
	# Dry run
	(time timeout ${TIMEOUT} mvn ${SKIP} -Djava.io.tmpdir=${tmp_dir} -Dmaven.repo.local=${OUTPUT_DIR}/repo -Dmaven.ext.class.path=${extension_jar} clean test-compile surefire:test) &> ${OUTPUT_DIR}/logs/run-original.log
	check_status_code "run_test_without_blocktests_dry"
	
	rm -rf target/surefire-reports ${tmp_dir} && mkdir ${tmp_dir}
	
	# Actually running tests
	local start=$(date +%s%3N)
	(time timeout ${TIMEOUT} mvn ${SKIP} -Djava.io.tmpdir=${tmp_dir} -Dmaven.repo.local=${OUTPUT_DIR}/repo -Dmaven.ext.class.path=${extension_jar} test-compile surefire:test) &> ${OUTPUT_DIR}/logs/run-original-timed.log
	check_status_code "run_test_without_blocktests"
	local end=$(date +%s%3N)
	DURATION=$((end - start))
	
	if [[ -d target/surefire-reports ]]; then
		cp -r target/surefire-reports ${OUTPUT_DIR}/reports-original
	fi
	
	echo ">>> Time to run tests without block tests: ${DURATION}"
	
	popd &> /dev/null
}

copy_blocktests_to_project() {
	echo "Copy files to project..."
	pushd ${OUTPUT_DIR}/project &> /dev/null
	
	for filename in $(ls ${SCRIPT_DIR}/../../paper/data/evaluations/blocks/${PROJECT_NAME}/); do
		blocktest_file="${SCRIPT_DIR}/../../paper/data/evaluations/blocks/${PROJECT_NAME}/${filename}"
		if [[ -n $(grep "// BLOCKTEST EVAL: " ${blocktest_file} | head -n 1) ]]; then
			local url=$(grep "// BLOCKTEST EVAL: " ${blocktest_file} | head -n 1 | xargs | cut -d ' ' -f 4)
			local relative_path=$(echo ${url} | cut -d '/' -f 8- | cut -d '#' -f 1)
			echo "URL is ${url}, relative_path is ${relative_path}, blocktest_file is ${blocktest_file}"
			cp ${blocktest_file} ${relative_path}
			check_status_code "copy_blocktests_to_project"
		else
			echo "Step copy_blocktests_to_project failed: no block test in file ${filename}"
			echo "copy_blocktests_to_project,-1" > "${OUTPUT_DIR}/logs/status.log"
		fi
	done
	popd &> /dev/null
}

run_test_with_blocktests() {
	echo "Running test without block tests"
	
	pushd ${OUTPUT_DIR}/project &> /dev/null
	
	local tmp_dir="${TMP_DIR}/${PROJECT_NAME}"
	local extension_jar="${EXTENSION_DIR}/target/blocktest-extension-1.0.jar"
	rm -rf ${tmp_dir} && mkdir ${tmp_dir}
	
	# Dry run
	(time timeout ${TIMEOUT} mvn ${SKIP} -Djava.io.tmpdir=${tmp_dir} -Dmaven.repo.local=${OUTPUT_DIR}/repo -Dmaven.ext.class.path=${extension_jar} test-compile) &> ${OUTPUT_DIR}/logs/run-modified.log
	check_status_code "run_test_with_blocktests_dry"
	
	rm -rf target/surefire-reports ${tmp_dir} && mkdir ${tmp_dir}
	
	# Actually running tests
	local start=$(date +%s%3N)
	(time timeout ${TIMEOUT} mvn ${SKIP} -Djava.io.tmpdir=${tmp_dir} -Dmaven.repo.local=${OUTPUT_DIR}/repo -Dmaven.ext.class.path=${extension_jar} test-compile surefire:test) &> ${OUTPUT_DIR}/logs/run-modified-timed.log
	check_status_code "run_test_with_blocktests"
	local end=$(date +%s%3N)
	DURATION=$((end - start))
	
	if [[ -d target/surefire-reports ]]; then
		cp -r target/surefire-reports ${OUTPUT_DIR}/reports-modified
	fi
	
	echo ">>> Time to run tests with block tests: ${DURATION}"
	
	popd &> /dev/null
}


echo "BLOCKTEST version: ($(git rev-parse HEAD) - $(date +%s))"
check_inputs
clone_project
install
run_test_without_blocktests
copy_blocktests_to_project
run_test_with_blocktests
