#!/bin/bash

pushd MailService && docker build -t stonksevent/mail-service . && popd

