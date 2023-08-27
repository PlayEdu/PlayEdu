#!/bin/sh

echo '开始打包...'

export MAVEN_OPTS=-Dmaven.test.skip=true

/app/mvnw clean package