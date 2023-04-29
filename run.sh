#!/bin/bash
echo '--------------------'
echo '   Fast Work Time v1.1   '
echo '--------------------'
SECONDS_FILE='seconds.dat'
TIMES_FILE='times.dat'

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
