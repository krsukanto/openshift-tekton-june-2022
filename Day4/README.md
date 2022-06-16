# Day 4

## OpenShift Operators
- Custom Resource(s) + Custom Controller(s)
- One Controller should manage one type of Resource
- You can add new Custom Resource by creating a CRD yaml file
- To manage the Custom Resource, we also need to develop a Custom Controller
- Custom Controllers can be developed in Go, Java or any programming language
apiVersion: tekton.dev/v1beta1
kind: TaskRun
metadata:
  generateName: hello-task-with-multiple-steps-and-params-
spec:
  params:
  - name: msg1
    value: "Taskrun value1"
  - name: msg2
    value: "Taskrun value2"
  taskSpec:
    params:
    - name: msg1
      type: string
    - name: msg2
      type: string
    steps:
    - name: step1
      image: ubuntu
      script: | 
        echo $(params.msg1)
        echo $(params.msg2)
    - name: step2
      image: ubuntu
      command:  
        - echo
      args:
        - $(params.msg1)
        - $(params.msg2)
~                       
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
</pre>


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

## ⛹️‍♂️ Lab - TaskRun with Task inline defined with params

Create a file lab-4.yml
<pre>
apiVersion: tekton.dev/v1beta1
kind: TaskRun
metadata:
  generateName: hello-task-with-multiple-steps-and-params-
spec:
  params:
  - name: msg1
    value: "Taskrun value1"
  - name: msg2
    value: "Taskrun value2"
  taskSpec:
    params:
    - name: msg1
      type: string
    - name: msg2
      type: string
    steps:
    - name: step1
      image: ubuntu
      script: | 
        echo $(params.msg1)
        echo $(params.msg2)
    - name: step2
      image: ubuntu
      command:  
        - echo
      args:
        - $(params.msg1)
        - $(params.msg2)
</pre>

Create the taskrun as shown below
```
oc create -f lab-4.yml
```
Expected output
<pre>
(jegan@tektutor.org)$ <b>oc create -f lab-4.yml</b>
taskrun.tekton.dev/hello-task-with-multiple-steps-and-params-x4h7z created
</pre>

Check the logs
<pre>
(jegan@tektutor.org)$ <b>tkn taskrun logs --last</b>
[step1] + echo Taskrun value1
[step1] + echo Taskrun value2
[step1] Taskrun value1
[step1] Taskrun value2

[step2] Taskrun value1 Taskrun value2
</pre>

### Task with configmap

Create a file named taskrun-with-cm.yml

<pre>
apiVersion: v1
kind: ConfigMap
metadata:
  name: tools-path-cm
data:
  jdk_home: /usr/lib/jdk11
  m2_home: /usr/share/maven
---
apiVersion: tekton.dev/v1beta1
kind: TaskRun
metadata:
  generateName: taskrun-with-confimap-
spec:
  workspaces:
  - name: env-settings
    configMap:
      name: tools-path-cm
      items:
      - key: jdk_home
        path: jdk_home.txt
      - key: m2_home
        path: m2_home.txt
  taskSpec:
    steps:
    - name: read-tools-path-from-cm
      image: ubuntu
      script: |
        cat $(workspaces.env-settings.path)/jdk_home.txt
        cat $(workspaces.env-settings.path)/m2_home.txt
    workspaces:
    - name: env-settings
      mountPath: /my/configmap
</pre>

Create the taskrun as shown below
```
oc create -f taskrun-with-cm.yml
```
Expected output
<pre>
(jegan@tektutor.org)$ <b>oc create -f task-with-cm.yml</b>
configmap/tools-path-cm created
taskrun.tekton.dev/taskrun-with-confimap-z2svb created
</pre>

Check the output logs
<pre>
(jegan@tektutor.org)$ <b>tkn taskrun logs -f --last</b>
[read-tools-path-from-cm] + cat /my/configmap/jdk_home.txt
[read-tools-path-from-cm] /usr/lib/jdk11+ cat /my/configmap/m2_home.txt
[read-tools-path-from-cm] /usr/share/maven
</pre>

## ⛹️‍♀️ Lab - Task with Secrets

Create the taskrun-with-secrets.yml
<pre>
apiVersion: v1
kind: Secret
metadata:
  name: openshift-login-credentials
type: Opaque
data:
  username: a3ViZWFkbWlu
  password: Tlp2REgtcHhtVWotSWFHN20tUFNHcVo=
