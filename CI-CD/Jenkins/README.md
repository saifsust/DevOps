## Docker Credential Configuration
- Generate self-signed certificates for Docker-server, Client and CA certificates
- Openssl scripts are given below
```shell
#CA certificates and private key
openssl genrsa -aes256 -passout pass:bjit1234 -out ca-key.pem 2048
openssl req -new -x509 -days 3650 -passin pass:bjit1234 -key ca-key.pem -subj="/CN=docker-ca/O=controlplane" -extensions v3_ca -config openssl.cnf -out ca.pem

#Docker server certificates and private key
openssl genrsa -out docker-server-key.pem 2048
openssl req -new -key docker-server-key.pem -subj="/CN=docker-server/O=controlplane" -extensions v3_ca -config openssl.cnf -out docker-server.csr
openssl x509 -days 3650 -req -in docker-server.csr -passin pass:bjit1234 -CA ca.pem -CAkey ca-key.pem -CAcreateserial -extensions v3_ca -extfile openssl.cnf -out docker-server.pem

#Docker client certificates and private key
openssl genrsa -out key.pem 2048
openssl req -new -subj="/CN=client/O=controlplane" -key key.pem -extensions v3_ca -config openssl.cnf -out client.csr
openssl x509 -days 3650 -req -in client.csr -CA ca.pem -CAkey ca-key.pem -CAcreateserial -passin pass:bjit1234 -extensions v3_ca -extfile openssl.cnf -out cert.pem

#Remove CertificateSigningRequest
rm client.csr docker-server.csr

#Verify Docker-server and Docker-client certificate
openssl verify -CAfile ca.pem docker-server.pem
openssl verify -CAfile ca.pem cert.pem

#Restart Daemon and Docker
sudo systemctl daemon-reload
sudo systemctl restart docker

#Verify Docker mTLS connectivity
openssl s_client -connect 192.168.0.30:2376
```
- Docker remote communication need to add alternative subject names using below configuration file
```shell
[ v3_ca ]
subjectKeyIdentifier=hash
authorityKeyIdentifier=keyid:always,issuer
basicConstraints = critical,CA:true
subjectAltName=@alt_names
[ alt_names]
DNS.1 = controlplane
IP.1 = 192.168.0.30
DNS.2 = node01
IP.2 = 192.168.0.31
```
- Create Secrets for Docker Daemon remote communication
```yaml
apiVersion: v1
data:
  ca: LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSURaVENDQWsyZ0F3SUJBZ0lVR0RQM25iMTZJL3dTY2I2cnlENjdCNjAxV2pRd0RRWUpLb1pJaHZjTkFRRUwKQlFBd0t6RVNNQkFHQTFVRUF3d0paRzlqYTJWeUxXTmhNUlV3RXdZRFZRUUtEQXhqYjI1MGNtOXNjR3hoYm1VdwpIaGNOTWpVd09URTVNVFl3TXpNM1doY05NelV3T1RFM01UWXdNek0zV2pBck1SSXdFQVlEVlFRRERBbGtiMk5yClpYSXRZMkV4RlRBVEJnTlZCQW9NREdOdmJuUnliMnh3YkdGdVpUQ0NBU0l3RFFZSktvWklodmNOQVFFQkJRQUQKZ2dFUEFEQ0NBUW9DZ2dFQkFLU25wOUttUk9iaDNLWFJPakJEc21maHhaMEFFR2JJRGI4UnZEOTBYY3N1RVRTWApsakxlUXR5UGszMFhGcEpSbGdoRUdwOW0zL21JbVJLQVExR2Z1YTNqeWh3K1dxRDRqM1B2NnpTaEdlRzZ2dzFuCmZ1eFg4SHYveEdLeHhHZzIyUDQxcjFIV1V0dFJYMHUrNTgwZ2d4RWwrYXpnSHU0elMvUEp5Y1V4bzRKd2JyUG4KajFUZHllZ0RCcEgxdUF5cmU1bms1aW4vYml1Vk1Ka2dIa2YyWjVoU1IrOGlkeUtTMFZTRGxlMEd3MkV0WExwNAo1VWUwa3ZyNXY2WEN3YStlV0szdUJGNG52cW5jUU9INElXblYyanY1ek5CUThVanVwMllNR25sRExpdC92dU40CjZDcTRJTFZtbFJrZUVNOTN1c3ROT1pzelFaQlJqQ0UxdWI1VDhZY0NBd0VBQWFPQmdEQitNQjBHQTFVZERnUVcKQkJTbWhOWGx3ck5CampycXZDT0NEem9jYzZCR2R6QWZCZ05WSFNNRUdEQVdnQlNtaE5YbHdyTkJqanJxdkNPQwpEem9jYzZCR2R6QVBCZ05WSFJNQkFmOEVCVEFEQVFIL01Dc0dBMVVkRVFRa01DS0NER052Ym5SeWIyeHdiR0Z1ClpZY0V3S2dBSG9JR2JtOWtaVEF4aHdUQXFBQWZNQTBHQ1NxR1NJYjNEUUVCQ3dVQUE0SUJBUUJETDlkZ1pMS2kKaXd2dkNwUWxZTVU4amxrSWM0RzJOb3dNTEFGMkMzRXlPZ3k4d3lld1BRSHZYSEMreDN6RmtQeDNyRmlqODFjYQo3TnNmcGsxNHZVeDNQaWlKOUFkaGZIT2g4bzJnS1lNREJVS3JRRzZES25IbGZBaEoyK2hMNGRUZXNCYStvbWt3Cm9uYVUzWU0xdEdHWlJwZzdHN0JXVUkvLzRnRTNXRVl3L1JvZGVVTCtjTS9vRFN3eFY3MXV3YmVxQ0xrZGh5WC8KY1p2dW5RUllhUjQ1V0JJVFdDeC9OZFFmeURucnp6SlR6SjlCRW55TTFXWlF0SExHQ1k5YkNyV1lIVWcyZ1dTdgo3NTlaL0RaSllOWXpiTFFQMExDMWdHeWhQMlZ6T2pFZDFzVVRCMjdiRDhlb2I2M1BJRXNkMVIwK05uRXdid2IwCnFjb2hUUEd4SzBMYgotLS0tLUVORCBDRVJUSUZJQ0FURS0tLS0tCg==
  cert: LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSURZakNDQWtxZ0F3SUJBZ0lVWmsrQy8yT0FFdnowNWpSQlUwRTBPeHBDOTRvd0RRWUpLb1pJaHZjTkFRRUwKQlFBd0t6RVNNQkFHQTFVRUF3d0paRzlqYTJWeUxXTmhNUlV3RXdZRFZRUUtEQXhqYjI1MGNtOXNjR3hoYm1VdwpIaGNOTWpVd09URTVNVFl3TXpNM1doY05NelV3T1RFM01UWXdNek0zV2pBb01ROHdEUVlEVlFRRERBWmpiR2xsCmJuUXhGVEFUQmdOVkJBb01ER052Ym5SeWIyeHdiR0Z1WlRDQ0FTSXdEUVlKS29aSWh2Y05BUUVCQlFBRGdnRVAKQURDQ0FRb0NnZ0VCQU9HNWQza2ZhelBydFF3MmZIVmkxeGVmNHNqdDZ1NDZGQVFVdDMxNVZrVVJGRGpXTkdDZQpNb0FpY0ppWWE4UFhtdCtpK0xNUGdBM3hHWWJ5UHF4Y2s4TzFyTTFrd2NVdm9HZDIxeUxBSGpvd0dreG03L0RNCjRZNGgwZVlpdGM3eDBjd09mNUhob3BGcHdKdHZ2WW5YV0NiOENKa09DTnpIdHpJOVlVN3F3ek5zRUJFRk5uN2EKY1dOQVFackFOcXErRS9FQzlSSXk5cURWblBkTUhFNEwvWHVYSXRpTW51UWltVnhXQ21iUmp5TzFwK0Z1YkpRTQoxVS9tQk4wS3VkTVZPOTIvQWFObUh0R1VkS2o0aTVkT3FMK1RjcUxGQTdEWG1QL3RKNUpwZEJmYjBtOTJOYXFBCmU1aEEzN0VuTXAxY2x1dWI0cC9zQlo4R0M0cm9IV2QvdzZrQ0F3RUFBYU9CZ0RCK01CMEdBMVVkRGdRV0JCU2cKa1VhRVpmNDM3T2s2NS9NbjNKaUtkRHdWTURBZkJnTlZIU01FR0RBV2dCU21oTlhsd3JOQmpqcnF2Q09DRHpvYwpjNkJHZHpBUEJnTlZIUk1CQWY4RUJUQURBUUgvTUNzR0ExVWRFUVFrTUNLQ0RHTnZiblJ5YjJ4d2JHRnVaWWNFCndLZ0FIb0lHYm05a1pUQXhod1RBcUFBZk1BMEdDU3FHU0liM0RRRUJDd1VBQTRJQkFRQURwZzQ1YkFyM0FMTmMKTXdqeHFrNzBRWGpNTVJENTlqdUREc3UzVVhYdkVwZWlVRTdvbzRrSktIb0ZsTW9pSDR4T3JyRmxQLzJjUFQ1VgppTmRYa3pFaFd3Y3NLSTZsdlRmdWR2ZnNvRzNLdVFJRmxKL2d4SnMyWTlDdTRvdWxzZFNSK2E5VC9VRStPWllUCjF6V01MUm1FU3NIalVDdXJ5S2NHME1FcmlmWFB2QWhVd3p1MGJmL3lDNDhEK0d2czhvbWhta1BuOVhsZlpYOHoKbjJlSk8rN1pzWnRHeVBEZVZkaktKb2tjeDkzVm1RMWtINGZsSGxnZkEvS2hDa2ZFeURzblVNTEJ6TmtkZkN3awpwYjZKUzZ0KzlpT0YyNGRMYnJhUlJ3SjR6M3EvYXF6bkNuQWFQM2JmamcxL2ZJa2dBc3ZJTTZsdGdXVkcxdjhXCm9LWjYvbHBiCi0tLS0tRU5EIENFUlRJRklDQVRFLS0tLS0K
  key: LS0tLS1CRUdJTiBQUklWQVRFIEtFWS0tLS0tCk1JSUV2d0lCQURBTkJna3Foa2lHOXcwQkFRRUZBQVNDQktrd2dnU2xBZ0VBQW9JQkFRRGh1WGQ1SDJzejY3VU0KTm54MVl0Y1huK0xJN2VydU9oUUVGTGQ5ZVZaRkVSUTQxalJnbmpLQUluQ1ltR3ZEMTVyZm92aXpENEFOOFJtRwo4ajZzWEpQRHRhek5aTUhGTDZCbmR0Y2l3QjQ2TUJwTVp1L3d6T0dPSWRIbUlyWE84ZEhNRG4rUjRhS1JhY0NiCmI3MkoxMWdtL0FpWkRnamN4N2N5UFdGTzZzTXpiQkFSQlRaKzJuRmpRRUdhd0RhcXZoUHhBdlVTTXZhZzFaejMKVEJ4T0MvMTdseUxZako3a0lwbGNWZ3BtMFk4anRhZmhibXlVRE5WUDVnVGRDcm5URlR2ZHZ3R2paaDdSbEhTbworSXVYVHFpL2szS2l4UU93MTVqLzdTZVNhWFFYMjlKdmRqV3FnSHVZUU4reEp6S2RYSmJybStLZjdBV2ZCZ3VLCjZCMW5mOE9wQWdNQkFBRUNnZ0VBQVYvMFVDcTJwL0VpYXFuSzllOW9BNlJTZmgvT0xsZWJOMFVsc3ZWeUVaSDAKNUwyNmx2QzZaUU83V2lxVTFCNEprRWdqLzA4enNVeTJkNWNkSVBGS3ZjYVZxckNYbE9CaE4rVnc1NnpIalFUWQpVWDU2Vi90S0pSS2ovbm9DcFNQZGxSZkNMaWR6ZjkyUmgwRFZ6NGp6YUFBTCsxd0FXM3ZrbHBhVXlQckVFcG1ZCmQ1VmJUUlBjVVdVS1pTTlhiYlg3UTVlNlI2emh3dEdOOUdyWjNqS0xHNXc4T0llVVRic3UrWm1MaVhWb21LSlkKa2RtNk81eFo5ZkNkNHA1aEtXQ2E2RVVVM3BGMGpKc1ZpdE5SZmpHR0ZLOGxVSnpqc0ZXUXQzV2lORUVVZ21URgpqWlowTVlxbkN2UkJlQ3RINUU1NERBRE9teWVMcEcrdTZlZnNkcyt3aVFLQmdRRHFmYURGa1VnRExybkhPbTBLCm9iSFo4ZVlMN0IvV2VNWGttM3BPcU9qeGJVTEljQjd6YXQwamZRSGdjaVBDZE1rYjVheXdLS2ZvVitQUkkwR1YKdllxUHhodmVOenRSdVZienpxbzluR1ZQTFR4c0ZHRWhodFE5NXUrblZ5dm5nWWRhN0h3RldaSjZ2ckxlbnpEawpCbkRFajRqYjdjd29CTXRUU1JoTXdIMUNyUUtCZ1FEMmJmeTV3d2lBMnAxOHVGdGNHaG5SVGVWTmxINlhHa2k3CkRJWjZJaVc4d0oxY0tYM3F4RkRmVENFWENMUEFDdjRvNEx5b3BFL3Q5UzRJTFh2azJzdVYxU2pFaEFKU1dMZEQKSVBFMnE3SWdRS05ZWDk2QmYrVms2bUFwWnYxYWVSTm55RHkxRTFweEgwVFZnZTIzZjdWbTlDb21yaTNrUGNacAoyWmVSVkgvZ2JRS0JnUUNrS1drNWgxa3I2VFZrZGRISzVLSjNXek5iVzJaNnByaWNNQUR6T3F0Q0FlVUVJQUo3CkZYQlZuUG1JWVVBSnJlcnhYZHlFd3lHeHRuNG1keEUrUEtEWndLN1g1a0RnR09uMzY5SWhLZkYrK2NOWVF2Z3gKR1l4Uy9lNy9iemFzYjNFTVJZMFp6YmlQdUJ2UFlSRkFRZ1lHaGhvaEpoeU1VN2JveXI1NVZUVWNUUUtCZ1FEdQpGUU5ISVpQVWtlSDN2bEdkNFNXeVh2UzhDRXhwaGg1Z2dOQm5yUmdTalhFNnVtZUo2dE5MZVV5NjZWQm9xUFkyCjNqSEhLTUNFWVExeEh5aUV5WkpKbHlDRkJCa09IK0ltdSt2djZ0LzBZeDZ0WWlvaHFVUWlSSUpWWGNoR29aVksKL2wwUWdMc3ZjWXRhVCtZd0UvVlptMm9oNXNRMXNDMUMxTk9udUNDMzNRS0JnUUNQVFVGWitxNE1KRVRDL00xZgpUSlFCOHBKYW5wL1pQVTBnNlBybVl3aGg0OWw0bVFCT0pPNWxVM1I2TWhRMWpDeEpQWFpBTUd3cGJjMmd5c05mCk9VYXRBOVpvTnBEM3UyUjIzUi83MzNNbkt5OHJ5Mnp1SnlCSzByd0tkQ05UL2RpSkJRWS9UUnBpM3ZRVExyby8KbmZyMW1aUTFUVjMvQ05PU0gyNmZFM3lYWmc9PQotLS0tLUVORCBQUklWQVRFIEtFWS0tLS0tCg==
kind: Secret
metadata:
  name: jenkins-cert
  namespace: jenkins-system
```
- Create ConfigMap for Docker Host
```yaml
apiVersion: v1
data:
  host: tcp://192.168.0.30:2376
kind: ConfigMap
metadata:
  name: jenkins-conf
  namespace: jenkins-system
```
- Create Jenkins deployment
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  labels:
    app: jenkins-controller
  name: jenkins-controller
  namespace: jenkins-system
