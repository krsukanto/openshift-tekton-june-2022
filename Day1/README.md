# Day 1 - OpenShift

## Container Engine vs Container Runtime
(jegan@tektutor.org)$ oc project
Using project "jegan" on server "https://api.ocp.tektutor.org:6443".

Docker is a Container Enginer
Docker uses runc Container Runtime

Container Runtime
- is the tool that manages the containers
- create, list, stop, start, restart, kill, abort delete
- Example
  runc, CRI-O(Used in RedHat OpenShift)

Container Engine
- is a high-level tool that depends on Container Runtime to manage containers
- it might depend on other tools to manage Container images
- this is the user-friendly tool that make the end-user life easy to manage images and containers
- For example
  Docker, Podman(Used in RedHat OpenShift)

## What is a Container Orchestration Platform?
- a container management platform
- Example
   - Docker SWARM ( Docker Inc organization - supports only Docker Container Engine )
   - Google Kubernetes ( supports many different Container Engines including Docker, CRI-O, etc., )
   - RedHat OpenShift(Enterprise Product from RedHat) 
       - until v3.x it was supporting Docker
       - starting from v4.x officially supports only CRI-O with Podman
   - OKD Origin(Opensource)
       - officially supports CRI-O but can be configured to support Docker or other container runtimes
   - AWS ROSA - Managed OpenShift service
       - supports only CRI-O with Podman
   - Azure RedHat OpenShift - Managed service
       - supports only CRI-O with Podman
   - AWS EKS ( Elastic Kubernetes Service )
       - supports Docker, CRI-O, etc.,
   - Azure AKS ( Azure Kubernetes Service )
       - supports Docker, CRI-O, etc
- Container Orchestration Platform offers the below features
  - High Availabilty for your application 
  - Self healing platform ( it can both rectify it own issues as well as your application issues )
  - scaling up/down your application/microservices instances on demand
  - rolling update ( upgrading your application on live environment from one version to other without downtime)
  - exposing service 
    - service represents a group of similar application Pods
    - load-balanced group of similar application Pods
    - internal/external service(jegan@tektutor.org)$ oc project
Using project "jegan" on server "https://api.ocp.tektutor.org:6443".

  - inbuilt monitoring facility
    - whenever your application stops responding, it will be replaced with another good working instance
      automatically

## OpenShift Overview
- an Orchestration Platfrom built on top of Google Kubernetes(Opensource)
- OpenShift comes in 2 flavours
  1. OKD(Origin) - opensource
  2. RedHat OpenShift ( Enterprise Product ) 
  3. AWS ROSA - Managed Red OpenShift from AWS(Amazon)
  4. Azure OpenShift - Managed Red OpenShift from Azure(Microsoft)
- RedHat's distribution of Kubernetes with many additional features added on top of Kubernetes

## Difference between Kubernetes andS RedHat OpenShift

### Kubernetes
- mainly command-line
- supports RBAC(Role Based Access Control) but there is no inbuilt user management
- there is a basic version of Webconsole(Dashboard, but generally not used in Production)
- most of the organization use only the CLI
- orchestration platform
- supports rolling update, services, scale up/down, self-healing, monitoring features
- No webconsole
- no built-in private container registry, however we can configure Kubernetes to use an externally setup
  container private registry
- no build-in version control support within Kubernetes
- Tekton CI/CD is not pre-integrated but can be installed to support CI/CD
- no support, only community support with no SLA
- Command-Lint tools used here
   - kubectl ( client tool used by all of us to deploy and manage applications )
   - kubeadm ( administrative tool used to setup kubernetes cluster )
   - tkn ( tekton client tool used to create and manage Tekton CI/CD pipeline )

### RedHat OpenShift
- supports command line and webconsole GUI
- supports User Management out of the box
- supports Private container registry within RedHat OpenShift out of the box
- supports setting up Private GitHub like version control softwares within RedHat OpenShift
- Tekton CI/CD is nicely integrated and supported by RedHat
- overall Openshift gets good support from RedHat as it is a paid Enterprise Product 
- RedHat OpenShift can do all the things that Kubernetes can do + many additional features
- Command-Line tool used here
   - oc ( client tool used to deploy and manage applications )
   - kubectl ( client tool used to deploy and manage applications )
   - tkn ( Tekton client tool used to create CI/CD pipline and manage them )


