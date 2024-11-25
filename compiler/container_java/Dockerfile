FROM openjdk:23-jdk-slim

WORKDIR /app

USER root

ADD . .

RUN chmod a+x ./main.java
RUN chmod a+x ./entrypoint.sh

ENTRYPOINT ["./entrypoint.sh"]