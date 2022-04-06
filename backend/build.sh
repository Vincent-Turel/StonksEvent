#!/bin/bash

echo "Building the backend package"
pushd ../../repository/isa-devops-21-22-team-h-2022/backend && mvn clean package && popd
echo "Done"

# building the docker image
docker build --build-arg JAR_FILE=../../repository/isa-devops-21-22-team-h-2022/backend/event-app/target/event-app-0.0.1-SNAPSHOT.jar -t stonksevent/spring-backend .
