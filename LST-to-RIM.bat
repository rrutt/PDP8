@echo off
set /P PALNAME=Enter LST name (no extension): 
java -jar .\PDP-8-LST-to-RIM\out\ListToRimEncoder.jar SW\%PALNAME%.lst
pause
