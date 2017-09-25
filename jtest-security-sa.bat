@echo off

IF NOT EXIST set-vars.bat (
  echo "Incorrect usage ... running script in wrong directory, cannot find set-vars.bat"
  GOTO End
)
call set-vars

set BUILD_ID=%BUILD_ID%-security

echo ===================================================================
echo Running Static Analysis and Unit Tests against build.id=%BUILD_ID%
echo ===================================================================

echo **1** Execute Static Analysis: Security Rules
call mvn jtest:jtest -Djtest.config="Security Rules.properties" -Dmaven.test.skip=true -Dmaven.test.failure.ignore=true -Dproperty.dtp.project=%DTP_PROJECT% -Djtest.showsettings=true -Djtest.report=%REPORT_DIR%\jtest-sa-security -Dproperty.console.verbosity.level=high -Dproperty.build.id=%BUILD_ID% -Djacoco.skip=true > %LOG_DIR%\jtest-sa-security-%RUN_TIME%.log 2>&1

echo =================================================================
echo Finished Static Analysis and Unit Testing of build.id=%BUILD_ID%
echo =================================================================

:End
