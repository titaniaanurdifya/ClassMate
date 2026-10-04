@echo off
REM ============================================================
REM ClassMate - Build and Run Script
REM Requires: JDK 25, JavaFX SDK 25, sqlite-jdbc JAR in lib/
REM ============================================================

SET JAVA_HOME=C:\Program Files\Java\jdk-25.0.3
SET PATH_TO_FX=C:\Program Files\JavaFX\javafx-sdk-25.0.3\lib
SET JAVAC=%JAVA_HOME%\bin\javac
SET JAVA=%JAVA_HOME%\bin\java
SET SQLITE_JAR=lib\sqlite-jdbc.jar

REM ---- Check sqlite-jdbc ----
IF NOT EXIST "%SQLITE_JAR%" (
    echo.
    echo [ERROR] sqlite-jdbc.jar not found in lib\ folder.
    echo Please download from: https://github.com/xerial/sqlite-jdbc/releases
    echo Place the JAR file as: lib\sqlite-jdbc.jar
    echo.
    pause
    exit /b 1
)

REM ---- Clean output ----
IF EXIST out rmdir /s /q out
mkdir out

REM ---- Collect source files ----
echo [1/3] Collecting source files...
dir /s /b src\*.java > sources.txt 2>nul

REM ---- Compile ----
echo [2/3] Compiling...
"%JAVAC%" --module-path "%PATH_TO_FX%" ^
    --add-modules javafx.controls,javafx.fxml ^
    -cp "%SQLITE_JAR%" ^
    -d out ^
    @sources.txt

IF %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Compilation failed. Check the errors above.
    del sources.txt
    pause
    exit /b 1
)

REM ---- Copy resources ----
echo [2b/3] Copying resources...
xcopy /s /q /y resources\* out\ >nul 2>&1

del sources.txt

REM ---- Run ----
echo [3/3] Launching ClassMate...
echo.
"%JAVA%" --module-path "%PATH_TO_FX%" ^
    --add-modules javafx.controls,javafx.fxml ^
    -cp "out;%SQLITE_JAR%" ^
    com.classmate.Main

pause
