#include <string>
#include <iostream>
#include <sstream>
#include "dtime.h"

bool DigitalTime::isEqual(const DigitalTime& time1) const
{
	return time1.hour == this->hour && time1.minute == this->minute;
}

bool DigitalTime::isLess(const DigitalTime& time1) const
{
	if(this->hour < time1.hour)	// if our hour is less, the entire time is less
		return true;
	else if(this->hour == time1.hour) // if the hours are equal then test the minutes
		return this->minute < time1.minute;
	return false;
}

DigitalTime::DigitalTime(int the_hour, int the_minute)
{
	hour = the_hour;
	minute = the_minute;
}

DigitalTime::DigitalTime()
{
	hour = 0;
	minute = 0;
}

bool DigitalTime::input(istream& ins)
{
	string tok;
	ins >> tok;
	size_t colonPos = tok.find(':');
	if(colonPos > 0) {
		string hourStr = tok.substr(0, colonPos);
		string minuteStr = tok.substr(colonPos+1);

		int hl = hourStr.length();
		int ml = minuteStr.length();
		if(hl >= 1 && hl <= 2 && ml == 2) {
			stringstream ss;
			ss << hourStr << " " << minuteStr;

			int newHour, newMinute;
			ss >> newHour >> newMinute;

			if(newHour >= 0 && newHour <= 23 &&
			   newMinute >= 0 && newMinute <= 59) {
				set(newHour, newMinute);
				return true;
			}
		}
	}

	return false;
}

void DigitalTime::output(ostream& outs) const
{
	outs << hour << ':';
	if(minute < 10)
		outs << '0';
	outs << minute;
}

int DigitalTime::getHour() const
{
	return hour;
}

int DigitalTime::getMinute() const
{
	return minute;
}

void DigitalTime::set(int hour, int minute)
{
	if(hour >= 0 && hour <= 23 &&
	   minute >= 0 && minute <= 59) {
		this->hour = hour;
		this->minute = minute;
	}
}

bool operator<(const DigitalTime& a, const DigitalTime& b)
{
	return a.isLess(b);
}

bool operator>(const DigitalTime& a, const DigitalTime& b)
{
	return b <= a;
}

bool operator<=(const DigitalTime& a, const DigitalTime& b)
{
	return a.isEqual(b) || a.isLess(b);
}

bool operator>=(const DigitalTime& a, const DigitalTime& b)
{
	return b < a;
}

bool operator==(const DigitalTime& a, const DigitalTime& b)
{
	return a.isEqual(b);
}

bool operator!=(const DigitalTime& a, const DigitalTime& b)
{
	return !(a == b);
}

istream& operator>>(istream& ins, DigitalTime& d)
{
	d.input(ins);
	return ins;
}

ostream& operator<<(ostream& outs, const DigitalTime& d)
{
	d.output(outs);
	return outs;
}
