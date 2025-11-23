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
## Requirement
- The name of template should be same as kind of CRD but characters must be small case.

```yaml
apiVersion: templates.gatekeeper.sh/v1
kind: ConstraintTemplate
metadata:
  name: k8srequiredpodnamespace
spec:
  crd:
    spec:
      names:
        kind: K8sRequiredPodNamespace
      validation:
         openAPIV3Schema:
           type: object
           properties:
             namespace:
               type: string
  targets:
  - target: admission.k8s.gatekeeper.sh
    rego: | 
      package k8srequiredpodnamespace
      
      violation[{"msg": msg}]{
        provided := input.review.object.metadata.namespace
        required := input.parameters.namespace
        provided != required
        msg := sprintf("You must deploy resources within namespace %v", [required])
      }
```