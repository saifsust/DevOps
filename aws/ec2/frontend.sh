#!/bin/bash
sudo apt-get install nginx -y
index="index.nginx-debian.html"
cat <<EOF > $index
 <h1> Response is coming from frontend app that's IP: $(hostname -I)</h1>
EOF
sudo mv $index /var/www/html/
sudo systemctl daemon-reload
sudo systemctl restart nginx