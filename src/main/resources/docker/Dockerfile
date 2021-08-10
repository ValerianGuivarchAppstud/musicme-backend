FROM openjdk:11.0.6-jre-slim

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    APPLICATION_SLEEP=0

# add directly the war
ADD *.jar /app.jar

# set container to Europe/Paris timezone
ENV TZ=Europe/Paris
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

VOLUME /tmp
EXPOSE 8080
CMD echo "The application will start in ${APPLICATION_SLEEP}s..." && \
    sleep ${APPLICATION_SLEEP} && \
    java -Djava.security.egd=file:/dev/./urandom -jar /app.jar
