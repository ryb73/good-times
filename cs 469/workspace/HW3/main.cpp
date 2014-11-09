#include <iostream>
#include <string.h>

using namespace std;

void printUsage(char* exName);
int parseBitString(char* numStr);
string intToBitString(int num, int paddingLen);
int lookupSbox(int key);
void printMatches(int mask, int out);

/**
 * Representation of S-Box
 */
const int SBOX[4][16] = { { 14,  4, 13, 1,  2, 15, 11,  8,  3, 10,  6, 12,  5,  9, 0,  7 },
						  {  0, 15,  7, 4, 14,  2, 13,  1, 10,  6, 12, 11,  9,  5, 3,  8 },
						  {  4,  1, 14, 8, 13,  6,  2, 11, 15, 12,  9,  7,  3, 10, 5,  0 },
						  { 15, 12,  8, 2,  4,  9,  1,  7,  5, 11,  3, 14, 10,  0, 6, 13 } };

int main(int argc, char* argv[]) {
	if(argc < 2) {
		printUsage(argv[0]);
		return 1;
	}

	if(strcmp(argv[1], "lookup") == 0) {
		// Make sure the right number of arguments was passed
		if(argc != 3) {
			printUsage(argv[0]);
			return 2;
		}

		int key = parseBitString(argv[2]);
		if(key < 0) { // signifies that the bit string was invalid
			cout << "Unable to parse bit string \"" << argv[2] << "\"." << endl;
			return 3;
		} else if(key > 0x3f) { // make sure it's constrained to 6 bits
			cout << "Must enter a 6-bit string (leading zeroes omitted)." << endl;
			return 4;
		}

		cout << "Output: " << intToBitString(lookupSbox(key), 4) << endl;
	} else if(strcmp(argv[1], "check") == 0) {
		// Make sure the right number of arguments was passed
		if(argc != 4) {
			printUsage(argv[0]);
			return 5;
		}

		int mask = parseBitString(argv[2]);
		if(mask < 0) { // signifies that the bit string was invalid
			cout << "Unable to parse bit string \"" << argv[2] << "\"." << endl;
			return 6;
		} else if(mask > 0x3f) { // make sure it's constrained to 6 bits
			cout << "Must enter a 6-bit string (leading zeroes omitted)." << endl;
			return 7;
		}

		int out = parseBitString(argv[3]);
		if(out < 0) { // signifies that the bit string was invalid
			cout << "Unable to parse bit string \"" << argv[3] << "\"." << endl;
			return 8;
		} else if(out > 0xf) { // make sure it's constrained to 4 bits
			cout << "Must enter a 4-bit string (leading zeroes omitted)." << endl;
			return 9;
		}

		printMatches(mask, out);
	} else {
		printUsage(argv[0]);
	}
}

void printUsage(char* exName) {
	cout << "Usage: " << exName << " [lookup <6-bit> | check <6-bit> <4-bit>]" << endl;
}

/**
 * Converts a bit string to an int.
 */
int parseBitString(char* numStr) {
	int res = 0;
	for(unsigned int i = 0; i < strlen(numStr); ++i) {
		res <<= 1;

		// If the current character is a 1, set the current bit in res to 1.
		if(numStr[i] == '1') {
			res |= 1;
		} else if(numStr[i] != '0') {
			return -1; // illegal character encountered
		}
	}

	return res;
}

/**
 * Converts an int to a bit string, then adds 0s to the beginning until the
 * length reaches paddingLen.
 */
string intToBitString(int num, int paddingLen) {
	string res;

	// Go through each bit in num, from the right
	while(num > 0) {
		// If the current bit is a 1, add a 1, otherwise add a 0.
		if(num & 1) {
			res = '1' + res;
		} else {
			res = '0' + res;
		}

		// Move to the next bit
		num >>= 1;
	}

	// Pad with 0s until the length reaches paddingLen
	while(res.length() < paddingLen) {
		res = '0' + res;
	}

	return res;
}

/**
 * Find the corresponding cell in the S-Box
 */
int lookupSbox(int key) {
	// For the row, take the first and last bits
	int row = ((key >> 4) & 2) | (key & 1);
	// For the column, take the middle four bits
	int col = (key >> 1) & 0xf;
	return SBOX[row][col];
}

void printMatches(int mask, int out) {
	for(int i = 0; i < 0x40; ++i) {
		int enc = lookupSbox(i) ^ lookupSbox(i ^ mask);
		if(enc == out) { // if the left side == the right side
			cout << "S1(" << intToBitString(i, 6) << ") ^ ";
			cout << "S1(" << intToBitString(i, 6) << " ^ " << intToBitString(mask, 6) << ")";
			cout << " = " << intToBitString(out, 4) << endl;
		}
	}
}
