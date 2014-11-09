#include <stdlib.h>
#include <string>
#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <map>
#include <set>

#include "StoryFileReadException.h"
#include "MalformedStoryFileException.h"
#include "IllegalItemListException.h"
#include "IllegalStateException.h"
#include "TemplateFileReadException.h"
#include "NoOutcomesFoundException.h"
#include "ItemList.h"
#include "State.h"

#define DEFAULT_STATE 1

using namespace std;

void loadStoryFile(const char* filename);
void getParams(map<string,string>& params);
void doOutput(const string& story, const string& have, string pathText[3], string pathP[3], string pathMinus[3], string pathOut[3]);
string stringReplace(const string& src, const string& before, const string& after);

vector<State> states;
ItemList allItems;
ItemList initialHave;

int main(int argc, char** argv) {
	try {
		cout << "Content-type: text/html" << endl << endl;

		loadStoryFile("story.def");

		map<string, string> params;
		getParams(params);

		params["have"] = stringReplace(params["have"], "%2C", ",");
		params["plus"] = stringReplace(params["plus"], "%2C", ",");
		params["minus"] = stringReplace(params["minus"], "%2C", ",");

		if(params["out"] == "none") {
			NoOutcomesFoundException nofe;
			throw nofe;
		}

		ItemList haveList(params["have"]);
		ItemList plusList(params["plus"]);
		ItemList minusList(params["minus"]);
		if(!allItems.isSubset(haveList)) {
			string error("have list contains invalid items");
			IllegalItemListException iile(error);
			throw iile;
		} else if(!allItems.isSubset(plusList)) {
			string error("plus list contains invalid items");
			IllegalItemListException iile(error);
			throw iile;
		} else if(!allItems.isSubset(minusList)) {
			string error("minus list contains invalid items");
			IllegalItemListException iile(error);
			throw iile;
		}

		int currentStateId;
		if(params["out"] == "") {
			currentStateId = DEFAULT_STATE;
			haveList = haveList + initialHave;
		} else {
			stringstream ss;
			ss << params["out"];
			ss >> currentStateId;
			if(currentStateId < 1) {
				string error("state must a positive integer");
				IllegalStateException iile(error);
				throw iile;
			}
		}

		// Remove unnecessary plus/minus items
		plusList = plusList - haveList;
		minusList = minusList.intersection(haveList);

		haveList = haveList + plusList;
		haveList = haveList - minusList;

		State* currentState = NULL;
		for(unsigned int i = 0; i < states.size(); ++i) {
			if(states[i].getId() == currentStateId)
				currentState = &states[i];
		}

		if(currentState == NULL) {
			stringstream ss;
			ss << "state " << currentStateId << " does not exist.";

			string error;
			getline(ss, error);
			IllegalStateException iile(error);
			throw iile;
		}

		string story;
		vector<string> plusStrings = plusList.getList();
		for(unsigned int i = 0; i < plusStrings.size(); ++i) {
			story += "You have gained the " + plusStrings[i] + ".<br/>";
		}

		vector<string> minusStrings = minusList.getList();
		for(unsigned int i = 0; i < minusStrings.size(); ++i) {
			story += "You have lost the " + minusStrings[i] + ".<br/>";
		}

		story += currentState->getStory();

		string pathText[3];
		string pathPlus[3];
		string pathMinus[3];
		string pathOut[3];
		for(int i = 0; i < 3; ++i) {
			pathText[i] = "";
			pathPlus[i] = "";
			pathMinus[i] = "";

			stringstream ss;
			ss << currentStateId;
			ss >> pathOut[i];
		}

		vector<Path> paths = currentState->getPaths();
		for(unsigned int i = 0; i < paths.size(); ++i) {
			pathText[i] = paths[i].getText();

			const Outcome* outcome = paths[i].whichOutcome(haveList);
			if(outcome != NULL) {
				pathPlus[i] = outcome->getPlus();
				pathMinus[i] = outcome->getMinus();

				stringstream ss;
				ss << outcome->getTargetState();
				ss >> pathOut[i];
			} else {
				pathOut[i] = "none";
			}
		}

		doOutput(story, haveList.toString(), pathText, pathPlus, pathMinus, pathOut);
	} catch (runtime_error& error) {
		cout << "<html><body><p>";
		cout << error.what();
		cout << "</p><p>Go <a href='javascript:history.back()'>back</a> and try a different path.</p>";
		cout << "</body></html>" << endl;
	}

	return 0;
}

