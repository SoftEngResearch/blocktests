#!/bin/bash
#
# Switch between verbose and non-verbose mode
# Usage: bash build.sh <agent.jar> [verbose=false]
#
AGENT=$1
VERBOSE=$2
if [[ ! -f ${AGENT} ]]; then
	echo "Agent not found"
	exit 1
fi

mkdir -p META-INF
if [[ ${VERBOSE} == "true" ]]; then
	cp resources/aop-ajc-verbose.xml META-INF/aop-ajc.xml
else
	cp resources/aop-ajc.xml META-INF/aop-ajc.xml
fi
zip ${AGENT} META-INF/aop-ajc.xml
rm -rf META-INF
