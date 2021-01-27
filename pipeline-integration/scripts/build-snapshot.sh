#!/usr/bin/env bash

set -e
source pipeline-repo/scripts/commons.sh

# Script Variables
FULL_DIR=$(readlink -f "$0")
SCRIPT_DIR=$(dirname $FULL_DIR)
BASE_DIR=$(pwd)

# Other Globals
GRADLE_USER_HOME="${BASE_DIR}/.gradle"
current_version="0.0.0"
export BUILD_VERSION="$(calculate_next_version $current_version "patch")-SNAPSHOT"

pushd src-repo
./gradlew build -x test
cp build/libs/Athena-${BUILD_VERSION}.jar ../build-output/
echo $BUILD_VERSION >> ../build-output/version
