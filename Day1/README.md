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


