chcp 65001
@echo off
set /p folder=copy to which foleder?:
call setBasicVariable.bat
call renameVersionStart.bat

call build-uat.bat
cd %originLocation%
call build-prod.bat
cd %originLocation%
call build-dev.bat
cd %originLocation%
call build-local.bat

cd %originLocation%
call renameVersionEnd.bat

pause