import org.aktota.utils.AppUtils


def call() {
    pipeline {
        agent any
        environment {
            GIT_CREDENTIAL_ID = 'gitRootAccess'
            DOCKER_IMAGE_VERSION = ''
        }
        parameters {
            script{
                def appUtil = (new AppUtils())
                properties([
                        parameters([
                                choice(
                                        name: 'APP_CHOICE',
                                        choices: appUtil.getAppNames(),
                                        description: 'Select the application for deployment'
                                )
                        ])
                ])
            }
          //  choice(name: 'APP_NAME', description: 'Choose app for deploying', choices: appUtil.getAppNames())
          //  string(name: 'GIT_BRANCH', defaultValue: 'master', description: 'Git Branch to build docker image', trim: true)
          //  choice(name: 'DEPLOY_ENV', choices: ['cks', 'graphql-system', 'cka', 'test'])
        }
        stages {
            stage('Preset and Git Checkout') {
                steps {
                    git url: applications()[APP_NAME], branch: "${params.GIT_BRANCH.trim()}", credentialsId: GIT_CREDENTIAL_ID
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

