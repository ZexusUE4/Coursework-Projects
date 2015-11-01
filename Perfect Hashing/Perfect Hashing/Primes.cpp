#include <vector>
#include "Primes.h"
#include <iostream>
#include <algorithm>

using namespace std;
bool isPrime(long long x){
	if (x == 0 || x == 1)
		return 0;
	for (long long i = 2LL; i <= sqrt(x); i++){
		if (x % i == 0)
			return 0;
	}
	return 1;
}

long long upperPrime(long long x){
	x = abs(x);
	for (long long i = x+1LL;; i++){
		if (isPrime(i)){
			return i;
		}
	}
}