#pragma once
#include "HashTableN2.h"
class HashTable
{
public:
	HashTable(vector<int> data);
	~HashTable();
	bool lookUp(int key);
	int retrials, size;

private:
	vector<HashTableN2> table;
	long long a, b, mod, prime;
	long long hash(long long x);
	void randomize();
	int getPrime(int max);
};

