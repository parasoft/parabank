@echo off
IF %1.==. GOTO IncorrectUsage
IF %2.==. GOTO IncorrectUsage

call set-vars
IF %1.==. (
  echo "Incorrect usage ... requires build_id (e.g. PARABANK3-%date:~10,4%%date:~4,2%%date:~7,2%-<BUILD_NUMBER>)"
  echo "Usage: jtest-mt-cov.bat <BUILD_ID> <REPORT XML>"
  GOTO End
)
IF %2.==. (
  echo "Incorrect usage ... requires report.xml file)"
  echo "Usage: jtest-mt-cov.bat <BUILD_ID> <REPORT XML>"
  GOTO End
)
IF NOT EXIST %2 (
  echo "Incorrect usage ... %2 does not exist"
  echo "Usage: jtest-mt-cov.bat <BUILD_ID> <REPORT XML>"
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

echo **1/3** Uploading report to DTP
REM curl.exe -k --user admin:admin -F file=@%2 https://localhost:8082/api/v2/dataCollector

echo **2/3** Processing Coverage
call jtestcli -config "builtin://Calculate Application Coverage" -staticcoverage %APP_COVERAGE_DIR%\monitor\static_coverage.xml -runtimecoverage %APP_COVERAGE_DIR%\monitor\runtime_coverage\__default__ -property build.id=%BUILD_ID% -property dtp.project=%DTP_PROJECT% -property report.coverage.images="Parabank-MT;Parabank-All" -report %REPORT_DIR%\jtest-mt-cov -property session.tag="manual-win32_x86_64" > %LOG_DIR%\jtest-mt-cov-%RUN_TIME%.log 2>&1

echo **3/3** Cleaning up processed coverage data
call erase /Q %APP_COVERAGE_DIR%\monitor\runtime_coverage\__default__\runtime_coverage*

echo =================================================================
echo Finished processing Manual test results for build.id=%BUILD_ID%
echo =================================================================
GOTO End

:IncorrectUsage
  echo "Incorrect usage ... requires build_id and location of report.xml and runtime_coverage directory for coverage data"
  echo "Usage: jtest-mt-cov.bat <BUILD_ID> <DATA DIRECTORY>"
GOTO End

:End