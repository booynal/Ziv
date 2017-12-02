#!/bin/bash

home=$(cd $(dirname $0); pwd)

opts=$*
$home/package.sh $opts clean
