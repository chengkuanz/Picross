
CLS

:: LOCAL VARIABLES ....................................................

SET SRCDIR=src
SET BINDIR=bin
SET BINOUT=game-javac.out
SET BINERR=game-javac.err
SET JARNAME=Game.jar
SET JAROUT=game-jar.out
SET JARERR=game-jar.err
SET DOCDIR=doc
SET DOCPACK=Picross
SET DOCOUT=game-javadoc.out
SET DOCERR=game-javadoc.err
SET MAINCLASSSRC=src/Picross/Game.java
SET MAINCLASSBIN=Picross.Game

@echo off


:: EXECUTION STEPS  ...................................................

echo The current directory is1 %CD%

ECHO "1. Compiling ......................"
::javac -Xlint -cp ".;%SRCDIR%/*" %MAINCLASSSRC% -d %BINDIR% > %BINOUT% 2> %BINERR%
::cd src 
cd src\Picross
echo The current directory is2 %CD%
javac Game.java GameController.java GameModel.java GameView.java GameClient.java GameConfig.java GameServer.java
copy *.class ..\..\bin\Picross
ECHO "2. Creating Jar ..................."
cd ../../bin
jar cvfe %JARNAME% %MAINCLASSBIN% . > %JAROUT% 2> %JARERR%
ECHO "3. Creating Javadoc ..............."
cd ..
::javadoc -cp ".;%BINDIR%" --module-path "%JAVAFXDIR%" --add-modules %MODULELIST% -d %DOCDIR% -sourcepath %SRCDIR% -subpackages %DOCPACK% > %DOCOUT% 2> %DOCERR%
javadoc -cp ".;%BINDIR%" -d %DOCDIR% -sourcepath %SRCDIR% -subpackages %DOCPACK% > %DOCOUT% 2> %DOCERR%
cd bin
ECHO "4. Running Jar ...................."
start java -jar %JARNAME%
cd ..

ECHO "[END OF SCRIPT -------------------]"
ECHO "                                   "
@echo on

:: ---------------------------------------------------------------------
:: End of Script 
:: ---------------------------------------------------------------------

::pause