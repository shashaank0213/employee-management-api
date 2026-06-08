# Employee Management API - End-to-End DevOps Project

## Project Overview

This project demonstrates a complete DevOps CI/CD pipeline for deploying a Java Spring Boot application on Kubernetes using Jenkins, SonarQube, Docker, AWS ECR, Prometheus, and Grafana.

## Architecture

GitHub → Jenkins → Maven Build → SonarQube → Quality Gate → Docker Build → Trivy Scan → AWS ECR → Kubernetes (K3s) → Prometheus → Grafana

## Tools Used

### Source Control

* Git
* GitHub

### CI/CD

* Jenkins
* GitHub Webhooks

### Build & Code Quality

* Maven
* SonarQube

### Security

* Trivy

### Containerization

* Docker

### Container Registry

* AWS ECR

### Container Orchestration

* Kubernetes (K3s)

### Monitoring

* Prometheus
* Grafana

### Cloud Platform

* AWS EC2

---

# Infrastructure Setup

## EC2-1 (CI/CD Server)

Installed:

* Jenkins
* Docker
* Maven
* AWS CLI
* kubectl

Responsibilities:

* Build application
* Run SonarQube analysis
* Build Docker image
* Push image to ECR
* Deploy to Kubernetes

## EC2-2 (Kubernetes Server)

Installed:

* K3s Kubernetes Cluster
* Prometheus Stack
* Grafana

Responsibilities:

* Run application workloads
* Monitor cluster health
* Expose dashboards

---

# CI/CD Pipeline Flow

## Stage 1: Checkout

Jenkins pulls source code from GitHub.

## Stage 2: Build & Test

```bash
mvn clean verify
```

## Stage 3: SonarQube Analysis

```bash
mvn sonar:sonar
```

## Stage 4: Quality Gate

Pipeline waits for SonarQube Quality Gate approval.

## Stage 5: Docker Build

```bash
docker build -t employee-api:${BUILD_NUMBER} .
```

## Stage 6: Trivy Scan

```bash
trivy image employee-api:${BUILD_NUMBER}
```

## Stage 7: Push to AWS ECR

```bash
docker push <ECR-IMAGE>
```

## Stage 8: Deploy to Kubernetes

```bash
kubectl set image deployment/employee-api
```

---

# Kubernetes Deployment

Namespace:

```bash
employee
```

Deployment:

```bash
employee-api
```

Service:

```bash
employee-api-service
```

Type:

```bash
NodePort
```

---

# Monitoring Setup

## Prometheus

Used for:

* Metrics collection
* Alert generation

## Grafana

Used for:

* Dashboard visualization
* Pod monitoring
* Node monitoring
* Cluster health

---

# Current Project Features

* Automated GitHub Webhook Trigger
* Jenkins CI/CD Pipeline
* SonarQube Integration
* Trivy Security Scanning
* Docker Image Build
* AWS ECR Integration
* Kubernetes Deployment
* Prometheus Monitoring
* Grafana Dashboards

---

# Future Enhancements

* Slack Alerting
* ArgoCD GitOps Deployment
* Kubernetes Ingress
* Terraform Infrastructure Provisioning
* Jenkins on Kubernetes
* Advanced Monitoring Alerts
