#!/bin/bash

echo "Current branches..."
git branch --list

if [ "$1" = "--delete-unmerged" ] || [ "$1" = "-D" ]; then
  delete_arg=D
else
  delete_arg=di
fi

git checkout main && git pull && git fetch -p && git branch -vv | awk '/: gone]/{print $1}' | xargs git branch -$delete_arg

echo "Remaining branches..."
git branch --list

if [ "$1" = "--nuke-stash" ]; then
  echo "Clearing git stash..."
  git stash clear
fi
