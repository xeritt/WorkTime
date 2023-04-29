#!/bin/bash

source config.sh

res=`awk '{ sum += $1 } END { print sum }' $SECONDS_FILE`
times=$(convertsecs $res)
echo $times
