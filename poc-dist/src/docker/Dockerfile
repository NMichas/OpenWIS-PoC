FROM blacklabelops/java:ubuntu.server-jre.8.102
MAINTAINER European Dynamics SA <info@eurodyn.com>

ENV DOCKERIZE_VERSION v0.2.0

COPY poc-dist-1.0.0-SNAPSHOT.tar.gz /tmp
RUN apt-get update && \
    apt-get install -y wget && \
    wget https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz && \
    tar -C /usr/local/bin -xzvf dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz && \
    mkdir /opt/openwis && \
    tar xvfz /tmp/poc-dist-1.0.0-SNAPSHOT.tar.gz -C /opt/openwis --strip 1 && \
    rm /tmp/poc-dist-1.0.0-SNAPSHOT.tar.gz