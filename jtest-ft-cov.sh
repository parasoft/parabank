#!/bin/bash

if [ -f "set-vars.sh" ]; then

	source set-vars.sh
 
	echo ===================================================================
	echo Calculating Application Coverage against build.id=$BUILD_ID
	echo ===================================================================

	echo **1/2** Calculating coverage
	jtestcli -config "builtin://Calculate Application Coverage" -staticcoverage $APP_COVERAGE_DIR/monitor/static_coverage.xml -runtimecoverage $APP_COVERAGE_DIR/monitor/runtime_coverage  -property build.id=$BUILD_ID -property dtp.project=$DTP_PROJECT -property report.coverage.images="Parabank-FT;Parabank-All" -report $REPORT_DIR/jtest-ft-cov -property session.tag="parabank-win32_x86_64" > $LOG_DIR/jtest-ft-cov-$RUN_TIME.log 2>&1

	echo **2/2** Cleaning up processed coverage data
	rm $APP_COVERAGE_DIR/monitor/runtime_coverage/runtime_coverage*

	echo =================================================================
	echo "Finished Calculating Application Coverage for build.id="$BUILD_ID
	echo =================================================================

else
	echo "Incorrect usage ... running script in wrong directory, cannot find setvars.sh"
fi
