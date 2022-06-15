# Day3

## Deploying a multi-pod application

Demonstrates how pod-to-pod communication in a multi-pod applications.

Wordpress is a Content Management Software used to setup a blog website.  Wordpress dependes on database either mariadb or mysql.

We will be create two deployments, one for mysql and other wordpress.

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
