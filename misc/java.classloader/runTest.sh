#!/bin/bash

$(dirname $0)/package.sh
cd target ; java -classpath *jar com.ziv.java.classload.multiversion.MultiVersionTest $*
