import org.aktota.utils.PodUtils
import org.aktota.utils.AppUtils

def call(def appName) {
    def podUtil = (new PodUtils())
    def appUtil = (new AppUtils())
    pipeline {
        agent any
        environment {
            GIT_CREDENTIAL_ID = 'gitRootAccess'
            GIT_REPOSITORY = "${appUtil.applications()[appName]}"
            DOCKER_IMAGE_VERSION = ''
        }
        parameters {
            string(name: 'GIT_BRANCH', defaultValue: 'master', description: 'Git Branch to build docker image', trim: true)
            choice(name: 'DEPLOY_ENV', choices: ['cks', 'graphql-system', 'cka', 'test'])
        }
        stages {
            stage('Preset and Git Checkout') {
                steps {
                    git url: GIT_REPOSITORY, branch: "${params.GIT_BRANCH.trim()}", credentialsId: GIT_CREDENTIAL_ID
                }
                post {
                    success {
                        sh 'git checkout master'
                    }
                }
            }

            stage('Test') {
                steps {
                    sh './gradlew test'
                }
                post {
                    success {
                        println 'Application Test is completed'
                    }
                }
            }

            stage('Prepare For Docker') {
                steps {
                    sh './gradlew :api:DockerCopyJar'
                }
                post {
                    success {
                        println 'Successfully Jar is copied to docker directory'
                    }
                }
            }

            stage('Build Docker Image') {
                steps {
                    sh './gradlew :api:DockerBuildImage'
                }
                post {
                    success {
                        println 'Successfully docker image is built'
                    }
                }
            }
            stage('Push Image') {
                steps {
                    script {
                        DOCKER_IMAGE_VERSION = sh(script: './gradlew :api:DockerPush -q', returnStdout: true).trim()
                    }
                }
                post {
                    success {
                        println 'Successfully docker image is pushed'
                    }
                }
            }
            stage('Update Manifest Image') {
                steps {
                    script {
                        def podYaml = podUtil.getDeployYaml("${params.APP_NAME}", "${params.APP_NAME}-" + env.BUILD_NUMBER, "${params.DEPLOY_ENV}", ["${DOCKER_IMAGE_VERSION}"])
                        println podYaml
                        podUtil.writeYaml(podYaml, String.format("%s/workspace/%s", env.JENKINS_HOME, env.JOB_NAME))
                    }
                }
                post {
                    success {
                        println "Successfully docker image is pushed ${DOCKER_IMAGE_VERSION}"
                    }
                }
            }

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

