#!/bin/bash
# Builds and runs the benchmark on a small graph, so a change that would break the benchmark fails before it lands.
set -euo pipefail
cd "$(dirname "$0")/.."

bash spike-benchmark/publish.sh
python3 -m unittest discover -s spike-benchmark -p 'test_*.py'
./gradlew -p spike-benchmark :generator:test :generator:run -PbenchmarkSize=120
./gradlew -p spike-benchmark :runtime:test :runtime:smokeBenchmark
