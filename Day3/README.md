# Day3

## ⛹️‍♂️ Lab - Deploying a multi-pod application

Demonstrates how pod-to-pod communication in a multi-pod applications.

Wordpress is a Content Management Software used to setup a blog website.  Wordpress depends on database either mariadb or mysql.

Let's capture all the non-sensitive config details in confimap wordpress-cm.yml as shown below
<pre>
apiVersion: v1
kind: ConfigMap
metadata:
  name: wordpress-cm
data:
  database_host: mysql
  database_port: "3306"
  database_name: bitnami_wordpress
  database_client_flavor: mysql
</pre>

Let's create the above configmap in the cluster
```
oc apply -f wordpress-cm.yml
```

Let's capture all the sensitive login credential details in secret mysql-credentials.yml
<pre>
apiVersion: v1
kind: Secret
metadata:
  name: mysql-credentials
data:
  username: cm9vdA==
  password: cm9vdEAxMjM=
</pre>

Let's create the above secret in the cluster
```
oc apply -f mysql-credentials.yml
```

We will be creating two deployments, one for mysql and other for wordpress.

Let's first create the mysql deployment mysql-deploy.yml
<pre>
apiVersion: apps/v1
kind: Deployment
metadata:
  labels:
    app: mysql
  name: mysql
spec:
  replicas: 1
  selector:
    matchLabels:
      app: mysql
  template:
    metadata:
      labels:
        app: mysql
    spec:
      containers:
      - image: bitnami/mysql:latest
        name: mysql
        env:
        - name: MYSQL_ROOT_PASSWORD
          valueFrom:
             secretKeyRef:
               name: mysql-credentials
               key: password
</pre>

Let's create the mysql deployment
```
oc apply -f mysql-deploy.yml
```
Expected output
<pre>
(jegan@tektutor.org)$ <b>oc apply -f mysql-deploy.yml</b>
deployment.apps/mysql created
</pre>

Let's create a clusterip internal service for mysql deployment mysql-svc.yml
<pre>
apiVersion: v1
kind: Service
metadata:
  labels:
    app: mysql
  name: mysql
spec:
  ports:
  - port: 3306
    protocol: TCP 
    targetPort: 3306
  selector:
    app: mysql
</pre>

Let's create the above service in the cluster
```
oc apply -f mysql-svc.yml
```

Let's create the wordpress deployment manifest file wordpress-deploy.yml
<pre>
apiVersion: apps/v1
kind: Deployment
metadata:
  labels:
    app: wordpress
  name: wordpress
spec:
  replicas: 1
  selector:
    matchLabels:
      app: wordpress
  template:
    metadata:
      labels:
        app: wordpress
    spec:
      containers:
      - image: bitnami/wordpress:latest
        name: wordpress
        env:
          - name: WORDPRESS_DATABASE_USER
            valueFrom:
              secretKeyRef:
                name: mysql-credentials
                key: username

          - name: WORDPRESS_DATABASE_PASSWORD 
            valueFrom:
              secretKeyRef:
                name: mysql-credentials
                key: password 

          - name: WORDPRESS_DATABASE_HOST
            valueFrom:
              configMapKeyRef:
                name: wordpress-cm
                key: database_host

          - name: WORDPRESS_DATABASE_PORT
            valueFrom:
              configMapKeyRef:
                name: wordpress-cm
                key: database_port

          - name: WORDPRESS_DATABASE_NAME 
            valueFrom:
              configMapKeyRef:
                name: wordpress-cm
                key: database_name

          - name: MYSQL_CLIENT_FLAVOR
            valueFrom:
              configMapKeyRef:
                name: wordpress-cm
                key: database_client_flavor

          - name: MYSQL_CLIENT_DATABASE_HOST
            valueFrom:
              configMapKeyRef:
                name: wordpress-cm
                key: database_host

          - name: MYSQL_CLIENT_DATABASE_ROOT_USER
            valueFrom:
              secretKeyRef:
                name: mysql-credentials
                key: username

          - name: MYSQL_CLIENT_DATABASE_ROOT_PASSWORD
            valueFrom:
              secretKeyRef:
                name: mysql-credentials
                key: password

          - name: MYSQL_CLIENT_CREATE_DATABASE_NAME
            valueFrom:
              configMapKeyRef:
                name: wordpress-cm
                key: database_name
