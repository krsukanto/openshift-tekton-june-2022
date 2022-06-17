## Installing tekton-polling-operator
```
oc apply -f https://github.com/bigkevmcd/tekton-polling-operator/releases/download/v0.4.0/release-v0.4.0.yaml
```

Create the pipeline that will be triggered when git poll finds a code commit
```
oc apply -f java-cicd-pipeline.yml
```

You need to create a Repository to setup the GitHub polling
```
oc apply -f git-trigger.yml
```

