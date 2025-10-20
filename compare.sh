#!/usr/bin/env bash
set -euo pipefail
DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
OUT="$DIR/compare-out"
mkdir -p "$OUT"
V1="$OUT/v1.txt"
V2="$OUT/v2.txt"

echo "Running V1 (imperative)..."
bash "$DIR/v1-imperative/run.sh" > "$V1"

echo "Running V2 (functional)..."
bash "$DIR/v2-functional/run.sh" > "$V2"

echo "Comparing outputs..."
if diff -u "$V1" "$V2"; then
  echo "Outputs are identical."
  echo "Saved outputs:"
  echo "  $V1"
  echo "  $V2"
  exit 0
else
  echo "Differences found. Saved outputs:"
  echo "  $V1"
  echo "  $V2"
  exit 1
fi

