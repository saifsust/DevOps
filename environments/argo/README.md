```shell
helm repo add argo https://argoproj.github.io/argo-helm
helm install argo-workflows argo/argo-workflows --version 1.0.20 -f argo-workflow-values.yaml
```

```shell
helm install  argo-cd argo/argo-cd --version 10.1.4 -f argocd-values.yaml 
helm upgrade  argo-cd argo/argo-cd --version 10.1.4 -f argocd-values.yaml 
```

