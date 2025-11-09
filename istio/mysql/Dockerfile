FROM mysql:5.7.44
# MYSQL_ROOT_PASSWORD must be supplied as an env var

COPY ./mysqldb-init.sql /docker-entrypoint-initdb.d

EXPOSE 3306

USER 1001