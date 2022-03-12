def deploy_module = {
    sh "mvn -Drepo.id=snapshots -Drepo.login=$REPO_USER -Drepo.pwd=$REPO_USER_PWD -Premote-artefact -pl ${env.MODULE} clean deploy -U"
}

pipeline {
    agent any
    tools {
        maven "3.6.3"
        jdk "jdk11"
    }
    environment {
       REPO_USER = credentials('artifactory_user')
       REPO_USER_PWD = credentials('artifactory_password')
    }
    stages {
       stage ('Initialize') {
            steps {
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                '''
            }
        }
        stage('Testing unstable') {
            when {
                anyOf {
                    changeset "entities/**"
                    changeset "event-app/**"
                }
                not {
                   anyOf {
                     branch 'main'
                     branch 'develop'
                   }
                }
            }
            steps {
                sh "mvn clean test"
            }
        }
        stage('Build entities - dev') {
            environment {
                MODULE = "entities"
            }
            when {
                expression { env.BRANCH_NAME == 'develop' }
            }
            steps {
                echo "Building entities develop"
                script { deploy_module() }
            }
        }
        stage('Build event-app - dev') {
            environment {
                MODULE = "event-app"
            }
            when {
                expression { env.BRANCH_NAME == 'develop' }
            }
            steps {
                echo "Building event-app develop"
                script { deploy_module() }
            }
        }
        stage('Build all') {
            when {
                expression { env.BRANCH_NAME == 'main' }
            }
            steps {
                sh "mvn -Drepo.id=snapshots -Drepo.login=$REPO_USER -Drepo.pwd=$REPO_USER_PWD clean deploy"
            }
        }
     }
}
