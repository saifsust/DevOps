import org.aktota.utils.PodUtils

def call(def podName, def namespace, def images){
    println podName

    def podYaml = (new PodUtils()).getDeployYaml(podName, namespace, images)
    pipeline {
        agent any
        environment{
            POD_YAML = "${podYaml}"
        }
        stages {
            stage('Preset and Git Checkout') {
                steps {
                     sh label: "Deploy APP", script: "kubectl apply -f - ${POD_YAML}"
                }
                post {
                    success {
                        println 'shared library tested successfully'
                    }
                }
            }
        }
    }
}
