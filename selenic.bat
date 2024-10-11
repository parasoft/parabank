@echo off

echo ======================Run impacted tests demo======================

IF "%SELENIC_HOME%"=="" (
    set /p SELENIC_HOME=Setup SELENIC_HOME=
    echo SELENIC_HOME=%SELENIC_HOME%

    IF "%SELENIC_HOME%"=="" (
        echo SELENIC_HOME must be set
        exit /b 1
    )
) else (
    echo Using SELENIC_HOME=%SELENIC_HOME%
)

set PATH=%SELENIC_HOME%;%PATH%

echo ======================Create baseline coverage from master======================

echo "**1/7** checking out master branch"
call git checkout master

echo "**2/7** packaging parabank war"
call mvn clean package -DskipTests

echo "**3/7** Running parabank integration tests and  collect coverage runtime data"
call mvn ^
    -DargLine=-javaagent:%SELENIC_HOME%\selenic_agent.jar=captureDom=true,collectCoverage=true,coverageDir=runtime_coverage ^
    -Dcargo.jvmargs=-javaagent:%SELENIC_HOME%\coverage\Java\jtest_agent\agent.jar=settings=%SELENIC_HOME%\coverage\Java\jtest_agent\agent.properties ^
    cargo:start ^
    failsafe:integration-test ^
    cargo:stop

echo "**4/7** Running jtestcov to create a baseline coverage.xml"
call java -jar %SELENIC_HOME%\coverage\Java\jtestcov\jtestcov.jar coverage -selenic ^
    -app target\parabank-5.0.0-SNAPSHOT.war ^
    -include com\parasoft\parabank/** ^
    -runtime runtime_coverage ^
    -report coverage_report ^
    -settings %SELENIC_HOME%\selenic.properties ^
    -showdetails

echo ======================Run impacted tests from selenium-demo branch======================

echo "**5/7** checking out selenium-demo branch"
call git checkout selenium-demo

echo "**6/7** packaging parabank-demo war"
call mvn clean package -DskipTests

echo "**7/7** Running impacted tests"
call mvn
    -Dselenic.home=%SELENIC_HOME% ^
    -Dselenic.coverage.binaries=target\parabank-5.0.0-selenium-demo-SNAPSHOT.war ^
    -Dselenic.coverage.binaries.includes=com\parasoft\parabank\** ^
    -Dselenic.coverage.baseline=coverage_report\coverage.xml ^
    -Dselenic.coverage.showdetails=true ^
    -DargLine=-javaagent:%SELENIC_HOME%\selenic_agent.jar=captureDom=true,selfHealing=true ^
    cargo:start ^
    com.parasoft:selenic-maven-plugin:1.0.0:impacted-tests ^
    failsafe:integration-test ^
    cargo:stop
