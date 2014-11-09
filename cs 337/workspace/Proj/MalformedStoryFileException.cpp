#include "MalformedStoryFileException.h"

MalformedStoryFileException::MalformedStoryFileException(const string& what_arg) : runtime_error(what_arg) {
}

const char* MalformedStoryFileException::what() const throw() {
	string res("Illegal format for story file: ");
	res += runtime_error::what();
	return res.c_str();
}
