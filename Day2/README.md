# Day 2

## What happens when we execute the below command
```
oc create deploy nginx --image=bitnami/nginx:latest --replicas=3
```

1. oc client tool will make REST call to API Server requesting API Server to create a Deployment
2. API Server, receives the request from oc client tool, it then creates the Deployment in the etcd database.
3. API Server then triggers an event something like "New Deployment Created"
4. Deployment Controller receives the "New Deployment Created" event and then fetches the details from the event.  It then sends a request to API Server to create a ReplicaSet by making a REST call.
5. API Server receives the request from Deployment Controller, it creates the ReplicaSet in the etcd.
6. API Server triggers an event something like "New ReplicaSet Created"
7. ReplicaSet Controller receives the "New ReplicaSet created" event and then it will request the API Server
   to create Pods based on the repilcas count mentioned in the ReplicaSet by making REST Call to API Server.
8. API Server creates the Pod entries in the etcd.
9. API Server trigger event(s) like "New Pod Created"
10. Scheduler receives this event, identifies the Node whether the Pod(s) can be deployed.
11. Scheduler sends the Pod Scheduling recommendations to the API Server via REST call.
12. API Server updates the Pod entries with the Nodes details where those Pod(s) can be scheduled in the etcd.
13. API Server then triggers event that Pod is scheduled on master-1, worker-2, etc kind of details.
14. The kubelet on respective nodes, receives this event, kubelet pulls the container images required to
    create the Pod.  Then it creates the Pod on the node where the kubelet is running. Kubelet also send heart-beat like event notification to the API Server via REST call to the update status of all the Pods running in that node.

## Deleting project delets all the resources under the project
```
oc delete project jegan
```

## ⛹️‍♂️ Lab - Creating a NodePort external service
```
oc new-project jegan
oc create deploy nginx --image=bitnami/nginx:1.20 --replicas=3
```


Let's create a NodePort service.  OpenShift has reserved 30000 to 32767 ports on all nodes for NodePort services.
```
oc expose deploy/nginx --type=NodePort --port=8080
```

You may now list the nodeport service as shown below
```
oc get svc
```
Expected output
<pre>
(jegan@tektutor.org)$ <b>oc get svc</b>
NAME    TYPE       CLUSTER-IP       EXTERNAL-IP   PORT(S)          AGE
nginx   NodePort   172.30.161.212   <none>        8080:32726/TCP   3s
</pre>

You may describe the nodeport service as shown below
```
oc describe svc/nginx 
```
Expected output
<pre>
(jegan@tektutor.org)$ <b>oc describe svc/nginx</b>
Name:                     nginx
Namespace:                jegan
Labels:                   app=nginx
Annotations:              <none>
Selector:                 app=nginx
Type:                     NodePort
IP Family Policy:         SingleStack
IP Families:              IPv4
IP:                       172.30.161.212
IPs:                      172.30.161.212
Port:                     <unset>  8080/TCP
TargetPort:               8080/TCP
NodePort:                 <unset>  32726/TCP
Endpoints:                10.128.2.38:8080,10.130.0.92:8080,10.131.1.94:8080
Session Affinity:         None
External Traffic Policy:  Cluster
Events:                   <none>
</pre>

You may find the node ip as shown below
```
oc get nodes -o wide
```
Expected output
<pre>
jegan@tektutor.org)$ <b>oc get nodes -o wide</b>
NAME                        STATUS   ROLES           AGE    VERSION           INTERNAL-IP       EXTERNAL-IP   OS-IMAGE                                                        KERNEL-VERSION                 CONTAINER-RUNTIME
master-1.ocp.tektutor.org   Ready    master,worker   3d5h   v1.23.5+3afdacb   192.168.122.245   <none>        Red Hat Enterprise Linux CoreOS 410.84.202206010432-0 (Ootpa)   4.18.0-305.49.1.el8_4.x86_64   cri-o://1.23.2-12.rhaos4.10.git5fe1720.el8
master-2.ocp.tektutor.org   Ready    master,worker   3d5h   v1.23.5+3afdacb   192.168.122.152   <none>        Red Hat Enterprise Linux CoreOS 410.84.202206010432-0 (Ootpa)   4.18.0-305.49.1.el8_4.x86_64   cri-o://1.23.2-12.rhaos4.10.git5fe1720.el8
master-3.ocp.tektutor.org   Ready    master,worker   3d5h   v1.23.5+3afdacb   192.168.122.144   <none>        Red Hat Enterprise Linux CoreOS 410.84.202206010432-0 (Ootpa)   4.18.0-305.49.1.el8_4.x86_64   cri-o://1.23.2-12.rhaos4.10.git5fe1720.el8
worker-1.ocp.tektutor.org   Ready    worker          3d5h   v1.23.5+3afdacb   192.168.122.26    <none>        Red Hat Enterprise Linux CoreOS 410.84.202206010432-0 (Ootpa)   4.18.0-305.49.1.el8_4.x86_64   cri-o://1.23.2-12.rhaos4.10.git5fe1720.el8
worker-2.ocp.tektutor.org   Ready    worker          3d5h   v1.23.5+3afdacb   192.168.122.180   <none>        Red Hat Enterprise Linux CoreOS 410.84.202206010432-0 (Ootpa)   4.18.0-305.49.1.el8_4.x86_64   cri-o://1.23.2-12.rhaos4.10.git5fe1720.el8
</pre>

