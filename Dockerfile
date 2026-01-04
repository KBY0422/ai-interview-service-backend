# =========================
# 1. Build stage
# =========================
FROM gradle:8.5-jdk21 AS build
WORKDIR /app

# 캐시 최적화
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
RUN gradle dependencies --no-daemon

# 소스 복사 & 빌드
COPY . .
RUN gradle clean build -x test --no-daemon

# =========================
# 2. Runtime stage
# =========================
FROM eclipse-temurin:21-jre
WORKDIR /app

# 빌드 산출물 복사
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
