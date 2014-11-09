#ifndef STORYFILEREADEXCEPTION_H_
#define STORYFILEREADEXCEPTION_H_

#include <string>
#include <stdexcept>

using namespace std;

class StoryFileReadException : public runtime_error {
	public:
		StoryFileReadException(const string& what_arg);
		const char* what() const throw();
};

#endif /* STORYFILEREADEXCEPTION_H_ */
