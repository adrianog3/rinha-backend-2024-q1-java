#!/usr/bin/env bash

function jvmBuild() {
  ./mvnw package -Dmaven.test.skip=true

  docker build -t rinha-backend-2024-q1:latest -f docker/Dockerfile.jvm .
}

function nativeBuild() {
  ./mvnw clean package -Pnative -Dquarkus.native.container-build=true

  docker build -t rinha-backend-2024-q1:latest -f docker/Dockerfile.native .
}

docker-compose down -v \
  && nativeBuild \
  && docker-compose --project-name rinha up -d
