package org.aktota.utils

import java.text.MessageFormat

class PodUtils implements Serializable {
    private static def template = """
apiVersion: v1
kind: Pod
metadata:
  name: {0}
spec:
  serviceAccount: default
  securityContext:
     runAsUser: 1000
     runAsGroup: 1000
  automountServiceAccountToken: true
  containers:
{1}
"""

    def getDeployYaml(def podName, def images) {
        def containerSpec = images.collect { getContainers(it) }.join("\n")
        return MessageFormat.format(template, podName, containerSpec)
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
