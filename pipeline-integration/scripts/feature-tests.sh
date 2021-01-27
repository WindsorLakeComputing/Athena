#!/bin/bash

set -e -x

# start the java backend
env SPRING_PROFILES_ACTIVE=test SERVER_PORT=8080 java -jar ./snapshot/athena*.jar &> ./temp &

set +x

#Wait for Java backend to come up
countdown=100
until grep -m 1 "Started AthenaApplicationKt\|Build failed" ./temp || (( $countdown < 0 ));do
    echo "Starting server..."
    sleep 2
    ((countdown--))
done

#Clean up tmp
rm ./temp

set -x

pushd src-repo/frontend

apt-get update
apt-get install xvfb -y
apt-get install libxss1
apt install libgconf2-4 -y
yarn install
yarn cypress run --env testUrl=http://localhost:8080

popd

exit $?
