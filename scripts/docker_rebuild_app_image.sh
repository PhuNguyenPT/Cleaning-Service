#!/bin/sh

chmod +x scripts/build_app_image.sh
./scripts/build_app_image.sh

echo "Remove old service and compose"
# docker file image
docker compose --env-file docker-compose.env -p cleaning-service -f docker-compose.yaml stop app
docker compose --env-file docker-compose.env -p cleaning-service -f docker-compose.yaml  rm -f app
docker compose --env-file docker-compose.env -p cleaning-service -f docker-compose.yaml up -d app

# jib image
#docker compose --env-file docker-compose-jib.env -p cleaning-service -f docker-compose.yaml stop app
#docker compose --env-file docker-compose-jib.env -p cleaning-service -f docker-compose.yaml  rm -f app
#docker compose --env-file docker-compose-jib.env -p cleaning-service -f docker-compose.yaml up -d app