                 The PDP-8 Emulator Program
                      A Brief Overview


1.   Introduction
-----------------

The PDP-8 Emulator Program (pdp8main.exe) is a program that emulates 
(mimics) the PDP-8 architecture on an MS-DOS computer. With it 
a user can write, assemble, debug, and run PDP-8 programs and 
thereby obtain an understanding of and appreciation for the architecture of the PDP-8. 

The architecture of the PDP-8 is simple and yet elegant. Simple
in that there are only 8 opcodes, clean in that it has an
easily understood architecture, and elegant in its ability to
implement complex operations using a limited instruction set.

I use the PDP-8 Emulator Program in the teaching of computer organization
at Wittenberg where the simple architecture of the PDP-8 provides
an easy introduction to computer organization, machine code,
and assembly language programming.

The PDP-8 Emulator Program is provided "as is". Any problems 
or suggestions should be addressed to

     Brian J. Shelburne
     Department of Math and Comp Sci 
     Wittenberg University
     Springfield, Ohio 45501
     email: bshelburne@wittenberg.edu

The following sections provide a quick introduction to PDP-8 
Assembler Language (PAL) programming. It is necessarily brief
and does not cover all details yet kowledgeable readers should
be able to learn enough to write simple programs for 
the PDP-8 Emulator. A more complete description of the PDP-8 
Emulator and the PAL langauge can be obtained from the accompanying pdf files
containing "The PDP-8 Emulator Program User's Manual- Fall 2002". 


Table of Contents
------------------

1.   Introduction
2.   Quick Overview of PDP-8 Architecture
3.   Effective Address Calculation
4.   An Overview of the PDP-8 Emulator
5.   PDP-8 Machine Code 
6.   PDP-8 Assembler Language (PAL) Programs
7.   Calling Subroutines
8.   Doing Simple I/O
9.   The PDP-8 Emulator Program User's Manual

2.   Quick Overview of PDP-8 Architecture
-----------------------------------------

Main Memory
-----------

Main memory of the PDP-8 consists of 4096 (2^12) twelve-bit
words. Memory is partitioned into 32 pages of 128 words.
Addresses in memory are given using a page:offset format. 

Bits within a word are numbered left to right 0 - 11

msb ->                                               <- lsb
     +---+---+---+---+---+---+---+---+---+---+---+---+
       0   1   2   3   4   5   6   7   8   9   10  11 


CPU Registers
-------------

There is a single twelve bit accumulator (AC) with a one-bit link
register (L) that captures any carry out of the accumulator.

 Link bit                      Accumulator                        
                       
   +---+    +---+---+---+---+---+---+---+---+---+---+---+---+
              0   1   2   3   4   5   6   7   8   9   10  11 

An additional twelve-bit multiply-quotient register (MQ) was used
for multiply/divide operations in advanced models of the PDP-8
(These operations are not supported by emulator program). 

Other registers include  

    Console Switch Register (SR) - 12 bits
    Program Counter register (PC) - 12 bits
    Instruction Register (IR) - 3 bits
    Central Processor Memory Address register (CPMA) - 12 bits
    Memory Buffer (MB) - 12 bits

Instruction Formats
-------------------

The PDP-8 has eight op-codes and three instruction formats.
Opcodes 0 - 5 reference memory (memory reference instructions or
MRI). The MRI format is given below

            Memory Reference Instruction Format

     +---+---+---+---+---+---+---+---+---+---+---+---+
     |  op-code  |IA |MP |    Offset Address         |  
     +---+---+---+---+---+---+---+---+---+---+---+---+
       0   1   2   3   4   5   6   7   8   9   10  11 

     Bits 0 - 2 : operation code
     Bit 3      : Indirect Addressing Bit (0:direct/1:indirect) 
     Bit 4      : Memory Page (0:Zero Page/1:Current Page)
     Bits 5 -11 : Offset Address


MRI op-codes
 
 0 - Boolean AND memory with accumulator (AND)
 1 - Two's complement Add memory to accumulator (TAD). 
     and complement Link on overflow
 2 - Increment memory and Skip if Zero (ISZ)
 3 - Deposit accumulator to memory and Clear Accumulator (DCA)
 4 - Jump to Subroutine (JMS)
 5 - Jump always (JMP)        


Opcode 6 is an I/O operation, more exactly a family of I/O
instructions

                     Opcode 6 Format    

     +---+---+---+---+---+---+---+---+---+---+---+---+
     | 1   1   0 |    device number      | function  |  
     +---+---+---+---+---+---+---+---+---+---+---+---+
       0   1   2   3   4   5   6   7   8   9   10  11 

     Bits 0 - 2  : opcode 6
     Bits 3 - 8  : Device Number
     Bits 9 - 11 : extended function (operation specification)



