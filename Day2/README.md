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
