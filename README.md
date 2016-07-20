# Introduction
The Parabank demo web application from Parasoft.

# Build and Install
Build the Parabank application using Maven ("mvn clean install"). After a successful build, deploy the parabank.war (located in "/target") onto a Tomcat 8 web server.

Parabank uses a built-in HyperSQL database. You must shut down all instances of Parabank and HyperSQL for the build to succeed.

# Test scripts 
(NOTE: All scripts should be executed from project director)
**jtest-sa-ut**: Executes Parasoft Jtest DTP Engine for Static Analysis and Unit Testing results/coverage

**soatest**: Executes Parasoft SOAtest API and Web functiona tests (including integration with Jtest DTP Engine for monitoring code coverage)

**jtest-ft-cov**: Executes Parasoft Jtest DTP Engine for processing monitored coverage during functional testing with SOAtest

**jtest-mt-cov**: Uploads results from manual testing efforts (managed using the Parasoft Coverage Agent Manager) and processing coverage data captured during manual testing

**jtest-sa-ut-delta**: same operation as 'jtest-sa-ut' but used to rescan code based for localized changes - used for demonstration purposes when scanning 'dirty' branch

**set-vars**: utility script called by other scripts to consistently set BUILD_ID and the Project name sent to DTP