Opcode 7 is a family of "micro-instructions" divided into three
groups which include instructions to test, increment,
complement, and rotate the accumulator and/or link bit. Generally
each bit in the extended op-code field (bits 3 - 11)
independently controls a different function. Functions can be
combined creating a class of fairly powerful instructions. 

            Opcode 7 Group One Microinstructions

     +---+---+---+---+---+---+---+---+---+---+---+---+
     | 1   1   1 | 0  cla cll cma cml rar ral 0/1 iac|  
     +---+---+---+---+---+---+---+---+---+---+---+---+
       0   1   2   3   4   5   6   7   8   9   10  11 
                                              
            Opcode 7 Group Two Microinstructions 

     +---+---+---+---+---+---+---+---+---+---+---+---+
     | 1   1   1 | 1  cla sma sza snl 0/1 osr hlt  0 |  
     +---+---+---+---+---+---+---+---+---+---+---+---+
       0   1   2   3   4   5   6   7   8   9   10  11 

            Opcode 7 Group Three Microinstructions 

     +---+---+---+---+---+---+---+---+---+---+---+---+
     | 1   1   1 | 1  cla mqa     mql              1 |  
     +---+---+---+---+---+---+---+---+---+---+---+---+
       0   1   2   3   4   5   6   7   8   9   10  11 


     Bits 0 - 2  : opcode 7
        Bit 3       : 0: Group 1
        Bit 3, 11   : 10 : Group 2/ 11: Group 3
     Bits 4 - 11 : extended opcodes - see below 

     Group One
        cla - clear accumulator
        cll - clear link
        cma - complement accumulator
        cml - complement link
        rar - rotate accumulator+link right
        ral - rotate accumulator+link left
        iac - increment accumulator
        bit 10 : 0:rotate once/1:rotate twice

     Group Two
        sma - skip on minus accumulator
        sza - skip in zero accumulator
        snl - skip in non-zero link
        osr - or switch register with accumulator
        hlt - halt
        bit 8 : if 1 then reverse "sense" of bits 5 - 7 
                not sma -> skip on positive accumulator or spa
                not sza -> skip on non-zero accumulator or sna
                not snl -> skip on zero link or szl 

     Group Three 
        mqa - or accumulator with MQ register
        mql - load MQ register from accumulator and clear 
      

3.   Effective Address Calculation
-----------------------------------

The PDP-8 supports Zero and Current Page Direct Addressing,
Indirect Addressing, and Auto Increment Addressing

Zero/Current Page Direct Addressing
-----------------------------------

The format of a Memory Reference Instruction contains only the
seven-bit offset for a memory reference, The five-bit page number
was obtained by using either Zero page addressing (page number =
00000) or Current page addressing (the five-bit page number of
the address of the instruction was used).

Example (zero page); If address 6224o contained the instruction
1167o (written 6224/1167), since bit 4 of the instruction is
zero, the effective address of this TAD instruction would be
obtained by concatenating five zeros to the seven-bit offset
address

    1   1   6   7        op I M   offset
   001 001 110 111 ->   001|0|0|1 110 111
                                             0   1   6   7  
   effective address -> 00000 + 1 110 111 = 000 001 110 111 

for an effective address of 0167.


Example (current page) ; On the other hand if we have 6224/1367,
since bit 4 is one, the effective address of this TAD instruction
would be obtained by concatenating the leading five bits of the
address of the instruction (110 01 from 6224) to the seven-bit
offset address

 6   2   2   4                          1   1   6   7 
110 010 010 100 <- address/contents -> 001 001 110 111
110 01 <- page number                        1 110 111 <- offset
                                            
Effective address -> 110 01  1 110 111 = 6337


Page Zero is accessible from any address in memory. "Global"
variables should be stored on page Zero. "Local" variables should
be stored on the Current page.


Indirect Addressing 
-------------------

Zero/Current Page Direct addressing can only address two pages
out of 32. To access the other 30 pages, the PDP-8 supports
indirect addressing (bit three equals 1) where the Zero/Current
Page addressing calculation given above is used to calculate the
address of the effective address. 

Auto Increment Addressing
-------------------------
 
Memory locations 010 - 017 (on page zero) function as auto-
increment registers. That is, whenever these addresses are
addressed using indirection, their contents are FIRST incremented
then used as the address of the effective address.

Example (auto-increment addressing) : Suppose address 0010
contains the value 3407. Instruction 1410 obtains the effective
address as follows:

      1   4   1   0      op I M offset   
     001 100 001 000 -> 001|1|0|0 01 000 

Since indirection is used through address 0010, the contents at
0010 (3407) first incremented (3410) and this is the effective
address.


