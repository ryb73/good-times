#include "timeInterval.h"

TimeInterval::TimeInterval() : startTime(), endTime() {
	// do nothing
}

TimeInterval::TimeInterval(const DigitalTime& a, const DigitalTime& b) : startTime(), endTime() {
	setInterval(a, b);
}

void TimeInterval::setInterval(const DigitalTime& a, const DigitalTime& b) {
	if(a <= b) {
		startTime = a;
		endTime = b;
	}
}

DigitalTime TimeInterval::getStart() const {
	return startTime;
}

DigitalTime TimeInterval::getEnd() const {
	return endTime;
}

void TimeInterval::output(ostream& outs) const {
	outs << startTime << " - " << endTime;
}

bool operator&&(const TimeInterval& a, const TimeInterval& b) {
	return a.getStart() <= b.getEnd() && b.getStart() <= a.getEnd();
}

ostream& operator<<(ostream& outs, const TimeInterval& d) {
	d.output(outs);
	return outs;
}
