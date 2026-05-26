@echo off
echo Pokrecem frontend...
cd /d "%~dp0frontend"
call npm run dev
pause
