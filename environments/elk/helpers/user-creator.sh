#!/bin/bash

set -x  # Changed from +x to -x for debugging

CA_CERT="/opt/elasticsearch/v94/config/certs/ca.crt"
IP="192.168.0.144"
PORT="9200"
PASSWORD="LvX6DVUf0RuivyicqCoX"
SUPER_USER="elastic"

create_user(){
  local username=$1
  local password=$2
  local roles=$3
  local fullName=$4
  local email=$5

  curl -X POST "https://${IP}:${PORT}/_security/user/${username}" \
   -u "${SUPER_USER}:${PASSWORD}" \
   --cacert "${CA_CERT}" \
   -H "Content-Type: application/json" \
   -d "{
           \"password\" : \"${password}\",
           \"roles\" : [ ${roles} ],
           \"full_name\" : \"${fullName}\",
           \"email\" : \"${email}\",
           \"enabled\": true
      }"
}

reset_password(){
  local username=$1
  local password=$2

  curl -X PUT "https://${IP}:${PORT}/_security/user/${username}/_password" \
   -u "${SUPER_USER}:${PASSWORD}" \
   --cacert "${CA_CERT}" \
   -H "Content-Type: application/json" \
   -d "{
           \"password\" : \"${password}\"
      }"
}

# Create kibana admin user
create_user \
  "kibana_admin" \
  "bjit1234" \
  "\"superuser\"" \
  "Kibana Administration" \
  "saiful.sust.cse2013@gmail.com"

reset_password \
"logstash_system" \
"bjit1234"