## What is a OpenShift cluster?
- a collection of many nodes
- each Node could be a physical server, virtual machine of a cloud ec2 instance running in AWS/Azure, etc.,
- nodes are of two types
  1. Master Node
  2. Worker Node

## What is RedHat Enterprise Core OS(RHCOS)?
- RedHat acquired a company called CoreOS, who developed rkt container runtime and optimized OS for containers
- this Operating System is based RedHat Enterprise Linux combined with Core OS
- it is an Operating System optimized for Containerized applications
- an Operating system that is optimized to be used in an Orchestration Platfrom like RedHat OpenShift
- it comes with CRI-O container runtime pre-installed
- it is easy to upgrade the OS with OpenShift commands
- it follows many container best practices, it enforces best practice by imposing certain restrictions on our containerized applications
- Restrictions imposed by CoreOS
  - certain ports like 80 is reserved
  - our applications aren't supposed used Port 80
  - containers can only access files and folder within their home directory
  - normally applictions are allowed to run only as non-admin users unlike Kubernetes(where the applications can be executed as admin/non-admin)
  - /var, /etc /, /root folders are restricted. Only super users will have access to these folder
  - we must avoid using container images that doesn't adhere to the above best practices

## What would happen if you have installed RHEL on RedHat OpenShift worker nodes
- upgrading Operating System from one version to other is your responsibilty
- it is not possible to upgrade using OpenShift,you need to find your own ways to automate

## RedHat OpenShift Master Node
- this is where the Control Plane components runs
- Control Plane Components 
  1. API Server(jegan@tektutor.org)$ oc project
Using project "jegan" on server "https://api.ocp.tektutor.org:6443".

  2. etcd datastore
  3. Schedule
  4. Controller Managers ( a collection of many Controllers )
