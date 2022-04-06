#!/bin/bash

#Preparing environment
./compile.sh

docker build -t stonksevent/mail-service .