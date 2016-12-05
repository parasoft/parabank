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

echo ===================================================================
echo DTP Project:$DTP_PROJECT "(BUILD_ID:"$BUILD_ID", RUN_TIME:"$RUN_TIME")"
echo ===================================================================

