#!/bin/bash
set -e
VERSION=$(grep -m2 '<version>' pom.xml | sed 's/[[:space:]]//g' | sed -E 's/<.{0,1}version>//g' | sed -n 2p)
echo "Version = $VERSION"
docker build -t "c8n.io/armel-evelyn-jondongamga/zepe-auth:$VERSION" .