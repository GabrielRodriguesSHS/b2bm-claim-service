#!/bin/bash

echo "Running Google Java Format..."
./mvnw fmt:format

echo "Running Checkstyle..."
./mvnw checkstyle:check

echo "Running SpotBugs..."
./mvnw spotbugs:check

echo "All linting tools and formatter completed!"