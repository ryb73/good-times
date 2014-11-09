#ifndef ITEMLIST_H_
#define ITEMLIST_H_

#include <string>
#include <vector>

using namespace std;

class ItemList {
	public:
		ItemList();
		ItemList(const string& itemStr);

		bool hasItem(const string& item) const;
		bool isSubset(const ItemList& il) const;
		bool overlapping(const ItemList& il) const;
		ItemList intersection(const ItemList& il) const;

		ItemList operator+(const ItemList& il) const;
		ItemList operator-(const ItemList& il) const;

		void removeItem(const string& item);
		void addItem(const string& item);

		vector<string> getList() const;
		string toString() const;
	private:
		vector<string> items;
};


#endif /* ITEMLIST_H_ */
