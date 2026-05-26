@echo off
set JAVA_HOME=C:\Program Files\Java\jdk-21
set PATH=%JAVA_HOME%\bin;%PATH%
echo JAVA_HOME=%JAVA_HOME%
java -version
echo.
echo Pokrecem backend (MySQL profil - XAMPP mora biti ukljucen)...
cd /d "%~dp0backend"
call mvnw.cmd spring-boot:run
pause
