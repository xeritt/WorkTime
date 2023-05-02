#!/bin/bash

TIMES_FILE='times.dat'
SECONDS_FILE='seconds.dat'

PROJECT_FILE='project.dat'
PROJECTS_DIR='projects'

convertsecs() {
 ((h=${1}/3600))
 ((m=(${1}%3600)/60))
 ((s=${1}%60))
 printf "%02d:%02d:%02d\n" $h $m $s
}

getproject() {
	name=`cat ${1}`
	printf "%s" $name
}


if [ -f $PROJECT_FILE ]; then

	PROJECT=$(getproject $PROJECT_FILE)
	if [ -z PROJECT ]; then
		echo 'Empty project name.'
		exit 3
	fi
	
	if [ ! -d $PROJECTS_DIR ]; then
		echo 'No projects dir.'
		exit 4
	fi
	PROJECT_PATH=$PROJECTS_DIR'/'$PROJECT
	TIMES_FILE=$PROJECT_PATH'/'$TIMES_FILE
	SECONDS_FILE=$PROJECT_PATH'/'$SECONDS_FILE
else
	echo 'No project file: '$PROJECT_FILE
	PROJECT_PATH=''
	##exit 1
fi
