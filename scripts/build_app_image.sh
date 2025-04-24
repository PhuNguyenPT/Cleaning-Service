#!/bin/sh

echo "Build app image for different dockerfiles..."
docker build -t phunpt01/app-image:jammy -f Dockerfile-ubuntu-jammy-eclipse-temurin-jammy .
docker build -t phunpt01/app-image:alpine -f Dockerfile-alpine-eclipse-temurin-alpine .
docker build -t phunpt01/app-image:corretto-bookworm-slim -f Dockerfile-bookworm-slim-amazon-corretto .
docker build -t phunpt01/app-image:eclipse-temurin-bookworm-slim -f Dockerfile-bookworm-slim-eclipse-temurin .

./mvnw compile jib:dockerBuild \
  -Djib.from.image=eclipse-temurin:21-jre\
  -Djib.to.image=docker.io/phunpt01/app-image:jib-eclipse-temurin-21-jre \
  -Djib.container.jvmFlags="-XX:+UseParallelGC,-XX:MaxRAMPercentage=75" \
  -Djib.from.platforms=linux/amd64

./mvnw compile jib:dockerBuild \
  -Djib.from.image=eclipse-temurin:21-jre-jammy \
  -Djib.to.image=docker.io/phunpt01/app-image:jib-eclipse-temurin-21-jre-jammy \
  -Djib.container.jvmFlags="-XX:+UseParallelGC,-XX:MaxRAMPercentage=75" \
  -Djib.from.platforms=linux/amd64

./mvnw compile jib:dockerBuild \
  -Djib.from.image=amazoncorretto:21 \
  -Djib.to.image=docker.io/phunpt01/app-image:jib-amazoncorretto-21 \
  -Djib.container.jvmFlags="-XX:+UseParallelGC,-XX:MaxRAMPercentage=75" \
  -Djib.from.platforms=linux/amd64