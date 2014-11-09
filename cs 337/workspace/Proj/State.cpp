#include <string>
#include <vector>
#include <iostream>
#include <sstream>
#include "State.h"
#include "MalformedStoryFileException.h"

State::State(int id) {
	this->id = id;
}

State::State(int id, const string& story, const vector<Path>& paths) {
	this->id = id;
	this->story = story;
	this->paths = paths;
}

istream& operator>>(istream& in, State& s) {
	s.read(in);
	return in;
}

void State::read(istream & in) {
	string line;
	bool stateDone = false;
	while(!stateDone && !in.eof()) {
		getline(in, line);
		if(line[line.size()-1] == '\r') // strip carraige returns
			line.resize(line.size() - 1);

		if(line == "#story#") {
			bool storyDone = false;
			while(!storyDone && !in.eof()) {
				getline(in, line);
				if(line[line.size()-1] == '\r') // strip carraige returns
							line.resize(line.size() - 1);

				if(line == "#endstory#")
					storyDone = true;
				else
					story += line + "\n";
			}

			if(!storyDone) {
				string error;
				stringstream ss;
				ss << "expected #endstory# in state " << id;
				getline(ss, error);

				MalformedStoryFileException msfe(error);
				throw msfe;
			}
		} else if(line == "#path#") {
			Path path;
			in >> path;
			paths.push_back(path);
		} else if(line.find("#id#") == 0) {
			in.seekg(-((int)line.size()) - 2, ios_base::cur);
			stateDone = true;
		} else if(line == "") {
			// blank line -- skip
		} else {
			string error("error parsing state, encountered: '");
			error += line;
			error += "'";
			MalformedStoryFileException msfe(error);
			throw msfe;
		}
	}

	if(paths.size() > 3) {
		string error("each state can have a maximum of 3 paths");
		MalformedStoryFileException msfe(error);
		throw msfe;
	}
}

vector<Path> State::getPaths() const {
	vector<Path> res = paths;
	return res;
}

int State::getId() const {
	return id;
}

string State::getStory() const {
	return story;
}

State& State::operator=(const State& s) {
	id = s.id;
	story = s.story;
	paths = s.paths;
	return *this;
}
