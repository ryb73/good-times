README
------------------

1. Build Instructions

In the directory containing the source file, run the following command:

> g++ -o desSbox main.cpp

2. Running Instructions

To look up a cell in the S-Box:

> desSbox lookup 111010

Output: 1010

To check the strength of the S-Box:

> desSbox check 101110 1010

S1(001000) ^ S1(001000 ^ 101110) = 1010
S1(001011) ^ S1(001011 ^ 101110) = 1010
S1(001100) ^ S1(001100 ^ 101110) = 1010
S1(100010) ^ S1(100010 ^ 101110) = 1010
S1(100101) ^ S1(100101 ^ 101110) = 1010
S1(100110) ^ S1(100110 ^ 101110) = 1010