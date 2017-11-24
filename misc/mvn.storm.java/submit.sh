#!/bin/bash

jar=target/mvn.storm.java-1.0.jar
mainClass=com.ziv.mvn.storm.java.queueack.QueueAckWordcountTest
name=QueueAckWordcountTest

storm jar $jar $mainClass $name
