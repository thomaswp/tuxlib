cd /D %~dp0
call mvn -Pandroid install
@echo off
SETLOCAL ENABLEDELAYEDEXPANSION
set check=0
FOR /F "tokens=*" %%A IN ('adb devices') DO (
	if not !check! == 0 (
		echo %%A
		for /f "tokens=1" %%i in ("%%A") DO (
			adb -s %%i shell am start -n tuxkids.android/tuxkids.android.TestGameActivity
		)
	)
	set check=1
)