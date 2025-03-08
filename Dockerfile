FROM openjdk:21-jdk-slim

# JAR 파일 복사 및 실행
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} 0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/0.0.1-SNAPSHOT.jar"]