#ifndef ILLEGALSTATEEXCEPTION_H_
#define ILLEGALSTATEEXCEPTION_H_

#include <string>
#include <stdexcept>

using namespace std;

class IllegalStateException : public runtime_error {
	public:
		IllegalStateException(const string& what_arg);
		const char* what() const throw();
};

#endif /* ILLEGALSTATEEXCEPTION_H_ */
