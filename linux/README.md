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