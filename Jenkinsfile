pipeline {
    agent any

    environment {
        AWS_REGION = "us-east-1"
        AWS_ACCOUNT_ID = "754397067775"
        ECR_REPO = "employee-api"
        IMAGE_TAG = "${BUILD_NUMBER}"
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
                    mvn sonar:sonar \
                    -Dsonar.projectKey=employee-api \
                    -Dsonar.projectName=employee-api
                    '''
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh '''
                docker build -t employee-api:${IMAGE_TAG} .
                docker tag employee-api:${IMAGE_TAG} employee-api:latest
                '''
            }
        }

        stage('Trivy Scan') {
            steps {
                sh '''
                trivy image --severity HIGH,CRITICAL employee-api:${IMAGE_TAG}
                '''
            }
        }

        stage('Login to ECR') {
            steps {
                sh '''
                aws ecr get-login-password --region ${AWS_REGION} | \
                docker login \
                --username AWS \
                --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com
                '''
            }
        }

        stage('Push to ECR') {
            steps {
                sh '''
                docker tag employee-api:${IMAGE_TAG} \
                ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${ECR_REPO}:${IMAGE_TAG}

                docker tag employee-api:${IMAGE_TAG} \
                ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${ECR_REPO}:latest

                docker push \
                ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${ECR_REPO}:${IMAGE_TAG}

                docker push \
                ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${ECR_REPO}:latest
                '''
            }
        }

        stage('Deploy to K3s') {
            steps {
                sh '''
                kubectl set image deployment/employee-api \
                employee-api=${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/${ECR_REPO}:${IMAGE_TAG} \
                -n employee

                kubectl rollout status deployment/employee-api -n employee
                '''
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully!'
        }

        failure {
            echo 'Pipeline failed. Check logs.'
        }
    }
}
