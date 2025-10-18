import org.aktota.utils.PodUtils

def call(def podName, def namespace, def images) {
    println podName

    def podUtil = (new PodUtils())
    def podYaml = (podUtil).getDeployYaml(podName, namespace, images)
    (podUtil).writeYaml(podYaml, String.format("%s/%s",env.WORKSPACE, env.JOB_NAME))
    pipeline {
        agent any
        environment {
            POD_YAML = "${podYaml}"
        }
        stages {
            stage('Preset and Git Checkout') {
                steps {
                    sh 'cat api/docker/resource.yaml'
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
