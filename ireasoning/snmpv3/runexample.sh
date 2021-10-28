#!/bin/sh
# script for running example code
if [ "$1" != "" ]
then
	java -classpath lib/examples.jar:lib/ireasoningsnmp.jar $*
else
echo "Usage: runexample javaclass parameters"
echo "e.g. runexample snmpget localhost sysuptime"
fi

