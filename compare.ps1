$ErrorActionPreference = 'Stop'
Set-StrictMode -Version Latest

$root = $PSScriptRoot
if (-not $root) { $root = Split-Path -Parent $MyInvocation.MyCommand.Path }
$v1 = Join-Path $root 'v1-imperative/run.ps1'
$v2 = Join-Path $root 'v2-functional/run.ps1'
$outDir = Join-Path $root 'compare-out'
if (!(Test-Path -LiteralPath $outDir)) { New-Item -ItemType Directory -Path $outDir | Out-Null }
$v1Out = Join-Path $outDir 'v1.txt'
$v2Out = Join-Path $outDir 'v2.txt'

Write-Host 'Running V1 (imperative)...'
$o1 = & $v1 | Out-String
Set-Content -LiteralPath $v1Out -Value $o1 -Encoding UTF8

Write-Host 'Running V2 (functional)...'
$o2 = & $v2 | Out-String
Set-Content -LiteralPath $v2Out -Value $o2 -Encoding UTF8

Write-Host "Comparing outputs..."
$l1 = ($o1 -split "`r?`n")
$l2 = ($o2 -split "`r?`n")
$diff = Compare-Object -ReferenceObject $l1 -DifferenceObject $l2 -IncludeEqual:$false

if (-not $diff -or $diff.Count -eq 0) {
  Write-Host 'Outputs are identical.' -ForegroundColor Green
  Write-Host "Saved outputs:" (Resolve-Path $v1Out), (Resolve-Path $v2Out)
  exit 0
} else {
  Write-Host 'Differences found:' -ForegroundColor Yellow
  $diff | Format-Table -AutoSize | Out-String | Write-Host
  Write-Host "Saved outputs:" (Resolve-Path $v1Out), (Resolve-Path $v2Out)
  exit 1
}