---
apiVersion: tekton.dev/v1beta1
kind: TaskRun
metadata:
  generateName: task-with-secrets-
spec:
  workspaces:
  - name: openshift-credentials
    secret:
      secretName: openshift-login-credentials
      items:
      - key: username
        path: username.txt
      - key: password
        path: password.txt
  taskSpec:
    steps:
    - name: c1
      image: ubuntu
      script: |
        cat $(workspaces.openshift-credentials.path)/username.txt
        cat $(workspaces.openshift-credentials.path)/password.txt
    workspaces:
    - name: openshift-credentials
      mountPath: /my/secrets
</pre>

Create the above taskrun
```
oc create -f taskrun-with-secrets.yml
```

Expected output
<pre>
(jegan@tektutor.org)$ <b>oc create -f task-with-secrets.yml</b>
secret/openshift-login-credentials created
</pre>

Check the output log
<pre>(jegan@tektutor.org)$ oc create -f task-with-secrets.yml 
secret/openshift-login-credentials created

(jegan@tektutor.org)$ <b>tkn taskrun logs --last -f</b>
[c1] + cat /my/secrets/username.txt
[c1] kubeadmin+ cat /my/secrets/password.txt
[c1] NZvDH-pxmUj-IaG7m-PSGqZ
</pre>

## ⛹️‍♀️ Lab - Task with Persistent Volume

Create a task-with-pv.yml
<pre>
apiVersion: v1
kind: PersistentVolume
metadata:
  name: tekton-pv-jegan
  labels:
    name: jegan
spec:
  capacity:
    storage: 5Mi 
  volumeMode: Filesystem
  accessModes:
    - ReadWriteMany
  persistentVolumeReclaimPolicy: Retain
  nfs:
    server: "192.168.1.80"
    path: "/mnt/nfs_share"
---
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: tekton-pvc-jegan
  labels:
    name: jegan
spec:
  selector:
    matchLabels:
      name: jegan
  resources:
    requests:
      storage: 5Mi 
  volumeMode: Filesystem
  accessModes:
    - ReadWriteMany
---
apiVersion: tekton.dev/v1beta1
kind: TaskRun
metadata:
  generateName: taskrun-with-pv-
spec:
  workspaces:
  - name: myworkspace
    persistentVolumeClaim:
      claimName: tekton-pvc-jegan
  taskSpec:
    steps:
    - name: task-with-pv
      image: ubuntu
      script: |
        echo $(workspaces.myworkspace.path) > $(workspaces.myworkspace.path)/path
    - name: print-path
      image: ubuntu
      script: cat $(workspaces.myworkspace.path)/path
    workspaces:
    - name: myworkspace
      mountPath: /my/myworkspace 
</pre>

Create the taskrun in the cluster
```
oc create -f taskrun-with-pv.yml
```

Expected output
<pre>
(jegan@tektutor.org)$ <b>oc create -f task-with-pv.yml</b>
persistentvolume/tekton-pv-jegan created
persistentvolumeclaim/tekton-pvc-jegan created
taskrun.tekton.dev/taskrun-with-pv-w6t99 created
</pre>

Check the taskrun logs output
<pre>
(jegan@tektutor.org)$ <b>tkn taskrun logs --last -f</b>
[task-with-pv] + echo /my/myworkspace

[print-path] + cat /my/myworkspace/path
[print-path] /my/myworkspace
</pre>


## ⛹️‍♀️ Lab - Ignore step errors to continue the task steps

task-with-error.yml
<pre>
apiVersion: tekton.dev/v1beta1
kind: Task
metadata:
  name: task-with-error
spec:
  steps:
  - name: step1-with-error
    onError: continue
    image: ubuntu
    command: 
      - /bin/bash
    args:
      - /bin/false 

  - name: step2
    image: ubuntu
    command:
      - bin/bash
    args:
      - echo "Step 2 - though there was en error in step1"
</pre>

Create the above task
```
oc apply -f task-with-error.yml
```

Expected output
<pre>
(jegan@tektutor.org)$ oc apply -f task-with-error.yml 
task.tekton.dev/task-with-error created
</pre>

You can execute the taskrun and see the log output
<pre>
(jegan@tektutor.org)$ <b>tkn task start task-with-error --showlog</b>
TaskRun started: task-with-error-run-fwgsh
Waiting for logs to be available...
[step1-with-error] /bin/false: /bin/false: cannot execute binary file

[step2] bin/bash: echo "Step 2 - though there was en error in step1": No such file or directory
</pre>
