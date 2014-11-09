#ifndef NOOUTCOMESFOUNDEXCEPTION_H_
#define NOOUTCOMESFOUNDEXCEPTION_H_

#include <string>
#include <stdexcept>

using namespace std;

class NoOutcomesFoundException : public runtime_error {
	public:
		NoOutcomesFoundException();
		const char* what() const throw();
};

#endif /* NOOUTCOMESFOUNDEXCEPTION_H_ */
