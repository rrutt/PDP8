# PDP-8 Assembly Language Studio

## Introduction

The development of digital computers has occurred within the single lifetime of people still alive today:

[Wikipedia "Computer" article](http://en.wikipedia.org/wiki/Computer)

Programmable digital computers execute software written in a variety of programming languages.
Most software written today is written in a high-level language processed by a [compiler](http://en.wikipedia.org/wiki/Compiler) or [interpreter](http://en.wikipedia.org/wiki/Interpreted_language).
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
DOSBox serves the historical computer game community but also runs other MS-DOS software.


## PDP-8 Assembly Language

The original PDP-8 models were implemented using discrete diode-transistor logic, with following models using transistor-transistor logic.
Digital Equipment Corp. mounted several components onto a printed circuit board dubbed a [Flip-Chip](http://en.wikipedia.org/wiki/Flip_Chip_%28trademark%29) module.
This approach was soon superseded by [integrated circuit](http://en.wikipedia.org/wiki/Integrated_circuit) technology, but was a breakthrough approach in the 1960's.

Here is an image of the PDP-8 chassis with its Flip-Chip module boards: [PDP-8 boards](http://www.vandermark.ch/pdp8/uploads/PDP8/PDP8.Hardware/pdp8-boards.jpg)
(Note that this is a later model PDP-8 and the Flip-Chip boards in this image contain some small-scale integrated circuit chips along with discrete electronic components.)

In order to keep the Flip-Chip module count to a minimum, the PDP-8 instruction set was extremely minimal even for its day.
This minimalism provides an excellent teaching environment for the basics of digital software computation.

Here is a primer for the PDP-8 Assembly Language, also known as PAL:

<http://www4.wittenberg.edu/academics/mathcomp/shelburne/Comp255/notes/PDP8Assembler.htm>

**_Note:_** The PAL source code comment character is a single forward slash "/".
The example PAL programs in this repository use a double-slash "//" to allow the [Notepad++](http://en.wikipedia.org/wiki/Notepad%2B%2B) editor to be configured to color-code *.pal files as if they are the C# language.
This marks comments in green, and numeric values in orange, so they stand out.

## Setting up the PDP-8 Assembly Language Studio on Microsoft Windows

The default scripts in this repository assume it resides on your local workstation in the folder **C:\PDP8**

This can be achieved by clone the repository with the following commands in a Command Prompt window:

    cd /d C:
    git clone <git-repo-url> PDP8
    cd PDP8

The example PAL programs reside in the **C:\PDP8\SW** folder.

## Hello World using PAL and the PDP-8 Emulator

To start the PDP-8 Emulator navigate to the **C:\PDP8** folder in Windows File Explorer and double-click on either the **DOSBox-PDP8.bat** batch script or the **DOSBox-PDP8** shortcut icon.

The DOSBox program launches with the configuration files **dosbox-0.74.conf** and **dosbox-0.74-PDP-8.conf**

This mounts the **C:\PDP8** Windows folder as if it were the **C:\** drive for the DOSBox, sets the **SW** folder as the current directory, and runs the PDP8Main.exe emulator as directed by the last few lines of **dosbox-0.74-PDP-8.conf**:

    [autoexec]
    mount c c:\pdp8
    c:
    cd sw
    ..\pdp8main\pdp8main.exe

The PDP-8 Emulator main screen appears.

Press the "F1" key to open the emulator help screens.
Press the "Enter" key to cycle thru the help screens.
Press the "Esc" key to return to the emulator.

From the PDP-8 Emulator main screen, press the "E" key to enter the **Editor/Assembler** screen in the emulator.

Press the "R" key to read in a PAL source file.
Type "hello.pal" (without quotes) as the File Name, and then press the "Enter" key.
(Remember that the DOSBox apparent **C:\SW** folder, which is mapped to the **C:\PDP8\SW** Windows folder, is the current directory.)

The **Hello.pal** assembly language source code appears in the emulator's Editor/Assembler screen in text edit mode.

Press the "Esc" key (or the "F10" key) to exit text edit mode and return to the Editor/Assembler menu.

Press the "A" key to assemble the code into the emulator Random Access Memory (RAM).

When prompted "Do want a list file (y/n) ?" type "y" followed by the "Enter" key.
Enter "hello.lst" followed by the "Enter" key to save the listing file.

If prompted "File Exists. Overwrite? (y/n) >" answer with "y" followed by the "Enter" key.

Back in the Editor/Assembler menu, press the "Q" key (or the "Esc" key) to return to emulator main screen.

Press the "R" key to enter the Run PDP-8 Screen.

Press the "G" key to run the program.
The phrase "Hello, World!" should appear in the text display and the program will halt, as requested by the **hlt** (halt) instruction at octal address 0204.

After the **hlt** instruction, the Program Counter (PC) has the octal value 0205, which is a  **jmp** (jump) to the program start address at octal 0200.

Press the "G" key again to resume execution of the program, which should repeat the display of the "Hello, World!" message.

Each press of the "G" key repeats another execution of the program.

Return to the emulator main screen by pressing the "Q" or "Esc" key.

## Using the Emulator Debug Screen

To really appreciate the simple elegance underlying all computer software, spend some time in the PDP-8 Emulator Debug Screen.

Here is a sample debugging session using the "Hello World" program **hello.pal**

From the PDP-8 Emulator main screen, press the "D" key to enter the Debug screen.

Type "r" followed by the "Enter" key to reset the emulated PDP-8 memory and registers to all zeros.

Type "l hello.lst" followed by the "Enter" key to load the assembled "Hello World" program into memory.
This loader command in the emulator interprets the octal address/data pairs that appear in the left margin of the assembly language listing file and loads the appropriate values into the emulated Random Access Memory (RAM).
The **$0200** line near the bottom of the listing, prior to the symbol table dump, instructs the emulator to set the Program Counter (PC) address.

Type "u" followed by the enter key to un-assemble the instruction at the current address, meaning show the mnemonic symbols for that instruction.

Press the space bar once to single-step execute the instruction at the current address of the Program Counter.
The next instruction is automatically un-assembled after the instruction completes.

Continue pressing the space bar to single-step thru the program.

At any time, you may type "g" followed by the "Enter" key to proceed with automatic execution of the program from the current Program Counter address.

For more advanced debugging options, press the "F1" key to open the PDP-8 Emulator help screens positioned at the Debugger command page.

## Conway's Life

The example program **Conway.pal** implements [Conway's Life](http://en.wikipedia.org/wiki/Conway%27s_Game_of_Life) cellular automata algorithm.

Further details on this implementation will be documented in a WordPress blog article.

Load, assemble, and run the program using the same steps described above for "Hello World", substituting the file name **conway.pal** for **hello.pal**

The program can be paused after any generation by pressing any key on the keyboard.
The current generation will finish displaying then the program will pause.
Resume the program by pressing the "G" key.

The PDP-8 Emulator also allows a running program to be paused immediately using the Control-C keyboard combination.

## Advanced PDP-8 Emulator with VT-100 Terminal

The Conway's Life program execution appears best when the output is rendered on a VT-100 terminal that can interpret the [ANSI cursor addressing escape sequences](http://en.wikipedia.org/wiki/ANSI_escape_code).

A Java implementation of a PDP-8 emulator includes several peripheral devices, including a VT-100 video text terminal.

[A PDP-8/E Simulator in Java](http://www.vandermark.ch/pdp8/index.php?n=PDP8.Emulator)

The Java emulator does not read the .LST/.OBJ) paper tape format allowed by the DOSBox compatible PDP-8 Emulator
Instead we need to generate a Read-In Mode (RIM) paper tape file using a custom program provided here.

In the **C:\PDP8** folder, double-click the **LST-to-RIM.bat** batch script.
When prompted "Enter LST name (no extension):" enter "conway" followed by the "Enter" key.
This will generate *.RIM and *.OBJ files based on the octal address/data pairs in the left margin of the assembly listing file.

To run the Conway's Life program in the Java emulator, follow these steps:

Navigate to the **C:\PDP8** folder in Windows file explorer.

Double-click the **PDP8.jar** file to open the emulator.
Several separate windows appear, representing the various peripheral devices of a high-end late model PDP-8 model E.

Drag the main **PDP-8/E** console window to the right to expose other windows.

Minimize the **LP02/LA180** printer window, as we do not need it.
Also minimize the **TU56** DEC-Tape drive.
Minimize the **SI3040-M4043** disk drive.

Drag the **VT100 Terminal** window down to expose the **PC8E** reader/punch window.

Right-click somewhere in the **PC8E** window and select **Reader | Open input reader hispeed** from the pop-up menu.
Navigate to and open **C:\PDP8\SW\CONWAY.RIM**

Click the right side of the third switch graphic to turn the "Reader On".

Right-click somewhere in the **PDP-8/E** console panel window and select **Boot | RIM loader hi speed** from the pop-up menu.

The paper tape graphic in the **PC8E** window should proceed to the left.

Click the **Halt** toggle switch graphic in the **PDP-8/E** console panel window to halt the RIM loader program.
Click the **Halt** toggle again to return it to its "up" position.

Click the "4" toggle button in the **SWITCH REGISTER** section of the **PDP-8/E** console panel window to lift it up to encode an octal 0200 start address into the Switch Register.

Click the "AL" toggle to Address Load the 0200 address into the Program Counter memory address.

Click the "CO" (Continue) toggle in the **START** section of the console panel window to run the program.
(Make sure the "HALT" toggle is in the "up" position.)



