@echo off
rem **** Setup
set DTP_PROJECT=Parabank-v3
set BUILD_ID=PARABANK3-%date:~10,4%%date:~4,2%%date:~7,2%-%BUILD_NUMBER%
set RUN_TIME=%date:~10,4%%date:~4,2%%date:~7,2%-%time:~1,1%%time:~3,2%
set
set APP_COVERAGE_DIR=c:\tmp

for /F "usebackq delims==" %%i in (`bash.exe -c ^" cygpath -w \"$^(ls -1d /cygdrive/c/Program\ Files/java/jdk1.8* ^| ta
il -1^)\" ^"`) do @set JAVA_HOME=%%i

echo JAVA_HOME=%JAVA_HOME%
echo ===================================================================
echo DTP Project: %DTP_PROJECT% (%RUN_TIME%)
echo ===================================================================

