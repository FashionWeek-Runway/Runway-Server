#!/bin/bash

BUILD_JAR=$(ls /var/www/dev/build/libs/*.jar)
JAR_NAME=$(basename $BUILD_JAR)
echo ">>> build 파일명: $JAR_NAME" >> /var/www/dev/deploy.log

echo ">>> build 파일 복사" >> /var/www/dev/deploy.log
DEPLOY_PATH=/var/www/dev/build/libs/
cp $BUILD_JAR $DEPLOY_PATH

echo ">>> 현재 실행중인 애플리케이션 pid 확인" >> /var/www/dev/deploy.log
CURRENT_PID=$(pgrep -f $JAR_NAME)

echo ">>> $CURRENT_PID"

if [ -z $CURRENT_PID ]
then
  echo ">>> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> /var/www/dev/deploy.log
else
  fuser -k 9000/tcp
  echo ">>> kill -15 $CURRENT_PID" >> /var/www/dev/deploy.log
  sleep 5
fi

source ~/.bashrc

echo ">>>환경변수 주입" >> /var/www/dev/deploy.log

DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
echo ">>> DEPLOY_JAR 배포"    >> /var/www/dev/deploy.log
nohup java -jar -Dspring.profiles.active=local $DEPLOY_JAR >> /var/www/dev/deploy.log 2>/var/www/dev/deploy_err.log &