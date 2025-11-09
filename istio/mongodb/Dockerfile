FROM mongo:8.0.8
WORKDIR /app/data/
COPY ratings_data.json /app/data/
COPY script.sh /docker-entrypoint-initdb.d/

RUN groupadd -r mongogroup && useradd -r --uid=101 -g mongogroup mongodbUser

RUN chown -R mongodbUser:mongogroup /app

RUN chmod +x /docker-entrypoint-initdb.d/script.sh

USER 101