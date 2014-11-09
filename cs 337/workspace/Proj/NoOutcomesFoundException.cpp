#include "NoOutcomesFoundException.h"

NoOutcomesFoundException::NoOutcomesFoundException() : runtime_error("") { }

const char* NoOutcomesFoundException::what() const throw() {
	return "No matching outcome for the specified path given your current items.";
}
