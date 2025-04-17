#!/bin/sh

chmod +x scripts/update_run.sh
# Start all services
echo "Starting all services..."
docker compose -f docker-compose.yaml up -d