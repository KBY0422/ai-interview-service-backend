# =========================
# 1. Build stage
# =========================
FROM gradle:8.5-jdk17 AS build
WORKDIR /app

# Gradle wrapper & 설정 복사
COPY gradlew gradlew.bat ./
COPY gradle ./gradle
COPY build.gradle settings.gradle ./

RUN chmod +x gradlew

# 전체 소스 복사
COPY . .

# build (여기서 한 번만 실행)
RUN ./gradlew clean build -x test --no-daemon

# =========================
# 2. Runtime stage
# =========================
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
