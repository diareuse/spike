#!/bin/bash
set -euo pipefail
cd "$(dirname "$0")/.."
only=${1:-}

bash spike-benchmark/publish.sh

rm -rf spike-benchmark/output
./gradlew -p spike-benchmark :generator:run
./gradlew -p spike-benchmark :runtime:test jar

gradle_version=$(sed -n 's#.*gradle-\(.*\)-bin.zip#\1#p' gradle/wrapper/gradle-wrapper.properties)
gradle-profiler --benchmark \
  --project-dir spike-benchmark \
  --gradle-user-home "$HOME/.gradle" \
  --output-dir spike-benchmark/output/build \
  --scenario-file spike-benchmark/scenarios.conf \
  --gradle-version "$gradle_version" \
  --warmups 10 \
  --iterations 10 \
  ${only:+"clean_${only//-/}" "abi_${only//-/}" "nonabi_${only//-/}"}

./gradlew -p spike-benchmark :runtime:mainBenchmark :runtime:coldBenchmark ${only:+"-PbenchmarkOnly=$only"}
mkdir -p spike-benchmark/output/runtime
for configuration in main cold; do
  cp "$(ls -td spike-benchmark/runtime/build/reports/benchmarks/$configuration/*/ | head -1)main.json" \
    "spike-benchmark/output/runtime/$configuration.json"
done

python3 spike-benchmark/report.py spike-benchmark
