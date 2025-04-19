#!/bin/sh

echo "Build app image for different dockerfiles..."
docker build -t phunpt01/app-image:jammy -f Dockerfile-ubuntu-jammy-eclipse-temurin-jammy .
docker build -t phunpt01/app-image:alpine -f Dockerfile-alpine-eclipse-temurin-alpine .
docker build -t phunpt01/app-image:corretto-bookworm-slim -f Dockerfile-bookworm-slim-amazon-corretto .
docker build -t phunpt01/app-image:eclipse-temurin-bookworm-slim -f Dockerfile-bookworm-slim-eclipse-temurin .