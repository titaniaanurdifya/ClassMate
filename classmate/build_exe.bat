@echo off
SET JAVA_HOME=C:\Program Files\Java\jdk-25.0.3
SET PATH_TO_FX=C:\Program Files\JavaFX\javafx-sdk-25.0.3\lib
SET JAVAC="%JAVA_HOME%\bin\javac"
SET JAR="%JAVA_HOME%\bin\jar"
SET JPACKAGE="%JAVA_HOME%\bin\jpackage"
SET SQLITE_JAR=lib\sqlite-jdbc.jar

echo [1/4] Membersihkan folder lama...
if exist out rmdir /s /q out
if exist dist rmdir /s /q dist
mkdir out
mkdir dist

echo [2/4] Mengompilasi kode sumber...
dir /s /b src\*.java > sources.txt
%JAVAC% --module-path "%PATH_TO_FX%" --add-modules javafx.controls,javafx.fxml -cp "%SQLITE_JAR%" -d out @sources.txt
if %ERRORLEVEL% NEQ 0 ( echo Kompilasi gagal! & pause & exit /b 1 )
del sources.txt

echo [2b/4] Menyalin resource...
xcopy /s /q /y resources\* out\ >nul 2>&1

echo [3/4] Membuat file JAR...
%JAR% --create --file dist\ClassMate.jar --main-class com.classmate.Main -C out .

echo [4/4] Membuat Portable EXE (App Image)...
%JPACKAGE% --type app-image ^
  --dest dist ^
  --name ClassMateApp ^
  --main-jar ClassMate.jar ^
  --main-class com.classmate.Main ^
  --module-path "%PATH_TO_FX%" ^
  --add-modules javafx.controls,javafx.fxml ^
  --input dist ^
  --vendor "ClassMate" ^
  --icon resources\com\classmate\images\icon.ico 2>nul

echo.
echo ======================================================
echo BERHASIL! 
echo Folder Portable ada di: dist\ClassMateApp
echo Klik dua kali "ClassMateApp.exe" di dalam folder tersebut.
echo ======================================================
pause
