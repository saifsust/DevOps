import org.aktota.utils.PodUtils

def call(def podName, def namespace, def images){
    println podName

    def podYaml = (new PodUtils()).getDeployYaml(podName, namespace, images)
    pipeline {
        agent any
        stages {
            stage('Preset and Git Checkout') {
                steps {
                    script{
                        sh('kubectl apply -f ${podYaml}')
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
