#!/bin/bash
## Docker Installation
sudo apt update
sudo apt install ca-certificates curl -y
sudo install -m 0755 -d /etc/apt/keyrings
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
sudo chmod a+r /etc/apt/keyrings/docker.asc

sudo tee /etc/apt/sources.list.d/docker.sources <<EOF
Types: deb
URIs: https://download.docker.com/linux/ubuntu
Suites: $(. /etc/os-release && echo "${UBUNTU_CODENAME:-$VERSION_CODENAME}")
Components: stable
Signed-By: /etc/apt/keyrings/docker.asc
EOF

sudo apt update
sudo apt install docker-ce docker-ce-cli containerd.io -y

# Proxy Server Installation
sudo apt update
sudo apt-get install nginx -y

# Proxy Setup
sudo chown $USER /etc/nginx/sites-available
sudo rm -f /etc/nginx/sites-available/proxy-conf
config="/etc/nginx/sites-available/proxy-conf"
cat <<EOF > $config
server {
   listen 80 default_server;
   listen [::]:80 default_server;

   server_name _;
   location / {
      proxy_buffers 16 4k;
      proxy_buffer_size 2k;
      proxy_pass http://127.0.0.1:9080;
   }
}

server {
   listen 443;
   listen [::]:443;
   location / {
      proxy_buffers 16 4k;
      proxy_buffer_size 2k;
      proxy_pass http://127.0.0.1:9080;
   }
}
EOF
sudo rm /etc/nginx/sites-enabled/default
sudo ln -s /etc/nginx/sites-available/proxy-conf /etc/nginx/sites-enabled/default
sudo systemctl restart nginx

# APIs Environment variables are set up
sudo chown $USER /home/ubuntu/.bashrc
cat <<EOF >> /home/ubuntu/.bashrc
# Required environment variables are set up
# Domain name
export SERVICES_DOMAIN="us-east-1.elb.amazonaws.com"

# Rating hostname
export RATINGS_HOSTNAME=""
export RATINGS_SERVICE_PORT=80

# Environment values
export STAR_COLOR="red"
export ENABLE_RATINGS=false
EOF
source /home/ubuntu/.bashrc

# Run Application
sudo docker run -d -p 9080:9080 saifsust/reviews:istio
