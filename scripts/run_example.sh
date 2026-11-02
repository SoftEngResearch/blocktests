#!/bin/bash
SCRIPT_DIR=$( cd $( dirname $0 ) && pwd )

EXAMPLE_FILE=$1
OUTPUT_DIR=$2

SKIP="-Dcheckstyle.skip -Drat.skip -Denforcer.skip -Danimal.sniffer.skip -Dmaven.javadoc.skip -Dfindbugs.skip -Dwarbucks.skip -Dmodernizer.skip -Dimpsort.skip -Dpmd.skip -Dxjc.skip -Djacoco.skip -Dinvoker.skip -DskipDocs -DskipITs -Dmaven.plugin.skip -Dlombok.delombok.skip -Dlicense.skipUpdateLicense -Dremoteresources.skip -Dlicense.skip -Dgpg.skip -Dspotbugs.skip"

if [[ ! -f ${EXAMPLE_FILE} ]]; then
    echo "Cannot find example file ${EXAMPLE_FILE}"
    exit 1
fi

if [[ ! -d ${OUTPUT_DIR} ]]; then
    OUTPUT_DIR=${SCRIPT_DIR}/test
fi

if [[ ! ${EXAMPLE_FILE} = /* ]]; then
    EXAMPLE_FILE=${SCRIPT_DIR}/${EXAMPLE_FILE}
fi

if [[ ! ${OUTPUT_DIR} = /* ]]; then
    OUTPUT_DIR=${SCRIPT_DIR}/${OUTPUT_DIR}
fi

if [[ ! -f ${SCRIPT_DIR}/../extension/target/blocktest-extension-1.0.jar ]]; then
    pushd ${SCRIPT_DIR}/../extension &> /dev/null
    echo "Building extension..."
    mvn package &> /dev/null
    if [[ $? -ne 0 ]]; then
        echo "Unable to build extension"
        exit 1
    fi
    popd &> /dev/null
fi

mkdir -p ${OUTPUT_DIR}

loi=$(head ${EXAMPLE_FILE} | grep "BLOCKTEST EXAMPLE")

if [[ -z ${loi} ]]; then
    exit 0
fi

url=$(echo ${loi} | cut -d ' ' -f 4)
repo=$(echo ${url} | cut -d '/' -f 4-5)
name=$(echo ${repo} | tr / -)
sha=$(echo ${url} | cut -d '/' -f 7)
path=$(echo ${url} | cut -d '/' -f 8- | cut -d '#' -f 1)

cd ${OUTPUT_DIR}

if [[ -d ${name} ]]; then
    rm -rf ${name}
fi

git clone https://github.com/${repo} ${name}
cd ${name}
git checkout ${sha}

bash ${SCRIPT_DIR}/treat_special.sh ${name} ${sha}
echo "${SCRIPT_DIR}/treat_special.sh ${name} ${sha}"

current_pwd=$(pwd)
cp ${EXAMPLE_FILE} ${path}
mvn test-compile ${SKIP} -Dmaven.ext.class.path=${SCRIPT_DIR}/../extension/target/blocktest-extension-1.0.jar
if [[ $? -ne 0 ]]; then
    echo "Test ${EXAMPLE_FILE} - Unable to compile blocktest()"
    exit 1
fi

pushd ${SCRIPT_DIR}/../blocktest &> /dev/null
fullpath=${current_pwd}/${path}
if [[ -d ${OUTPUT_DIR}/${name}/src/test ]]; then
    test_filename="$(basename ${fullpath} | cut -d '.' -f 1)BlockTest"
    fullpath_dir=$(dirname $(echo "${fullpath}" | sed 's/\/main\//\/test\//'))
else
    force_path=$(head ${EXAMPLE_FILE} | grep "PATH:")
    if [[ -z ${force_path} ]]; then
        echo "Test ${EXAMPLE_FILE} - Unable to find test directory, please manually add PATH: to test file"
        exit 1
    fi
    
    fullpath=${current_pwd}/$(echo "${force_path}" | cut -d ':' -f 2- | xargs)
    if [[ -n $(echo "${fullpath}" | grep "BlockTest") ]]; then
        test_filename="$(basename ${fullpath} | cut -d '.' -f 1)"
    else
        test_filename="$(basename ${fullpath} | cut -d '.' -f 1)BlockTest"
    fi
    fullpath_dir=$(dirname $(echo "${fullpath}" | sed 's/\/main\//\/test\//'))
    fullpath=${current_pwd}/${path}
fi
echo "Using arguments: --input_file=${fullpath} --output_dir=${fullpath_dir}"
mvn ${SKIP} compile &> /dev/null
mvn exec:java -Dexec.mainClass="org.blocktest.BlockTestRunnerSourceCode" -Dexec.args="--input_file=${fullpath} --output_dir=${fullpath_dir}"
if [[ $? -ne 0 ]]; then
    echo "Test ${EXAMPLE_FILE} - Unable to generate test using blocktest()"
    exit 1
fi

popd &> /dev/null

echo "Generated test @ ${test_filename}"

if [[ ! -f ${fullpath_dir}/${test_filename}.java ]]; then
    echo "Test ${EXAMPLE_FILE} - No test file is generated"
    exit 1
fi

if [[ -z $(grep "@Test" ${fullpath_dir}/${test_filename}.java) ]]; then
    echo "Test ${EXAMPLE_FILE} - No test case is generated"
    exit 1
fi

mvn test ${SKIP} -Dtest="${test_filename}" -Dmaven.ext.class.path=${SCRIPT_DIR}/../extension/target/blocktest-extension-1.0.jar
if [[ $? -ne 0 ]]; then
    echo "Test ${EXAMPLE_FILE} - Some blocktest() failed"
    exit 1
fi
