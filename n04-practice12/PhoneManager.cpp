#include<iostream>
#include<time.h>
#include<stdlib.h>
#include<fstream>
#include<string>
#include<vector>
#include"PhoneManager.h"

using namespace std;

PhoneManager::PhoneManager(string path) {
	this->path = path;
}

void PhoneManager::fillFile() {
	// fill file random data
	srand(time(NULL));
	const char* names[4] = { "Alexey", "Andrey", "Stas", "Vladimir" };
	const char* surnames[4] = { "Kudj", "Smirnov", "Sirok", "Surname" };
	const char* patronymics[4] = { "Alexeevich", "Ptr", "Andreevich" };
	const char* addresses[4] = { "Moscow", "LA", "NY", "Amsterdam" };
	ofstream fout(this->path);
	for (int i = 0; i < 10; i++) {
		string phone = "+"; 
		for (int j = 0; j < 11; j++) phone += to_string(rand() % 9);
		fout << Person(phone, names[rand() % 3], surnames[rand() % 3], patronymics[rand() % 3], addresses[rand() % 3]) << endl;
	}
	fout.close();
}

void PhoneManager::printFile(string path) {
	// read .txt / print data
	ifstream fin;
	fin.open(path);
	string iter("");
	while (getline(fin, iter, ',')) cout << iter + " ";
	fin.close();
	cout << endl;
}

void PhoneManager::deletePhone(string phone) {
	// delete phone by full-phone
	vector<Person> persons = getData();
	for (vector<Person>::iterator it = persons.begin(); it != persons.end(); it++) 
		if ((*it).getPhone()._Equal(phone)) {
			*it = persons.at(persons.size() - 1);
			persons.pop_back();
			break;
		}
	fill_txt_byData(persons, this->path);
}

void PhoneManager::deletePhones(char firstNumber) {
	// delete phones by 1st number
	vector<Person> persons = getData();
	for (vector<Person>::iterator it = persons.begin(); it != persons.end(); it++)
		if ((*it).getPhone().at(1) == firstNumber) {
			*it = persons.at(persons.size() - 1);
			persons.pop_back();
		}
	fill_txt_byData(persons, this->path);
}

void PhoneManager::createFile(string phoneNumbers, string name) {
	// create new .txt with users by first 3 numbers
	vector<Person> persons = getData();
	vector<Person> data;
	for (vector<Person>::iterator it = persons.begin(); it != persons.end(); it++)
		if ((*it).getPhone().substr(1, 3)._Equal(phoneNumbers)) data.push_back(*it);
	fill_txt_byData(data, name);
}

void PhoneManager::fill_txt_byData(vector<Person> data, string path) {
	// fill .txt by data
	ofstream fout(path);
	for (vector<Person>::iterator it = data.begin(); it != data.end(); it++) fout << *it << endl;
	fout.close();
}

vector<Person> PhoneManager::getData() {
	vector<Person> persons;
	ifstream fin(this->path);
	string iter(""), params[5];
	while (getline(fin, iter, ',')) {
		if (iter.find("\n") != string::npos) iter = iter.substr(1);
		params[0] = iter;
		for (int i = 0; i < 4; i++) {
			getline(fin, iter, ',');
			params[i + 1] = iter;
		}
		persons.push_back(Person(params[0], params[1], params[2], params[3], params[4]));
	}
	persons.pop_back();
	fin.close();
	return persons;
}