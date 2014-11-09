#include <string>
#include "course.h"
#include "daysOfWeek.h"

Course::Course() {
}

string Course::getCourseCode() const {
	return courseCode;
}

void Course::setCourseCode(string courseCode) {
    this->courseCode = courseCode;
}

DaysOfWeek Course::getDaysMeeting() const {
    return daysMeeting;
}

void Course::setDaysMeeting(DaysOfWeek& daysMeeting) {
    this->daysMeeting = daysMeeting;
}

string Course::getInstructor() const {
    return instructor;
}

void Course::setInstructor(string instructor) {
    this->instructor = instructor;
}

string Course::getSection() const {
    return section;
}

void Course::setSection(string number) {
    this->section = number;
}

TimeInterval Course::getTimeMeeting() const {
    return timeMeeting;
}

void Course::setTimeMeeting(TimeInterval& timeMeeting) {
    this->timeMeeting = timeMeeting;
}

bool Course::isOverlap(const Course& c) const {
	return instructor == c.instructor &&
			(daysMeeting && c.daysMeeting) &&
			(timeMeeting && c.timeMeeting);
}

bool Course::isMatch(const Course& c) const {
	return courseCode == c.courseCode && section == c.section;
}

bool operator&&(const Course& a, const Course& b) {
	return a.isOverlap(b);
}

bool operator==(const Course& a, const Course& b) {
	return a.isMatch(b);
}

bool operator!=(const Course& a, const Course& b) {
	return !(a == b);
}
