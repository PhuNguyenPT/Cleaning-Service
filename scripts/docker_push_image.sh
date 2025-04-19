#!/bin/sh

echo "Push all images to docker hub..."
# Tag
docker tag app-image:ubuntu phunpt01/app-image:ubuntu
docker tag app-image:alpine phunpt01/app-image:alpine
docker tag app-image:bookworm-slim-corretto phunpt01/app-image:bookworm-slim-corretto
docker tag app-image:bookworm-slim-eclipse-temurin phunpt01/app-image:bookworm-slim-eclipse-temurin

# Push
docker push phunpt01/app-image:ubuntu
docker push phunpt01/app-image:alpine
docker push phunpt01/app-image:bookworm-slim-corretto
docker push phunpt01/app-image:bookworm-slim-eclipse-temurin