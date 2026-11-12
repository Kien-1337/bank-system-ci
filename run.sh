#!/bin/bash
set -e

if [ -f "./mvnw" ]; then
    ./mvnw clean checkstyle:check test
else
    mvn clean checkstyle:check test
fi