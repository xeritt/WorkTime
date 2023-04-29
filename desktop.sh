#!/bin/bash

cd /home/tirex/work/worktime

java -jar WorkTime-1.0-SNAPSHOT.jar

function pause(){
   read -p "$*"
}

# ...
# call it
##pause 'Press [Enter] key to continue...'