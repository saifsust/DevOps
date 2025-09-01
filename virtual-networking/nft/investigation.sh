
## kubernetes service network for control plane
chain KUBE-SERVICES {
		meta l4proto tcp ip daddr 10.96.0.1  tcp dport 443 counter packets 0 bytes 0 jump KUBE-SVC-NPX46M4PTMTKRN6Y
		meta l4proto udp ip daddr 10.96.0.10  udp dport 53 counter packets 0 bytes 0 jump KUBE-SVC-TCOU7JCQXEZGVUNU
		meta l4proto tcp ip daddr 10.96.0.10  tcp dport 53 counter packets 0 bytes 0 jump KUBE-SVC-ERIFXISQEP7F7OF4
		meta l4proto tcp ip daddr 10.96.0.10  tcp dport 9153 counter packets 0 bytes 0 jump KUBE-SVC-JD5MR3NA4I4DYORP
		ip daddr 192.168.0.100  counter packets 77 bytes 4824 jump KUBE-NODEPORTS
	}


### kubernetes service network for node01
chain KUBE-SERVICES {
		meta l4proto tcp ip daddr 10.96.0.10  tcp dport 53 counter packets 0 bytes 0 jump KUBE-SVC-ERIFXISQEP7F7OF4
		meta l4proto tcp ip daddr 10.96.0.1  tcp dport 443 counter packets 1 bytes 60 jump KUBE-SVC-NPX46M4PTMTKRN6Y
		meta l4proto tcp ip daddr 10.96.0.10  tcp dport 9153 counter packets 0 bytes 0 jump KUBE-SVC-JD5MR3NA4I4DYORP
		meta l4proto udp ip daddr 10.96.0.10  udp dport 53 counter packets 0 bytes 0 jump KUBE-SVC-TCOU7JCQXEZGVUNU
		ip daddr 192.168.0.101  counter packets 7 bytes 696 jump KUBE-NODEPORTS
	}
