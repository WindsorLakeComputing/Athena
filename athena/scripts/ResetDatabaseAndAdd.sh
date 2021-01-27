#!/bin/bash
set -e
set +x

echo "Wiping database"
pwd
psql postgres://athena:password@127.0.0.1:5432/athena -f ../scripts/ClearTables.sql

if [ $# -eq 1 ]
then
    echo "Reseeding"
    psql postgres://athena:password@127.0.0.1:5432/athena -f $1
fi
