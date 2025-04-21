#!/bin/sh

echo "Build app image for different dockerfiles..."
docker build -t phunpt01/app-image:jammy -f Dockerfile-ubuntu-jammy-eclipse-temurin-jammy .
docker build -t phunpt01/app-image:alpine -f Dockerfile-alpine-eclipse-temurin-alpine .
docker build -t phunpt01/app-image:corretto-bookworm-slim -f Dockerfile-bookworm-slim-amazon-corretto .
docker build -t phunpt01/app-image:eclipse-temurin-bookworm-slim -f Dockerfile-bookworm-slim-eclipse-temurin .

./mvnw compile jib:dockerBuild \
  -Djib.from.image=eclipse-temurin:21-jre@sha256:9fe87739492c449b9d4e036ac0a79a7e2c75ce36c99d4ba4a0666a5d4e409e73 \
  -Djib.to.image=docker.io/phunpt01/app-image:jib-eclipse-temurin-21-jre \
  -Djib.container.jvmFlags="-XX:+UseParallelGC,-XX:MaxRAMPercentage=75" \
  -Djib.from.platforms=linux/amd64

./mvnw compile jib:dockerBuild \
  -Djib.from.image=eclipse-temurin:21-jre-jammy@sha256:903dcea12637919198041da42cd270d549656c06bfda9126be36f182bb0de72d \
  -Djib.to.image=docker.io/phunpt01/app-image:jib-eclipse-temurin-21-jre-jammy \
  -Djib.container.jvmFlags="-XX:+UseParallelGC,-XX:MaxRAMPercentage=75" \
  -Djib.from.platforms=linux/amd64

./mvnw compile jib:dockerBuild \
  -Djib.from.image=amazoncorretto:21.0.7@sha256:674d24b818b8e889fb530a98c250f8638f14116f34eddbe0191f74e4dbc12aa0 \
  -Djib.to.image=docker.io/phunpt01/app-image:jib-amazoncorretto-21.0.7 \
  -Djib.container.jvmFlags="-XX:+UseParallelGC,-XX:MaxRAMPercentage=75" \
  -Djib.from.platforms=linux/amd64

./mvnw compile jib:dockerBuild \
  -Djib.from.image=amazoncorretto:21@sha256:674d24b818b8e889fb530a98c250f8638f14116f34eddbe0191f74e4dbc12aa0 \
  -Djib.to.image=docker.io/phunpt01/app-image:jib-amazoncorretto-21 \
  -Djib.container.jvmFlags="-XX:+UseParallelGC,-XX:MaxRAMPercentage=75" \
  -Djib.from.platforms=linux/amd64