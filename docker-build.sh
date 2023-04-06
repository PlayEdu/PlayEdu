#!/bin/sh

echo '设置M2_HOME...'

cp -r docker/.m2 /root

ls /root/.m2

echo '开始打包...'

export MAVEN_OPTS=-Dmaven.test.skip=true

/app/mvnw clean package