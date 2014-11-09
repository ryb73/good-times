#ifndef TEMPLATEFILEREADEXCEPTION_H_
#define TEMPLATEFILEREADEXCEPTION_H_

#include <string>
#include <stdexcept>

using namespace std;

class TemplateFileReadException : public runtime_error {
	public:
		TemplateFileReadException(const string& what_arg);
		const char* what() const throw();
};

#endif /* TEMPLATEFILEREADEXCEPTION_H_ */
