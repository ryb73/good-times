#ifndef DAYSOFWEEK_H_
#define DAYSOFWEEK_H_

#include <string>
#include <iostream>

using namespace std;

const string WEEKDAYS = "MTWRFS";

class DaysOfWeek
{
    public:
        DaysOfWeek( );
        DaysOfWeek(string daysStr);

        void setDays(string daysStr);
        string getDays() const;

        void input(istream& in);
        void output(ostream& out) const;

        bool isEqual(const DaysOfWeek& d) const;
        bool isOverlap(const DaysOfWeek& d) const;

    private:
        bool days[6];
};

bool operator&&(const DaysOfWeek& a, const DaysOfWeek& b);
bool operator==(const DaysOfWeek& a, const DaysOfWeek& b);
bool operator!=(const DaysOfWeek& a, const DaysOfWeek& b);

#endif /* DAYSOFWEEK_H_ */
