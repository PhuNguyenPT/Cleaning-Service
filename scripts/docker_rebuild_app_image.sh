#!/bin/sh

chmod +x scripts/build_app_image.sh
./scripts/build_app_image.sh

echo "Remove old service and compose"
docker compose -f docker-compose.yaml stop app
docker compose -f docker-compose.yaml rm -f app
docker compose -f docker-compose.yaml up -d app