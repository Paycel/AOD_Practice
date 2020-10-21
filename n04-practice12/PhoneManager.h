#ifndef MANAGER_H
#define MANAGER_H

#include<iostream>
#include<vector>
using namespace std;

class Person {
private:
	string phone, name, surname, patronymic, address;
public:
	inline Person(string phone, string name, string surname, string patronymic, string address) {
		this->phone = phone; 
		this->surname = surname;
		this->name = name;
		this->patronymic = patronymic;
		this->address = address;
	}

	inline friend ostream& operator<<(ostream& str, Person const& v) {
		str << v.phone + "," + v.name + "," + v.surname + "," + v.patronymic + "," + v.address + ",";
		return str;
	}

	inline string getPhone() {
		return phone;
	}
};

class PhoneManager {
private:
	string path;
public:
	PhoneManager(string path);
	void fillFile();
	void printFile(string path);
	void createFile(string phoneNumbers, string name);
	void deletePhone(string phone);
	void deletePhones(char firstNumber);
	void fill_txt_byData(vector<Person> data, string path);
	vector<Person> getData();
};

#endif // !MANAGER_H

