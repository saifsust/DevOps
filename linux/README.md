# How to create Linux process with isolated network namespace, hostname, filesystem and user

```shell
sudo unshare --user --pid --uts --net --ipc --mount --mount-proc --fork  bash
```

# Linux namespaces:
There are several types of namespaces in Linux
- PID: 
- - Process ID namespace used to isolate processes of Linux
- NET:
- - Network namespace used to isolate network interfaces, routing tables, and firewall rules
- MNT:
- - Mount namespace used to isolate filesystem mount points
- UTS:
- - UTS namespace used to isolate hostname and domain name
- IPC:
- - IPC namespace used to isolate inter-process communication resources
- USER:
- - User namespace used to isolate user and group IDs

[further more specification we can go through the link](https://man7.org/linux/man-pages/man7/namespaces.7.html)

# How to create network interface in Linux namespace

## What is network interface:

A network interface is a point of interaction between a computer and a private or public network.

```shell
sudo ip link add veth-host type veth peer name veth-container
```

We will see two virtual network inferfac created. one is for host machine and other for container.

Example:
```shell
3: veth-container@veth-host: <BROADCAST,MULTICAST,M-DOWN> mtu 1500 qdisc noop state DOWN group default qlen 1000
    link/ether 56:ee:a1:99:eb:5a brd ff:ff:ff:ff:ff:ff
4: veth-host@veth-container: <BROADCAST,MULTICAST,M-DOWN> mtu 1500 qdisc noop state DOWN group default qlen 1000
    link/ether 6a:e3:6b:3b:76:a6 brd ff:ff:ff:ff:ff:ff
```

## How to assign one network interface to a Linux process
one virtual network interface need to assign Linux process to isolate network namespace.

```shell
sudo ip link set veth-container netns 1813
```
We will see one network interface assigned to process with PID 1813. we can check it by using below command.

Example:
```shell
4: veth-host@if3: <BROADCAST,MULTICAST> mtu 1500 qdisc noop state DOWN group default qlen 1000
    link/ether 6a:e3:6b:3b:76:a6 brd ff:ff:ff:ff:ff:ff link-netnsid 0
```
```shell
sudo ip netns exec 1813 ip addr
```
## How to assign CIDR in network interface
We can assign CIDR to network interface using below command.
```shell
sudo ip addr add 10.144.0.0/24 dev veth-host brd 10.144.0.255
```

## How to activate network interface
We can activate network interface using below command.

Host Virtual interface:
```shell
sudo ip link set veth-host up
```

Example:
```
4: veth-host@if3: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue state <UP> group default qlen 1000
    link/ether 6a:e3:6b:3b:76:a6 brd ff:ff:ff:ff:ff:ff link-netnsid 0
    inet 10.144.0.0/24 brd 10.144.0.255 scope global veth-host
       valid_lft forever preferred_lft forever
    inet6 fe80::68e3:6bff:fe3b:76a6/64 scope link proto kernel_ll 
       valid_lft forever preferred_lft forever
```
Process Virtual interface:
```shell
ip link set dev veth-container up
``` 

Example:
```
3: veth-container@if4: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc noqueue state <UP> group default qlen 1000
    link/ether 56:ee:a1:99:eb:5a brd ff:ff:ff:ff:ff:ff link-netnsid 0
    inet 10.144.0.1/24 scope global veth-container
       valid_lft forever preferred_lft forever
    inet6 fe80::54ee:a1ff:fe99:eb5a/64 scope link tentative proto kernel_ll 
       valid_lft forever preferred_lft forever
```

# Verification:
verify host machine can communicate with process by virtual ip address

Example:
```shell
ping 10.144.0.1
PING 10.144.0.1 (10.144.0.1) 56(84) bytes of data.
64 bytes from 10.144.0.1: icmp_seq=1 ttl=64 time=0.015 ms
64 bytes from 10.144.0.1: icmp_seq=2 ttl=64 time=0.023 ms
64 bytes from 10.144.0.1: icmp_seq=3 ttl=64 time=0.037 ms
```

verify process can communicate with host machine by virtual ip address

Example:
```shell
ping 10.144.0.0
PING 10.144.0.0 (10.144.0.0) 56(84) bytes of data.
64 bytes from 10.144.0.0: icmp_seq=1 ttl=64 time=0.045 ms
64 bytes from 10.144.0.0: icmp_seq=2 ttl=64 time=0.020 ms
64 bytes from 10.144.0.0: icmp_seq=3 ttl=64 time=0.026 ms
```