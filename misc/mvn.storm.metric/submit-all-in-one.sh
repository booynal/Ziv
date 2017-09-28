#!/bin/bash

home=$(cd $(dirname $0); pwd)

$home/kill.sh
mvn package -f $home/pom.xml
$home/submit.sh

