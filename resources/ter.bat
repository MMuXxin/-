@echo off
chcp 65001 >nul
cls

set TARGET_IP=localhost
if not "%1"=="" set TARGET_IP=%1

set TARGET_PORT=5275

echo Target Host : %TARGET_IP%
echo Target Port : %TARGET_PORT%
echo Encoding    : UTF-8 (Active code page: 65001)
echo Connecting...
echo ---------------------------------------------------
echo.

%~dp0curl.exe telnet://%TARGET_IP%:%TARGET_PORT%

echo.
echo ---------------------------------------------------
echo [!] Connection closed or lost.
pause