@echo off
IF NOT EXIST set-vars.bat (
  echo "Incorrect usage ... running script in wrong directory, cannot find setvars.bat"
  GOTO End
) 

call set-vars

echo ===================================================================
echo Running Functional Tests against build.id=%BUILD_ID%
echo ===================================================================

echo **1/2** Calculating coverage
call jtestcli -config "builtin://Calculate Application Coverage" -staticcoverage %APP_COVERAGE_DIR%\monitor\static_coverage.xml -runtimecoverage %APP_COVERAGE_DIR%\monitor\runtime_coverage\__default__  -property build.id=%BUILD_ID% -property dtp.project=%DTP_PROJECT% -property report.coverage.images="Parabank-FT;Parabank-All" -report %REPORT_DIR%\jtest-ft-cov -property session.tag="parabank-win32_x86_64" > %LOG_DIR%\jtest-ft-cov-%RUN_TIME%.log 2>&1

echo **2/2** Cleaning up processed coverage data
call erase /Q %APP_COVERAGE_DIR%\monitor\runtime_coverage\__default__\runtime_coverage*

echo =================================================================
echo Finished Functional Tests of build.id=%BUILD_ID%
echo =================================================================

:End