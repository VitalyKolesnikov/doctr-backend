FROM maven:3.6.3 AS maven
LABEL MAINTAINER="vitaly.kolesnikov88@gmail.com"
WORKDIR /usr/src/app
COPY . /usr/src/app
RUN mvn package -DskipTests

FROM adoptopenjdk/openjdk11:alpine-jre
WORKDIR /opt/app
COPY --from=maven /usr/src/app/target/*.jar /opt/app/app.jar
ENTRYPOINT ["java","-jar","app.jar"]