#ifndef ILLEGALITEMLISTEXCEPTION_H_
#define ILLEGALITEMLISTEXCEPTION_H_

#include <string>
#include <stdexcept>

using namespace std;

class IllegalItemListException : public runtime_error {
	public:
		IllegalItemListException(const string& what_arg);
		const char* what() const throw();
};

#endif /* ILLEGALITEMLISTEXCEPTION_H_ */
