import org.aktota.utils.AppUtils

def call(def appName) {
    def appUtil = (new AppUtils())
    pipeline {
        agent any
        environment {
            GIT_CREDENTIAL_ID = 'gitRootAccess'
        }
        parameters {
            GIT_REPOSITORY = "${appUtil.applications()[appName]}"
            string(name: 'GIT_BRANCH', defaultValue: 'master', description: 'Git Branch to build docker image', trim: true)
        }
        stages {
            stage('Preset and Git Checkout') {
                steps {
                    git url: "${params.GIT_REPOSITORY.trim()}", branch: "${params.GIT_BRANCH.trim()}", credentialsId: GIT_CREDENTIAL_ID
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
        }
    }
}

