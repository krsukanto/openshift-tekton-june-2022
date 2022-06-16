# Day 4

## OpenShift Operators
- Custom Resource(s) + Custom Controller(s)
- One Controller should manage one type of Resource
- You can add new Custom Resource by creating a CRD yaml file
- To manage the Custom Resource, we also need to develop a Custom Controller
- Custom Controllers can be developed in Go, Java or any programming language

## Controllers
- is an application that runs in a Pod just any any other normal microservice or regualar application
- the only difference, the controllers might require some low-level/cluster level permissions to monitor when New Custom Resources created under any namespace
- Controller can gain low-level access to cluster by creating a Service Account
- Service Account
    - can have Role
      - can request access to one or resources
      - can request for notifications from OpenShift(API Server) when certain resources are added,modified or deleted
    - can have secrets
      - login credentials of OpenShift, weblogic, db servers, web servers, etc.,
    - can have configmaps
 
## OpenShift
- it is RedHat's distribution of Kubernetes with many additional features
- How additional features were added in OpenShift?
  - as Operators
- OpenShift => Kubernetes Orchestration Platform + Many RedHat Operators added

## CI/CD Build Servers
- Jenkins (Opensource)
- Cloudbees (Enterprise Jenkins - with official support)
- TeamCity
- Bamboo
- Microsoft Team Foundation Server(TFS)
- Jenkins-X (Runs within Kubernetes and uses TekTon for CI/CD)
- Tekton
- ArgoCD

## Jenkins/Cloudbees/TeamCity/Bamboo/TFS
- a dedicated server will host these CI/CD platform
- it uses the file-system to store all the Jobs in a specific workspace folders
- Jenkins/Cloudbees comes with many plugins to integrate various types of Version Control
- server should be running 24x7 (365 days) so that it get's notification when someone does code commit to your source control repository.  It then triggers automated build, test and deploy.

## Tekton
- is a knative CI/CD Framework
- can be installed within Kubernetes/OpenShift as an Operator called Tekton Operator
- OpenShift - RedHat Tekton CI/CD Operator
- Tekton adds many Custom Resources + many Custom Controllers
- Tekton runs with OpenShift
- Tekton is serverless
- Tekton can be deployed within Kubernetes/OpenShift
- no dedicated server is required because it runs with OpenShift cluster
- Your application is deployed with OpenShift, so with Tekton even the CI/CD can happen with OpenShift
- Tekton adds some of these below Custom Resources when installed in OpenShift
  - Task
  - TaskRun
  - Pipeline
  - PipelineRun
  - Workspace
  - PipelineResource

## What is a Tekton Task?
- Tekton Task does one functionality like it can clone source code from GitHub/GitLab repository
- Each Tekton Task will create a Pod
- Task has one or more Steps
- Each Step creates a Container within the Task Pod
- The Steps within the Task they are executed sequentially i.e in the order they were created 

## What is a Tekton Pipeline?
- TekTon Pipeline has one or more Tasks
- The Tasks can be executed in sequence or in parallel or a combination as per your requirement
- is chain of Task executed one after the other, wherein some task are executed in sequence while some are executed in parallel

## What is a Workspace?
- each Task accepts some input and produces some output
- Workspace is nothing but a directory in the filesystem that is accessible within the Task Pod
- Workspaces can be restricted to one pod or can optionally be shared by many Tasks in a Pipeline
- Workspaces
    - can be a volume that mounted a ConfigMap (Read Only)
    - can be a volume that mounted a Secret (Read Only)
    - can be a volume that mounted a PersistentVolumeClaim ( can be ReadOnly, Read/Write, Read/Write/Excute )
- For example:-
  - Task1 - Git Clone
      Input  => GitHub URL for your project
      Output => source code cloned from the GitHub project repo given in input
  - Task2 - Compile the application
      Input  => The code cloned by the Task1 
      Output => compiled binaries
  - Task3 - Unit Test the compiled application binary
      Input  => the compiled binaries from Task2
      Output => Unit Test Report
  - Task4 - Static code Analysis using SonarQube
      Input  => the compiled binaries from Task2
      Output => Sonar Report 
