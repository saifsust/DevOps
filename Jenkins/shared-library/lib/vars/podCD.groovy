import org.aktota.utils.PodUtils

def call(def podName, def images){
    def podYaml = (new PodUtils()).getDeployYaml(podName, images)
    pipeline {
        agent any
        stages {
            stage('Preset and Git Checkout') {
                steps {
                    script{
                        println podYaml
                    }
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
