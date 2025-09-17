FROM nginx:1.28.0-alpine

# Create a non-root user and group
RUN addgroup --gid 1001 vagrant && \
    adduser --system --uid 1001 --ingroup vagrant vagrant

COPY app/ /etc/nginx
RUN chown -R vagrant:vagrant /usr/share/nginx/html && \
    chown -R vagrant:vagrant /var/cache/nginx && \
    chown -R vagrant:vagrant /var/log/nginx && \
    chown -R vagrant:vagrant /etc/nginx/conf.d && \
    chown -R vagrant:vagrant /etc/nginx && \
    chown -R vagrant:vagrant /run

USER vagrant
EXPOSE 8080
