# Day 1 - OpenShift

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
    - internal/external service
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

## What would happen if you have installed RHEL on RedHat OpenShift worker nodes
- upgrading Operating System from one version to other is your responsibilty
- it is not possible to upgrade using OpenShift,you need to find your own ways to automate

## RedHat OpenShift Master Node
- this is where the Control Plane components runs
- Control Plane Components 
  1. API Server
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

### Listing OpenShift cluster nodes
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
