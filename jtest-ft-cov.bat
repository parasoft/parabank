@echo off
call set-vars

echo ===================================================================
echo Calculating Application Coverage against build.id=%BUILD_ID%
echo ===================================================================

call jtestcli -config "builtin://Calculate Application Coverage" -staticcoverage c:\tmp\monitor\static_coverage.xml -runtimecoverage c:\tmp\monitor\runtime_coverage  -property build.id=%BUILD_ID% -property dtp.project=%DTP_PROJECT% -property report.coverage.images="Parabank-FT;Parabank-All" -report jtest-ft-cov -property session.tag="parabank-win32_x86_64" > jtest-ft-cov-%RUN_TIME%.log 2>&1

echo =================================================================
echo Finished Calculating Application Coverage for build.id=%BUILD_ID%
echo =================================================================
