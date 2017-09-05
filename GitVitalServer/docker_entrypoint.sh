#!/bin/bash

cd /
unzip db.zip -d /data
mongod --fork --syslog
cd /usr/local/tomcat
catalina.sh run
