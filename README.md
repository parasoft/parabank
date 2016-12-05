# Introduction
The Parabank demo web application and associated web services (SOAP and REST) from Parasoft.

# Build and Install
Build the Parabank application using Maven ("mvn clean install"). After a successful build, deploy the parabank.war (located in "/target") onto a Tomcat 8 web server.

NOTE: if using the coverage agent when running the functional/manual tests (see below), execute the build using "mvn -Dmaven.test.skip=true clean install jtest:monitor"

Parabank uses a built-in HyperSQL database. You must shut down all instances of Parabank and HyperSQL for the build to succeed.

# Test scripts 
(NOTE: All scripts should be executed from the project directory and require the following Parasoft products (and versions)
* Parasoft DTP 5.3.0
* Parasoft Jtest 10.3.0
* Parasoft SOAtest 9.10.0

**jtest-sa-ut**: Executes Parasoft Jtest DTP Engine for Static Analysis and Unit Testing results/coverage

**deploy-jtest-monitor**: Deploys the Jtest Monitor package (created using mvn goal jtest:monitor) into directory specified by APP_COVERAGE_DIR in set-vars.bat

**soatest**: Executes Parasoft SOAtest API and Web functiona tests (including integration with Jtest DTP Engine for monitoring code coverage)

**jtest-ft-cov**: Executes Parasoft Jtest DTP Engine for processing monitored coverage during functional testing with SOAtest

**jtest-mt-cov**: Uploads results from manual testing efforts (managed using the Parasoft Coverage Agent Manager) and processing coverage data captured during manual testing

**jtest-sa-ut-delta**: same operation as 'jtest-sa-ut' but used to rescan code based for localized changes - used for demonstration purposes when scanning 'dirty' branch

**set-vars**: utility script called by other scripts to consistently set BUILD_ID and the Project name sent to DTP
