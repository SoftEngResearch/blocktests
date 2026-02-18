#!/bin/bash
#
# Run block tests in Docker
# Before running this script, run `docker login`
# Usage: bash run_blocktest_with_time_in_docker.sh [-c <coverage:false>] <projects-url-list> <output-dir> [branch=false] [timeout=86400 (sec)]
#
SCRIPT_DIR=$(cd $(dirname $0) && pwd)

COLLECT_COVERAGE=false
DUPLICATE=""
while getopts :c:d: opts; do
  case "${opts}" in
    c ) COLLECT_COVERAGE="${OPTARG}" ;;
    d ) DUPLICATE="${OPTARG}" ;;
  esac
done
shift $((${OPTIND} - 1))

PROJECTS_LIST=$1
OUTPUT_DIR=$2
BRANCH=$3
TIMEOUT=$4

function check_input() {
  if [[ ! -f ${PROJECTS_LIST} || -z ${OUTPUT_DIR} ]]; then
    echo "Usage: run_blocktest_with_time_in_docker.sh [-c <coverage:false>] <projects-url-list> <output-dir> [branch=false] [timeout=86400 (sec)]"
    exit 1
  fi

  if [[ ! ${OUTPUT_DIR} =~ ^/.* ]]; then
    OUTPUT_DIR=${SCRIPT_DIR}/${OUTPUT_DIR}
  fi

  mkdir -p ${OUTPUT_DIR}

  if [[ ! -s ${PROJECTS_LIST} ]]; then
    echo "${PROJECTS_LIST} is empty..."
    exit 0
  fi

  if [[ -z $(grep "###" ${PROJECTS_LIST}) ]]; then
    echo "You must end your projects-list file with ###"
    exit 1
  fi

  if [[ -z ${TIMEOUT} ]]; then
    TIMEOUT=86400
  fi
}


function run_project() {
  local url=$1
  sha=$(echo "${url}" | cut -d '/' -f 7)
  repo=$(echo "${url}" | cut -d '/' -f 4-5 | tr / -)
  local filename=$(echo ${url} | rev | cut -d '/' -f 1 | rev | cut -d '#' -f 1 | cut -d '.' -f 1)

  local project_name="${repo}-${sha}-${filename}"
  if [[ -n ${DUPLICATE} ]]; then
    project_name="${repo}-${sha}-${filename}-dup${DUPLICATE}"
  fi
  
  if [[ -d ${OUTPUT_DIR}/${project_name} ]]; then
    echo "$(date) Skip ${project_name} because output already exists" |& tee -a docker.log
    return
  fi

  local start=$(date +%s%3N)
  echo "Running ${project_name} with SHA ${sha}"
  mkdir -p ${OUTPUT_DIR}/${project_name}

  local id=$(docker run -itd --name btest-${project_name} blocktests:latest)
  docker exec -w /home/blocktests/block-tests ${id} git pull
  if [[ $? -ne 0 ]]; then
    echo "$(date) Unable to pull project ${project_name}, try again in 60 seconds" |& tee -a docker.log
    sleep 60
    docker exec -w /home/blocktests/block-tests ${id} git pull
    if [[ $? -ne 0 ]]; then
      echo "$(date) Skip ${project_name} because script can't pull" |& tee -a docker.log
      return
    fi
  fi

  if [[ -n ${BRANCH} && ${BRANCH} != "false" ]]; then
    docker exec -w /home/blocktests/block-tests ${id} git checkout ${BRANCH}
    docker exec -w /home/blocktests/block-tests ${id} git pull
  fi

  if [[ -n ${TIMEOUT} ]]; then
    echo "Setting test timeout to ${TIMEOUT}"
    docker exec -w /home/blocktests/block-tests ${id} sed -i "s/TIMEOUT=.*/TIMEOUT=${TIMEOUT}/" scripts/constants.sh
  fi

  if [[ ${COLLECT_COVERAGE} == "true" ]]; then
    timeout ${TIMEOUT} docker exec -w /home/blocktests/block-tests/scripts/time -e M2_HOME=/home/blocktests/apache-maven -e MAVEN_HOME=/home/blocktests/apache-maven -e CLASSPATH=/home/blocktests/aspectj-1.9.7/lib/aspectjtools.jar:/home/blocktests/aspectj-1.9.7/lib/aspectjrt.jar:/home/blocktests/aspectj-1.9.7/lib/aspectjweaver.jar: -e PATH=/home/blocktests/apache-maven/bin:/usr/lib/jvm/java-8-openjdk/bin:/home/blocktests/aspectj-1.9.7/bin:/home/blocktests/aspectj-1.9.7/lib/aspectjweaver.jar:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin ${id} timeout ${TIMEOUT} bash run_blocktest_with_time_coverage.sh ${url} /home/blocktests/block-tests/extension/ /home/blocktests/block-tests/output ${DUPLICATE} &> ${OUTPUT_DIR}/${project_name}/docker.log
  else
    timeout ${TIMEOUT} docker exec -w /home/blocktests/block-tests/scripts/time -e M2_HOME=/home/blocktests/apache-maven -e MAVEN_HOME=/home/blocktests/apache-maven -e CLASSPATH=/home/blocktests/aspectj-1.9.7/lib/aspectjtools.jar:/home/blocktests/aspectj-1.9.7/lib/aspectjrt.jar:/home/blocktests/aspectj-1.9.7/lib/aspectjweaver.jar: -e PATH=/home/blocktests/apache-maven/bin:/usr/lib/jvm/java-8-openjdk/bin:/home/blocktests/aspectj-1.9.7/bin:/home/blocktests/aspectj-1.9.7/lib/aspectjweaver.jar:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin ${id} timeout ${TIMEOUT} bash run_blocktest_with_time.sh ${url} /home/blocktests/block-tests/extension/ /home/blocktests/block-tests/output ${DUPLICATE} &> ${OUTPUT_DIR}/${project_name}/docker.log
  fi

  docker cp ${id}:/home/blocktests/block-tests/output ${OUTPUT_DIR}/${project_name}
  docker rm -f ${id}
  
  local end=$(date +%s%3N)
  local duration=$((end - start))
  echo "$(date) Finished running ${project_name} in ${duration} ms" |& tee -a docker.log
}

function run_all() {
  local start=$(date +%s%3N)
  while true; do
    if [[ ! -s ${PROJECTS_LIST} ]]; then
      echo "${PROJECTS_LIST} is empty..."
      exit 0
    fi

    local project=$(head -n 1 ${PROJECTS_LIST})
    if [[ ${project} == "###" ]]; then
      local end=$(date +%s%3N)
      local duration=$((end - start))
      echo "$(date) Finished running all projects in ${duration} ms" |& tee -a docker.log

      exit 0
    fi

    if [[ -z $(grep "###" ${PROJECTS_LIST}) ]]; then
      echo "You must end your projects-list file with ###"
      exit 1
    fi

    sed -i 1d ${PROJECTS_LIST}
    echo $project >> ${PROJECTS_LIST}
    run_project ${project} $@
  done

}

check_input
run_all $@
