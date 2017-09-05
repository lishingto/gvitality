#!/bin/bash

cd /gvital
npm install
bower install
sed -i "1s|.*|var gitvitalUrl = $GVITAL_SERVER_URL|" app/scripts/config.js
grunt serve
