@echo off

SETLOCAL ENABLEEXTENSIONS ENABLEDELAYEDEXPANSION

cd %~dp0..

IF NOT EXIST dist mkdir dist

call ant compile.module.aho-corasick.production
call scripts\make-javadoc.cmd

jar -cvf dist\jessepav-ahocorasick.jar -C build\classes\aho-corasick org 
jar -cvf dist\jessepav-ahocorasick-javadoc.jar -C build\javadoc . 
jar -cvf dist\jessepav-ahocorasick-source.jar -C src\main\java . 


:END
ENDLOCAL
ECHO ON
@EXIT /B 0
