#!/bin/bash

#Preparing environment
echo "Compiling the SE (StonksEvent) Spring cli"
pushd ../../repository/isa-devops-21-22-team-h-2022/cli && mvn clean package && popd
echo "Done"

# building the docker image
docker build --build-arg JAR_FILE=../../repository/isa-devops-21-22-team-h-2022/cli/target/cli-0.0.1-SNAPSHOT.jar -t stonksevent/spring-cli .