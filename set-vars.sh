#!/bin/bash

#**** Setup
export DTP_PROJECT=Parabank-v3
export RUN_DATE=`date +%Y%m%d`

if ($BUILD_NUMBER == "") then
	export BUILD_ID=PARABANK3-$RUN_DATE
else
	export BUILD_ID=PARABANK3-$BUILD_NUMBER
fi

export RUN_TIME=`date +%Y%m%d%H%M`

export APP_COVERAGE_DIR=~/tmp
export REPORT_DIR=target/reports/$BUILD_ID
export LOG_DIR=target/logs

echo ===================================================================
echo DTP Project:$DTP_PROJECT "(BUILD_ID:"$BUILD_ID", RUN_TIME:"$RUN_TIME")"
echo ===================================================================

echo Setup DTP Engine & SOAtest Installation
JTEST_HOME=
SOATEST_HOME=

if [ -z "JTEST_HOME" ]; then
	echo JTEST_HOME must be set
	exit 1;
fi
if [ -z "SOATEST_HOME" ]; then
	echo SOATEST_HOME must be set
	exit 1;
fi

export JTEST_HOME SOATEST_HOME
export PATH=$JTEST_HOME:$SOATEST_HOME:$PATH



