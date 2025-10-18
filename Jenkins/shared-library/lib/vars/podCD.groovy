def call() {
    pipeline {
        agent any
        stages {

            stage('Deploy APP') {
                steps {
                    script {
                        sh 'kubectl apply -f k8s/resource.yaml'
                    }
                }
                post {
                    success {
                        println 'shared library tested successfully'
                    }
                }
            }

            stage('clean workspace') {
                steps {
                    cleanWs()
                }
            }
        }
    }
}
