# ---- Build stage ----
FROM maven:3.9.8-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY . .
# Build module main và dependencies
RUN mvn -B -q -DskipTests package -pl main -am

# ---- Run stage ----
FROM eclipse-temurin:17-jre
WORKDIR /app
# Đổi tên nếu JAR khác
COPY main/target/main-0.0.1-SNAPSHOT.jar app.jar

ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS=""
EXPOSE 8080
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
