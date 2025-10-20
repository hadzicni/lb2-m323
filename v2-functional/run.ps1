$ErrorActionPreference = 'Stop'
if (!(Test-Path -LiteralPath 'out')) { New-Item -ItemType Directory -Path 'out' | Out-Null }
$files = Get-ChildItem -Recurse -File -Filter *.java | ForEach-Object { $_.FullName }
& javac -d out -encoding UTF-8 @files
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
& java -cp out Main
