REM bat script for App Coverage Demo (Manual Test Prep)
@echo off
call set-vars

echo ====================================================
echo Deploying Parabank build.id=%BUILD_ID%
echo ====================================================

rem call mvn clean compile jtest:monitor
copy /Y target\parabank.war %PARABANK_TOMCAT%\webapps\

pushd %PARABANK_TOMCAT%\bin
call shutdown.bat
timeout 5
call startup.bat
echo Waiting for Parabank to startup
timeout 20
popd

echo =====================================================
echo Finished Parabank build.id=%BUILD_ID%
echo =====================================================
