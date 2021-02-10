#! /bin/bash

cd ..
git pull
./gradlew bootJar -Pprod jibDockerBuild -x webpack
docker-compose -f ../src/main/docker/app.yml up -d

