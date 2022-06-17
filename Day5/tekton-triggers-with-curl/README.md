# TekTon Trigger
```
oc apply -f tekton-trigger.yaml
```

Check if the event listener pod is running
```
 oc get pod --field-selector=status.phase==Running
```

Find the service name
```
oc get el tektutor-trigger-listener -o=jsonpath='{.status.configuration.generatedName}'
```

Expected output
<pre>
(jegan@tektutor.org)$ <b>oc get el tektutor-trigger-listener -o=jsonpath='{.status.configuration.generatedName}'</b>
el-tektutor-trigger-listener
</pre>

List the service now
```
oc get service $(oc get el tektutor-trigger-listener -o=jsonpath='{.status.configuration.generatedName}')
```

Expected output
<pre>
(jegan@tektutor.org)$ <b>oc get service $(oc get el tektutor-trigger-listener -o=jsonpath='{.status.configuration.generatedName}')</b>
NAME                           TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)             AGE
el-tektutor-trigger-listener   ClusterIP   172.30.214.111   <none>        8080/TCP,9000/TCP   3m58s
</pre>

Load the service name into an environment variable
```
SVC_NAME=$(oc get el tektutor-trigger-listener -o=jsonpath='{.status.configuration.generatedName}')
```

Expected output
<pre>
(jegan@tektutor.org)$ <b>SVC_NAME=$(oc get el tektutor-trigger-listener -o=jsonpath='{.status.configuration.generatedName}')</b>
</pre>

Let's create a route now
```
oc create route edge ${SVC_NAME}-route --service=${SVC_NAME}
```

Expected output
<pre>
(jegan@tektutor.org)$ <b>oc create route edge ${SVC_NAME}-route --service=${SVC_NAME}</b>
route.route.openshift.io/el-tektutor-trigger-listener-route created
</pre>

See if the route is created
```
oc get route ${SVC_NAME}-route
```

Expected output
<pre>
(jegan@tektutor.org)$ <b>oc get route ${SVC_NAME}-route</b>
NAME                                 HOST/PORT                                                        PATH   SERVICES                       PORT            TERMINATION   WILDCARD
el-tektutor-trigger-listener-route   el-tektutor-trigger-listener-route-jegan.apps.ocp.tektutor.org          el-tektutor-trigger-listener   http-listener   edge          None
</pre>

Let's us invoke the Trigger now
```
HOOK_URL=https://$(oc get route ${SVC_NAME}-route -o=jsonpath='{.spec.host}')
curl --insecure --location --request POST ${HOOK_URL} --header 'Content-Type: application/json' --data-raw '{"name":"run-my-app","run-it":"yes-please"}'
```

Expected output
<pre>
(jegan@tektutor.org)$ <b>HOOK_URL=https://$(oc get route ${SVC_NAME}-route -o=jsonpath='{.spec.host}')</b>
(jegan@tektutor.org)$ <b>curl --insecure --location --request POST ${HOOK_URL} --header 'Content-Type: application/json' --data-raw '{"name":"run-my-app","run-it":"yes-please"}'</b>
{"eventListener":"tektutor-trigger-listener","namespace":"jegan","eventListenerUID":"778e9325-11b9-488a-81ef-e8b05866cc16","eventID":"ed729332-efb5-4e6b-95ef-565480db0f58"}
</pre>
