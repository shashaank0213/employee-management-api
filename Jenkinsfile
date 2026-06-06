pipeline {
    agent any

    environment {
        APP_NAME = "employee-api"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t employee-api:latest .'
            }
        }

        stage('Trivy Scan') {
            steps {
                sh 'trivy image employee-api:latest'
            }
        }

        stage('Deploy') {
            steps {
                sh '''
                docker stop employee-api || true
                docker rm employee-api || true

                docker run -d \
                --name employee-api \
                -p 8081:8081 \
                employee-api:latest
                '''
            }
        }
    }
}
