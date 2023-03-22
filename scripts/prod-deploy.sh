#!/bin/bash

BUILD_JAR=$(ls /var/www/prod/build/libs/*.jar)
JAR_NAME=$(basename $BUILD_JAR)
echo ">>> build 파일명: $JAR_NAME" >> /var/www/prod/prod-deploy.log

echo ">>> build 파일 복사" >> /var/www/prod/prod-deploy.log
DEPLOY_PATH=/var/www/prod/build/libs/
cp $BUILD_JAR $DEPLOY_PATH

echo ">>> 현재 실행중인 애플리케이션 pid 확인" >> /var/www/prod/prod-deploy.log
CURRENT_PID=$(pgrep -f $JAR_NAME)

echo ">>> $CURRENT_PID"

if [ -z $CURRENT_PID ]
then
  echo ">>> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> /var/www/prod/prod-deploy.log
else
  #kill -15 $CURRENT_PID 출력
  fuser -k 8000/tcp
  echo ">>> kill -15 $CURRENT_PID" >> /var/www/prod/prod-deploy.log
  sleep 5
fi

source ~/.bashrc

echo ">>>환경변수 주입" >> /var/www/prod/prod-deploy.log

DEPLOY_JAR=$DEPLOY_PATH$JAR_NAME
echo ">>> DEPLOY_JAR 배포"    >> /var/www/prod/prod-deploy.log
nohup java -jar -Dspring.profiles.active=prod $DEPLOY_JAR >> /var/www/prod/prod-deploy.log 2>/var/www/prod/prod-deploy_err.log &
