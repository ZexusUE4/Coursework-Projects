#include "HashTable.h"
#include "Primes.h"
#include <iostream>
#include <fstream>
#include <time.h>

vector<int> readfile(string filename){
	ifstream input(filename);
	vector<int> vec;
	while (input.good()){
		int x;
		input >> x;
		vec.push_back(x);
	}
	return vec;
}
int main(){
	srand(time(NULL));
	rand();

	vector<int> vec;
	vec = readfile("keys_1000.txt");
	//for (int i = 0; i < 10000; i++){
		//int x = rand();
		//vec.push_back(x);
	//}
	HashTableN2 ht(vec);
	//bool found = 1;
	//for (int i = 0; i < 1000; i++){
	//	found &= ht.lookUp(vec[i]);
	//}
	cout << "Size : " << ht.size << endl;
	cout << "Retrials : " << ht.retrials << endl;
	while (1){
		int x;
		printf("Enter Integer to lookUp: ");
		cin >> x;
		if (ht.lookUp(x)){
			cout << "found\n";
		}
		else
			cout << "Not found\n";

	}
}