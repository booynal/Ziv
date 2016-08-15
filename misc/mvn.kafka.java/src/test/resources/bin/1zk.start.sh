#!/bin/sh

$KAFKA_HOME/bin/zookeeper-server-start.sh $KAFKA_HOME/config/zookeeper.properties >$KAFKA_HOME/logs/zk.log 2>1 &


