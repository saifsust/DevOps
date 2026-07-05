#!/bin/bash

set -x  # Changed from +x to -x for debugging

CA_CERT="/opt/elasticsearch/v94/config/certs/ca.crt"
IP="192.168.0.144"
PORT="9200"
PASSWORD="LvX6DVUf0RuivyicqCoX"
SUPER_USER="elastic"
USER="exporter-monitor"
ROLE="exporter"

curl -k -X POST "https://${IP}:${PORT}/_security/role/${ROLE}" \
  -u ${SUPER_USER}:${PASSWORD} \
  -H 'Content-Type: application/json' \
  -d '{
        "cluster": ["monitor"],
        "indices": [
          {
            "names": ["*"],
            "privileges": ["monitor"]
          }
        ]
     }'

sleep 5;

curl -k -X POST "https://${IP}:${PORT}/_security/user/${USER}" \
  -u ${SUPER_USER}:${PASSWORD} \
  -H 'Content-Type: application/json' \
  -d '{
        "password": "bjit1234",
        "roles": ["exporter"],
        "full_name": "Elastic Search Exporter",
        "email": "saiful.sust.cse2013@gmail.com"
      }'