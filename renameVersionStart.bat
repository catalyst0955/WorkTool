chcp 65001
for /f "tokens=*" %%i in (./nsap-ui/src/app/agent-portal/layout/layout.component.html) do (
	set a=%%i
	setlocal enabledelayedexpansion
	set "a=!a:[pathFolder]=%folder%!"
	echo !a!>>$
	endlocal
)
move $ ./nsap-ui/src/app/agent-portal/layout/layout.component.html
