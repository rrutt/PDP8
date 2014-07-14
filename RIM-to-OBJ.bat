@echo off
set /P RIMNAME=Enter RIM name (no extension): 
java -jar .\PDP-8-RIM-to-OBJ\out\RimToObjDecoder.jar SW\%RIMNAME%.rim
pause
