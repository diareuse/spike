#!/bin/bash
# Publishes the Spike artifacts the benchmark graphs build with, as version 0.0.0-benchmark in Maven Local.
set -euo pipefail
cd "$(dirname "$0")/.."

./gradlew \
  :spike-core:publishJvmPublicationToMavenLocal \
  :spike-core:publishKotlinMultiplatformPublicationToMavenLocal \
  :spike-compiler:publishMavenPublicationToMavenLocal \
  :spike-compiler-androidx:publishMavenPublicationToMavenLocal \
  :spike-compiler-conversion:publishMavenPublicationToMavenLocal \
  -PVERSION_NAME=0.0.0-benchmark \
  -PsignAllPublications=false
