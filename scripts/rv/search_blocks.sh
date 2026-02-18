#!/bin/bash
#
# Given a project, search for blocks that contain RV events
# Usage: bash search_blocks.sh -r <repo> -s <sha> <extension_dir> <output_dir>
#
SCRIPT_DIR=$( cd $( dirname $0 ) && pwd )

source ${SCRIPT_DIR}/../constants.sh

while getopts :r:s: opts; do
	case "${opts}" in
		r ) REPO="${OPTARG}" ;;
		s ) SHA="${OPTARG}" ;;
	esac
done
shift $((${OPTIND} - 1))

EXTENSION_DIR=$1
OUTPUT_DIR=$2
PROJECT_NAME=$(echo ${REPO} | tr / -)
export RVMLOGGINGLEVEL=UNIQUE

check_status_code() {
	local error_code=$?
	if [[ ${error_code} -ne 0 ]]; then
		echo "Step $1 failed"
		echo "$1,${error_code}" > "${OUTPUT_DIR}/logs/status.log"
		exit 1
	fi
}

check_inputs() {
	if [[ -z ${REPO} || -z ${SHA} || ! -d ${EXTENSION_DIR} || -z ${OUTPUT_DIR} ]]; then
		echo "Usage: bash search_block.sh -r <repo> -s <sha> <extension_dir> <output_dir>"
		exit 1
	fi
	
	if [[ ! ${EXTENSION_DIR} =~ ^/.* ]]; then
		EXTENSION_DIR=${SCRIPT_DIR}/${EXTENSION_DIR}
	fi
	
	if [[ ! ${OUTPUT_DIR} =~ ^/.* ]]; then
		OUTPUT_DIR=${SCRIPT_DIR}/${OUTPUT_DIR}
	fi
	
	mkdir -p ${OUTPUT_DIR}/logs

	
	if [[ ! -f ${SCRIPT_DIR}/resources/non-track-agent.jar ]]; then
		echo "JavaMOP agent is missing"
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
	
	if [[ ! -f ${SCRIPT_DIR}/resources/non-track-agent-verbose.jar ]]; then
		echo "JavaMOP [verbose] agent is missing. Building..."
		cp ${SCRIPT_DIR}/resources/non-track-agent.jar ${SCRIPT_DIR}/resources/non-track-agent-verbose.jar
		bash ${SCRIPT_DIR}/build.sh ${SCRIPT_DIR}/resources/non-track-agent-verbose.jar true &>> ${OUTPUT_DIR}/logs/setup.log
		check_status_code "install agent"
	fi
	
	pushd ${SCRIPT_DIR}/../../blocktest &> /dev/null
	mvn -Dmaven.repo.local=${OUTPUT_DIR}/repo install &>> ${OUTPUT_DIR}/logs/setup.log
	popd &> /dev/null
}

clone_project() {
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

run_rv_with_verbose() {
	echo "Running RV with verbose mode..."
	local tmp_dir="${TMP_DIR}/${PROJECT_NAME}"
	local extension_jar="${EXTENSION_DIR}/target/blocktest-extension-1.0.jar"
	export ADD_AGENT=1
	export MOP_AGENT_PATH="-javaagent:${SCRIPT_DIR}/resources/non-track-agent-verbose.jar"
	rm -rf ${tmp_dir} && mkdir ${tmp_dir}

	pushd ${OUTPUT_DIR}/project &> /dev/null
	(time mvn ${SKIP} -Djava.io.tmpdir=${tmp_dir} -Dmaven.repo.local=${OUTPUT_DIR}/repo -Dmaven.ext.class.path=${extension_jar} test) &> ${OUTPUT_DIR}/logs/run.log
	check_status_code "run_rv_with_verbose"
	popd &> /dev/null
	
	unset ADD_AGENT
	unset MOP_AGENT_PATH
}

search_rv_log() {
	echo "Searching RV log..."
	file_to_search="${OUTPUT_DIR}/logs/run.log"
	if [[ -z $(grep "weaveinfo Join point" ${file_to_search}) ]]; then
		if [[ -n $(find ${OUTPUT_DIR}/project/target/surefire-reports -name "*.dumpstream") ]]; then
			echo "" > "${OUTPUT_DIR}/logs/results.csv"
			for file_to_search in $(find ${OUTPUT_DIR}/project/target/surefire-reports -name "*.dumpstream"); do
				grep "weaveinfo Join point" ${file_to_search} | \
				parallel --pipe -N2000 ${SCRIPT_DIR}/resources/fast_search.sh >> "${OUTPUT_DIR}/logs/results.csv"
			done
			
			file_to_search=""
		else
			echo "No event for RV"
			exit 1
		fi
	fi

	if [[ ${file_to_search} != "" ]]; then
		grep "weaveinfo Join point" ${file_to_search} | \
		parallel --pipe -N2000 ${SCRIPT_DIR}/resources/fast_search.sh > "${OUTPUT_DIR}/logs/results.csv"
	fi

	echo "OK - found $(cat ${OUTPUT_DIR}/logs/results.csv | tail -n +2 | wc -l) lines of interest"
}

echo "BLOCKTEST version: ($(git rev-parse HEAD) - $(date +%s))"
check_inputs
clone_project
install
run_rv_with_verbose
search_rv_log
