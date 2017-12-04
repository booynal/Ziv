#!/bin/bash

home=$(cd $(dirname $0); pwd)
echo home=$home

# opts
opts=$*
echo opts: $opts

mvn -version
mvn $opts package -DskipTests -f $home/pom.xml

cd $home/target
chmod +x *.sh
echo "head: $(git rev-parse HEAD)" > build
echo "time: $(date +'%Y-%m-%d %H:%M:%S')" >> build
tar -zcf jdbc-hive.tgz build *.sh *.properties *.jar lib/
