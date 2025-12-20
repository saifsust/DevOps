bom generate --image=saifsust/details:istio --format=json --output=spdx.json

bom document outline spdx.json

bom document query --format=json --fields=version  spdx.json 'name:openssl'

bom document query --format=json --fields=version,name,supplier,url,originator  spdx.json 'name:openssl'

bom generate --image=saifsust/non-privileged-nginx:stable --format=json --output=nginx.json

bom document query --fields=name,version,url,license --format=json nginx.json 'name:nginx'
