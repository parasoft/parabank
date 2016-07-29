@echo off
IF NOT EXIST set-vars.bat (
  echo "Incorrect usage ... running script in wrong directory, cannot find setvars.bat"
  GOTO End
) 

call set-vars

echo ===================================================================
echo Deploying Jtest Application Coverage Monitor for build.id=%BUILD_ID%
echo ===================================================================

echo **1/2** Cleaning up old coverage data
call erase /Q %APP_COVERAGE_DIR%\monitor\runtime_coverage\runtime_coverage*

echo **2/2** Copy and unzip monitor package
copy target\jtest\monitor\monitor.zip %APP_COVERAGE_DIR%

pushd %APP_COVERAGE_DIR%
7z x -y monitor.zip
popd

echo ===================================================================
echo Finished deploying Jtest Application Coverage Monitor for build.id=%BUILD_ID%
echo ===================================================================
