@echo off
setlocal enabledelayedexpansion
set "ROOT=%~dp0"
if not exist "%ROOT%out" mkdir "%ROOT%out"
set "files="
for /r "%ROOT%src" %%f in (*.java) do (
  if not defined files set "files=%%f"
  if defined files set "files=!files! %%f"
)
javac -d "%ROOT%out" -encoding UTF-8 %files%
if errorlevel 1 exit /b %errorlevel%
pushd "%ROOT%" >nul
java -cp "%ROOT%out" Main
popd >nul
