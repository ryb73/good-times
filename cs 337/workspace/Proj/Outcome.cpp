#include <sstream>
#include <iostream>

#include "Outcome.h"
#include "MalformedStoryFileException.h"
#include "IllegalStateException.h"

Outcome::Outcome() : targetState(-1) { }

Outcome::Outcome(int state, ItemList& plus, ItemList& minus, ItemList& has, ItemList& hasnot) {
	this->plus = plus;
	this->minus = minus;
	this->has = has;
	this->hasnot = hasnot;
	targetState = state;
}

bool Outcome::isSatisfied(const ItemList& items) const {
	return items.isSubset(has) && !items.overlapping(hasnot);
}

ItemList Outcome::outcomeOf(const ItemList& before) const {
	return before + plus - minus;
}

int Outcome::getTargetState() const {
    return targetState;
}

string Outcome::getPlus() const {
	return plus.toString();
}

string Outcome::getMinus() const {
	return minus.toString();
}

void Outcome::read(istream& in) {
	string line;
	ItemList* currentList = NULL;
	bool outcomeDone = false;
	while(!outcomeDone && !in.eof()) {
		getline(in, line);
		if(line[line.size()-1] == '\r') // strip carriage returns
			line.resize(line.size() - 1);

		if(line == "#has#") {
			currentList = &has;
		} else if(line == "#not#") {
			currentList = &hasnot;
		} else if(line == "#plus#") {
			currentList = &plus;
		} else if(line == "#minus#") {
			currentList = &minus;
		} else if(line.find("#state#") == 0) {
			stringstream ss;
			ss << line;

			string dummy;
			int state;
			ss >> dummy >> state;

			if(state < 1) {
				string error("target state id must be positive");
				IllegalStateException ise(error);
				throw ise;
			}

			targetState = state;
		} else if(line == "#endout#") {
			outcomeDone = true;
		} else if(line == "") {
			// skip blank lines
		} else {
			if(currentList == NULL) {
				string error("error parsing outcome, encountered: '");
				error += line;
				error += "'";
				MalformedStoryFileException msfe(error);
				throw msfe;
			}

			currentList->addItem(line);
		}
	}

	if(!outcomeDone) {
		string error("expected #endout#");
		MalformedStoryFileException msfe(error);
		throw msfe;
	}

	if(targetState == -1) {
		string error("outcome must have target state");
		MalformedStoryFileException msfe(error);
		throw msfe;
	}
}

istream& operator>>(istream& in, Outcome& o) {
	o.read(in);
	return in;
}
