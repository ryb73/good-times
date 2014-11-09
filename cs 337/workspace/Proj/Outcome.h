#ifndef OUTCOME_H_
#define OUTCOME_H_

#include <istream>
#include "ItemList.h"

class Outcome {
	public:
		Outcome();
		Outcome(int state, ItemList& plus, ItemList& minus, ItemList& has, ItemList& hasnot);

		bool isSatisfied(const ItemList& items) const;
		ItemList outcomeOf(const ItemList& before) const;

		int getTargetState() const;
		string getPlus() const;
		string getMinus() const;

		void read(istream& in);

	private:
		ItemList plus;
		ItemList minus;
		ItemList has;
		ItemList hasnot;
		int targetState;
};

istream& operator>>(istream& in, Outcome& o);

#endif /* OUTCOME_H_ */
