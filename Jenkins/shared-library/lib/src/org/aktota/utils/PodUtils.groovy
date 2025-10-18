package org.aktota.utils

import java.text.MessageFormat

class PodUtils implements Serializable {
    private static def template = """
apiVersion: v1
kind: Pod
metadata:
  name: {0}
  namespace: {1}
spec:
  serviceAccount: default
  securityContext:
     runAsUser: 1000
     runAsGroup: 1000
     runAsNonRoot: true
  automountServiceAccountToken: true
  containers:
{2}
"""

    def getDeployYaml(def podName, def namespace, def images) {
        def containerSpec = images.collect { getContainers(it, podName) }.join("\n")
        return MessageFormat.format(template, podName, namespace, containerSpec) as String
    }

    static def writeYaml(def contents, def path){
       def file =  new File("$path/k8s/resource.yaml")
        if(file.exists()){
            file.deleteDir()
        }

        file.getParentFile().mkdir()

        if(file.createNewFile()){
            println 'Successfully resource is created.'
        }
        file.write(contents)
    }

    private def getContainers(def imageName, def podName) {
        return """
  - image: ${imageName}
    name: ${podName}
    securityContext:
        capabilities:
            drop:
            - ALL
            add:
            - NET_BIND_SERVICE
        seccompProfile:
            type: RuntimeDefault
        allowPrivilegeEscalation: false
"""
    }
}