void loadStoryFile(const char* filename) {
	ifstream fileIn(filename);
	if(!fileIn.is_open()) {
		string error("unable to open story.def");
		StoryFileReadException sfre(error);
		throw sfre;
	}

	string line;
	bool itemsDone = false;
	while(!itemsDone && !fileIn.eof()) {
		fileIn >> line;
		int eqPos;
		eqPos = line.find('=');
		if(eqPos >= 0 && eqPos < line.size() - 1) {
			string itemName = line.substr(0, eqPos);
			bool hasItem = line[eqPos + 1] == '1';

			allItems.addItem(itemName);
			if(hasItem)
				initialHave.addItem(itemName);
		} else if(line == "#id#") {
			itemsDone = true;
		} else {
			string error("expected item_name=[0|1]");
			MalformedStoryFileException msfe(error);
			throw msfe;
		}
	}

	if(fileIn.eof()) {
		string error("must contain at least one state");
		MalformedStoryFileException msfe(error);
		throw msfe;
	}

	do {
		if(line != "#id#") {
			string error("expected #id#");
			MalformedStoryFileException msfe(error);
			throw msfe;
		}

		int id;
		fileIn >> id;

		State state(id);
		fileIn >> state;
		states.push_back(state);

		fileIn >> line;
	} while(!fileIn.eof());

	set<int> validStateIds;
	for(unsigned int i = 0; i < states.size(); ++i) {
		int id = states[i].getId();
		if(validStateIds.find(id) != validStateIds.end()) {
			stringstream ss;
			ss << "duplicate state id: " << id;

			string error;
			getline(ss, error);
			IllegalStateException ise(error);
			throw ise;
		}

		validStateIds.insert(id);
	}
}

void getParams(map<string,string>& params) {
    string line;
    //getline(cin, line); // for POST
    line = getenv("QUERY_STRING"); // for GET
    for(unsigned int i = 0;i < line.size() && i >= 0;){
        if(line[i] == '&')
            i++;

        int startField = i;
        i = line.find("=", i);
        int endField = i - 1;
        int startValue = i + 1;
        i = line.find("&", i);
        int endValue = i - 1;
        params[line.substr(startField, endField - startField + 1)] = line.substr(startValue, endValue - startValue + 1);
    }
}

void doOutput(const string& story, const string& have, string pathText[3], string pathPlus[3], string pathMinus[3], string pathOut[3]) {
	ifstream tempIn("template.html");
	if(!tempIn.is_open()) {
		string error("unable to open template.html");
		TemplateFileReadException tfre(error);
		throw tfre;
	}

	string line;
	while(!tempIn.eof()) {
		getline(tempIn, line);
		line = stringReplace(line, "#story#", story);
		line = stringReplace(line, "#have#", have);

		line = stringReplace(line, "#path1#", pathText[0]);
		line = stringReplace(line, "#plus1#", pathPlus[0]);
		line = stringReplace(line, "#minus1#", pathMinus[0]);
		line = stringReplace(line, "#out1#", pathOut[0]);

		line = stringReplace(line, "#path2#", pathText[1]);
		line = stringReplace(line, "#plus2#", pathPlus[1]);
		line = stringReplace(line, "#minus2#", pathMinus[1]);
		line = stringReplace(line, "#out2#", pathOut[1]);

		line = stringReplace(line, "#path3#", pathText[2]);
		line = stringReplace(line, "#plus3#", pathPlus[2]);
		line = stringReplace(line, "#minus3#", pathMinus[2]);
		line = stringReplace(line, "#out3#", pathOut[2]);

		cout << line << endl;
	}

	tempIn.close();
}

string stringReplace(const string& src, const string& before, const string& after) {
	string res = src;
	size_t pos;
	while((pos = res.find(before)) != string::npos) {
		res.replace(pos, before.size(), after);
	}

	return res;
}
