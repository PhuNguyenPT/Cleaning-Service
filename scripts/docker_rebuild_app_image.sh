#!/bin/sh

chmod +x scripts/build_app_image.sh
./scripts/build_app_image.sh

echo "Remove old service and compose"
docker-compose stop app && docker-compose rm -f app && docker compose up -f docker-compose.yaml -d app