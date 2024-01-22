include emu8086.inc

ORG    100h

PRINT 'Hello World!'

GOTOXY 10, 5

PUTC 65           ; 65 - is an ASCII code for 'A'
PUTC 'B'

RET               ; return to operating system.
END               ; directive to stop the compiler.