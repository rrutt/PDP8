# PDP-8 Assembly Language Studio

## Introduction

The development of digital computers has occurred within the single lifetime of people still alive today:

[Wikipedia "Computer" article](http://en.wikipedia.org/wiki/Computer)

Programmable digital computers execute software written in a variety of programming languages.
Most software written today is written in a high-level language processed by a _[compiler](http://en.wikipedia.org/wiki/Compiler)_ or _[interpreter](http://en.wikipedia.org/wiki/Interpreter_%28computing%29)_.
The central processing units (CPUs) of computers actually process lower-level instructions produced by the compilers and interpreters.
In earlier decades software programmers worked directly with the CPU instructions using _[Assembly Language](http://en.wikipedia.org/wiki/Assembly_language)_.

To fully appreciate the power and convenience of high-level programming languages, and the service provided by compilers and interpreters, spend some time writing an algorithm in assembly language.

This GitHub project provides a teaching environment to learn assembly language programming against the Digital Equipment Corp. PDP-8, which is considered the first successful _[mini-computer](http://en.wikipedia.org/wiki/Minicomputer)_ in the Western Hemisphere.
The PDP-8 was often used for instrumentation and process control applications by small laboratories, who considered the original USD $18,000 price tag a bargain in the mid-1960's.
For comparison, the modern _open source hardware_ [Arduino micro-controller](http://en.wikipedia.org/wiki/Arduino) serves the same purpose for between $25 and $100, depending on the model.

## PDP-8 Emulator

Brian Shelbourne of Wittenberg University has written a PDP-8 emulator which he uses in his computer organization course.
This emulator is written using Borland [Turbo Pascal](http://en.wikipedia.org/wiki/Turbo_Pascal) version 6.0 and runs in MS-DOS.
The simplicity of the PDP-8 architecture, and the elegance of Turbo Pascal, allows the emulator program size to be only 85 K.

[Brian Shelburne's PDP-8 Home Page](http://www4.wittenberg.edu/academics/mathcomp/bjsdir/PDP8HomePage.htm)

Running the PDP-8 emulator in 64-bit versions of Windows, such as Windows 7, requires the use of the [DOSBox](http://www.dosbox.com/download.php?main=1) emulator to run the PDP-8 emulator.
Versions of DOSBox also exist for non-Windows operating systems.
DOSBox serves the historical computer game community but also serves for running other MS-DOS software.


## PDP-8 Assembly Language

The original PDP-8 models were implemented using discrete diode-transistor logic, with following models using transistor-transistor logic.
Digital Equipment Corp. mounted several components onto a printed circuit board dubbed a [Flip-Chip](http://en.wikipedia.org/wiki/Flip_Chip_%28trademark%29) module.
This approach was soon superseded by [integrated circuit](http://en.wikipedia.org/wiki/Integrated_circuit) technology, but was a breakthrough approach in the 1960's.

In order to keep the Flip-Chip module count to a minimum, the PDP-8 instruction set was extremely minimal even for its day.
This minimalism serves as an excellent teaching environment for the basics of digital computation.

Here is a primer for the PDP-8 Assembly Language, also known as PAL:

<http://www4.wittenberg.edu/academics/mathcomp/shelburne/Comp255/notes/PDP8Assembler.htm>






## Conway's Life







## Advanced PDP-8 Emulator with VT-100 Terminal

The Conway's Life program execution appears best when the output is rendered on a VT-100 terminal that can interpret the [ANSI cursor addressing escape sequences](http://en.wikipedia.org/wiki/ANSI_escape_code).

A Java implementation of a PDP-8 emulator includes several peripheral devices, including a VT-100 video text terminal.

[A PDP-8/E Simulator in Java](http://www.vandermark.ch/pdp8/index.php?n=PDP8.Emulator)
