package org.aktota.utils

import java.text.MessageFormat

class PodUtils implements Serializable {
    private static def template = """
<<EOF
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
  automountServiceAccountToken: true
  containers:
{2}
EOF
"""

    def getDeployYaml(def podName, def namespace, def images) {
        def containerSpec = images.collect { getContainers(it) }.join("\n")
        return MessageFormat.format(template, podName, namespace, containerSpec) as String
    }

    private def getContainers(def imageName) {
        return """
  - image: ${imageName}
    name: app-${imageName}
    env:
    - name: IMAGE_NAME_ENV
      value: "${imageName}"
"""
    }
}
