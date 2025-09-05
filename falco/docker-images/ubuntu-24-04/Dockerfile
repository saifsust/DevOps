FROM ubuntu:24.04

RUN apt-get update \
    && apt-get --fix-broken install
RUN apt-get install -y default-jdk curl iptables net-tools vim \
    && apt-get clean

COPY ./app  /app

CMD ["sleep", "infinity"]
