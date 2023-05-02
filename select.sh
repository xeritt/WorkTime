#!/bin/bash

source config.sh

if [ -z $1 ]; then
	echo 'Empty project name.'
	script=`basename "$0"`
	echo './'$script' [project_name]'
	exit 1
fi

if [ ! -d $PROJECTS_DIR'/'$1 ]; then
	echo 'No project path: '$PROJECTS_DIR'/'$1
	exit 2
fi


echo $1 > $PROJECT_FILE
echo 'Select project: '$1

