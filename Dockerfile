# Build
FROM --platform=linux/amd64 gradle:8.8-jdk21-alpine AS build
WORKDIR /home/gradle/project

COPY gradle gradle
COPY gradlew .
COPY gradle.properties .
COPY settings.gradle.kts .
COPY build.gradle.kts .
COPY src src

RUN gradle clean bootJar -x test --no-daemon

# Package
FROM --platform=linux/amd64 bellsoft/liberica-openjdk-alpine-musl:21
WORKDIR /app
COPY --from=build /home/gradle/project/build/libs/*.jar app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]