spec:
  replicas: 1
  selector:
    matchLabels:
      app: jenkins-controller
  template:
    metadata:
      labels:
        app: jenkins-controller
    spec:
      tolerations:
      - key: node-role.kubernetes.io/control-plane
        operator: "Equal"
        effect: NoSchedule
      affinity:
        nodeAffinity:
         requiredDuringSchedulingIgnoredDuringExecution:
           nodeSelectorTerms:
           - matchExpressions:
             - key: kubernetes.io/hostname 
               operator: In
               values:
               - controlplane
      volumes:
      - name: jenkins-home-directory
        persistentVolumeClaim:
         claimName: jenkins-pvc 
      - name: certs
        projected:
         sources:
         - secret:
             name: jenkins-cert
             items:
             - key: cert
               path: cert.pem
             - key: key
               path: key.pem
             - key: ca
               path: ca.pem
      containers:
      - image: saifsust/jenkins:docker
        name: jenkins
        imagePullPolicy: Always
        ports:
        - containerPort: 8080
        - containerPort: 50000
        volumeMounts:
        - name: jenkins-home-directory
          mountPath: /var/jenkins_home
        - name: certs
          mountPath: /home/.docker
        env:
        - name: DOCKER_TLS_VERIFY
          value: "1"
        - name: DOCKER_HOST
          valueFrom:
           configMapKeyRef:
              name: jenkins-conf
              key: host
