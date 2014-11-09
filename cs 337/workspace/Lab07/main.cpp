#include <string>
#include <iostream>
#include <sstream>
#include <fstream>
#include <vector>
#include <algorithm>
#include "course.h"

using namespace std;

void performAdd(istream& in);
void printHelp();
void exportCourses(istream& in);
void importCourses(istream& in);
void performRemove(istream& in);
void performValidate();

Course* readSffCourse(istream& in);
void writeSffCourse(ostream& out, Course& c);

vector<Course> courseList;

int main() {
	cout << "Welcome to UWM Course Manager. Type 'help' for a list of commands." << endl << endl;

	bool running = true;
	while(running) {
		cout << "> ";
		string cmd;
		getline(cin, cmd);

		if(cmd.length() > 0) {
			stringstream lineReader(cmd);
			string cmdName;
			lineReader >> cmdName;
			if(cmdName == "add") {
				performAdd(lineReader);
			} else if(cmdName == "clear") {
				courseList.clear();
				cout << "Course list cleared." << endl;
			} else if(cmdName == "export") {
				exportCourses(lineReader);
			} else if(cmdName == "import") {
				importCourses(lineReader);
			} else if(cmdName == "remove") {
				performRemove(lineReader);
			} else if(cmdName == "validate") {
				performValidate();
			} else if(cmdName == "help") {
				printHelp();
			} else if(cmdName == "quit") {
				cout << "Goodbye." << endl;
				running = false;
			} else {
				cout << "Unrecognized command \"" << cmdName << "\". Type \"help\" for a list " <<
						"of commands." << endl;
			}
		}
	}
}

void performAdd(istream& in) {
	Course* c = readSffCourse(in);
	if(c != NULL) {
		if(find(courseList.begin(), courseList.end(), *c) == courseList.end()) {
			courseList.push_back(*c);
			cout << "Course successfully added." << endl;
		} else {
			cout << "Not adding course; duplicate exists." << endl;
		}

		delete c;
	}
}

void printHelp() {
	cout << endl;
	cout << "add <days> <start time> <end time> <course code> <section> <instructor>" << endl;
	cout << "clear" << endl;
	cout << "export <file name>" << endl;
	cout << "import <file name>" << endl;
	cout << "remove <course code> <section>" << endl;
	cout << "validate" << endl;
	cout << "help" << endl;
	cout << "quit" << endl;
	cout << endl;
}

void exportCourses(istream& in) {
	string filename;
	in >> filename;

	ofstream fileOut(filename.c_str());
	if(!fileOut.is_open()) {
		cout << "Unable to open file \"" << filename << "\"." << endl;
		return;
	}

	for(unsigned int i = 0; i < courseList.size(); ++i) {
		writeSffCourse(fileOut, courseList[i]);
		fileOut << endl;
	}

	fileOut.close();

	cout << "Courses successfully exported to \"" << filename << "\"." << endl;
}

void importCourses(istream& in) {
	string filename;
	in >> filename;

	ifstream fileIn(filename.c_str());
	if(!fileIn.is_open()) {
		cout << "Unable to open file \"" << filename << "\"." << endl;
		return;
	}

	int lineNum = 0;
	int coursesImported = 0;
	while(!fileIn.eof()) {
		++lineNum;

		string line;
		getline(fileIn, line);
		if(line.length() > 0) {
			stringstream ss(line);

			Course* c = readSffCourse(ss);
			if(c != NULL) {
				if(find(courseList.begin(), courseList.end(), *c) == courseList.end()) {
					courseList.push_back(*c);
					++coursesImported;
				} else {
					cout << "Duplicate course on line " << lineNum << "." << endl;
				}

				delete c;
			} else {
				cout << "Error reading course on line " << lineNum << "." << endl;
			}
		}
	}

	fileIn.close();

	if(coursesImported > 0)
		cout << coursesImported << " courses successfully imported from \"" << filename << "\"." << endl;
	else
		cout << "No courses imported." << endl;
}

void performRemove(istream& in) {
	string courseCode, section;
	in >> courseCode >> section;

	if (section.length() < 1) {
		cout << "Illegal number of arguments for command \"remove\". See 'help' for details." << endl;
		return;
	}

	bool found = false;
	for(unsigned int i = 0; !found && i < courseList.size(); ++i) {
		Course& c = courseList[i];
		if(c.getCourseCode() == courseCode && c.getSection() == section) {
			courseList.erase(courseList.begin() + i);
			found = true;
		}
	}

	if(found) {
		cout << "Removed course \"" << courseCode << " " << section << "\"." << endl;
	} else {
		cout << "Couldn't find course \"" << courseCode << " " << section << "\"." << endl;
	}
}

void performValidate() {
	int errors = 0;

	for(unsigned int i = 0; i < courseList.size(); ++i) {
		Course& a = courseList[i];
		for(unsigned int j = i + 1; j < courseList.size(); ++j) {
			Course& b = courseList[j];
			if(a && b) {
				++errors;

				cout << "Warning: Courses \"";
				cout << a.getCourseCode() << " " << a.getSection() << "\" and \"";
				cout << b.getCourseCode() << " " << b.getSection() << "\" overlap." << endl;
			}
		}
	}

	cout << errors << " errors found." << endl;
}

Course* readSffCourse(istream& in) {
	// SFF format: <days> <start time> <end time> <course code> <section> <instructor>
	Course* res = new Course();

	// Read days
	string daysStr;
	DaysOfWeek days;
	in >> daysStr;
	days.setDays(daysStr);
	res->setDaysMeeting(days);

	// Read start time
	DigitalTime startTime;
	if(!startTime.input(in)) {
		cout << "Illegal format for start time." << endl;
		delete res;
		return NULL;
	}

	// Read end time
	DigitalTime endTime;
	if(!endTime.input(in)) {
		cout << "Illegal format for end time." << endl;
		delete res;
		return NULL;
	}

	// Put start and end times together as interval
	TimeInterval interval(startTime, endTime);
	res->setTimeMeeting(interval);

	// Read course code
	string courseCode;
	in >> courseCode;
	if(courseCode.length() < 1) {
		cout << "Illegal number of arguments for SFF formatted course." << endl;
		delete res;
		return NULL;
	}

	res->setCourseCode(courseCode);

	// Read section
	string section;
	in >> section;
	if(section.length() < 1) {
		cout << "Illegal number of arguments for SFF formatted course." << endl;
		delete res;
		return NULL;
	}

	res->setSection(section);

	// Read instructor name
	string instructor;
	in >> instructor;
	if(instructor.length() < 1) {
		cout << "Illegal number of arguments for SFF formatted course." << endl;
		delete res;
		return NULL;
	}

	res->setInstructor(instructor);

	return res;
}

void writeSffCourse(ostream& out, Course& c) {
	// SFF format: <days> <start time> <end time> <course code> <section> <instructor>
	c.getDaysMeeting().output(out);
	out << " ";

	TimeInterval interval = c.getTimeMeeting();
	out << interval.getStart() << " " << interval.getEnd() << " ";

	out << c.getCourseCode() << " ";
	out << c.getSection() << " ";
	out << c.getInstructor();
}
