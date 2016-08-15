#!/bin/sh

$KAFKA_HOME/bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic test --from-beginning