4    An Overview of the PDP-8 Emulator Program
---------------------------------------------------

The PDP-8 Emulator Program was written in Turbo Pascal v6.0 for DOS. 
It has Debug Screen, an Editor/Assembler, Help, and a Run PDP-8 Pgm
screens.

Debug Screen : Displays contents of registers and one page of
memory (in octal). PgUp and PgDn keys can be used to display any
one of the 32 memory pages. A command line interface allows the
user to directly enter, edit, debug, and execute machine code or
PDP-8 assembler language programs

Editor/Assembler : A small no-frills text editor allows the user
to create and edit PDP-8 Assembler Language (PAL) programs.
Programs can be assembled and loaded into the memory of the PDP-8
Emulator for execution. A list file showing PAL versus machine
code can be generated.

Run PDP-8 Pgm : Allows PDP-8 programs to be executed. Includes
displays for some of the registers found on the console of a real
PDP-8. User written PDP-8 I/O subroutines are required to display
results to this screen.

Help: There on-line help for the Debug, Editor/Assembler,and Run
PDP-8 Pgm screens as well as on-line help for the PDP-8 Assembler
Language.

Note that all numbers are displayed in OCTAL


5.   PDP-8 Machine Code 
-----------------------

Machine code program can be entered directly into the PDP-8
memory from the Debug Screen. The address/contents convention
"nnnn/mmmm" means address "nnnn" contains contents "mmmm". 

Four MRI instructions and seven Opcode 7 instructions are
sufficient to execute a number of simple PDP-8 programs

   Machine Code   Assembler   Effect
      1xxx           TAD      Two's Complement Add
      2xxx           ISZ      Increment and Skip on Zero
      3xxx           DCA      Deposit and Clear Accumulator
      5xxx           JMP      Jump Always
      7041           CIA      Complement & Increment Accumulator
                              (same as negate Accumulator)
      7300         CLA CLL    Clear Accumulator and Link
      7402           HLT      Halt
      7500           SMA      Skip on Minus Accumulator
      7440           SZA      Skip on Zero Accumulator    
      7510           SPA      Skip on Positive Accumulator
      7450           SNA      Skip on Non-zero Accumulator .

Example : The following program computes the absolute difference
of A - B (i.e. |A - B|) storing the value at C. A, B, and C are
stored at addresses 0070 - 0072 respectively. nnnn/mmmm denotes
stored mmmm at address nnnn (0050/7300 denotes store 7300 at
address 0050). Before running be sure to set the PC equal to
0050.

0050/7300      clear accumulator and link
0051/1071      load B (from address 0071)
0052/7041      negate (complement and add 1)
0053/1070      add A
0054/7500      skip if negative
0055/5057      jump to address 0057
0056/7041      negate A - B to make positive
0057/3072      deposit |A - B| to C
0060/7402      halt

0070/0015      A
0071/0025      B
0072/0000      C  

Note that a Load instruction is done by adding to cleared
accumulator. Subtraction is done by adding the negative.
Conditional branching is done using Skip on condition coupled
with Jump Always. 

Example ; The following loop program sums the values 1 through 10
(octal).  A loop counter (Count) is set up at address 0251 to be
used by the ISZ (Increment and Skip on Zero instruction)

0200/7300     clear accumulator and link
0201/1250     load N (from address 0250) 
0202/7041     negate
0203/3251     deposit it to Count (address 0251)
0204/3252     deposit 0 to Index (address 0252)
0205/3253     deposit 0 to Sum (address 0253) 
0206/2252     increment Index to 1
0207/1253     load Sum (AC is zero)
0210/1252     add in Index
0211/3253     deposit result in Sum (AC is cleared)
0212/2251     increment Count and Skip if Zero
0213/5206     otherwise loop - jump to 0206
0214/7402     halt

0250/0012     N 
0251/0000     Count
0252/0000     Index
0253/0000     Sum

Note that Increment and Skip on Zero (opcode 2) is used to
control a counting loop at address 0212 and to increment Index at
address 0206

  
6.   PDP-8 Assembler Language (PAL) Programs
--------------------------------------------

The format of a PDP-8 Assemble Language instruction has up to
five fields

   symbolic address, opcode(s) i offset /comment

Commas terminate a symbolic address label, a "/" indicates a
comment. "i" denotes indirection. All field are optional except for
opcodes.


Two assembler directive that are useful are 

    *nnnn     modify value of location counter; assemble code 
               starting at this address
 
    $starting_address    end of program; set entry point   

The code below sums the integers from 1 to N storing the result
in Sum. By convention most PAL programs begin on page one at
location 0200. 

/
/ Code Section
/
*0200          
Main,     cla cll   / clear AC and Link
          tad N     / load N
          cia       / negate it
          dca Count / deposit it to Count and clear AC
          dca Index / zero out Index
          dca Sum   / zero out sum
