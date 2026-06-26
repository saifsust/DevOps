#!/bin/bash

set -x  # Changed from +x to -x for debugging

CA_CERT="/opt/elasticsearch/v94/config/certs/ca.crt"
IP="192.168.0.144"
PORT="9200"
PASSWORD="LvX6DVUf0RuivyicqCoX"
SUPER_USER="elastic"
LOGSTASH_USER="logstash_admin"

curl -k -X POST "https://${IP}:${PORT}/_security/role/logstash_writer" \
  -u ${SUPER_USER}:${PASSWORD} \
  -H 'Content-Type: application/json' \
  -d '{
        "cluster": ["manage_index_templates", "monitor", "manage_ilm", "manage_pipeline"],
        "indices": [
          {
            "names": ["logstash-*", "ecs-logstash-*", "infra-*", "kafka-*", "uddom-*", "hashicorp-vault-*"],
            "privileges": ["write", "create", "create_index", "manage", "manage_ilm", "auto_configure"]
          }
        ]
     }'

sleep 5;

curl -k -X POST "https://${IP}:${PORT}/_security/user/${LOGSTASH_USER}" \
  -u ${SUPER_USER}:${PASSWORD} \
  -H 'Content-Type: application/json' \
  -d '{
        "password": "bjit1234",
        "roles": ["logstash_writer"],
        "full_name": "Logstash System",
        "email": "saiful.sust.cse2013@gmail.com"
      }'