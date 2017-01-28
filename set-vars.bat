@echo off
rem **** Setup
set DTP_PROJECT=Parabank-v3

IF "%BUILD_NUMBER%"=="" (
	set BUILD_ID=PARABANK3-%date:~10,4%%date:~4,2%%date:~7,2%
) else (
	set BUILD_ID=PARABANK3-%BUILD_NUMBER%
)

set RUN_TIME=%date:~10,4%%date:~4,2%%date:~7,2%-%time:~1,1%%time:~3,2%

set APP_COVERAGE_DIR=c:\tmp

echo =========================================================================
echo DTP Project: %DTP_PROJECT% (BUILD_ID:%BUILD_ID%, RUN_TIME:%RUN_TIME%)
echo =========================================================================

