@echo off
IF %1.==. (
  echo "Incorrect usage ... requires build_id (e.g. PARABANK3-%date:~10,4%%date:~4,2%%date:~7,2%-<BUILD_NUMBER>)"
  echo "Usage: jtest-mt-cov.bat <BUILD_ID> <REPORT XML>
  GOTO End
)
IF %2.==. (
  echo "Incorrect usage ... requires report.xml file)"
  echo "Usage: jtest-mt-cov.bat <BUILD_ID> <REPORT XML>
  GOTO End
)
IF NOT EXIST %2 (
  echo "Incorrect usage ... %2 does not exist"
  echo "Usage: jtest-mt-cov.bat <BUILD_ID> <REPORT XML>
  GOTO End
)

IF NOT EXIST set-vars.bat (
  echo "Incorrect usage ... running script in wrong directory, cannot find setvars.bat"
  GOTO End
)  
call set-vars

set BUILD_ID=%1

echo ===================================================================
echo Processing Manual test result for build.id=%BUILD_ID%
echo ===================================================================

echo **1/2** Uploading report to DTP
rem curl.exe -k --user admin:admin -F file=@%2 https://localhost:8082/api/v2/dataCollector

echo **2/2** Processing Coverage

call jtestcli -config "builtin://Calculate Application Coverage" -staticcoverage %APP_COVERAGE_DIR%\static_coverage.xml -runtimecoverage %APP_COVERAGE_DIR%\runtime_coverage -property build.id=%BUILD_ID% -property dtp.project=%DTP_PROJECT% -property report.coverage.images="Parabank-MT;Parabank-All" -report jtest-mt-cov -property session.tag="manual-win32_x86_64" > jtest-mt-cov-%RUN_TIME%.log 2>&1

echo =================================================================
echo Finished processing Manual test results for build.id=%BUILD_ID%
echo =================================================================

:End