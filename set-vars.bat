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
set REPORT_DIR=target\reports\%BUILD_ID%
set LOG_DIR=target\logs

IF NOT EXIST "%LOG_DIR%\" (
    mkdir "%LOG_DIR%"
)

echo =========================================================================
echo DTP Project: %DTP_PROJECT% (BUILD_ID:%BUILD_ID%, RUN_TIME:%RUN_TIME%)
echo =========================================================================

echo Setup DTP Engine and SOAtest intallation
SET JTEST_HOME=
SET SOATEST_HOME=
SET PARABANK_TOMCAT=

IF "%JTEST_HOME%"=="" (
	echo JTEST_HOME is not set
	exit 1;
)

IF "%SOATEST_HOME%"=="" (
	echo SOATEST_HOME is not set
	exit 1;
)

set PATH=%JTEST_HOME%;%SOATEST_HOME%;C:\Program Files\7-zip;%PATH%