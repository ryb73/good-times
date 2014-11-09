#include <string>
#include "daysOfWeek.h"

DaysOfWeek::DaysOfWeek() {
	for(int i = 0; i < 6; ++i)
		days[i] = 0;
}



DaysOfWeek::DaysOfWeek(string daysStr) {
	setDays(daysStr);
}



void DaysOfWeek::setDays(string daysStr) {
	for(int i = 0; i < 6; ++i)
		days[i] = false;

	for(unsigned int i = 0; i < daysStr.length(); ++i) {
		int pos = WEEKDAYS.find(daysStr[i]);
		if(pos >= 0) {
			days[pos] = true;
		}
	}
}

string DaysOfWeek::getDays() const {
	string res;
	for(int i = 0; i < 6; ++i)
		if(days[i])
			res += WEEKDAYS[i];

	if(res.length() == 0)
		return "O";
	return res;
}



void DaysOfWeek::input(istream & in) {
	string inStr;
	in >> inStr;
	setDays(inStr);
}



void DaysOfWeek::output(ostream & out) const {
	out << getDays();
}



bool DaysOfWeek::isEqual(const DaysOfWeek & d) const {
	for(int i = 0; i < 6; ++i)
		if(days[i] != d.days[i])
			return false;

	return true;
}



bool DaysOfWeek::isOverlap(const DaysOfWeek & d) const {
	for(int i = 0; i < 6; ++i)
		if(days[i] && d.days[i])
			return true;

	return false;
}



bool operator &&(const DaysOfWeek & a, const DaysOfWeek & b) {
	return a.isOverlap(b);
}



bool operator ==(const DaysOfWeek & a, const DaysOfWeek & b) {
	return a.isEqual(b);
}



bool operator !=(const DaysOfWeek & a, const DaysOfWeek & b) {
	return !(a == b);
}

