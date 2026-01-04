# 1. Java 17 런타임
FROM eclipse-temurin:17-jdk

# 2. 컨테이너 내부 작업 디렉토리
WORKDIR /app

# 3. 빌드된 JAR 파일 복사
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# 4. 포트 노출
EXPOSE 8080

# 5. 실행 명령
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
