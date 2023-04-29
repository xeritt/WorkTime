#!/bin/bash

source config.sh

timestamp=$(date +%s)

zip $timestamp'.zip' $SECONDS_FILE $TIMES_FILE alltime start starttime
rm -f $SECONDS_FILE $TIMES_FILE alltime start starttime
