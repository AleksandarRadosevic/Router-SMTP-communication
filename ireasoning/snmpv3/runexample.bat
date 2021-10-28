echo off
rem script for running example code
if "%1" == "" goto :help 
:run
cls
java -classpath lib\examples.jar;lib\ireasoningsnmp.jar %*
goto :exit
:help
cls
echo Usage: runexample javaclass parameters
echo e.g. runexample snmpget localhost sysuptime
:exit
