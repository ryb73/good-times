#ifndef COURSE_H_
#define COURSE_H_

#include <string>
#include "daysOfWeek.h"
#include "timeInterval.h"

using namespace std;

class Course
{
    public:
        Course( );

        string getCourseCode() const;
		DaysOfWeek getDaysMeeting() const;
		string getInstructor() const;
		string getSection() const;
		TimeInterval getTimeMeeting() const;

		void setCourseCode(string courseCode);
		void setDaysMeeting(DaysOfWeek& daysMeeting);
		void setInstructor(string instructor);
		void setSection(string number);
		void setTimeMeeting(TimeInterval& timeMeeting);

		bool isOverlap(const Course& c) const;
		bool isMatch(const Course& c) const;

    private:
        string courseCode;
        string section;
        DaysOfWeek daysMeeting;
        TimeInterval timeMeeting;
        string instructor;
};

bool operator&&(const Course& a, const Course& b);
bool operator==(const Course& a, const Course& b);
bool operator!=(const Course& a, const Course& b);

#endif /* COURSE_H_ */
