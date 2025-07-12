#Kubernetes virtual environment setup using vagrant
- install virtualbox
- install vagrant 
- create `Vagrantfile` using command `vagrant init`
- configure `Vagrantfile` take two virtual box machine with 2 GB memory and 2 core cups
- set static IPs for each virtual machine with default bridge of those machine (example `192.168.56.100` and `192.168.56.101`)
- `ip route show | grep default | awk '{ print $5 }''` this command will be the default device or bridge of the machine
- add each nodes IPs and DNS/hostname within `/etc/hosts` and also allow google DNS within `/etc/systemd/resolved.config` by adding `DNS=8.8.8.8` 
- ensue iptables proxy functions correctly by executing
```shell
cat <<EOF | sudo tee /etc/modules-load.d/k8s.conf
overlay
br_netfilter
EOF

sudo modprobe overlay
sudo modprobe br_netfilter

# sysctl params required by setup, params persist across reboots
cat <<EOF | sudo tee /etc/sysctl.d/k8s.conf
net.bridge.bridge-nf-call-iptables  = 1
net.bridge.bridge-nf-call-ip6tables = 1
net.ipv4.ip_forward                 = 1
EOF

# Apply sysctl params without reboot
sudo sysctl --system
```
[verification instruction shown by clicking the link](https://v1-29.docs.kubernetes.io/docs/setup/production-environment/container-runtimes/)
- install [containerd](https://docs.docker.com/engine/install/ubuntu/)
- configure containerd `sudo containerd config default | sudo tee /etc/containerd/config.toml` and set `SystemdCgroup = true`
- install [kubeadm](https://v1-29.docs.kubernetes.io/docs/setup/production-environment/tools/kubeadm/install-kubeadm/)
- setup [kubernetes cluster](https://v1-29.docs.kubernetes.io/docs/setup/production-environment/tools/kubeadm/create-cluster-kubeadm/)
- setup **node IP address** for resolving network subnet conflict within /etc/default/kubelet 
```shell
cat /etc/default/kubelet 
KUBELET_EXTRA_ARGS="--node-ip=192.168.0.30"
```