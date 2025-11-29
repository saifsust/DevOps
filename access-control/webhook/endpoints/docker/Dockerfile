FROM openjdk:26-ea-25-jdk-oraclelinux8
WORKDIR /home/app
COPY ./*.jar /home/app/app.jar

RUN groupadd  --gid 1000 -r appgroup && \
    adduser --home=/home/app --shell=/usr/bin/bash --group=appgroup --uid=1000 appuser

RUN chown -R appuser:appgroup /home/app/app.jar
RUN chmod +X /home/app/app.jar
USER appuser
EXPOSE 8081
CMD ["java", "-jar", "app.jar"]