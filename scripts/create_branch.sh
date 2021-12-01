#!/bin/bash

echo "Creating branch for issue $1..."

issue_name=`hub issue | grep $1 | awk -F $1 '{ print tolower($2); }' | awk '{$1=$1};1' | sed -e 's/ \|:|?/-/g'| sed -e 's/\"//g' | sed -e "s/'//g" | sed -e "s/?/-/g" | sed -e 's/^\(.\{60\}\).*/\1/g' | sed -e "s/:/-/g" | sed -e "s/\.//g" | sed -e "s/--\?/-/g" | sed -E -e "s/[[:blank:]]/-/g" | sed "s|[(),]||g" | sed "s|[<>,/]||g"`

if [ ${#issue_name} -gt 0 ]
then
	echo issue/$1/$issue_name
else
	echo "Problem creating branch for issue $1 - no issue found"
	exit 1
fi

git checkout -b issue/$1/$issue_name

if [ $? -eq 0 ]
then
	echo "Branch created successfully"
else
	echo "Couldn't create branch"
fi