Loop,     isz Index / add 1 to Index
          tad Sum   / load Sum
          tad Index / add in Index
          dca Sum   / store result in Sum
          isz Count / increment Count and skip on zero
          jmp Loop  / otherwise loop
          hlt       / done
          jmp Main  / allows easy restart
/
/ Data Section 
/
*0250
N,   10
Count, 0
Index, 0
Sum, 0
$Main               / end of pgm - entry point


7.   Calling Subroutines
------------------------

The JMS - Jump to Subroutine (opcode 4) - instruction stores the
return address at the first address of the subroutine and
branches control to the second location of the subroutine. A
return is made via an indirect jump. 

Example : The following subroutine returns the absolute value of
the accumulator

Abs, 0         / store return address here
     sma       / skip in minus accumulator
     jmp .+2   / other jump to current location (. dot) + 2
     cia       / negate accumulator
     jmp i Abs / return via indirect jump

To call this subroutine use

     jms Abs


8.   Doing Simple I/O
---------------------

I/O on the PDP-8 can be done using programmed I/O transfer using
explicit opcode 6 instructions to do I/O. 8-bit ASCII characters 
are transferred between the right-most 8 bits of the accumulator 
(bits 4 - 11) and the keyboard or printer. Device 3 is the 
keyboard, device 4 is the printer (computer screen).
Synchronization between the CPU and I/O device is handled by a
"flag" bit which is set if the device is ready and cleared if the
device if busy. One of the opcode 6 extended functions is a "skip
on flag set" which is used to put the CPU in a wait loop while
waiting on the device.

There are a number of Opcode 6 I/O instructions but only the
following five are really needed (two for output to the printer
and three for input from the keyboard). Recall that Device 3 is the
keyboard and Device 4 is the printer (screen).

Assembler Machine   Effect
Mnemonic    Code

   TSF      6041    Skip on printer Flag set

   TLS      6046    Load Printer Sequence
                    Clear printer flag to 0 and  
                    transfer accumulator to printer

   KSF      6031    Skip on Keyboard Flag Set

   KCC      6032    Clear Keyboard Flag and accumulator

   KRB      6036    Read Keyboard Buffer sequence
                    Clear keyboard flag to 0 and      
                    transfer keyboard buffer to accumulator 
                                                  
We demonstrate their use by displaying code to read a character
(GetChar) and write a character (Type). Putting the code for both
subroutines on page 30 and using indirect subroutine calls
through page 0 allows these subroutine to called from any memory
address.

/
/ Basic I/O Routine Vectors  - Page 0
/
*0050
GetChar,  XGetChar
Type,     XType
/
/ Code Segment - Page 1
/
*0200
Main,     cla cll   / clear AC and link
          kcc       / reset keyboard
          tls       / rest printer
...
          jms i GetChar  / read a character
          dca Hold       / store it
          tad Hold       / get character
          jms i Type     / echo to screen
...

/
/ Basic I/O routines - page 30
/
*7400
XGetChar, 0              / return address here
          ksf            / is keyboard flag raised?
          jmp .-1        / no  - loop
          krb            / yes - read character to AC
          jmp i XGetChar / return
XType,    0              / return address here
          tsk            / is printer flag raised?
          jmp .-1        / no  - loop
          tls            / yes - print character
          cla cll        / clear AC and link
          jmp i XType    / return
$Main

Note: PDP-8 Emulator Program does not echo a type character. 
GetChar only reads a character; to echo it to the screen you 
have to call Type.


9.   PDP-8 Emulator User's Manual
---------------------------------

Bundled with the PDP-8 Emulator is a PDP-Emulator Program User's Manual
which contains more information about the PDP-8 Emulator Program and the 
PDP-8 Assembler Language. The nine pdf files making up the manual are
listed below.

List of Chapters and Contents
-----------------------------

Pdp8covr.pdf - cover page and table of contents
Pdp8ch01.pdf - An Overview of PDP-8 Architecture and the 
               PDP-8 Emulator Program
Pdp8ch02.pdf - PDP-8 Addressing Modes
Pdp8ch03.pdf - PDP-8 Machine Language
Pdp8ch04.pdf - The PDP-8 Assembler Language
Pdp8ch05.pdf - The PDP-8 Instruction Set
Pdp8ch06.pdf - Subroutines, I/O, and Running PDP-8 Programs
Pdp8ch07.pdf - Advanced Programming Examples
Pdp8apdx.pdf - Appendix A: PDP-8 Instructions
               Appendix B: PDP-8 Addressing Modes
               Appendix C: PDP-8 Emulator Commands
               Appendix D: Binary and Octal Integers  
