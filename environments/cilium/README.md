```shell
helm install --install cilium cilium/cilium   --namespace kube-system   -f cilium-values.yaml 
helm upgrade --install cilium cilium/cilium   --namespace kube-system   -f cilium-values.yaml 
```