#!/bin/sh

chmod +x scripts/update_run.sh
# Start all services
echo "Starting all services..."

docker compose --env-file docker-compose.env -p cleaning-service -f docker-compose.yaml up -d

#docker compose --env-file docker-compose-jib.env -p cleaning-service -f docker-compose.yaml up -d