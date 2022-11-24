#!/bin/bash

branch=`git status | head -n 1 | awk '{ print $3; }'`
issue=`echo $branch | awk -F / '{ print $2; }'`
echo 'Issuing PR for current branch:' $branch 'to issue' $issue;

hub pull-request -i $issue -b "jamie-nxtgencare:main" -h "jamie-nxtgencare:$branch"
