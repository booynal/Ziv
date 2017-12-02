#!/bin/bash

home=$(cd $(dirname $0); pwd)
cd $home

console=logs/console.log

# JVM options
JAVA_OPTS="$JAVA_OPTS -Dfile.encoding=UTF-8"
JAVA_OPTS="$JAVA_OPTS -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -Xloggc:$home/logs/gc.log"
JAVA_OPTS="$JAVA_OPTS -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=$home/logs/mem.dump"
##### config end #####

##### exec #####
mkdir -p $home/logs
mainJar=$home/${build.finalName}.jar
java $JAVA_OPTS  -jar $mainJar $* | tee -a $console
##### exec end #####
