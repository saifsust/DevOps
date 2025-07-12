# Open Policy Agent (OPA)
The purpose of the tool is to secure **k8s** cluster by providing **fine-grained, policy-base** control over various aspect of the clusters.

### Admission Control Policies
OPA is typically integrated with kubernetes as validation admission webhook (GateKeeper). It allows you to define and enforce policies before objects are persisted to the clusters.

#### Usages
-  Required certain labels on resources (pods, namespaces)
- Deny privileged containers
- Enforce resources limitation (CPU, MEMORY)
- Restrict hostPath volume mounts
- Disallow latest image tags
- Predefine number of replicas of Deployments, ReplicaSets

