#!/bin/bash
echo '----------------------------'
echo '   Work Time Service v1.1   '
echo '----------------------------'
source config.sh

OPTION=exit
function mainMenu {
	OPTION=$(whiptail --title  "Work Time" --menu  "Select operation" 20 60 12 \
	"times" "Time" \
	"clean" "Reset" \
	3>&1 1>&2 2>&3)
}


mainMenu
estatus=$?
if [ $estatus = 1 ];  then
	exit
fi
timestamp=$(date +%s)
curtime=`date +%Y-%m-%d_%H:%M:%S`

if [ $OPTION = "times" ];  then
	echo 'Times'
	cat times.dat
	res=`awk '{ sum += $1 } END { print sum }' $SECONDS_FILE`
	echo $res
	times=$(convertsecs $res)
	echo $times
	echo 'All times '$times > alltime
	whiptail --textbox alltime 12 80
	exit
fi

if [ $OPTION = "clean" ];  then
	zip $timestamp'.zip' $SECONDS_FILE $TIMES_FILE alltime start starttime
	rm -f $SECONDS_FILE $TIMES_FILE alltime start starttime
	echo 'Clean ok.'
	exit
fi
