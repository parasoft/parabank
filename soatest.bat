@echo off
call set-vars

echo ===================================================================
echo Running Functional Tests against build.id=%BUILD_ID%
echo ===================================================================

set PATH=%PATH%;c:\bin;c:\Program Files\Parasoft\SOAtest\9.9

pushd soatest
call copy /Y templatesettings.properties localsettings.properties
echo build.id=%BUILD_ID% >> localsettings.properties
echo property.dtp.project=%DTP_PROJECT% >> localsettings.properties
echo session.tag=%DTP_PROJECT%-${config_name} >> localsettings.properties

echo **1/4** Importing project into SOAtest
soatestcli -data . -import TestAssets
soatestcli -data . -import Bookstore

echo **2/4** Running SOAtest (Parabank)
soatestcli -config "user://Example Configuration" -data . -resource TestAssets -report report-parabank -localsettings localsettings.properties
soatestcli -config "ApplicationCoverage.properties" -data . -resource Bookstore -report report-bookstore -localsettings localsettings.properties

echo **3/4** Uploading report to DTP
curl.exe -k --user admin:admin -F file=@report-parabank/report.xml https://localhost:8082/api/v2/dataCollector
curl.exe -k --user admin:admin -F file=@report-bookstore/report.xml https://localhost:8082/api/v2/dataCollector
popd

echo **4/4** Processing Coverage
rem call mvn jtest:loadCoverage -Djtest.config="builtin://Calculate Application Coverage" -Dproperty.build.id=%BUILD_ID% -Dproperty.dtp.project=%DTP_PROJECT% -Dproperty.report.coverage.images="Parabank-FT;Parabank-All" -Djtest.report=jtest-ft-parabank -Dproperty.session.tag="parabank-win32_x86_64" > jtest-ft-parabank-%RUN_TIME%.log 2>&1
rem call mvn jtest:loadCoverage -Djtest.config="builtin://Calculate Application Coverage" -Dproperty.build.id=%BUILD_ID% -Dproperty.dtp.project=%DTP_PROJECT% -Dproperty.report.coverage.images="Parabank-FT;Parabank-All" -Djtest.report=jtest-ft-bookstore -Dproperty.session.tag="bookstore-win32_x86_64" > jtest-ft-bookstore-%RUN_TIME%.log 2>&1

echo =================================================================
echo Finished Functional Tests of build.id=%BUILD_ID%
echo =================================================================