</pre>

Let's create the wordpress deployment in the cluster with the above manifest file
```
oc apply -f wordpress-deploy.yml
```

Expected output
<pre>
(jegan@tektutor.org)$ <b>oc apply -f wordpress-deploy.yml</b>
deployment.apps/wordpress created
</pre>


Let's create a manifest file to create a clusterip service for wordpress deployment wordpress-svc.yml
<pre>
apiVersion: v1
kind: Service
metadata:
  labels:
    app: wordpress
  name: wordpress
spec:
  ports:
  - port: 8080
    protocol: TCP 
    targetPort: 8080
  selector:
    app: wordpress
</pre>

Let's create the wordpress service with the above manifest file
```
oc apply -f wordpress-svc.yml
```

Let's create a route manifest file for wordpress service
<pre>
apiVersion: route.openshift.io/v1
kind: Route
metadata:
  labels:
    app: wordpress
  name: wordpress
spec:
  port:
    targetPort: 8080
  to:
    kind: ""
    name: wordpress
    weight: null
</pre>

Let's create the route as shown below
```
oc apply -f wordpress-route.yml
```

You may access the wordpress route from the RedHat OpenShift webconsole => Developer context => topology.

## HELM 
- package manager for Kubernetes/OpenShift
- the packaged applications are called charts
- you can install/uninstall/update/ugrage applications as charts
- comes with opensource web portal, which has a whole bunch third-party opensource charts

## In a manifest file
- we can many deployment, many service, routes, configmap and secrets resource creation in a single yaml file

## RedHat Openshift
- template file
- template file combines many resource definintions in a single yaml file

## What is Operator?
- is a way you can package your application and install it a Kubernetes/OpenShift cluster
- let's you extend the Kubernetes/OpenShift API by adding your own Custom Resource and Custom Controllers
- Custom Resources + Custom Controllers

## Operator Hub
- is an opensource web portal that a collection of many opensource operators that package many useful applications
  in a format they can readily deployed in Kubernetes/OpenShift

## What is Operator Lifecycle Manager ( OLM )?
- OLM is itself an Operator
- OLM simplies how operators can be installed/uninstalled/upgraded in OpenShift
- OLM integrates Operator Hub portal within OpenShift webconsole

## Operator Software Development Kits(SDK)
- this helps in developing your own custom Operators for your applications
- Operators can be developed natively in Go programming language but there are other options
- Ansible Operator let's us develop custom operators using Ansible 
- Operator SDK
  - can be used to develop operators using plain Go
  - in addition it also let's us develop operators by automating things in Ansible

## Setting up our Operator Development Environment

#### Installing Go programming Language

The official go language binary can be download over here https://go.dev/dl

```
wget https://go.dev/dl/go1.18.3.linux-amd64.tar.gz
tar xvfz go1.18.3.linux-amd64.tar.gz
pwd
```
The path printed above can be exported in your ~/.bashrc file as shown below
```
export PATH=/usr/local/bin/go/bin:$PATH
```
To run the .bashrc 
```
source ~/.bashrc
```

You can if go language is in your path
```
go version
```

Expected output
<pre>
[/home/user01@tektutor operator-sdk]# go version
go version go1.18.2 linux/amd64
</pre>

#### Installing OpenShift Operator SDK
```
wget https://mirror.openshift.com/pub/openshift-v4/x86_64/clients/operator-sdk/4.10.12/operator-sdk-v1.16.0-ocp-linux-x86_64.tar.gz
tar xvfz operator-sdk-v1.16.0-ocp-linux-x86_64.tar.gz
sudo mv ./operator-sdk /usr/local/bin/operator-sdk
```

Let's verify if operator-sdk is in path
```
operator-sdk version
```

#### Install Docker Community Edition
```
sudo yum install -y yum-utils
sudo yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
sudo yum -y install docker-ce docker-ce-cli containerd.io docker-compose-plugin 
```

Start the Docker service
```
sudo systemctl enable docker
sudo systemctl start docker
sudo usermod -aG docker $USER
newgrp docker
```

Check the docker version and see if you can list the images
```
docker version
docker images
```

#### Installing Ansible in CentOS 7.x
```
python3 -m pip install --upgrade pip
```

Now you can try installing ansible
```
pip3 install ansible
```

Check the verison of ansible
```
ansible --verison
```
