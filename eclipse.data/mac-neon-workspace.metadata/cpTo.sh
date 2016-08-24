#!/bin/sh

# source: ./prefs/*
# dest:   $1 : a workspace root folder
dest=$1/.metadata/.plugins/org.eclipse.core.runtime/.settings/
cp -rvf prefs/* $dest


