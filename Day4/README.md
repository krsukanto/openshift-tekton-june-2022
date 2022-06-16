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

## ⛹️‍♀️ Lab - Creating your very first Tekton Task

create a file lab-1.yml with the below code
```
apiVersion: tekton.dev/v1beta1
kind: Task
metadata:
  name: hello
spec:
  steps:
  - name: echo
    image: ubuntu
    command:
      - echo
    args:
      - "Hello World !"
```

You can then run the below command
```
oc apply -f lab-1.yml
```

Expected output
<pre>
(jegan@tektutor.org)$ oc apply -f lab-1.yml 
task.tekton.dev/hello created
</pre>

You can list the tasks either using oc or tkn client tools as shown below
<pre>
(jegan@tektutor.org)$ <b>oc get tasks</b>
NAME    AGE
hello   6s
(jegan@tektutor.org)$ <b>tkn tasks list</b>
NAME    DESCRIPTION   AGE
hello                 15 seconds ago
</pre>

You can run the task as shown below
<pre>
(jegan@tektutor.org)$ <b>tkn task start hello</b>
TaskRun started: hello-run-mlqmk

In order to track the TaskRun progress run:
tkn taskrun logs hello-run-mlqmk -f -n jegan
(jegan@tektutor.org)$ <b>tkn taskrun logs hello-run-mlqmk -f -n jegan</b>

[echo] Hello World !
</pre>

## ⛹️‍♂️ Lab - Creating a Tekton Task with multiple steps

lab-2.yml
<pre>
apiVersion: tekton.dev/v1beta1
kind: Task
metadata:
  name: hello-task-with-multiple-steps
spec:
  steps:
  - name: step-1 
    image: ubuntu
    command:
      - echo
    args:
      - "Step 1 => Hello World !"
  - name: step-2 
    image: ubuntu
    command:
      - echo
    args:
      - "Step 2 => Hello World !"
  - name: step-3 
    image: ubuntu
    command:
      - echo
    args:
      - "Step 3 => Hello World !"
</pre>

Create the task in the cluster
```
oc apply -f lab-2.yml
```

Expected output
<pre>
(jegan@tektutor.org)$ <b>oc apply -f lab-2.yml</b>
task.tekton.dev/hello-task-with-multiple-steps created
</pre>

Listing the task
```
tkn task list
```

Expected output
<pre>
(jegan@tektutor.org)$ <b>tkn task list</b>
NAME                             DESCRIPTION   AGE
hello                                          43 minutes ago
<b>hello-task-with-multiple-steps                 51 seconds ago</b>
</pre>

Executing the task
```
tkn task start hello-task-with-multiple-steps
```

Expected output
<pre>
(jegan@tektutor.org)$ <b>tkn task start hello-task-with-multiple-steps</b>
TaskRun started: hello-task-with-multiple-steps-run-xz8zw

In order to track the TaskRun progress run:
tkn taskrun logs hello-task-with-multiple-steps-run-xz8zw -f -n jegan
</pre>

Checking the output of the taskrun execution
<pre>
(jegan@tektutor.org)$ <b>tkn taskrun logs --last</b>
[step-1] Step 1 => Hello World !

[step-2] Step 2 => Hello World !

[step-3] Step 3 => Hello World !
</per>


## Passing parameters to Task

Create a file name lab-3.yml with the below code
<pre>
apiVersion: tekton.dev/v1beta1
kind: Task
metadata:
  name: hello-task-with-params
spec:
  params:
  - name: message
    type: string
    description: this is an optional describe about the parameter
    default: "Hello TekTon Task !"
  steps:
  - name: echo
    image: ubuntu
    command:
    - echo
    args:
    - $(params.message)
</pre>

Let's create the task in the cluster
```
oc apply -f lab-3.yml
```
Expected output
<pre>
(jegan@tektutor.org)$ <b>oc apply -f lab-3.yml</b>
task.tekton.dev/hello-task-with-params created
</pre>

List the tasks
```
tkn tasks list
```

Execute the tasks
<pre>
(jegan@tektutor.org)$ <b>tkn task start hello-task-with-params</b>
? Value for param `message` of type `string`? (Default is `Hello TekTon Task !`) Hello TekTon Task !
TaskRun started: hello-task-with-params-run-lbbvq

In order to track the TaskRun progress run:
tkn taskrun logs hello-task-with-params-run-lbbvq -f -n jegan
</pre>

Expected output
<pre>
(jegan@tektutor.org)$ <b>tkn task list</b>
NAME                             DESCRIPTION   AGE
hello                                          53 minutes ago
hello-task-with-multiple-steps                 10 minutes ago
<b>hello-task-with-params                         41 seconds ago</b>
</pre>

Check the output
<pre>
(jegan@tektutor.org)$ <b>tkn taskrun logs --last</b>
[echo] Hello TekTon Task !
</pre>

Rerun the task and this type you type your custom message
<pre>
(jegan@tektutor.org)$ <b>tkn taskrun logs --last</b>
[echo] Hello TekTon Task !

? Value for param `message` of type `string`? (Default is `Hello TekTon Task !`) My Custom Message
TaskRun started: hello-task-with-params-run-tqbvm

In order to track the TaskRun progress run:
tkn taskrun logs hello-task-with-params-run-tqbvm -f -n jegan
</pre>

Check the output
<pre>
(jegan@tektutor.org)$ <b>tkn taskrun logs --last -f</b>
[echo] My Custom Message
</pre>
