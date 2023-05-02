#!/bin/bash
echo '---------------------------------'
echo '   Fast Project Work Time v2.0   '
echo '---------------------------------'
source config.sh

timestamp=$(date +%s)
curtime=`date +%Y-%m-%d_%H:%M:%S`

if [ -f "start" ];  then
	echo 'Stop Time'
	startstamp=`cat start`
	seconds="$((timestamp-startstamp))"
	starttime=`cat starttime`
	echo $starttime $curtime $seconds >> $TIMES_FILE
	echo $seconds >> $SECONDS_FILE
	rm -f start starttime
else
	echo 'Start Time'
	echo $timestamp > start
	echo $curtime > starttime
fi
