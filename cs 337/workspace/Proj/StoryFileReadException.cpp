#include "StoryFileReadException.h"

StoryFileReadException::StoryFileReadException(const string& what_arg) : runtime_error(what_arg) {
}

const char* StoryFileReadException::what() const throw() {
	string res("Unable to read story file: ");
	res += runtime_error::what();
    return res.c_str();
}
