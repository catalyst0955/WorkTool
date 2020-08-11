@echo off
title deploy
echo start build prod
if "%folder%" == "" ( 
set needRename=true
call setBasicVariable.bat
set /p folder=copy to which foleder?:
)

if "%needRename%" == "true" (
 call renameVersionStart.bat
)

set remotePath=%pathVar%/%folder%/prod

echo step1: remove old file
if exist "%remotePath%" rmdir /s/q "%remotePath%"
if exist .\nsap\src\main\resources\static rmdir /s/q .\nsap\src\main\resources\static

echo step2: angular build and mavan build
cd ./nsap-ui
call  ng build --prod --outputPath ../nsap/src/main/resources/static

cd  ../nsap
call mvn clean package -P prod

echo step3:copy file 
if not exist "%remotePath%" mkdir "%remotePath%"
copy ".\target\nsap.jar" "%remotePath%"

if "%needRename%" == "true" (
	cd %originLocation%
	call renameVersionEnd.bat
)

echo end 

if "%needRename%" == "true" (
	pause
)
