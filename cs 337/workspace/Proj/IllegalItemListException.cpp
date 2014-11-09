#include "IllegalItemListException.h"

IllegalItemListException::IllegalItemListException(const string& what_arg) : runtime_error(what_arg) { }

const char* IllegalItemListException::what() const throw() {
	string res("Invalid item list: ");
	res += runtime_error::what();
    return res.c_str();
}
