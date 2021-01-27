#!/usr/bin/env bash

cd src-repo
./gradlew test

cd frontend
yarn install
yarn test