- it is also possible to configure master node to run user application in addition to control plane components
- if a master node has master and worker role, this is an indication that it can also run user applications
- if a master node has only master role, no user application can be deployed on to the master node
- this can only be RHCOS ( RedHat Enterprise Core OS 

## RedHat OpenShift Worker Node
- this is where user applications will be running
- Worker Node Operating can be either RHEL(RedHat Enterprise Linux) or RHCOS(RedHat Enterprise Core OS )
- RedHat recommned using RHCOS on all nodes 

## Common components that run on both Master and Worker Nodes in RedHat OpenShift
- kubelet Openshift container Agent 
- kubelet is a daemon that runs as a service
- kubelet communicates with the CRI-O container runtime to manage Pods
- kube-proxy - load-balancing for service
- core-dns - supports service discovery
- multus - network interface that allows to communicate with differents type of network add-ons like ( Flannel, Canal, Calico, Weave, etc.,)


### RedHat OpenShift Resources
- the smallest unit that can be deployed in an OpenShift cluster is called Pod
- Pod has one or more containers
- application containers will be running inside a Pod
- ReplicaSet is another resource supported in Kubernetes/OpenShift which manages Pods
- Deployment is another type of resource supported in Kubernetes/OpenShift which manges ReplicaSet
- Services are of 3 types
  1. ClusterIP ( Internal service )
  2. NodePort ( External Service )
  3. LoadBalancer ( External Service )
- DeployConfig is another type of resource supported in OpenShift to deploy applications
  - This was added in OpenShift when there was no support for Deployment and ReplicaSet in Kubernetes
- BuildConfig
  - a Custom Resource added in OpenShift
  - application build Pods are created based on BuildConfig definition
- Build
  - a Pod where your application source are cloned from code repository and compiled/packaged into
    custom container images, which are later pushed into OpenShift Image Registry as Image Streams

## RedHat OpenShift commands

### ⛹️‍♂️ Lab - Listing OpenShift cluster nodes
```
oc get nodes
```

Expected output 
<pre>
(jegan@tektutor.org)$ <b>oc get nodes</b>
NAME                        STATUS   ROLES           AGE    VERSION
master-1.ocp.tektutor.org   Ready    master,worker   2d6h   v1.23.5+3afdacb
master-2.ocp.tektutor.org   Ready    master,worker   2d6h   v1.23.5+3afdacb
master-3.ocp.tektutor.org   Ready    master,worker   2d6h   v1.23.5+3afdacb
worker-1.ocp.tektutor.org   Ready    worker          2d6h   v1.23.5+3afdacb
worker-2.ocp.tektutor.org   Ready    worker          2d6h   v1.23.5+3afdacb
</pre>

### ⛹️‍♂️ Lab - List nodes in wide mode
```
oc get nodes -o wide
```

Expected output
<pre>
(jegan@tektutor.org)$ <b>oc get nodes -o wide</b>
NAME                        STATUS   ROLES           AGE    VERSION           INTERNAL-IP       EXTERNAL-IP   OS-IMAGE                                                        KERNEL-VERSION                 CONTAINER-RUNTIME
master-1.ocp.tektutor.org   Ready    master,worker   2d7h   v1.23.5+3afdacb   192.168.122.245   <none>        Red Hat Enterprise Linux CoreOS 410.84.202206010432-0 (Ootpa)   4.18.0-305.49.1.el8_4.x86_64   cri-o://1.23.2-12.rhaos4.10.git5fe1720.el8
master-2.ocp.tektutor.org   Ready    master,worker   2d7h   v1.23.5+3afdacb   192.168.122.152   <none>        Red Hat Enterprise Linux CoreOS 410.84.202206010432-0 (Ootpa)   4.18.0-305.49.1.el8_4.x86_64   cri-o://1.23.2-12.rhaos4.10.git5fe1720.el8
master-3.ocp.tektutor.org   Ready    master,worker   2d7h   v1.23.5+3afdacb   192.168.122.144   <none>        Red Hat Enterprise Linux CoreOS 410.84.202206010432-0 (Ootpa)   4.18.0-305.49.1.el8_4.x86_64   cri-o://1.23.2-12.rhaos4.10.git5fe1720.el8
worker-1.ocp.tektutor.org   Ready    worker          2d7h   v1.23.5+3afdacb   192.168.122.26    <none>        Red Hat Enterprise Linux CoreOS 410.84.202206010432-0 (Ootpa)   4.18.0-305.49.1.el8_4.x86_64   cri-o://1.23.2-12.rhaos4.10.git5fe1720.el8
worker-2.ocp.tektutor.org   Ready    worker          2d7h   v1.23.5+3afdacb   192.168.122.180   <none>        Red Hat Enterprise Linux CoreOS 410.84.202206010432-0 (Ootpa)   4.18.0-305.49.1.el8_4.x86_64   cri-o://1.23.2-12.rhaos4.10.git5fe1720.el8
</pre>


## ⛹️‍♂️ Lab - Find more details about a node
```
oc describe node/master-1.ocp.tektutor.org
```

Expected output
<pre>
(jegan@tektutor.org)$ <b>oc describe node/master-1.ocp.tektutor.org</b>
Name:               master-1.ocp.tektutor.org
Roles:              master,worker
Labels:             beta.kubernetes.io/arch=amd64
                    beta.kubernetes.io/os=linux
                    kubernetes.io/arch=amd64
                    kubernetes.io/hostname=master-1.ocp.tektutor.org
                    kubernetes.io/os=linux
                    node-role.kubernetes.io/master=
                    node-role.kubernetes.io/worker=
                    node.openshift.io/os_id=rhcos
Annotations:        machineconfiguration.openshift.io/controlPlaneTopology: HighlyAvailable
                    machineconfiguration.openshift.io/currentConfig: rendered-master-7172f9670bde654c6c0bce4e95844ec8
                    machineconfiguration.openshift.io/desiredConfig: rendered-master-7172f9670bde654c6c0bce4e95844ec8
                    machineconfiguration.openshift.io/reason: 
                    machineconfiguration.openshift.io/state: Done
                    volumes.kubernetes.io/controller-managed-attach-detach: true
CreationTimestamp:  Sat, 11 Jun 2022 04:21:15 +0530
Taints:             <none>
Unschedulable:      false
Lease:
  HolderIdentity:  master-1.ocp.tektutor.org
  AcquireTime:     <unset>
  RenewTime:       Mon, 13 Jun 2022 11:45:33 +0530
Conditions:
  Type             Status  LastHeartbeatTime                 LastTransitionTime                Reason                       Message
  ----             ------  -----------------                 ------------------                ------                       -------
  MemoryPressure   False   Mon, 13 Jun 2022 11:40:44 +0530   Sat, 11 Jun 2022 04:36:20 +0530   KubeletHasSufficientMemory   kubelet has sufficient memory available
  DiskPressure     False   Mon, 13 Jun 2022 11:40:44 +0530   Sat, 11 Jun 2022 04:36:20 +0530   KubeletHasNoDiskPressure     kubelet has no disk pressure
  PIDPressure      False   Mon, 13 Jun 2022 11:40:44 +0530   Sat, 11 Jun 2022 04:36:20 +0530   KubeletHasSufficientPID      kubelet has sufficient PID available
  Ready            True    Mon, 13 Jun 2022 11:40:44 +0530   Sat, 11 Jun 2022 04:36:20 +0530   KubeletReady                 kubelet is posting ready status
Addresses:
  InternalIP:  192.168.122.245
  Hostname:    master-1.ocp.tektutor.org
Capacity:
  cpu:                8
  ephemeral-storage:  104322028Ki
  hugepages-1Gi:      0
  hugepages-2Mi:      0
  memory:             64405380Ki
  pods:               250
Allocatable:
  cpu:                7500m
  ephemeral-storage:  96143180846
  hugepages-1Gi:      0
  hugepages-2Mi:      0
  memory:             63254404Ki
  pods:               250
System Info:
  Machine ID:                                       6ce15721c778419e8a91f4e734d9ae6f
  System UUID:                                      6ce15721-c778-419e-8a91-f4e734d9ae6f
  Boot ID:                                          c88a3a90-b92d-458d-b751-22c786724620
  Kernel Version:                                   4.18.0-305.49.1.el8_4.x86_64
  OS Image:                                         Red Hat Enterprise Linux CoreOS 410.84.202206010432-0 (Ootpa)
  Operating System:                                 linux
  Architecture:                                     amd64
  Container Runtime Version:                        cri-o://1.23.2-12.rhaos4.10.git5fe1720.el8
  Kubelet Version:                                  v1.23.5+3afdacb
  Kube-Proxy Version:                               v1.23.5+3afdacb
Non-terminated Pods:                                (59 in total)
  Namespace                                         Name                                                        CPU Requests  CPU Limits  Memory Requests  Memory Limits  Age
  ---------                                         ----                                                        ------------  ----------  ---------------  -------------  ---
  openshift-apiserver-operator                      openshift-apiserver-operator-5c5594c98b-czrt5               10m (0%)      0 (0%)      50Mi (0%)        0 (0%)         2d7h
  openshift-apiserver                               apiserver-6d9bd56644-hpq8x                                  110m (1%)     0 (0%)      250Mi (0%)       0 (0%)         2d7h
  openshift-authentication-operator                 authentication-operator-66b6d4cb4d-rdj77                    20m (0%)      0 (0%)      200Mi (0%)       0 (0%)         2d7h
  openshift-authentication                          oauth-openshift-57f74fffb6-m4cs7                            10m (0%)      0 (0%)      50Mi (0%)        0 (0%)         2d7h
  openshift-cloud-credential-operator               cloud-credential-operator-77bbf9fc9f-hx8hc                  20m (0%)      0 (0%)      170Mi (0%)       0 (0%)         2d7h
  openshift-cluster-machine-approver                machine-approver-75bf9f9f78-d54wr                           20m (0%)      0 (0%)      70Mi (0%)        0 (0%)         2d7h
  openshift-cluster-node-tuning-operator            cluster-node-tuning-operator-7b576767b-7q7gv                10m (0%)      0 (0%)      20Mi (0%)        0 (0%)         2d7h
  openshift-cluster-node-tuning-operator            tuned-s95bf                                                 10m (0%)      0 (0%)      50Mi (0%)        0 (0%)         2d7h
  openshift-cluster-storage-operator                cluster-storage-operator-7b8888c4cd-g48gc                   10m (0%)      0 (0%)      20Mi (0%)        0 (0%)         2d7h
  openshift-cluster-storage-operator                csi-snapshot-controller-operator-54fbf6bcb4-5tswn           10m (0%)      0 (0%)      65Mi (0%)        0 (0%)         2d7h
  openshift-config-operator                         openshift-config-operator-554466db77-wgncz                  10m (0%)      0 (0%)      50Mi (0%)        0 (0%)         2d7h
  openshift-controller-manager-operator             openshift-controller-manager-operator-6cc4cf9d4-q786w       10m (0%)      0 (0%)      50Mi (0%)        0 (0%)         2d7h
  openshift-controller-manager                      controller-manager-x6774                                    100m (1%)     0 (0%)      100Mi (0%)       0 (0%)         31h
  openshift-dns-operator                            dns-operator-b69bb8cf-cskkj                                 20m (0%)      0 (0%)      69Mi (0%)        0 (0%)         2d7h
  openshift-dns                                     dns-default-lwjwf                                           60m (0%)      0 (0%)      110Mi (0%)       0 (0%)         2d7h
  openshift-dns                                     node-resolver-2jjwb                                         5m (0%)       0 (0%)      21Mi (0%)        0 (0%)         2d7h
  openshift-etcd-operator                           etcd-operator-6574c45d8-jf9nr                               10m (0%)      0 (0%)      50Mi (0%)        0 (0%)         2d7h
  openshift-etcd                                    etcd-master-1.ocp.t(jegan@tektutor.org)$ oc project
Using project "jegan" on server "https://api.ocp.tektutor.org:6443".
ektutor.org                              400m (5%)     0 (0%)      930Mi (1%)       0 (0%)         2d7h
  openshift-etcd                                    etcd-quorum-guard-54c99fcc95-2ptq2                          10m (0%)      0 (0%)      5Mi (0%)         0 (0%)         2d7h
  openshift-image-registry                          cluster-image-registry-operator-584d54c484-2nz7z            10m (0%)      0 (0%)      50Mi (0%)        0 (0%)         2d7h
  openshift-image-registry                          node-ca-fj6k7                                               10m (0%)      0 (0%)      10Mi (0%)        0 (0%)         2d7h
  openshift-ingress-canary                          ingress-canary-k98nm                                        10m (0%)      0 (0%)      20Mi (0%)        0 (0%)         2d7h
  openshift-ingress-operator                        ingress-operator-d5b6d8497-bxlkr                            20m (0%)      0 (0%)      96Mi (0%)        0 (0%)         2d7h
  openshift-ingress                                 router-default-5c56699d58-xtpgq                             100m (1%)     0 (0%)      256Mi (0%)       0 (0%)         2d7h
  openshift-insights                                insights-operator-6fc8988555-s4dpx                          10m (0%)      0 (0%)      30Mi (0%)        0 (0%)         2d7h
  openshift-kube-apiserver-operator                 kube-apiserver-operator-7fb744f444-kbm9x                    10m (0%)      0 (0%)      50Mi (0%)        0 (0%)         2d7h
  openshift-kube-apiserver                          kube-apiserver-guard-master-1.ocp.tektutor.org              10m (0%)      0 (0%)      5Mi (0%)         0 (0%)         2d7h
  openshift-kube-apiserver                          kube-apiserver-master-1.ocp.tektutor.org                    290m (3%)     0 (0%)      1224Mi (1%)      0 (0%)         31h
  openshift-kube-controller-manager-operator        kube-controller-manager-operator-847c8fc9cd-cr5sh           10m (0%)      0 (0%)      50Mi (0%)        0 (0%)         2d7h
  openshift-kube-controller-manager                 kube-controller-manager-guard-master-1.ocp.tektutor.org     10m (0%)      0 (0%)      5Mi (0%)         0 (0%)         2d7h
  openshift-kube-controller-manager                 kube-controller-manager-master-1.ocp.tektutor.org           80m (1%)      0 (0%)      500Mi (0%)       0 (0%)         2d7h
  openshift-kube-scheduler-operator                 openshift-kube-scheduler-operator-59c88f798b-mtcq7          10m (0%)      0 (0%)      50Mi (0%)        0 (0%)         2d7h
  openshift-kube-scheduler                          openshift-kube-scheduler-guard-master-1.ocp.tektutor.org    10m (0%)      0 (0%)      5Mi (0%)         0 (0%)         2d7h
  openshift-kube-scheduler                          openshift-kube-scheduler-master-1.ocp.tektutor.org          25m (0%)      0 (0%)      150Mi (0%)       0 (0%)         2d7h
  openshift-kube-storage-version-migrator-operator  kube-storage-version-migrator-operator-5b8f9fc4d7-v4n7h     10m (0%)      0 (0%)      50Mi (0%)        0 (0%)         2d7h
  openshift-machine-api                             cluster-autoscaler-operator-6859fb9676-xk84q                30m (0%)      0 (0%)      70Mi (0%)        0 (0%)         2d7h
  openshift-machine-api                             cluster-baremetal-operator-6d7b688697-m99v8                 20m (0%)      0 (0%)      70Mi (0%)        0 (0%)         2d7h
  openshift-machine-api                             machine-api-operator-f44fc8fb4-vdnqx                        20m (0%)      0 (0%)      70Mi (0%)        0 (0%)         2d7h
  openshift-machine-config-operator                 machine-config-daemon-hdjxk                                 40m (0%)      0 (0%)      100Mi (0%)       0 (0%)         2d7h
  openshift-machine-config-operator                 machine-config-operator-7b5c44f84-nckdj                     20m (0%)      0 (0%)      50Mi (0%)        0 (0%)         2d7h
  openshift-machine-config-operator                 machine-config-server-7hfbj                                 20m (0%)      0 (0%)      50Mi (0%)        0 (0%)         2d7h
  openshift-marketplace                             community-operators-bdv7p                                   10m (0%)      0 (0%)      50Mi (0%)        0 (0%)         2d7h
  openshift-marketplace                             marketplace-operator-75f56cb469-64xft                       10m (0%)      0 (0%)      50Mi (0%)        0 (0%)         2d7h
  openshift-monitoring                              cluster-monitoring-operator-6cdf847fd-cp2wb                 11m (0%)      0 (0%)      95Mi (0%)        0 (0%)         2d7h
  openshift-monitoring                              node-exporter-ftjbz                                         9m (0%)       0 (0%)      47Mi (0%)        0 (0%)         2d7h
  openshift-multus                                  multus-7chfv                                                10m (0%)      0 (0%)      65Mi (0%)        0 (0%)         2d7h
  openshift-multus                                  multus-additional-cni-plugins-ph92c                         10m (0%)      0 (0%)      10Mi (0%)        0 (0%)         2d7h
  openshift-multus                                  multus-admission-controller-228xr                           20m (0%)      0 (0%)      70Mi (0%)        0 (0%)         2d7h
  openshift-multus                                  network-metrics-daemon-jp54n                                20m (0%)      0 (0%)      120Mi (0%)       0 (0%)         2d7h
  openshift-network-diagnostics                     network-check-target-qxxs2                                  10m (0%)      0 (0%)      15Mi (0%)        0 (0%)         2d7h
  openshift-network-operator                        network-operator-795b8654d8-mvhgg                           10m (0%)      0 (0%)      50Mi (0%)        0 (0%)         2d7h
  openshift-oauth-apiserver                         apiserver-6576f465dc-wlv8b                                  150m (2%)     0 (0%)      200Mi (0%)       0 (0%)         2d7h
  openshift-operator-lifecycle-manager              catalog-operator-6f86b5477d-8s2qw                           10m (0%)      0 (0%)      80Mi (0%)        0 (0%)         2d7h
  openshift-operator-lifecycle-manager              olm-operator-86787bdfd6-whxfn                               10m (0%)      0 (0%)      160Mi (0%)       0 (0%)         2d7h
  openshift-operator-lifecycle-manager              package-server-manager-69f6fb76d6-48wrm                     10m (0%)      0 (0%)      50Mi (0%)        0 (0%)         2d7h
  openshift-operator-lifecycle-manager              packageserver-848c99f96c-b9pmj                              10m (0%)      0 (0%)      50Mi (0%)        0 (0%)         2d7h
  openshift-sdn                                     sdn-cdbc2                                                   110m (1%)     0 (0%)      220Mi (0%)       0 (0%)         2d7h
  openshift-sdn                                     sdn-controller-wrs67                                        20m (0%)      0 (0%)      70Mi (0%)        0 (0%)         2d7h
  openshift-service-ca-operator                     service-ca-operator-65d6c9595-ljwpz                         10m (0%)      0 (0%)      80Mi (0%)        0 (0%)         2d7h
Allocated resources:
  (Total limits may be over 100 percent, i.e., overcommitted.)
  Resource           Requests      Limits
  --------           --------      ------Expected output
322
<pre>
323
oc edit node/master-1.ocp.tektutor.org
324
</pre>
325

  cpu                2080m (27%)   0 (0%)
  memory             6773Mi (10%)  0 (0%)
  ephemeral-storage  0 (0%)        0 (0%)
  hugepages-1Gi      0 (0%)        0 (0%)
  hugepages-2Mi      0 (0%)        0 (0%)
Events:              <none>
</pre>

## ⛹️‍♀️ Lab - Editing node properties that are mutable
```
oc edit node/master-1.ocp.tektutor.org
```

## ⛹️‍♂️ Lab - Listing the projects in your OpenShift cluster
```
oc get projects
```

Expected output
<pre>
(jegan@tektutor.org)$ <b>oc get projects</b>
NAME                                               DISPLAY NAME   STATUS
default                                                           Active
jegan                                                             Active
kube-node-lease                                                   Active
kube-public                                                       Active
kube-system                                                       Active
nginx-ingress                                                     Active
openshift                                                         Active
openshift-apiserver                                               Active
openshift-apiserver-operator                                      Active
openshift-authentication                                          Active
openshift-authentication-operator                                 Active
openshift-cloud-controller-manager                                Active
openshift-cloud-controller-manager-operator                       Active
openshift-cloud-credential-operator                               Active
openshift-cloud-network-config-controller                         Active
openshift-cluster-csi-drivers                                     Active
openshift-cluster-machine-approver                                Active
openshift-cluster-node-tuning-operator                            Active
openshift-cluster-samples-operator                                Active
openshift-cluster-storage-operator                                Active
openshift-cluster-version                                         Active
openshift-config                                                  Active
openshift-config-managed                                          Active
openshift-config-operator                                         Active
openshift-console                                                 Active
openshift-console-operator                                        Active
openshift-console-user-settings                                   Active
openshift-controller-manager                                      Active
openshift-controller-manager-operator                             Active
openshift-dns                                                     Active
openshift-dns-operator                                            Active
openshift-etcd                                                    Active
openshift-etcd-operator                                           Active
openshift-host-network                                            Active
openshift-image-registry                                          Active
openshift-infra                                                   Active
openshift-ingress                                                 Active
openshift-ingress-canary                                          Active
openshift-ingress-operator                                        Active
openshift-insights                                                Active
openshift-kni-infra                                               Active
openshift-kube-apiserver                                          Active
openshift-kube-apiserver-operator                                 Active
openshift-kube-controller-manager                                 Active
openshift-kube-controller-manager-operator                        Active
openshift-kube-scheduler                                          Active
openshift-kube-scheduler-operator                                 Active
openshift-kube-storage-version-migrator                           Active
openshift-kube-storage-version-migrator-operator                  Active
openshift-machine-api                                             Active
openshift-machine-config-operator                                 Active
openshift-marketplace                                             Active
openshift-monitoring                                              Active
openshift-multus                                                  Active
openshift-network-diagnostics                                     Active
openshift-network-operator                                        Active
openshift-node                                                    Active
openshift-oauth-apiserver                                         Active
openshift-openstack-infra                                         Active
openshift-operator-lifecycle-manager                              Active
openshift-operators                                               Active
openshift-ovirt-infra                                             Active
openshift-sdn                                                     Active
openshift-service-ca                                              Active
openshift-service-ca-operator                                     Active
openshift-user-workload-monitoring                                Active
openshift-vsphere-infra                                           Active
</pre>


## ⛹️ Lab - Creating a project in OpenShift
Please change the name to your short name in the command below.
```
oc new-project jegan
```

Expected output
<pre>
(jegan@tektutor.org)$ <b>oc new-project jegan</b>
Already on project "jegan" on server "https://api.ocp.tektutor.org:6443".

You can add applications to this project with the 'new-app' command. For example, try:

    oc new-app rails-postgresql-example

to build a new example application in Ruby. Or use kubectl to deploy a simple Kubernetes application:

    kubectl create deployment hello-node --image=k8s.gcr.io/e2e-test-images/agnhost:2.33 -- /agnhost serve-hostname
</pre>

## ⛹️‍♀️ Lab - Finding the active project
```
oc project
```

Expected output
<pre>
(jegan@tektutor.org)$ <b>oc project</b>
Using project "jegan" on server "https://api.ocp.tektutor.org:6443".
</pre>

## ⛹️‍♂️ Lab - Deploying a nginx web server within your project
```
oc create deploy nginx --image=nginx:latest
```
Expected output 
<pre>
(jegan@tektutor.org)$ <b>oc create deployment nginx --image=nginx:latest</b>
deployment.apps/nginx created
</pre>

#### Listing deployments
```
oc get deployments
oc get deployment
oc get deploy
```
Expected output
<pre>
(jegan@tektutor.org)$ <b>oc get deploy</b>
NAME    READY   UP-TO-DATE   AVAILABLE   AGE
nginx   0/1     1            0           10s
</pre>

#### Listing Replicasets
```
oc get replicasets
oc get replicaset
oc get rs
```

Expected output
<pre>
(jegan@tektutor.org)$ <b>oc get replicasets</b>
NAME               DESIRED   CURRENT   READY   AGE
nginx-7c658794b9   1         1         0       15s
</pre>


#### Listing Pods
```
oc get pods
oc get pod
oc get po
```

Expected output
<pre>
(jegan@tektutor.org)$ <b>oc get pods</b>
NAME                     READY   STATUS              RESTARTS   AGE
nginx-7c658794b9-ttfqv   0/1     ContainerCreating   0          16s
</pre>

## ⛹️ Lab - Delete the nginx deployment
The command below will delete the deployment by name nginx, its corresponding replicaset(s) and the Pod(s)
```
oc delete deploy/nginx
```

Expected output 
<pre>
(jegan@tektutor.org)$ <b>oc delete deploy/nginx</b>
deployment.apps "nginx" deleted
</pre>

## ⛹️‍♂️ Lab - Listing multiple resource with a single command
```
oc get deploy,rs,po
```

Expected output
<pre>
(jegan@tektutor.org)$ <b>oc get deploy,rs,po</b>
No resources found in jegan namespace.
</pre>

## ⛹️‍♀️ Lab - Creating a nginx deployment using the bitnami image which is rootless
```
oc create deploy nginx --image=bitnami/nginx:1.20
```

Expected output
<pre>
(jegan@tektutor.org)$ <b>oc create deploy nginx --image=bitnami/nginx:1.20</b>
deployment.apps/nginx created
(jegan@tektutor.org)$ <b>oc get deploy,rs,po</b>
NAME                    READY   UP-TO-DATE   AVAILABLE   AGE
deployment.apps/nginx   0/1     1            0           8s

NAME                               DESIRED   CURRENT   READY   AGE
replicaset.apps/nginx-679c8f9884   1         1         0       8s

NAME                         READY   STATUS              RESTARTS   AGE
pod/nginx-679c8f9884-66hjs   0/1     ContainerCreating   0          8s
(jegan@tektutor.org)$ <b>oc get po -w</b>
NAME                     READY   STATUS    RESTARTS   AGE
nginx-679c8f9884-66hjs   1/1     Running   0          20s
</pre>

## ⛹️‍♂️ Lab - Scaling up the nginx deployment
```
oc scale deploy/nginx --replicas=3
```

Expected output 
<pre>
(jegan@tektutor.org)$ <b>oc scale deploy/nginx --replicas=3</b>
deployment.apps/nginx scaled
(jegan@tektutor.org)$ <b>oc get po -w</b>
NAME                     READY   STATUS              RESTARTS   AGE
nginx-679c8f9884-76vnt   0/1     ContainerCreating   0          4s
nginx-679c8f9884-bb895   1/1     Running             0          101s
nginx-679c8f9884-wm9fn   1/1     Running             0          4s
nginx-679c8f9884-76vnt   1/1     Running             0          14s
</pre>
