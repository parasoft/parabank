#!/bin/bash

if [ -f "set-vars.sh" ]; then

	source set-vars.sh
 
	if [ -z "$1"]; then
  		echo "Incorrect usage ... requires build_id (e.g. PARABANK3-<DATE>-<BUILD_NUMBER>)"
	  	echo "Usage: jtest-mt-cov.sh <BUILD_ID> <REPORT XML>"
		exit
	fi
	if [ -z "$2"]; then
	  	echo "Incorrect usage ... requires report.xml file)"
	  	echo "Usage: jtest-mt-cov.sh <BUILD_ID> <REPORT XML>"
		exit
	fi
	if [ ! -f "$2" ]; then
	  	echo "Incorrect usage ... $2 does not exist"
	  	echo "Usage: jtest-mt-cov.sh <BUILD_ID> <REPORT XML>"
		exit
	fi

	export BUILD_ID=$1

	echo ===================================================================
	echo "Processing Manual test result for build.id="$BUILD_ID
	echo ===================================================================

	echo **1/3** Uploading report to DTP
	curl -k --user admin:admin -F file=@$2 https://localhost:8082/api/v2/dataCollector

	echo **2/3** Processing Coverage
	jtestcli -config "builtin://Calculate Application Coverage" -staticcoverage $APP_COVERAGE_DIR/monitor/static_coverage.xml -runtimecoverage $APP_COVERAGE_DIR/monitor/runtime_coverage -property build.id=$BUILD_ID -property dtp.project=$DTP_PROJECT -property report.coverage.images="Parabank-MT;Parabank-All" -report $REPORT_DIR/jtest-mt-cov -property session.tag="manual-win32_x86_64" > $LOG_DIR/jtest-mt-cov-$RUN_TIME.log 2>&1

	echo **3/3** Cleaning up processed coverage data
	rm $APP_COVERAGE_DIR/monitor/runtime_coverage/runtime_coverage*

	echo =================================================================
	echo "Finished processing Manual test results for build.id="$BUILD_ID
	echo =================================================================

else
	echo "Incorrect usage ... running script in wrong directory, cannot find setvars.sh"
fi
