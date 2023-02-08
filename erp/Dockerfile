FROM openjdk:11-jdk
VOLUME /tmp
ARG JAR_FILE=build/libs/erp.jar
COPY ${JAR_FILE} erp.jar
ENTRYPOINT ["java","-jar","erp.jar"]