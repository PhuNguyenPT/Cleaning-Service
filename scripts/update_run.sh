#!/bin/sh
# This script is required to run kafka cluster (without zookeeper)

# Docker workaround: Remove check for KAFKA_ZOOKEEPER_CONNECT parameter
sed -i '/KAFKA_ZOOKEEPER_CONNECT/d' /etc/confluent/docker/configure

# Docker workaround: Ignore cub zk-ready
sed -i 's/cub zk-ready/echo ignore zk-ready/' /etc/confluent/docker/ensure

# KRaft required step: Format the storage directory with a new cluster ID
echo "kafka-storage format --ignore-formatted -t 45vUBEgyQjSywv_811biQw -c /etc/kafka/kafka.properties" >> /etc/confluent/docker/ensure