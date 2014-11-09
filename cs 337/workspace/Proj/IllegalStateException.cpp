#include "IllegalStateException.h"

IllegalStateException::IllegalStateException(const string& what_arg) : runtime_error(what_arg) { }

const char* IllegalStateException::what() const throw() {
	string res("Invalid state: ");
	res += runtime_error::what();
    return res.c_str();
}
