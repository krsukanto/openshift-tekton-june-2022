# Day3

## ⛹️‍♂️ Lab - Deploying a multi-pod application

Demonstrates how pod-to-pod communication in a multi-pod applications.

Wordpress is a Content Management Software used to setup a blog website.  Wordpress dependes on database either mariadb or mysql.

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
          value: root@123
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
            value: root
          - name: WORDPRESS_DATABASE_PASSWORD 
            value: root@123
          - name: WORDPRESS_DATABASE_HOST
            value: mysql
          - name: WORDPRESS_DATABASE_PORT
            value: "3306"
          - name: WORDPRESS_DATABASE_NAME 
            value: bitnami_wordpress
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
