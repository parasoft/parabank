# Introduction
The Parabank demo web application and associated web services (SOAP and REST) from Parasoft.

# Build and Install
Build the Parabank application using Maven (`mvn clean install`). After a successful build, deploy the parabank.war (located in "/target") onto a Tomcat 8 web server.

Parabank uses a built-in HyperSQL database. You must shut down all instances of Parabank and HyperSQL for the build to succeed.

## Tomcat notes
	The minimum Tomcat version is now 8.5.5 (this is the expected default build compliance, if earlier version is needed)
	
```sh
 #replace the X.X.X with the correct version of tomcat like 8.0.37 
 mvn clean install -Dtomcat.version=X.X.X
```	

NOTE:	To prevent warning in the log:

```text
Oct 06, 2016 3:53:33 PM org.apache.catalina.webresources.Cache getResource
WARNING: Unable to add the resource at [/WEB-INF/lib/wss4j-ws-security-stax-2.1.3.jar] to the cache because there was insufficient free space available after evicting expired cache entries - consider increasing the maximum size of the cache
```

Add the following to the `<tomcat install>/config/context.xml` add block below before `</Context>`

````xml
<Resources cachingAllowed="true" cacheMaxSize="100000" />
````

# Test scripts 
(NOTE: All scripts should be executed from project director)     

| Script | Description  |
|:--- |:--- |
| __jtest-sa-ut.sh__ | Executes Parasoft Jtest DTP Engine for Static Analysis and Unit Testing results/coverage
| __deploy-jtest-monitor.sh__ | Deploys the Jtest Monitor package (created using mvn goal jtest:monitor) into directory specified by APP_COVERAGE_DIR in set-vars.bat
| __soatest.sh__ | Executes Parasoft SOAtest API and Web functional tests (including integration with Jtest DTP Engine for monitoring code coverage)
| __jtest-ft-cov.sh__ | Executes Parasoft Jtest DTP Engine for processing monitored coverage during functional testing with SOAtest
| __jtest-mt-cov.sh__ | Uploads results from manual testing efforts (managed using the Parasoft Coverage Agent Manager) and processing coverage data captured during manual testing
| __jtest-sa-ut-delta.sh__ | same operation as 'jtest-sa-ut' but used to rescan code based for localized changes - used for demonstration purposes when scanning 'dirty' branch
| __set-vars.sh__ | utility script called by other scripts to consistently set BUILD_ID and the Project name sent to DTP
