#include <iostream>
#include<string>
#include "PhoneManager.h"
using namespace std;

int main()
{
	string path = "data.txt";
	string path2 = "new_data.txt";
	PhoneManager manager(path);
	manager.fillFile();
	manager.printFile(path);

	cout << "Print full-phone to delete: ";
	string phone; cin >> phone;
	manager.deletePhone(phone);

	cout << "Print 1-st phone number to delete all: ";
	char number; cin >> number;
	manager.deletePhones(number);

	cout << "Print 1-3 numbers of phone to create new file: ";
	cin >> phone;
	manager.createFile(phone, path2);
	
	manager.printFile(path);
	manager.printFile(path2);
	system("pause");
	return 0;
}