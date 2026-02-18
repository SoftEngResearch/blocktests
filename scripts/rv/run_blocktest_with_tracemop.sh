#!/bin/bash
#
# Given a project, search for blocks that contain RV events
# Usage: bash run_blocktest_with_tracemop.sh -f <blocktest-file> -u <block-url-file> -h <block-hash> <extension_dir> <output_dir>
#
SCRIPT_DIR=$( cd $( dirname $0 ) && pwd )

source ${SCRIPT_DIR}/../constants.sh

while getopts :f:u:h: opts; do
	case "${opts}" in
		f ) BLOCKTEST_FILE="${OPTARG}" ;;
		u ) BLOCK_URL_FILE="${OPTARG}" ;;
		h ) BLOCK_HASH="${OPTARG}" ;;
	esac
done
shift $((${OPTIND} - 1))

EXTENSION_DIR=$1
OUTPUT_DIR=$2

export RVMLOGGINGLEVEL=UNIQUE
export COLLECT_TRACES=1 # extension will add -Xmx500g -XX:-UseGCOverheadLimit
export COLLECT_MONITORS=1 # TraceMOP will collect monitor
export TRACEDB_PATH=${OUTPUT_DIR}/traces/all-traces # Store traces in this directory
export TRACEDB_RANDOM=1 # Directory name should end with random string, to prevent duplicated DB
export MAVEN_OPTS="-Xmx500g -XX:-UseGCOverheadLimit"
export TRACEDB_CONFIG_PATH="${SCRIPT_DIR}/.trace-db.config"
echo -e "db=memory\ndumpDB=false" > ${TRACEDB_CONFIG_PATH}

check_status_code() {
	local error_code=$?
	if [[ ${error_code} -ne 0 ]]; then
		echo "Step $1 failed"
		echo "$1,${error_code}" > "${OUTPUT_DIR}/logs/status.log"
		exit 1
	fi
}

check_inputs() {
	if [[ ! -f ${BLOCKTEST_FILE} || ! -f ${BLOCK_URL_FILE} || ! -d ${EXTENSION_DIR} || -z ${OUTPUT_DIR} ]]; then
		echo "Usage: bash run_blocktest_with_tracemop.sh -f <blocktest-file> -u <block-url> -h <block-hash> <extension_dir> <output_dir>"
		exit 1
	fi
	
	if [[ -z $(grep ${BLOCK_HASH} ${BLOCK_URL_FILE}) || $(grep ${BLOCK_HASH} ${BLOCK_URL_FILE} | wc -l) -gt 1 ]]; then
		echo "Error: ${BLOCK_HASH} not in ${BLOCK_URL_FILE}"
		exit 1
	fi
	
	if [[ ! ${BLOCKTEST_FILE} =~ ^/.* ]]; then
		BLOCKTEST_FILE=${SCRIPT_DIR}/${BLOCKTEST_FILE}
	fi
	
	
	if [[ ! ${BLOCK_URL_FILE} =~ ^/.* ]]; then
		BLOCK_URL_FILE=${SCRIPT_DIR}/${BLOCK_URL_FILE}
	fi
	
	BLOCK_URL=$(grep ${BLOCK_HASH} ${BLOCK_URL_FILE} | cut -d ',' -f 1)
	REPO=$(echo ${BLOCK_URL} | cut -d '/' -f 4-5)
	SHA=$(echo ${BLOCK_URL} | cut -d '/' -f 7)
	PROJECT_NAME=$(echo ${BLOCK_URL} | cut -d '/' -f 4-5 | tr / -)
	
	if [[ ! ${EXTENSION_DIR} =~ ^/.* ]]; then
		EXTENSION_DIR=${SCRIPT_DIR}/${EXTENSION_DIR}
	fi
	
	if [[ ! ${OUTPUT_DIR} =~ ^/.* ]]; then
		OUTPUT_DIR=${SCRIPT_DIR}/${OUTPUT_DIR}
	fi
	
	mkdir -p ${OUTPUT_DIR}/logs
	mkdir -p ${OUTPUT_DIR}/traces

	
	if [[ ! -f ${SCRIPT_DIR}/resources/track-agent.jar ]]; then
		echo "TraceMOP agent is missing"
		exit 1
	fi
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
	check_status_code "clone_project checkout"
	
	bash ${SCRIPT_DIR}/../treat_special.sh ${PROJECT_NAME} ${SHA}
	popd &> /dev/null
	popd &> /dev/null
}

