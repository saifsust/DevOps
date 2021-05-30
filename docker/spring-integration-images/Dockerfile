FROM openjdk:11
ARG DEBIAN_FRONTEND=noninteractive
WORKDIR /opt
COPY .  /opt
RUN apt-get update && apt-get install -y apt-utils 2>&1 | \
    grep -v "^debconf: delaying package configuration, since apt-utils.*"
RUN apt-get -y upgrade
RUN apt-get -y install maven
CMD [ "java", "-jar","target/online.jar"]
