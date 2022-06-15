## ⛹️‍♀️ Lab - Creating the mysql and wordpress deployment with Persistent Volume

Kindly ensure the mysql-pv and mysql-pvc is updated with your name wherever you see 'jegan'.

Also the mysql-deploy.yml file shall be updated to use your mysql-pvc-<your-name> before proceeding.

```
oc apply -f wordpress-cm.yml
oc apply -f mysql-credentials.yml
oc apply -f mysql-pv.yml
oc apply -f mysql-pvc.yml
oc apply -f mysql-deploy.yml
oc apply -f mysql-svc.yml

oc apply -f wordpress-deploy.yml
oc apply -f wordpress-svc.yml
oc apply -f wordpress-route.yml
```