generate_test() {
	echo "Copy file to project then generate tests..."
	if [[ ! -d ${OUTPUT_DIR}/project/src/test && -z $(head -n 2 ${BLOCKTEST_FILE} | grep "TEST_DIR") ]]; then
		echo "Unable to find test directory"
		exit 1
	fi
	
	local relative_path=$(echo ${BLOCK_URL} | cut -d '/' -f 8- | cut -d '#' -f 1)
	TEST_FILENAME="$(basename ${relative_path} | cut -d '.' -f 1)BlockTest"
	pushd ${OUTPUT_DIR}/project &> /dev/null
	cp ${BLOCKTEST_FILE} ${relative_path}
	local absolute_path=$(realpath ${relative_path})
	popd &> /dev/null
	
	if [[ -z $(head -n 2 ${BLOCKTEST_FILE} | grep "TEST_DIR") ]]; then
		fullpath_dir=$(dirname $(echo "${absolute_path}" | sed 's/\/main\//\/test\//'))
	else
		fullpath_dir=${OUTPUT_DIR}/project/$(head -n 2 ${BLOCKTEST_FILE} | grep "TEST_DIR" | cut -d ':' -f 2 | cut -d ' ' -f 2)/
	fi
	echo "Generating test"
	
	local framework=""
	if [[ -n $(head -n 2 ${BLOCKTEST_FILE} | grep "FRAMEWORK: junit5") ]]; then
		framework=" --junit_version=junit5"
	elif [[ -n $(head -n 2 ${BLOCKTEST_FILE} | grep "FRAMEWORK: testng") ]]; then
		framework=" --junit_version=testng"
	fi
	
	pushd ${SCRIPT_DIR}/../../blocktest &> /dev/null
	mvn -Dmaven.repo.local=${OUTPUT_DIR}/repo exec:java -Dexec.mainClass="org.blocktest.BlockTestRunnerSourceCode" -Dexec.args="--input_file=${absolute_path} --output_dir=${fullpath_dir}${framework}" &>> ${OUTPUT_DIR}/logs/gen.log
	check_status_code "test_generation"
	popd &> /dev/null
	
	if [[ ! -f ${fullpath_dir}/${TEST_FILENAME}.java ]]; then
		echo "No test file is generated"
		exit 1
	fi
	
	if [[ -z $(grep "@Test" ${fullpath_dir}/${TEST_FILENAME}.java) ]]; then
		echo "No test case is generated"
		exit 1
	fi
}

run_rv_with_tracemop() {
	echo "Running RV with tracemop..."
	local tmp_dir="${TMP_DIR}/${PROJECT_NAME}"
	local extension_jar="${EXTENSION_DIR}/target/blocktest-extension-1.0.jar"
	export ADD_AGENT=1
	export MOP_AGENT_PATH="-javaagent:${SCRIPT_DIR}/resources/track-agent.jar"
	rm -rf ${tmp_dir} && mkdir ${tmp_dir}
	
	if [[ -z ${TEST_FILENAME} ]]; then
		echo "Unable to find test file name"
		exit 1
	fi
	
	pushd ${OUTPUT_DIR}/project &> /dev/null
	(time mvn ${SKIP} -Djava.io.tmpdir=${tmp_dir} -Dmaven.repo.local=${OUTPUT_DIR}/repo -Dmaven.ext.class.path=${extension_jar} test -Dtest="${TEST_FILENAME}") &> ${OUTPUT_DIR}/logs/run.log
	check_status_code "run_rv_with_tracemop"
	popd &> /dev/null
	
	unset ADD_AGENT
	unset MOP_AGENT_PATH
}


echo "BLOCKTEST version: ($(git rev-parse HEAD) - $(date +%s))"
check_inputs
clone_project
install
generate_test
run_rv_with_tracemop
