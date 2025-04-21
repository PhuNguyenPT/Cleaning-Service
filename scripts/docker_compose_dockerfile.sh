#!/bin/sh

chmod +x scripts/update_run.sh
# Start all services
echo "Starting all services..."

docker compose --env-file docker-compose.env -p cleaning-service-dockerfile -f docker-compose.yaml up -d