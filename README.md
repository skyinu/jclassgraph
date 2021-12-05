# jclassgraph
A small tool used to find out the call relationship of a certain java class of a apk, and then use the browser to render the relationship.

## Uasge 
+ step 1 - git clone the project
+ step 2 - cd class_anaylize
+ step 3 - run gradle command: gradlew shadowJar
+ step 4 - java -jar 'the producation jar' -a  'apk path' -c 'class name' -o 'output dir'

## example

![example](https://github.com/skyinu/jclassgraph/raw/master/eg.png)