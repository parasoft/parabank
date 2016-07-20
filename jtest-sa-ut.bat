@echo off
call set-vars

echo ===================================================================
echo Running Static Analysis and Unit Tests against build.id=%BUILD_ID%
echo ===================================================================

echo **1/3** Execute Static Analysis: Recommended Rules
call mvn jtest:jtest -Djtest.config="jtest.dtp://Recommended Rules" -Dmaven.test.skip=true -Dmaven.test.failure.ignore=true -Dproperty.dtp.project=%DTP_PROJECT% -Djtest.showsettings=true -Djtest.report=jtest-sa -Dproperty.console.verbosity.level=high -Dproperty.build.id=%BUILD_ID% -Djacoco.skip=true > jtest-sa-%RUN_TIME%.log 2>&1

rem echo **X/X** (Optional) Execute Static Analysis: All Rules
rem call mvn jtest:jtest -Djtest.config="jtest.dtp://All Rules" -Dmaven.test.skip=true -Dmaven.test.failure.ignore=true -Dproperty.dtp.project=%DTP_PROJECT% -Djtest.showsettings=true -Djtest.report=jtest-sa-all -Dproperty.console.verbosity.level=high -Dproperty.build.id=%BUILD_ID% -Djacoco.skip=true > jtest-sa-all-%RUN_TIME%.log 2>&1

echo **2/3** Execute Metrics
call mvn jtest:jtest -Djtest.config="jtest.dtp://Metrics" -Dmaven.test.skip=true -Dmaven.test.failure.ignore=true -Dproperty.dtp.project=%DTP_PROJECT% -Djtest.showsettings=true -Djtest.report=jtest-ma -Dproperty.console.verbosity.level=high -Dproperty.build.id=%BUILD_ID% -Djacoco.skip=true > jtest-ma-%RUN_TIME%.log 2>&1

echo **3/3** Execute Unit Tests
call mvn process-test-classes jtest:agent test jtest:jtest -Djtest.config="builtin://Unit Tests" -Dmaven.test.failure.ignore=true -Dproperty.dtp.project=%DTP_PROJECT% -Dproperty.build.id=%BUILD_ID% -Dproperty.report.coverage.images="Parabank-UT;Parabank-All" -Dproperty.console.verbosity.level=high -Djtest.showsettings=true -Djtest.report=jtest-ut -Djacoco.skip=true > jtest-ut-%RUN_TIME%.log 2>&1

echo =================================================================
echo Finished Static Analysis and Unit Testing of build.id=%BUILD_ID%
echo =================================================================
