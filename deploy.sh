#!/usr/bin/env bash

function jvmBuild() {
  ./mvnw package -Dmaven.test.skip=true

  docker build -t adrianog3/rinha-backend-2024-q1:latest -f docker/Dockerfile.jvm .
}

function nativeBuild() {
  ./mvnw clean package -Dmaven.test.skip=true -Pnative -Dquarkus.native.container-build=true

  docker build -t adrianog3/rinha-backend-2024-q1:latest -f docker/Dockerfile.native .
}

docker-compose down -v \
  && nativeBuild \
  && docker-compose up -d
