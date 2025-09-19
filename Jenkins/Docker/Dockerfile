FROM jenkins/jenkins:jdk21

USER root

RUN apt-get update && \
    apt-get install -y \
    apt-transport-https \
    ca-certificates \
    curl \
    gnupg2 \
    software-properties-common

RUN curl -fsSL https://download.docker.com/linux/debian/gpg | apt-key add -

RUN echo "deb [arch=amd64] https://download.docker.com/linux/debian $(lsb_release -cs) stable" > /etc/apt/sources.list.d/docker.list

RUN apt-get update && \
    apt-get install -y docker-ce-cli


ARG DOCKER_GROUP_ID
RUN if [ -n "$DOCKER_GROUP_ID" ]; then groupadd -g $DOCKER_GROUP_ID docker; fi
RUN if [ -n "$DOCKER_GROUP_ID" ]; then usermod -aG docker jenkins; fi

COPY certs /home/certs

ENV DOCKER_CERT_PATH=/home/certs
ENV DOCKER_TLS_VERIFY=1

EXPOSE 8080 50000

CMD ["jenkins.sh"]
