#!/usr/bin/env bash
set -euo pipefail
mkdir -p out
javac -d out -encoding UTF-8 $(find src -name "*.java")
java -cp out Main
