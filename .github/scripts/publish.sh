#!/bin/bash
# Get the version from the release tag name
version=$1
echo "release version: $version"
# Update the version in build.gradle
sed -i "s/version = '.*'/version = '$version'/" build.gradle
# Publish to JitPack
./gradlew clean build publish -Pversion=$version