#!/usr/bin/env bash

set -e

./gradlew build generateClientStubs asciidoctor
./gradlew assemble --rerun-tasks
