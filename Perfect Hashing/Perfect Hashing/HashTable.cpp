#include "HashTable.h"
#include "Primes.h"
#include <iostream>
#include <algorithm>

HashTable::HashTable(vector<int> data)
{
	mod = data.size();
	int max = *max_element(data.begin(), data.end());
	prime = upperPrime(max);

	randomize(); 
	size = 0;
	vector<vector<int> > bins(data.size());
	for (int i = 0; i < data.size(); i++){
		bins[hash(data[i])].push_back(data[i]);
	}

	for (int i = 0; i < mod; i++){
		//cout << bins[i].size() << " ";
		HashTableN2 HT = HashTableN2(bins[i]);
		size += bins[i].size()*bins[i].size();
		table.push_back(HT);
		retrials += HT.retrials;
	}
	//cout << retrials << endl;
}


HashTable::~HashTable()
{
}

bool HashTable::lookUp(int key){
	return table[hash(key)].lookUp(key);
}

long long HashTable::hash(long long x){
	return (((a*x + b ) % prime) % mod + mod ) % mod;
}
void HashTable::randomize(){
	a = rand() % (prime) + 1;
	b = rand() % (prime);
}
int HashTable::getPrime(int max){
	max += 1000;
	vector<bool> notPrime(max, 0);
	int ret;
	for (int i = 2; i < max; i++){
		if (!notPrime[i]){
			for (int j = i*i; j < max; j += i){
				notPrime[j] = 1;
			}
		}
	}

	for (int i = 2; i < max; i++){
		if (!notPrime[i])
			ret = i;
	}
	return ret;
}