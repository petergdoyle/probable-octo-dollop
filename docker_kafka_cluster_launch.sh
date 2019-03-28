#!/usr/bin/env bash
docker-compose -f bitnami-docker-kafka/docker-compose.yml up -d
docker-compose -f bitnami-docker-kafka/docker-compose.yml ps
