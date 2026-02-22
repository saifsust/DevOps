FROM node:23.11-slim

#hadolint ignore=DL3008
RUN apt-get update \
    && apt-get install curl ca-certificates --no-install-recommends -y \
    && update-ca-certificates \
    && rm -rf /var/lib/apt/lists/*
#
#ARG service_version
#ENV SERVICE_VERSION=${service_version:-v1}

COPY package.json /opt/microservices/
COPY ratings.js /opt/microservices/
WORKDIR /opt/microservices
RUN npm install
## AWS set up
ENV SERVICE_VERSION="v2"
ENV DB_TYPE="mysql"

# Mysql Host name
ENV MYSQL_DB_PORT=3306
ENV MYSQL_DB_HOST="test.cafuqkquo0s1.us-east-1.rds.amazonaws.com"

# Credentials
ENV MYSQL_DB_USER=admin
ENV MYSQL_DB_PASSWORD=admin123456789

EXPOSE 9080
CMD ["node", "/opt/microservices/ratings.js", "9080"]

USER 1000
