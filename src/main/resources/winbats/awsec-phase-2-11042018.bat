D:

rmdir /s /q tmp

cd D:\apporchid\ELK\elasticsearch-6.2.2\

rmdir /s /q data
rmdir /s /q logs

mkdir data
mkdir logs

start "elasticsearch vanilla" CMD /c bin\elasticsearch.bat

timeout 10;


REM cd D:\apporchid\ArangoDB\
REM rd /Q /S ArangoDB3-3.3.5-1_win64
REM robocopy /MIR /CREATE D:\apporchid\ArangoDB\orig\ArangoDB3-3.3.5-1_win64\ D:\apporchid\ArangoDB\ArangoDB3-3.3.5-1_win64\

cd D:\apporchid\ArangoDB\ArangoDB3-3.3.5-1_win64\

start "arangodb vanilla" CMD /c usr\bin\arangod.exe
timeout 20


start "databot" "%PROGRAMFILES%\Git\git-bash.exe" --cd="D:/apporchid/security-workspace/ao-security-boilerplate-parent/src/main/resources/11APR2018/" ./ingest.sh
