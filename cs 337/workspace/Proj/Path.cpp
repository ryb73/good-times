#include <string>
#include <vector>
#include <iostream>
#include <fstream>
#include "Path.h"
#include "Outcome.h"
#include "MalformedStoryFileException.h"
#include "NoOutcomesFoundException.h"

Path::Path() { }

Path::Path(vector<Outcome>& outcomes) {
	this->outcomes = outcomes;
}

void Path::read(istream& in) {
	string line;
	bool pathDone = false;
	bool pathTextDone = false;
	while(!pathDone && !in.eof()) {
		getline(in, line);
		if(line[line.size()-1] == '\r') // strip carriage returns
			line.resize(line.size() - 1);

		if(line == "#out#") {
			pathTextDone = true;
			Outcome o;
			in >> o;
			outcomes.push_back(o);
		} else if(line == "#endpath#") {
			pathDone = true;
		} else if(!pathTextDone) {
			text += line + '\n';
		} else if(line == "") {
			// skip blank line
		} else {
			string error("error parsing path, encountered: '");
			error += line;
			error += "'";
			MalformedStoryFileException msfe(error);
			throw msfe;
		}
	}

	if(!pathDone) {
		string error("expected #endpath#");
		MalformedStoryFileException msfe(error);
		throw msfe;
	}

	if(outcomes.size() == 0) {
		string error("path must contain at least one outcome");
		MalformedStoryFileException msfe(error);
		throw msfe;
	}
}

istream& operator>>(istream& in, Path& p) {
	p.read(in);
	return in;
}

vector<Outcome> Path::getOutcomes() const {
	vector<Outcome> res = outcomes;
	return res;
}

const Outcome* Path::whichOutcome(const ItemList& have) const {
	for(unsigned int i = 0; i < outcomes.size(); ++i) {
		if(outcomes[i].isSatisfied(have))
			return &outcomes[i];
	}

	// no matching outcome found
	return NULL;
}

string Path::getText() const {
	return text;
}
