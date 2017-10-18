#!/bin/bash

# build this project
echo building...
mvn -q install -DskipTests -f pom.xml

# build executor-impl projects

function buildModel() {
    version=$1
    projectName=executor-$version
    mvn -q package -DskipTests -f $projectName/pom.xml
    jarPath=./target/executor-impl/$version
    mkdir -p $jarPath
    cp $projectName/target/*jar $jarPath
}

buildModel v1
buildModel v2
echo build done.
