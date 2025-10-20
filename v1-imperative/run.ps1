$ErrorActionPreference = 'Stop'
$root = $PSScriptRoot
if (-not $root) { $root = Split-Path -Parent $MyInvocation.MyCommand.Path }
$src = Join-Path $root 'src'
$out = Join-Path $root 'out'
if (!(Test-Path -LiteralPath $out)) { New-Item -ItemType Directory -Path $out | Out-Null }
$files = Get-ChildItem -Path $src -Recurse -File -Filter *.java | ForEach-Object { $_.FullName }
& javac -d $out -encoding UTF-8 @files
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
Push-Location $root
try { & java -cp $out Main } finally { Pop-Location }
