#!/bin/bash

source config.sh

if [ -z $1 ]; then
	echo 'Empty project name.'
	script=`basename "$0"`
	echo './'$script' [project_name]'
	exit -1
fi

if [ ! -d $PROJECTS_DIR ]; then
	mkdir $PROJECTS_DIR
fi

mkdir $PROJECTS_DIR'/'$1
##echo $1 > $PROJECT_FILE
