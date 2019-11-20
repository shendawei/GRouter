#!/usr/bin/env bash

./gradlew clean upload
cd ./buildsrc/
../gradlew clean upload

echo "done"
