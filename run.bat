@echo off
chcp 65001 >nul

set JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF-8

cd /d C:\Users\caslu\IdeaProjects\pruebamaxtime

call mvn clean compile
call mvn exec:java -Dfile.encoding=UTF-8 -Dexec.mainClass="org.example.App" ^
  -Dproyecto="Mantenimiento Datos" ^
  -DtipoHora="H. PROYECTO" ^
  -Dservicio="DATA TESTING" ^
  -Dactividad="GESTIÓN DE PRUEBAS" ^
  -Dcomentario="Support GCP"

pause