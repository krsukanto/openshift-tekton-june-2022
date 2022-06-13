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

## Difference between Kubernetes and RedHat OpenShift

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
   - kubectl
   - kubeadm
   - tkn

### RedHat OpenShift
- supports command line and webconsole GUI
- supports User Management out of the box
- supports Private container registry within RedHat OpenShift out of the box
- supports setting up Private GitHub like version control softwares within RedHat OpenShift
- Tekton CI/CD is nicely integrated and supported by RedHat
- overall Openshift gets good support from RedHat as it is a paid Enterprise Product 
- RedHat OpenShift can do all the things that Kubernetes can do + many additional features
- Command-Line tool used here
   - oc
   - kubectl
   - tkn




