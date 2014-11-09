#ifndef PATH_H_
#define PATH_H_

#include <vector>
#include "Outcome.h"

class Path {
	public:
		Path();
		Path(vector<Outcome>& outcomes);
		void read(istream& in);

		const Outcome* whichOutcome(const ItemList& have) const;

		vector<Outcome> getOutcomes() const;
		string getText() const;

	private:
		string text;
		vector<Outcome> outcomes;
};

istream& operator>>(istream& in, Path& p);

#endif /* PATH_H_ */
