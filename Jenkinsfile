pipeline {
    agent any

    environment {
        APP_NAME = "employee-api"
        SONAR_SERVER = "sonarqube"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                sh 'mvn clean verify'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    sh '''
                    mvn clean verify sonar:sonar \
                    -Dsonar.projectKey=employee-api \
                    -Dsonar.projectName=employee-api \
                    -Dsonar.host.url=http://34.237.245.203:30335
                    '''
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 2, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh """
                docker build -t employee-api:${BUILD_NUMBER} .
                docker tag employee-api:${BUILD_NUMBER} employee-api:latest
                """
            }
        }

        stage('Trivy Scan') {
            steps {
                sh """
                trivy image --severity HIGH,CRITICAL employee-api:latest
                """
            }
        }

        stage('Deploy') {
            steps {
                sh """
                docker stop employee-api || true
                docker rm employee-api || true

                docker run -d \
                --name employee-api \
                -p 8081:8081 \
                employee-api:latest
                """
            }
        }
    }
}
