FROM jenkins/jenkins:jdk21

USER root
# Keys storage directory
RUN mkdir -p -m 755 /etc/apt/keyrings

# Maven Installation
RUN apt-get update && \
    apt-get install -y maven

# Docker CLI Installation
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

#Kubernetes CLI installation
RUN curl -fsSL https://pkgs.k8s.io/core:/stable:/v1.34/deb/Release.key | \
    gpg --dearmor -o /etc/apt/keyrings/kubernetes-apt-keyring.gpg
RUN echo 'deb [signed-by=/etc/apt/keyrings/kubernetes-apt-keyring.gpg] https://pkgs.k8s.io/core:/stable:/v1.34/deb/ /' | \
    tee /etc/apt/sources.list.d/kubernetes.list

RUN apt-get update && \
    apt-get install -y kubectl
RUN apt-mark hold kubectl

# Docker CLI certificate directory
ENV DOCKER_CERT_PATH=/home/.docker

EXPOSE 8080 50000

CMD ["jenkins.sh"]
