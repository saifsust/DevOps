# kube linter analysis
```bash
 kube-linter lint attcker.yam
```
# KubeSec analysis
```shell
docker run -i kubesec/kubesec:512c5e0 scan /dev/stdin < attacker.yaml
```