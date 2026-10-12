#!/bin/bash
#
# Run the property block test on toy-app-propblocktest.
# Same steps as scripts/time/run_blocktest_with_time.sh:
# 1. install the property block test,
# 2. generate from inside the blocktest directory
# 3. run the generated test.
# Usage: bash run_propblocktest.sh
#
set -e

SCRIPT_DIR=$( cd $( dirname $0 ) && pwd )
REPO_ROOT=$( cd ${SCRIPT_DIR}/.. && pwd )

OUTPUT_DIR=${SCRIPT_DIR}/output/propblocktest
BLOCKTEST_DIR=${REPO_ROOT}/blocktest
EXTENSION_DIR=${REPO_ROOT}/extension
TOY_APP=${SCRIPT_DIR}/toy-app-propblocktest
TEST_FILENAME=ReverseListPropBlockTest

mkdir -p ${OUTPUT_DIR}/logs

install() {
	if [[ ! -f ${EXTENSION_DIR}/target/blocktest-extension-1.0.jar ]]; then
		echo "Extension is missing... Building extension..."
		pushd ${EXTENSION_DIR} &> /dev/null
		mvn package >> ${OUTPUT_DIR}/logs/setup.log 2>&1
		popd &> /dev/null
	fi

	echo "Setting up property block test"
	pushd ${BLOCKTEST_DIR} &> /dev/null
	mvn install -DskipTests >> ${OUTPUT_DIR}/logs/setup.log 2>&1
	popd &> /dev/null
}

generate_test() {
	echo "Generating test"
	pushd ${TOY_APP} &> /dev/null
	mvn compile >> ${OUTPUT_DIR}/logs/setup.log 2>&1

	mvn dependency:build-classpath -Dmdep.outputFile=${OUTPUT_DIR}/deps.txt >> ${OUTPUT_DIR}/logs/setup.log 2>&1
	echo "$(cat ${OUTPUT_DIR}/deps.txt):$(pwd)/target/classes" > ${OUTPUT_DIR}/deps2.txt
	mv ${OUTPUT_DIR}/deps2.txt ${OUTPUT_DIR}/deps.txt

	local absolute_path=$(realpath src/main/java/toy/ReverseList.java)
	popd &> /dev/null

	local fullpath_dir=$(dirname $(echo "${absolute_path}" | sed 's/\/main\//\/test\//'))
	local deps_file=${OUTPUT_DIR}/deps.txt
	local app_src=${TOY_APP}/src/main/java

	pushd ${BLOCKTEST_DIR} &> /dev/null
	# jqwik is the framework the current propblocktest skeleton emits (@Example).
	local framework="--test_type=propblocktest"

	# Dry run
	(time mvn exec:java -Dexec.mainClass="org.blocktest.BlockTestRunnerSourceCode" -Dexec.args="--input_file=${absolute_path} ${framework} --dep_file_path=${deps_file} --app_src_path=${app_src}") >> ${OUTPUT_DIR}/logs/gen.log 2>&1

	local start=$(date +%s)
	# Actually generate tests
	(time mvn exec:java -Dexec.mainClass="org.blocktest.BlockTestRunnerSourceCode" -Dexec.args="--input_file=${absolute_path} --output_dir=${fullpath_dir} ${framework} --dep_file_path=${deps_file} --app_src_path=${app_src}") >> ${OUTPUT_DIR}/logs/gen-timed.log 2>&1
	local end=$(date +%s)
	DURATION=$((end - start))
	popd &> /dev/null

	if [[ ! -f ${fullpath_dir}/${TEST_FILENAME}.java ]]; then
		echo "No test file is generated"
		exit 1
	fi

	if [[ -z $(grep -E "@Test|@Example|@Property" ${fullpath_dir}/${TEST_FILENAME}.java) ]]; then
		echo "No test case is generated"
		exit 1
	fi

	echo ">>> Time to generate tests: ${DURATION}"
	cp ${fullpath_dir}/*BlockTest*.java ${OUTPUT_DIR}/logs/
}

run_blocktest() {
	echo "Running property block test..."
	local extension_jar="${EXTENSION_DIR}/target/blocktest-extension-1.0.jar"
	local tmp_dir="/tmp/toy-app-propblocktest"

	rm -rf ${tmp_dir} && mkdir ${tmp_dir}
	pushd ${TOY_APP} &> /dev/null

	local start=$(date +%s)
	(time mvn -Djava.io.tmpdir=${tmp_dir} -Dmaven.ext.class.path=${extension_jar} test-compile surefire:test -Dtest=*BlockTest*) &> ${OUTPUT_DIR}/logs/run-timed.log
	local end=$(date +%s)
	DURATION=$((end - start))

	if [[ -d target/surefire-reports ]]; then
		rm -rf ${OUTPUT_DIR}/reports
		cp -r target/surefire-reports ${OUTPUT_DIR}/reports
	fi

	echo ">>> Time to run tests: ${DURATION}"
	popd &> /dev/null
}

install
generate_test
run_blocktest
