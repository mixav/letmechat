FROM adoptopenjdk/openjdk11:jdk-11.0.6_10-alpine-slim
ADD /build/libs/letmechat*.jar app.jar
ENTRYPOINT [ "sh", "-c", "java -jar /app.jar" ]