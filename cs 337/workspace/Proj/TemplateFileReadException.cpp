#include "TemplateFileReadException.h"

TemplateFileReadException::TemplateFileReadException(const string& what_arg) : runtime_error(what_arg) { }

const char* TemplateFileReadException::what() const throw() {
	string res("Unable to read template file: ");
	res += runtime_error::what();
    return res.c_str();
}
