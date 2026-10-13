#!/bin/bash
set -euo pipefail
cd "$(dirname "$0")/.."

./gradlew \
  :spike-core:publishJvmPublicationToMavenLocal \
  :spike-core:publishKotlinMultiplatformPublicationToMavenLocal \
  :spike-compiler:publishMavenPublicationToMavenLocal \
  -PVERSION_NAME=0.0.0-benchmark \
  -PsignAllPublications=false

rm -rf spike-benchmark/output
./gradlew -p spike-benchmark :generator:run :runtime:test jar

gradle_version=$(sed -n 's#.*gradle-\(.*\)-bin.zip#\1#p' gradle/wrapper/gradle-wrapper.properties)
gradle-profiler --benchmark \
  --project-dir spike-benchmark \
  --gradle-user-home "$HOME/.gradle" \
  --output-dir spike-benchmark/output/build \
  --scenario-file spike-benchmark/scenarios.conf \
  --gradle-version "$gradle_version" \
  --warmups 3 \
  --iterations 10

./gradlew -p spike-benchmark :runtime:mainBenchmark :runtime:coldBenchmark
mkdir -p spike-benchmark/output/runtime
for configuration in main cold; do
  cp "$(ls -td spike-benchmark/runtime/build/reports/benchmarks/$configuration/*/ | head -1)main.json" \
    "spike-benchmark/output/runtime/$configuration.json"
done

python3 spike-benchmark/report.py spike-benchmark
