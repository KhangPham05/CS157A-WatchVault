@echo off
REM ─────────────────────────────────────────────────────────────
REM  WatchVault – compile script (Windows)
REM  Usage: double-click or run from cmd: compile.bat
REM ─────────────────────────────────────────────────────────────

SET JAR=lib\mysql-connector-j-9.1.0.jar
SET OUT=out
SET SRC_DIR=src\main\java

echo =^> Checking for MySQL Connector/J...
IF NOT EXIST "%JAR%" (
    echo     lib\mysql-connector-j-9.1.0.jar not found.
    echo     Please download it from:
    echo     https://dev.mysql.com/downloads/connector/j/
    echo     and place the .jar file inside the lib\ folder.
    pause
    exit /b 1
) ELSE (
    echo     Found: %JAR%
)

echo =^> Compiling Java sources...
IF NOT EXIST "%OUT%" mkdir "%OUT%"
IF NOT EXIST "%OUT%\META-INF" mkdir "%OUT%\META-INF"

(
  echo Manifest-Version: 1.0
  echo Main-Class: watchvault.ui.WatchVaultCLI
  echo Class-Path: lib/mysql-connector-j-9.1.0.jar
) > "%OUT%\META-INF\MANIFEST.MF"

for /r "%SRC_DIR%" %%f in (*.java) do (
    javac -cp "%JAR%" -d "%OUT%" --release 14 "%%f"
)

echo =^> Packaging WatchVault.jar...
cd "%OUT%"
jar cfm ..\WatchVault.jar META-INF\MANIFEST.MF -C . .
cd ..

echo.
echo   Build successful! WatchVault.jar created.
echo.
echo   Before running:
echo     1. Start MySQL and run:  mysql -u root -p ^< sql\01_schema.sql
echo     2.                       mysql -u root -p ^< sql\02_seed_data.sql
echo     3. Edit DBConnection.java if your MySQL password is not empty, then recompile.
echo.
echo   To run:
echo     java -jar WatchVault.jar
echo.
echo   Default admin login:
echo     Email:    admin@watchvault.com
echo     Password: admin123
echo.
pause