Accessing the NodePort service
```
curl http://192.168.122.180:32726
```
In the above curl command, you can substitute any one of the OpenShift node IP irrespective of where the Pods are running.

Expected output
<pre>
(jegan@tektutor.org)$ <b>curl http://192.168.122.180:32726</b>
<!DOCTYPE html>
<html>
<head>
<title>Welcome to nginx!</title>
<style>
    body {
        width: 35em;
        margin: 0 auto;
        font-family: Tahoma, Verdana, Arial, sans-serif;
    }
</style>
</head>
<body>
<h1>Welcome to nginx!</h1>
<p>If you see this page, the nginx web server is successfully installed and
working. Further configuration is required.</p>

<p>For online documentation and support please refer to
<a href="http://nginx.org/">nginx.org</a>.<br/>
Commercial support is available at
<a href="http://nginx.com/">nginx.com</a>.</p>

<p><em>Thank you for using nginx.</em></p>
</body>
</html>
</pre>

## Creating LoadBalancer External Service

You can refer this blog for step by step instructions
<pre>
https://medium.com/tektutor/using-metallb-loadbalancer-with-bare-metal-openshift-onprem-4230944bfa35
</pre>

You may to delete the existing service before you actually create a LoadBalancer service for nginx deployment.
```
oc delete svc/nginx
```

Let's create the LoadBalancer service
```
oc expose deploy/nginx --type=LoadBalancer --port=8080
```

Let's describe and see if an external load balancer IP is getting assigned to the service.
```
oc describe svc/nginx
```
Expected output
<pre>
(jegan@tektutor.org)$ <b>oc get svc</b>
NAME    TYPE           CLUSTER-IP       EXTERNAL-IP   PORT(S)          AGE
nginx   LoadBalancer   172.30.167.250   <pending>     8080:32539/TCP   2s
</pre>

In the absence of metallb or similar load balancer in our cluster, the above loadbalancer service will not get an external IP.  Hence you need to install the Metallb Operator as an Admin from OpenShift webconsole, allocate an address pool, create a metallb resource so that our nginx lb service will get an external IP as shown below.

<pre>
(jegan@tektutor.org)$ <b>oc get svc</b>
NAME    TYPE           CLUSTER-IP       EXTERNAL-IP      PORT(S)          AGE
nginx   LoadBalancer   172.30.167.250   192.168.122.90   8080:32539/TCP   13m
</pre>

You may now access the nginx lb service as shown below
```
curl http://192.168.122.90:8080
```
Expected output
<pre>
(jegan@tektutor.org)$ curl 192.168.122.90:8080
<!DOCTYPE html>
<html>
<head>
<title>Welcome to nginx!</title>
<style>
    body {
        width: 35em;
        margin: 0 auto;
        font-family: Tahoma, Verdana, Ar(jegan@tektutor.org)$ 
ial, sans-serif;
    }
</style>
</head>
<body>
<h1>Welcome to nginx!</h1>
<p>If you see this page, the nginx web server is successfully installed and
working. Further configuration is required.</p>jegan@tektutor.org)$ <b>oc apply -f pod-cm.yml</b>
pod/pod-with-cm created


<p>For online documentation and support please refer to
<a href="http://nginx.org/">nginx.org</a>.<br/>
Commercial support is available at
<a href="http://nginx.com/">nginx.com</a>.</p>

<p><em>Thank you for using nginx.</em></p>
</body>
</html>
</pre>

## ⛹️‍♀️ Lab - Creating configmaps and accessing those config details from a Pod/Deployment

You may find this blog useful
<pre>
https://medium.com/@jegan_50867/openshift-secrets-and-configmap-223f1815bf8a
</pre>

First create a configmap file tools-cm.yml
<pre>
apiVersion: v1
kind: ConfigMap
metadata:
  name: my-config-map
data:
  JDK_HOME: /usr/lib/jdk8
  M2_HOME: /usr/share/maven
</pre>

Create the configmap in the cluster
```
oc apply -f tools-cm.yml
```
Expected output
<pre>
(jegan@tektutor.org)$ <b>oc apply -f tools-cm.yml</b>
configmap/my-config-map created
</pre>

List the configmaps
```
oc get cm
```
Expected output
<pre>
(jegan@tektutor.org)$ <b>oc get cm</b>
NAME                       DATA   AGE
kube-root-ca.crt           1      132m
<b>my-config-map              2      115s</b>
openshift-service-ca.crt   1      132m
</pre>

Describe the configmap
```
oc describe cm my-config-map
```
Expected output
<pre>
(jegan@tektutor.org)$ <b>oc describe cm/my-config-map</b>
Name:         my-config-map
Namespace:    jegan
Labels:       <none>
Annotations:  <none>

Data
====
JDK_HOME:
----
/usr/lib/jdk8
M2_HOME:
----
/usr/share/maven

