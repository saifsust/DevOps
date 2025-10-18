import org.aktota.utils.PodUtils

def call(def gitRepository, def podName, def namespace, def images) {
    println podName

    def podUtil = (new PodUtils())
    def podYaml = podUtil.getDeployYaml(podName, namespace, images)
    pipeline {
        agent any
        environment {
            POD_YAML = "${podYaml}"
        }
        stages {

            stage('Preset and Git Checkout') {
                steps {
                    git url: ${gitRepository}, branch: 'master', credentialsId: 'gitRootAccess'
                }
                post {
                    success {
                        sh 'git checkout master'
                    }
                }
            }

            stage('K8s Deployment Yaml Preparation') {
                steps {
                    script {
                        podUtil.writeYaml(podYaml, String.format("%s/workspace/%s", env.JENKINS_HOME, env.JOB_NAME))
                        sh 'kubectl apply -f api/docker/resource.yaml'
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
