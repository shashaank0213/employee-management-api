# DevOps Project Runbook

## Issue 1: GitHub Webhook Not Triggering Jenkins

### Symptoms

GitHub webhook delivery fails.

### Root Cause

Incorrect Jenkins public IP in webhook URL.

### Resolution

Update webhook URL:

```text
http://<JENKINS_PUBLIC_IP>:8080/github-webhook/
```

Verify:

```bash
curl http://<JENKINS_PUBLIC_IP>:8080
```

---

## Issue 2: SonarQube Quality Gate Stuck in PENDING

### Symptoms

Pipeline hangs at:

```text
waitForQualityGate
```

### Root Cause

SonarQube webhook missing.

### Resolution

Configure SonarQube Webhook:

```text
http://<JENKINS_IP>:8080/sonarqube-webhook/
```

Verify background task completion in SonarQube.

---

## Issue 3: AWS ECR Login Failed

### Symptoms

```text
Unable to locate credentials
```

### Root Cause

AWS credentials not available to Jenkins.

### Resolution

Configure Jenkins AWS Credentials:

```text
Manage Jenkins
→ Credentials
→ Global
→ AWS Credentials
```

Use:

```groovy
withCredentials([
[$class: 'AmazonWebServicesCredentialsBinding',
credentialsId: 'aws-cred']
])
```

---

## Issue 4: ImagePullBackOff in Kubernetes

### Symptoms

```text
ImagePullBackOff
```

### Root Cause

Kubernetes unable to authenticate to ECR.

### Resolution

Create image pull secret:

```bash
kubectl create secret docker-registry ecr-secret
```

Attach secret:

```yaml
imagePullSecrets:
- name: ecr-secret
```

---

## Issue 5: kubectl Authentication Failed from Jenkins

### Symptoms

```text
Authentication required
```

### Root Cause

Jenkins user missing kubeconfig.

### Resolution

Copy kubeconfig:

```bash
sudo cp /home/ubuntu/.kube/config /var/lib/jenkins/.kube/config
```

Set permissions:

```bash
sudo chown -R jenkins:jenkins /var/lib/jenkins/.kube
```

---

## Issue 6: Prometheus UI Not Accessible

### Symptoms

Browser unable to access Prometheus.

### Root Cause

NodePort not allowed in Security Group.

### Resolution

Open Prometheus NodePort in AWS Security Group.

Example:

```text
32056/TCP
```

---

## Issue 7: Grafana UI Not Accessible

### Symptoms

Connection timeout.

### Root Cause

Grafana service not exposed externally.

### Resolution

Change service to NodePort and allow port in Security Group.

Verify:

```bash
kubectl get svc -n monitoring
```

---

## Useful Commands

### Kubernetes

```bash
kubectl get pods -A
kubectl get svc -A
kubectl describe pod <pod-name>
kubectl logs <pod-name>
```

### Docker

```bash
docker images
docker ps
docker logs <container>
```

### Jenkins

```bash
sudo systemctl status jenkins
```

### K3s

```bash
sudo systemctl status k3s
```