BinaryData
====

Events:  <none>
</pre>

Now let's create a Pod manifest file pod-with-cm.yml
<pre>
apiVersion: v1
kind: Pod
metadata:
  name: pod-with-cm
spec:
  containers:
    - name: c1
      image: registry.redhat.io/ubi8/ubi-minimal      
      command:
        - /bin/bash
      args:
        - -c
        - echo '$(JDK_PATH) $(MAVEN_PATH)'
      env:
        - name: JDK_PATH
          valueFrom:
            configMapKeyRef:
              name: my-config-map
              key: JDK_HOME 
        - name: MAVEN_PATH
          valueFrom:
            configMapKeyRef:
              name: my-config-map
              key: M2_HOME 
  restartPolicy: Never
</pre>

Let's create the pod in the cluster
```
oc apply -f pod-with-cm.yml
```
Expected output
<pre>
jegan@tektutor.org)$ <b>oc apply -f pod-cm.yml</b>
pod/pod-with-cm created
</pre>

Finally you can check the logs of the pod-with-cm pod
```
oc logs pod-with-cm
```

Expected output
<pre>
(jegan@tektutor.org)$ <b>oc logs pod-with-cm</b>
/usr/lib/jdk8 /usr/share/maven
</pre>

## ⛹️‍♂️ Lab - Creating secret and accessing the secret values from Pod/Deployment

You may find this blog useful
<pre>
https://medium.com/@jegan_50867/openshift-secrets-and-configmap-223f1815bf8a
</pre>

First create the secret file mysql-credentials.yml
<pre>
apiVersion: v1
kind: Secret 
metadata:
  name: mysql-credentials
data:
  username: cm9vdA==
  password: cm9vdEAxMjM=
</pre>

The value for username and password are base64 encoded as shown below
<pre>
(jegan@tektutor.org)$ <b>echo -n root|base64</b>
cm9vdA==
(jegan@tektutor.org)$ <b>echo -n root@123|base64</b>
cm9vdEAxMjM=
</pre>

You may now create the secret resource in the cluster
```
oc apply -f mysql-credentials.yml
```
Expected output
<pre>
(jegan@tektutor.org)$ oc apply -f mysql-credentials.yml 
secret/mysql-credentials created
</pre>

List the secrets
```
oc get secrets
```

Expected output
<pre>
(jegan@tektutor.org)$ <b>oc get secrets</b>
NAME                                   TYPE                                  DATA   AGE
builder-dockercfg-2flx6                kubernetes.io/dockercfg               1      138m
builder-token-lrdwr                    kubernetes.io/service-account-token   4      138m
builder-token-mxgdw                    kubernetes.io/service-account-token   4      138m
default-dockercfg-j9pgq                kubernetes.io/dockercfg               1      138m
default-token-mjh55                    kubernetes.io/service-account-token   4      138m
default-token-t4c5n                    kubernetes.io/service-account-token   4      138m
deployer-dockercfg-hr5l9               kubernetes.io/dockercfg               1      138m
deployer-token-g9mpc                   kubernetes.io/service-account-token   4      138m
deployer-token-qbq2r                   kubernetes.io/service-account-token   4      138m
<b>mysql-credentials                      Opaque                                2      33s</b>
</pre>

Describe the secret see if it reveals the secret values
```
oc describe secret mysql-credentials
```

Expected output
<pre>
(jegan@tektutor.org)$ <b>oc describe secret mysql-credentials</b>
Name:         mysql-credentials
Namespace:    jegan
Labels:       <none>
Annotations:  <none>

Type:  Opaque

Data
====
username:  4 bytes
password:  8 bytes
</pre>

Now let's see how to access the secret details from a Pod/Deployment

Let's create a pod manifest file pod-with-secrets.yml
<pre>
apiVersion: v1
kind: Pod 
metadata:
  name: pod-with-secrets
spec:
  containers:
    - name: c1
      image: registry.redhat.io/ubi8/ubi-minimal
      command:
        - /bin/bash
      args:
        - -c
        - echo '$(USERNAME) $(PASSWORD)'
      env:
        - name: USERNAME
          valueFrom:
            secretKeyRef:
               name: mysql-credentials 
               key: username
        - name: PASSWORD 
          valueFrom:
            secretKeyRef:
               name: mysql-credentials 
               key: password 
  restartPolicy: Never 
</pre>

Let's create the pod in the cluster
```
oc apply -f pod-with-secrets.yml
```

Expected output
<pre>
(jegan@tektutor.org)$ <b>oc apply -f pod-with-secrets.yml</b>
pod/pod-with-secrets created
</pre>

List the pods
```
oc get po

Expected output
<pre>
(jegan@tektutor.org)$ <b>oc get po</b>
NAME               READY   STATUS      RESTARTS   AGE
pod-with-cm        0/1     Completed   0          14m
pod-with-secrets   0/1     Completed   0          79s
</pre>

You may check the logs to see the output
```
oc logs pod-with-secrets
```

Expected output
<pre>
(jegan@tektutor.org)$ oc logs pod-with-secrets
root root@123
</pre>
