#!/bin/bash

source ~/.bash_profile

PROJECT_ROOT="/home/ubuntu/app"
JAR_FILE=$PROJECT_ROOT/build/libs/PerformanceKeeper-0.0.1-SNAPSHOT.jar
APP_LOG="$PROJECT_ROOT/app.log"
ERROR_LOG="$PROJECT_ROOT/error.log"

echo "Now, port 8080 should be clear!"
# 1. 8080 포트가 사용 중이면 프로세스 종료
if lsof -Pi :8080 -sTCP:LISTEN -t >/dev/null ; then
    echo "Port 8080 is in use. Trying to stop the process..."
    # 여기에 필요한 프로세스 종료 명령어를 추가하세요.
    kill -9 $(lsof -ti :8080)
fi
echo "Port 8080 is cleared!"

# 2. ~/app/build/libs 디렉토리로 이동하고, nohup으로 실행
echo "change dir"
cd ~/app/build/libs
chmod +x $JAR_FILE
if [ -f PerformanceKeeper-0.0.1-SNAPSHOT.jar ]; then
    nohup  java -jar PerformanceKeeper-0.0.1-SNAPSHOT.jar > $APP_LOG 2> $ERROR_LOG &
                                             CURRENT_PID=$(pgrep -f $JAR_FILE)
                                             TIME_NOW=$(date +%s)

                                             echo "Started $JAR_FILE with PID: $CURRENT_PID on: $TIME_NOW" >> "$PROJECT_ROOT/deploy.log"
else
    echo "Error: failed...T_T"
fi
