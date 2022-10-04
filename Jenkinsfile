pipeline {
    agent any
    tools {
        maven 'maven'
    }
    stages {
        stage('Build JAR File') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/Vanina11/mueblesstgo']]])
                bat 'mvn clean install -DskipTests'
            }
        }
        stage('Test') {
            steps {
                bat 'sonar:sonar'
            }
        }
        stage('Build Docker Image') {
            steps {
                bat 'docker build -t vcchavez/mueblesstgo .'
            }
        }
        stage('Push docker image') {
            steps {
                script{
                    withCredentials([string(credentialsId: 'dckpassword', variable: 'dckpass')]) {
                        bat 'docker login -u vcchavez -p %dckpass%'
                    }
                    bat 'docker push vcchavez/mueblesstgo'
                }
            }
        }
    }
    post {
        always {
            bat 'docker logout'
        }
    }
}