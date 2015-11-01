#include "HashTableN2.h"
#include <algorithm>
#include <random>
#include <iostream>
#include <time.h>
#include "Primes.h"

HashTableN2::HashTableN2(vector<int> data){
	srand(time(NULL));
	if (data.size() > 20000){
		printf("Data size is too large !!\n");
		return;
	}

	if (data.size() == 0){
		mod = 0;
		return;
	}

	mod = data.size()*data.size();
	size = mod;

	int max = *max_element(data.begin(), data.end());
	prime = upperPrime(max);
	//cout << "table\n";
	table = new pair<int, bool>[mod];
	//cout << "here\n";
	while (1){
		randomize();
		bool hasCollisions = 0;
		for (int i = 0; i < data.size(); i++){
			int h = hash(data[i]);
			//cout << i << "  " << data[i] << " " << h << endl;
			if (table[h].second && table[h].first != data[i]){
				hasCollisions = 1;
				break;
			}
			else{
				table[h].first = data[i];
				table[h].second = 1;
			}
		}
		if (hasCollisions){
			retrials++;
			table = new pair<int, bool>[mod];
		}
		else
			break;
	}
	//for (int i = 0; i < table.size(); i++){
	//	if (table[i].second)
	//		cout << table[i].first << " ";
	//	else
	//		cout << "-1 ";
	//}
	//printf("Rebuild times = %d\n", retrials);
}

HashTableN2::~HashTableN2()
{
}

bool HashTableN2::lookUp(int key){

	if (mod == 0)
		return 0;
	return table[hash(key)].second && key == table[hash(key)].first;
}

long long HashTableN2::hash(int x){
	long long ret = (((a*x + b) % prime) % mod + mod) % mod;
	return ret;
}

void HashTableN2::randomize(){
	a = rand()  % (prime)+ 1;
	b = rand() % prime;
}
int HashTableN2::getPrime(int max){
	max += 1000;
	vector<bool> notPrime(max, 0);
	int ret;
	for (int i = 2; i < max; i++){
		if (!notPrime[i]){
			for (int j = i*i; j < max; j+=i){
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