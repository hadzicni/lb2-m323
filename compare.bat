@echo off
setlocal enabledelayedexpansion
set "ROOT=%~dp0"
set "OUT=%ROOT%compare-out"
if not exist "%OUT%" mkdir "%OUT%"
set "V1=%OUT%\v1.txt"
set "V2=%OUT%\v2.txt"

echo Running V1 (imperative)...
powershell -NoProfile -ExecutionPolicy Bypass -Command "& '%ROOT%v1-imperative\run.ps1' | Out-String | Set-Content -LiteralPath '%V1%' -Encoding UTF8"
if errorlevel 1 goto :diff

echo Running V2 (functional)...
powershell -NoProfile -ExecutionPolicy Bypass -Command "& '%ROOT%v2-functional\run.ps1' | Out-String | Set-Content -LiteralPath '%V2%' -Encoding UTF8"

:diff
echo Comparing outputs...
fc /n "%V1%" "%V2%"
if errorlevel 1 (
  echo Differences found. Saved outputs:
  echo   %V1%
  echo   %V2%
  exit /b 1
) else (
  echo Outputs are identical.
  echo Saved outputs:
  echo   %V1%
  echo   %V2%
  exit /b 0
)

