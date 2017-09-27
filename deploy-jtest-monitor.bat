@echo off
IF NOT EXIST set-vars.bat (
  echo "Incorrect usage ... running script in wrong directory, cannot find setvars.bat"
  GOTO End
) 

call set-vars
echo ===================================================================
echo Deploying Jtest Application Coverage Monitor for build.id=%BUILD_ID%
echo ===================================================================

echo **1/3** Generate montior package
call erase /Q target\jtest\runtime_coverage\__default__\runtime_coverage*

call mvn package jtest:monitor -Dmaven.test.skip=true -Dmaven.test.failure.ignore=true > %LOG_DIR%\jtest-monitor-%RUN_TIME%.log

echo **2/3** Cleaning up old coverage data
call erase /Q /S %APP_COVERAGE_DIR%\monitor\runtime_coverage\*

echo **3/3** Copy and unzip monitor package
copy /Y target\jtest\monitor\monitor.zip %APP_COVERAGE_DIR%

pushd %APP_COVERAGE_DIR%
7z x -y -omonitor-cpy monitor.zip
xcopy /I /y monitor-cpy\monitor monitor
popd

echo ===================================================================
echo Finished deploying Jtest Application Coverage Monitor for build.id=%BUILD_ID%
echo ===================================================================
