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
stream {
  upstream mysql_db_server {
    server 127.0.0.1:3306;
  }

  server {
    listen 3306;
    proxy_pass mysql_db_server;
  }
}
EOF
sudo rm /etc/nginx/sites-enabled/default
sudo ln -s /etc/nginx/sites-available/proxy-conf /etc/nginx/sites-enabled/default
sudo systemctl restart nginx

# Run Application
sudo docker run -d -p 3306:3306 saifsust/mysql-server:1.0.99
