#ifndef STATE_H_
#define STATE_H_

#include <string>
#include <vector>
#include <istream>
#include "Path.h"

using namespace std;

class State {
	public:
		State(int id);
		State(int id, const string& story, const vector<Path>& paths);
		void read(istream& in);

		int getId() const;
		string getStory() const;
		vector<Path> getPaths() const;

		State& operator=(const State& s);

	private:
		int id;
		string story;
		vector<Path> paths;
};

istream& operator>>(istream& in, State& s);

#endif /* STATE_H_ */
