# AWS VPC Configuration
- Create VPC within a defined CIDR like
```shell
10.144.0.0/16
```
- Create subnets. Subnets can be public or private according to required
```shell
10.144.128.0/24 -> public 
10.144.192.0/24 -> private
```
- Create Route Table and associate the subnets where we want to transfer traffic.
- To allow internet access, then Create Internet-Gateway