#include <vector>
using namespace std;

class HashTableN2
{
public:
	HashTableN2(vector<int> data);
	~HashTableN2();
	bool lookUp(int key);
	int retrials = 0,size;

private:
	pair<int,bool> *table;
	long long a, b, mod, prime;
	long long hash(int x);
	void randomize();
	int getPrime(int max);
};

