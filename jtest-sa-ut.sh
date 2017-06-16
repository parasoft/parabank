#!/bin/bash

if [ -f "set-vars.sh" ]; then
 
	source set-vars.sh

	echo ===================================================================
	echo Running Static Analysis and Unit Tests against build.id=$BUILD_ID
	echo ===================================================================

	echo **1/3** Execute Static Analysis: Recommended Rules
	mvn jtest:jtest -Djtest.config="jtest.dtp://Recommended Rules" -Dmaven.test.skip=true -Dmaven.test.failure.ignore=true -Dproperty.dtp.project=$DTP_PROJECT -Djtest.showsettings=true -Djtest.report=$REPORT_DIR/jtest-sa -Dproperty.console.verbosity.level=high -Dproperty.build.id=$BUILD_ID > $LOG_DIR/jtest-sa-$RUN_TIME.log 2>&1

	# echo **X/X** (Optional) Execute Static Analysis: All Rules
	# mvn jtest:jtest -Djtest.config="jtest.dtp://All Rules" -Dmaven.test.skip=true -Dmaven.test.failure.ignore=true -Dproperty.dtp.project=$DTP_PROJECT -Djtest.showsettings=true -Djtest.report=$REPORT_DIR/jtest-sa-all -Dproperty.console.verbosity.level=high -Dproperty.build.id=$BUILD_ID > $LOG_DIR/jtest-sa-all-$RUN_TIME.log 2>&1

	echo **2/3** Execute Metrics
	mvn jtest:jtest -Djtest.config="jtest.dtp://Metrics" -Dmaven.test.skip=true -Dmaven.test.failure.ignore=true -Dproperty.dtp.project=$DTP_PROJECT -Djtest.showsettings=true -Djtest.report=$REPORT_DIR/jtest-ma -Dproperty.console.verbosity.level=high -Dproperty.build.id=$BUILD_ID > $LOG_DIR/jtest-ma-$RUN_TIME.log 2>&1

	echo **3/3** Execute Unit Tests
	mvn process-test-classes jtest:agent test jtest:jtest -Djtest.config="builtin://Unit Tests" -Dmaven.test.failure.ignore=true -Dproperty.dtp.project=$DTP_PROJECT -Dproperty.build.id=$BUILD_ID -Dproperty.report.coverage.images="Parabank-UT;Parabank-All" -Dproperty.console.verbosity.level=high -Djtest.showsettings=true -Djtest.report=$REPORT_DIR/jtest-ut > $LOG_DIR/jtest-ut-$RUN_TIME.log 2>&1

	echo =================================================================
	echo Finished Static Analysis and Unit Testing of build.id=$BUILD_ID
	echo =================================================================

else
	echo "Incorrect usage ... running script in wrong directory, cannot find setvars.sh"
fi
