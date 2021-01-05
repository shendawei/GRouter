#!/usr/bin/env bash

set -e

clear

if [ -n "$1" ]; then
    git tag $1 -m "release $1"
    git push origin $1
else
    echo "please input Tag [ ./tag.sh x.x.x]"
fi