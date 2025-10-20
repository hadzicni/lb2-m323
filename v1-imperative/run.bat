@echo off
setlocal
if not exist out mkdir out
for /r src %%f in (*.java) do (
  if not defined files set files=%%f
  if defined files set files=!files! %%f
)
javac -d out -encoding UTF-8 %files%
if errorlevel 1 exit /b %errorlevel%
java -cp out Main
