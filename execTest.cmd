@setlocal ENABLEEXTENSIONS ENABLEDELAYEDEXPANSION
@call setenv_m3g
@for /F "usebackq" %%I IN (`C:\cygwin\bin\date.exe '+%%Y%%m%%d'`) DO @set RUN_DATE=%%I
@for /F "usebackq" %%I IN (`C:\cygwin\bin\date.exe '+%%Y%%m%%d-%%H%%M%%S'`) DO @set RUN_TIME=%%I
@for /F "usebackq" %%I IN (`C:\cygwin\bin\date.exe '+%%Y%%m%%d-%%H%%M'`) DO @set BUILD_ID=buildpb-ut-%%I
@echo RUN_DATE=%RUN_DATE%
@echo RUN_TIME=%RUN_TIME%
@echo BUILD_ID=%BUILD_ID%
@call mvn clean process-test-classes jtest:agent test jtest:monitor jtest:jtest -Djtest.config=^"builtin://Unit^ Tests^" -Dmaven.test.failure.ignore=true -Dproperty.dtp.project=Parabank3.0 -Dproperty.build.id=%BUILD_ID% -Dproperty.report.coverage.images="parabank-ut;combined" -Dproperty.console.verbosity.level=high -Djtest.showsettings=true -Djacoco.skip=true %* >ut-%RUN_TIME%.log
@tail -50 ut-%RUN_TIME%.log
@endlocal
