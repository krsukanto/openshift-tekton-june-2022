# Day 5

## Tekton Pipeline
- a execution of chain of tasks
- the tasks in a pipeline are executed some in sequence and some in parallel as per our requirement
- output produced by one Task can be passed on to other Task using workspaces with Persisten Volume


## Creating your first pipeline ( as non-admin user )

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

oc apply -f first-pipeline.yml
```

Expected output
<pre>
(jegan@tektutor.org)$ <b>oc apply -f first-pipeline.yml</b>
task.tekton.dev/task1 created
task.tekton.dev/task2 created
pipeline.tekton.dev/first-pipeline created
</pre>
