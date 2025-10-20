#!/usr/bin/env bash
set -euo pipefail
DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
mkdir -p "$DIR/out"
javac -d "$DIR/out" -encoding UTF-8 $(find "$DIR/src" -name "*.java")
cd "$DIR"
java -cp "$DIR/out" Main
