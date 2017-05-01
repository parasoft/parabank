#!/bin/bash

if [ -f "set-vars.sh" ]; then
 
	source set-vars.sh

	echo ===================================================================
	echo Running Functional Tests against build.id=$BUILD_ID
	echo ===================================================================

	pushd soatest
	cp templatesettings.properties localsettings.properties
	echo build.id=$BUILD_ID >> localsettings.properties
	echo general.project=$DTP_PROJECT >> localsettings.properties
	echo session.tag=$DTP_PROJECT-${config_name} >> localsettings.properties

	echo ======================ParaBank==============================

	echo **1/3** Importing project into SOAtest
	soatestcli -data . -import TestAssets

	echo "**2/3** Running SOAtest (ParaBank)"

	rm ../$REPORT_DIR/soa-parabank/*.*
	soatestcli -config "user://Example Configuration" -data . -resource TestAssets -report ../$REPORT_DIR/soa-parabank -localsettings localsettings.properties > ../$LOG_DIR/soatest-parabank-$RUN_TIME.log 2>&1

	#echo **3/3** Uploading report to DTP
	#curl -k --user admin:admin -F file=@../$REPORT_DIR/soa-parabank/report.xml https://localhost:8082/api/v2/dataCollector
	popd

	echo ======================Bookstore==============================
	pushd soatest

	echo **1/3** Importing project into SOAtest
	soatestcli -data . -import Bookstore

	echo "**2/3** Running SOAtest (Bookstore)"
	rm ../$REPORT_DIR/soa-bookstore/*.*
	soatestcli -config "ApplicationCoverage.properties" -data . -resource Bookstore -report ../$REPORT_DIR/soa-bookstore -localsettings localsettings.properties > ../$LOG_DIR/soatest-bookstore-$RUN_TIME.log 2>&1

	#echo **3/3** Uploading report to DTP
	#curl -k --user admin:admin -F file=@../$REPORT_DIR/soa-bookstore/report.xml https://localhost:8082/api/v2/dataCollector
	popd

	echo =================================================================
	echo Finished Functional Tests of build.id=$BUILD_ID
	echo =================================================================

else
	echo "Incorrect usage ... running script in wrong directory, cannot find setvars.sh"
fi
