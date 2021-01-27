#!/usr/bin/env bash

set -e


pushd src-repo/frontend

yarn install
yarn tslint --project ./tsconfig.json
