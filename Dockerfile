# Build
FROM --platform=linux/amd64 maven:3.9.9-eclipse-temurin-21-alpine AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

# Package
FROM --platform=linux/amd64 bellsoft/liberica-openjdk-alpine-musl:21
COPY --from=build /home/app/target/*.jar /usr/local/lib/app.jar
ENTRYPOINT ["java","-jar","/usr/local/lib/app.jar"]
