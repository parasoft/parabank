#!/bin/bash

#**** Setup
export DTP_PROJECT=Parabank-v3
export RUN_DATE=`date +%Y%m%d`
export BUILD_ID=PARABANK3-$RUN_DATE-$BUILD_NUMBER
export RUN_TIME=`date +%Y%m%d%H%M`

export APP_COVERAGE_DIR=/tmp

echo ===================================================================
echo DTP Project:$DTP_PROJECT "("$RUN_TIME")"
echo ===================================================================

