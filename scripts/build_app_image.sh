#!/bin/sh

echo "Build app image for different dockerfiles..."
docker build -t app-image:ubuntu -f Dockerfile-ubuntu-jammy-eclipse-temurin-jammy .
docker build -t app-image:alpine -f Dockerfile-alpine-eclipse-temurin-alpine .
docker build -t app-image:bookworm-slim-corretto -f Dockerfile-bookworm-slim-amazon-corretto .
docker build -t app-image:bookworm-slim-eclipse-temurin -f Dockerfile-bookworm-slim-eclipse-temurin .