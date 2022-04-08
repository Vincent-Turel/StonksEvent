#!/bin/bash

cd backend && chmod u+x build.sh && ./build.sh
cd ../cli && chmod u+x build.sh && ./build.sh
cd ..
docker-compose up -d