```
- Create Service 
```yaml
apiVersion: v1
kind: Service
metadata:
  creationTimestamp: null
  labels:
    app: jenkins-controller
  name: jenkins-svc
  namespace: jenkins-system
spec:
  ports:
  - port: 8080
    protocol: TCP
    targetPort: 8080
    nodePort: 30080
    name: ui-tcp
  - port: 50000
    targetPort: 50000
    protocol: TCP
    nodePort: 30085
    name: agent-protocol
  selector:
    app: jenkins-controller
  type: NodePort
status:
  loadBalancer: {}
```
- Create Persistent Volume
```yaml
apiVersion: v1
kind: PersistentVolume
metadata:
 name: jenkins-pv
spec:
 nodeAffinity:
   required:
     nodeSelectorTerms:
     - matchExpressions:
       - key: kubernetes.io/hostname 
         operator: In
         values:
         - controlplane
 storageClassName: local-storage
 accessModes:
 - ReadWriteMany
 capacity:
  storage:  500Mi
 hostPath:
    path: /opt/jenkins
```
- Create Persistent Volume Claim
```yaml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
 name: jenkins-pvc
 namespace: jenkins-system
spec:
 accessModes:
 - ReadWriteMany
 resources:
    requests:
      storage: 500Mi
 volumeName: jenkins-pv
 storageClassName: local-storage
```
- Create Storage Class
```yaml
apiVersion: storage.k8s.io/v1
kind: StorageClass
metadata:
  name: local-storage
provisioner: kubernetes.io/no-provisioner
volumeBindingMode: WaitForFirstConsumer
```