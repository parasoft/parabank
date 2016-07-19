@echo off
IF %1.==. GOTO IncorrectUsage
IF %2.==. GOTO IncorrectUsage

call set-vars
set BUILD_ID=%1

echo ===================================================================
echo Processing Manual test result for build.id=%BUILD_ID%
echo ===================================================================

echo **1/2** Uploading report to DTP
curl.exe -k --user admin:admin -F file=@%2/report.xml https://localhost:8082/api/v2/dataCollector

echo **2/2** Processing Coverage

call jtestcli -config "builtin://Calculate Application Coverage" -staticcoverage c:\tmp\monitor\static_coverage.xml -runtimecoverage %2 -property build.id=%BUILD_ID% -property dtp.project=%DTP_PROJECT% -property report.coverage.images="Parabank-MT;Parabank-All" -report jtest-mt-cov -property session.tag="manual-win32_x86_64" > jtest-mt-cov-%RUN_TIME%.log 2>&1

GOTO End

echo =================================================================
echo Finished processing Manual test results for build.id=%BUILD_ID%
echo =================================================================

:IncorrectUsage
  echo "Incorrect usage ... requires build_id and location of report.xml and runtime_coverage directory for coverage data"
  echo "Usage: jtest-mt-cov.bat <BUILD_ID> <DATA DIRECTORY>"
GOTO End

:End