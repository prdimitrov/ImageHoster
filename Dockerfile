FROM openjdk:17-jdk-alpine
MAINTAINER pesho
COPY target/imagehoster-0.0.1-SNAPSHOT.jar imagehoster-0.0.1-SNAPSHOT.jar
COPY target/classes/bassheads-48d43-firebase-adminsdk-n6p6f-680a95a219.json src/main/resources/bassheads-48d43-firebase-adminsdk-n6p6f-680a95a219.json

ENTRYPOINT ["java","-jar","/imagehoster-0.0.1-SNAPSHOT.jar"]