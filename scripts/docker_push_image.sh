#!/bin/sh

echo "Push all images to docker hub..."

# Push
docker push phunpt01/app-image:jammy
docker push phunpt01/app-image:alpine
docker push phunpt01/app-image:corretto-bookworm-slim
docker push phunpt01/app-image:eclipse-temurin-bookworm-slim
docker push phunpt01/app-image:jib-eclipse-temurin-21-jre
docker push phunpt01/app-image:jib-eclipse-temurin-21-jre-jammy
docker push phunpt01/app-image:jib-amazoncorretto-21.0.7
docker push phunpt01/app-image:jib-amazoncorretto-21
