#ifndef MALFORMEDSTORYFILEEXCEPTION_H_
#define MALFORMEDSTORYFILEEXCEPTION_H_

#include <string>
#include <stdexcept>

using namespace std;

class MalformedStoryFileException : public runtime_error {
	public:
		MalformedStoryFileException(const string& what_arg);
		const char* what() const throw();
};

#endif /* MALFORMEDSTORYFILEEXCEPTION_H_ */
