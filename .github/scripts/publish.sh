#!/bin/bash
# Get the version from the release tag name
version="${{ github.event.release.tag_name }}"
# Update the version in build.gradle
sed -i "s/version = '.*'/version = '$version'/" build.gradle
# Publish to JitPack
./gradlew clean build publish -Pgroup=com.example -Pversion=$version