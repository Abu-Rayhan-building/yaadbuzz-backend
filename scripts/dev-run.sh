#! /bin/bash

git pull
docker-compose -f ../src/main/docker/postgresql.yml up -d
docker-compose -f ../src/main/docker/mail.yml up -d
sleep 5
../gradlew -x webpack
