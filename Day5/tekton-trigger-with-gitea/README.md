# Tekton Trigger with our private Gitea Code Repository

## Lab - Install the Gitea template
```
oc apply -f gitea-template.yml
```

Expected output
<pre>
(jegan@tektutor.org)$ <b>oc apply -f gitea-template.yml</b>
template.template.openshift.io/gitea-persistent created
</pre>

## Let's deploy gitea using the installed gitea template

List the template
```
oc get templates
```

Expected output
<pre>
(jegan@tektutor.org)$ <b>oc get templates</b>
NAME               DESCRIPTION                                      PARAMETERS     OBJECTS
gitea-persistent   The Gitea git server (https://gitea.io/en-US/)   15 (1 blank)   11
</pre>

Deploy the gitea

Find your OpenShift cluster default domain
```
oc describe ingresscontroller/default -n openshift-ingress-operator | grep Domain:
```
Expected output
<pre>
(jegan@tektutor.org)$ <b>oc describe ingresscontroller/default -n openshift-ingress-operator | grep Domain:</b>
  Domain:                  apps.ocp.tektutor.org
</pre>

You need to modify the hostname based on your OpenShift cluster domain, project name, etc.,
```
oc new-app --template=jegan/gitea-persistent -p HOSTNAME=gitea-jegan.apps.ocp.tektutor.org
```

In the above command, 
    gitea - application name
    jegan - project name
    default domain - apps.ocp.tektutor.org

Expected output
<pre>
(jegan@tektutor.org)$ <b>oc new-app --template=jegan/gitea-persistent -p HOSTNAME=gitea-jegan.apps.ocp.tektutor.org</b>
--> Deploying template "jegan/gitea-persistent" for "jegan/gitea-persistent" to project jegan

     gitea-persistent
     ---------
     The Gitea git server (https://gitea.io/en-US/)

     * With parameters:
        * APPLICATION_NAME=gitea
        * HOSTNAME=gitea-jegan.apps.ocp.tektutor.org
        * GITEA_VOLUME_CAPACITY=1Gi
        * DB_VOLUME_CAPACITY=1Gi
        * Database Username=gitea
        * Database Password=gitea
        * Database Name=gitea
        * Database Admin Password=3nAt6Ygy # generated
        * Maximum Database Connections=100
        * Shared Buffer Amount=12MB
        * Installation lock=true
        * Gitea Internal Security Token=RxlQX6uP1jQlcKLFpcXouJaotoeMdeC4fLuBYV4LXT2OEdbXWMfrFJJe6kGNo7HcLWUEEklaJsmCa4t6poOAL18kquTTtRWOSxMFj8x8P # generated
        * Gitea Secret Key=07dJwsueD3 # generated
        * Gogs Image=quay.io/gpte-devops-automation/gitea
        * Gogs Image Version Tag=latest

--> Creating resources ...
    serviceaccount "gitea" created
    service "gitea-postgresql" created
    deploymentconfig.apps.openshift.io "gitea-postgresql" created
    service "gitea" created
    route.route.openshift.io "gitea" created
    deploymentconfig.apps.openshift.io "gitea" created
    persistentvolume "gitea-pv-jegan" created
    persistentvolume "gitea-postgress-pv-jegan" created
    persistentvolumeclaim "gitea-repositories" created
    persistentvolumeclaim "gitea-postgres-data" created
    configmap "gitea-config" created
--> Success
    Access your application via route 'gitea-jegan.apps.ocp.tektutor.org' 
    Run 'oc status' to view your app.
</pre>

## Retrieve the self-signed certificate from the Gitea Route
```
ROUTE_CERT=$(openssl s_client -showcerts -connect $(oc get route gitea -o=jsonpath='{.spec.host}'):443 </dev/null 2>/dev/null|openssl x509 -outform PEM | while read line; do echo "    $line"; done)
```

## Create a ConfigMap
```
cat << EOF | oc apply -n openshift-config -f -
apiVersion: v1
kind: ConfigMap
metadata:
  name: demo-ca
data:
  ca-bundle.crt: |
    # CRC Route Cert
${ROUTE_CERT}

EOF
```

## Patch the default proxy instance of the our OpenShift cluster
```
oc patch proxy cluster --type=merge --patch '{"spec":{"trustedCA":{"name":"demo-ca"}}}'
```

The above command will trigger a rolling restart of our master nodes in the OpenShift cluster
```
oc get nodes -w
```
This might take sometime.

## Retrive the gitea url
```
echo "https://$(oc get route gitea -o=jsonpath='{.spec.host}')"
```

Expected output
<pre>
(jegan@tektutor.org)$ <b>echo "https://$(oc get route gitea -o=jsonpath='{.spec.host}')"</b>
https://gitea-jegan.apps.ocp.tektutor.org
</pre>

Launch your gitea hosted in the OpenShift with the above URL.

