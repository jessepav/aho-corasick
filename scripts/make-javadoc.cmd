@ECHO OFF
SETLOCAL ENABLEEXTENSIONS ENABLEDELAYEDEXPANSION

cd %~dp0..

SET output_dir=build\javadoc

SET visibility=protected

rmdir /S /Q %output_dir%
mkdir %output_dir%

javadoc -classpath lib\*;build\production\meterman2 -%visibility% %linksrc% ^
   -sourcepath src\main\java -subpackages org.ahocorasick:com.illcode.text -d "%output_dir%" ^
   -windowtitle "jessepav/Aho-Corasick Javadocs" -doctitle "jessepav/Aho-Corasick Javadocs"

:END
ENDLOCAL
ECHO ON
@EXIT /B 0
