#!/bin/sh

chmod +x scripts/update_run.sh
# Start all services
echo "Starting all services..."

docker compose --env-file docker-compose-jib.env -p cleaning-service-jib -f docker-compose-jib.yaml up -d