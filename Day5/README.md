# Day 5

## Tekton Pipeline
- a execution of chain of tasks
- the tasks in a pipeline are executed some in sequence and some in parallel as per our requirement
- output produced by one Task can be passed on to other Task using workspaces with Persisten Volume


## ⛹️‍♂️ Lab - Creating your first pipeline ( as non-admin user )

In case you haven't clone this repository, you may do now
```
cd ~
git clone https://github.com/tektutor/openshift-tekton-june-2022.git
```

Once the code repository is cloned, you may create your first pipeline as shown below
```
cd ~
cd openshift-tekton-june-2022
git pull
cd Day5
oc new-project jegan
oc apply -f first-pipeline.yml
```
Make sure, you have already created a project.  If you have existing project, you can ensure you have switched to your project.

Expected output
<pre>
(jegan@tektutor.org)$ <b>oc apply -f first-pipeline.yml</b>
task.tekton.dev/task1 created
task.tekton.dev/task2 created
pipeline.tekton.dev/first-pipeline created
</pre>

Listing the pipeline
```
tkn pipeline list
```

Executing the pipeline
```
tkn pipeline start first-pipeline
```

Expected output
<pre>
(jegan@tektutor.org)$ <b>tkn pipeline start first-pipeline</b>
PipelineRun started: first-pipeline-run-4mzhs

In order to track the PipelineRun progress run:
tkn pipelinerun logs first-pipeline-run-4mzhs -f -n jegan
</pre>

You may check the pipelinerun output logs as shown below
<pre>
(jegan@tektutor.org)$ <b>tkn pr logs -f first-pipeline-run-4mzhs</b>
[task1 : step1] Task1 - step1

[task1 : step2] Task1 - step2

[task2 : step1] Task2 - step1

[task2 : step2] Task2 - step2

[task2 : step3] Task2 - step3
</pre>


## ⛹️‍♂️ Lab - Creating your second pipeline
```
cd ~
cd openshift-tekton-june-2022
git pull
cd Day5
oc project
oc apply -f second-pipeline.yml
```

Expected ouptut
<pre>
(jegan@tektutor.org)$ oc apply -f second-pipeline.yml 
task.tekton.dev/task1 created
task.tekton.dev/task2 created
task.tekton.dev/task3 created
task.tekton.dev/task4 created
task.tekton.dev/task5 created
pipeline.tekton.dev/second-pipeline created
</pre>

You may start the pipeline execution as shown below
```
tkn pipeline start second-pipeline --showlog
```

Expected output
<pre>
(jegan@tektutor.org)$ tkn pipeline start second-pipeline --showlog
PipelineRun started: second-pipeline-run-pf4xm
Waiting for logs to be available...
[task1 : step1] Task1 - step1

[task1 : step2] Task1 - step2

[task3 : step1] Task3 - step1

[task3 : step2] Task3 - step2

[task2 : step1] Task2 - step1

[task2 : step2] Task2 - step2

[task2 : step3] Task2 - step3

[task4 : step1] Task4 - step1

[task4 : step2] Task4 - step2

[task5 : step1] Task5 - step1

[task5 : step2] Task5 - step2
</pre>

## ⛹️‍♂️ Lab - Create a CI/CD pipeline for a spring-boot java application using Tekton pipeline
```
cd ~
cd openshift-tekton-june-2022
git pull
cd Day5
oc project
oc create -f java-cicd-pipeline.yml
```

Expected output
<pre>
(jegan@tektutor.org)$ <b>oc create -f java-cicd-pipeline.yml</b>
persistentvolume/tektutor-tekton-pv-jegan created
persistentvolumeclaim/tektutor-tekton-pvc-jegan created
pipeline.tekton.dev/java-tekton-cicd-pipeline created
pipelinerun.tekton.dev/java-tekton-cicd-pipline-run-wzvjx created
</pre>

You can check the pipelinerun logs in the OpenShift webconsole.
