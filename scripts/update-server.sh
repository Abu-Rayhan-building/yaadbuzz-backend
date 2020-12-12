#! /bin/bash

cd ..
git pull
npm install
npm run webpack:build
./gradlew bootJar -Pprod jibDockerBuild -x webpack
docker-compose -f ../src/main/docker/app.yml up -d
