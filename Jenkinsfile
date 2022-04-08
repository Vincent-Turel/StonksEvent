def deploy_module = {
    configFileProvider(
        [configFile(fileId: 'maven-settings', variable: 'MAVEN_SETTINGS')]) {
            sh "mvn -Drepo.id=snapshots -Drepo.login=$REPO_USER -Drepo.pwd=$REPO_USER_PWD -Premote-artefact -pl ${env.MODULE} -s $MAVEN_SETTINGS clean deploy -U"
    }
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
                    pwd
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                '''
            }
        }
        stage('Testing unstable') {
            when {
                anyOf {
                    changeset "components/**"
                    changeset "controllers/**"
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
                dir("${env.WORKSPACE}/backend"){
                    script { deploy_module() }
                }
            }
        }
        stage('Build components - dev') {
            environment {
                MODULE = "components"
            }
            when {
                expression { env.BRANCH_NAME == 'develop' }
            }
            steps {
                echo "Building components develop"
                dir("${env.WORKSPACE}/backend"){
                    script { deploy_module() }
                }
            }
        }
        stage('Build controllers - dev') {
            environment {
                MODULE = "controllers"
            }
            when {
                expression { env.BRANCH_NAME == 'develop' }
            }
            steps {
                echo "Building controllers develop"
                dir("${env.WORKSPACE}/backend"){
                    script { deploy_module() }
                }
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
                dir("${env.WORKSPACE}/backend"){
                    script { deploy_module() }
                }
            }
        }
        stage('Build all') {
            when {
                expression { env.BRANCH_NAME == 'main' }
            }
            steps {
                dir("${env.WORKSPACE}/backend"){
                    configFileProvider(
                        [configFile(fileId: 'maven-settings', variable: 'MAVEN_SETTINGS')]) {
                            sh "mvn -Drepo.id=snapshots -Drepo.login=$REPO_USER -Drepo.pwd=$REPO_USER_PWD -s $MAVEN_SETTINGS clean deploy"
                        }
                }
                /*dir("${env.WORKSPACE}/cli"){
                    sh "mvn -Drepo.id=snapshots -Drepo.login=$REPO_USER -Drepo.pwd=$REPO_USER_PWD clean deploy"
                }*/
            }
        }
        stage('Launch on Docker') {
            when {
                anyOf {
                    branch "develop"
                }
            }
            steps {
                sh '''
                cd ${WORKSPACE}/backend && chmod u+x build.sh && ./build.sh
                cd ${WORKSPACE}/cli && chmod u+x build.sh && ./build.sh
                cd ${WORKSPACE}
                docker-compose up -d
                '''
            }
        }
     }
}