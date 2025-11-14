@echo off
chcp 65001 >nul
title System Configurator

:: Enable ANSI colors in Windows 10+
reg add HKCU\Console /v VirtualTerminalLevel /t REG_DWORD /d 1 /f >nul 2>&1

echo Starting System Configurator...
echo [INFO] For best experience use Windows Terminal or PowerShell
java -jar "target/SystemConfigurator-1.0.jar"
pause