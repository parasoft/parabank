@echo off
IF NOT EXIST set-vars.bat (
  echo "Incorrect usage ... running script in wrong directory, cannot find setvars.bat"
  GOTO End
) 

call set-vars

echo ===================================================================
echo Running Functional Tests against build.id=%BUILD_ID%
echo ===================================================================

pushd soatest
call copy /Y templatesettings.properties localsettings.properties
echo build.id=%BUILD_ID% >> localsettings.properties
echo general.project=%DTP_PROJECT% >> localsettings.properties
echo session.tag=%DTP_PROJECT%-${config_name} >> localsettings.properties

echo ======================ParaBank==============================

echo **1/3** Importing project into SOAtest
soatestcli -J-Xms512m -J-Xmx512m -J-Xverify:none -J-XX:+UseCompressedOops -data . -import TestAssets

echo **2/3** Running SOAtest (ParaBank)
call erase /Q report-parabank\*.*
soatestcli -config "user://Example Configuration" -data . -resource TestAssets -report report-parabank -localsettings localsettings.properties > soatest-parabank-%RUN_TIME%.log 2>&1

echo **3/3** Uploading report to DTP
curl.exe -k --user admin:admin -F file=@report-parabank/report.xml https://localhost:8082/api/v2/dataCollector
popd

echo ======================Bookstore==============================
pushd soatest

echo **1/3** Importing project into SOAtest
soatestcli -data . -import Bookstore

echo **2/3** Running SOAtest 
call erase /Q report-bookstore\*.*
soatestcli -config "ApplicationCoverage.properties" -data . -resource Bookstore -report report-bookstore -localsettings localsettings.properties > soatest-bookstore-%RUN_TIME%.log 2>&1

echo **3/3** Uploading report to DTP
curl.exe -k --user admin:admin -F file=@report-bookstore/report.xml https://localhost:8082/api/v2/dataCollector
popd

echo =================================================================
echo Finished Functional Tests of build.id=%BUILD_ID%
echo =================================================================

:End
