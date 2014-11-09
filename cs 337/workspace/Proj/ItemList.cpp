#include <algorithm>
#include <string>
#include "ItemList.h"

ItemList::ItemList() : items() { }

ItemList::ItemList(const string& itemStr) : items() {
	string currentStr = "";
	for(unsigned int i = 0; i < itemStr.size(); ++i) {
		if(itemStr[i] == ',') {
			items.push_back(currentStr);
			currentStr = "";
		} else if(currentStr.size() != 0 || itemStr[i] != ' ') { // ignore leading spaces.
			currentStr += itemStr[i];
		}
	}

	if(currentStr.size() != 0)
		items.push_back(currentStr);
}

bool ItemList::hasItem(const string& item) const {
	return find(items.begin(), items.end(), item) != items.end();
}



void ItemList::removeItem(const string& item) {
	vector<string>::iterator pos = find(items.begin(), items.end(), item);
	if(pos != items.end())
		items.erase(pos);
}

void ItemList::addItem(const string& item) {
	if(!hasItem(item))
		items.push_back(item);
}

/**
 * Returns true if il is a subset of this ItemList, otherwise false.
 */
bool ItemList::isSubset(const ItemList& il) const {
	for(unsigned int i = 0; i < il.items.size(); ++i) {
		if(!hasItem(il.items[i]))
			return false;
	}

	return true;
}

bool ItemList::overlapping(const ItemList& il) const {
	for(unsigned int i = 0; i < il.items.size(); ++i) {
		if(hasItem(il.items[i]))
			return true;
	}

	return false;
}

ItemList ItemList::operator+(const ItemList& il) const {
	ItemList sum;
	for(unsigned int i = 0; i < items.size(); ++i) {
		sum.addItem(items[i]);
	}

	for(unsigned int i = 0; i < il.items.size(); ++i) {
		sum.addItem(il.items[i]);
	}

	return sum;
}

ItemList ItemList::operator-(const ItemList& il) const {
	ItemList difference;
	for(unsigned int i = 0; i < items.size(); ++i) {
		difference.addItem(items[i]);
	}

	for(unsigned int i = 0; i < il.items.size(); ++i) {
		difference.removeItem(il.items[i]);
	}

	return difference;
}

ItemList ItemList::intersection(const ItemList & il) const {
	ItemList intersect;
	for(unsigned int i = 0; i < il.items.size(); ++i) {
		if(hasItem(il.items[i]))
			intersect.addItem(il.items[i]);
	}

	return intersect;
}

vector<string> ItemList::getList() const {
	vector<string> res = items;
	return res;
}

string ItemList::toString() const {
	string res = "";
	for(unsigned int i = 0; i < items.size(); ++i) {
		res += items[i];
		if(i != items.size() - 1)
			res += ",";
	}

	return res;
}
