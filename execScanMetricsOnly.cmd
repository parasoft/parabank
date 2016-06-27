@setlocal ENABLEEXTENSIONS ENABLEDELAYEDEXPANSION
@call setenv_m3g 
@for /F "usebackq" %%I IN (`C:\cygwin\bin\date.exe '+%%Y%%m%%d'`) DO @set RUN_DATE=%%I
@for /F "usebackq" %%I IN (`C:\cygwin\bin\date.exe '+%%Y%%m%%d-%%H%%M%%S'`) DO @set RUN_TIME=%%I
@echo RUN_DATE=%RUN_DATE%
@echo RUN_TIME=%RUN_TIME%

@echo running metrics
@call mvn clean install jtest:jtest -Djtest.config="jtest.dtp://Metrics" -Dmaven.test.skip=true -Dmaven.test.failure.ignore=true -Dproperty.dtp.project=Parabank3.0 -Djtest.showsettings=true -Dproperty.console.verbosity.level=high -Dproperty.build.id=buildpb-me-%RUN_DATE% -Djacoco.skip=true %* >ms-%RUN_TIME%.log 2>&1
::-Dproperty.session.tag=Recommended
@tail -50 ms-%RUN_TIME%.log
@endlocal