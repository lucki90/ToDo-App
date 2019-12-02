#!/usr/bin/env bash

echo "Deleting all containers..."
docker rm -f $(docker ps -a -q)
echo "Deleting all images..."
docker rmi -f $(docker images -q)
echo "Deleting all volumes"
docker volume prune -f

echo "List all containers:"
docker ps -a
echo "List all images:"
docker images
echo "List all volumes:"
docker volume ls