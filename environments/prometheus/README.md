```shell
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm install kube-prometheus-stack prometheus-community/kube-prometheus-stack --version 87.17.0 -f kube-prometheus-stack-values.yaml
```