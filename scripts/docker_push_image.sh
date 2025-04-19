#!/bin/sh

echo "Push all images to docker hub..."

# Push
docker push phunpt01/app-image:jammy
docker push phunpt01/app-image:alpine
docker push phunpt01/app-image:corretto-bookworm-slim
docker push phunpt01/app-image:eclipse-temurin-bookworm-slim