#!/bin/zsh
set -e
./gradlew publishToMavenLocal
gradle-profiler --benchmark \
  --project-dir spike-benchmark \
  --output-dir spike-benchmark/output \
  --gradle-user-home ~/.gradle \
  --warmups 5 \
  --iterations 10 \
  --gradle-version 9.4.0 \
  clean kspKotlin