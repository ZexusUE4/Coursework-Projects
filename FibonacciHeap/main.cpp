#include<iostream>
#include<vector>
#include <fstream>
#include "FibonacciHeap.cpp"

using namespace std;

vector<vector<pair<int, double> > > readFile(string filename){
	ifstream file(filename);
	int V, E;
	vector<vector<pair<int, double> > > G;
	file >> V >> E;
	G = vector < vector<pair<int, double> > >(V);
	double a, b, c;
	for (int i = 0; i < E; i++){
		file >> a >> b >> c;
		G[a].push_back(make_pair(b,c));
	}
	return G;
}

int main(){

//	BellmanFord BF(readFile("10000Test.txt"));
//	cout << "Done Reading\n";
//	while (1){
//		int start, end;
//		cin >> start >> end;
//		cout << BF.getShortestPathYen(start, end) << " ";
//		cout << BF.getShortestPath(start, end) << endl;
//	}

	FibonacciHeap<int> Fib;
	while (1){
		printf("1-Insert\n2-removeMin\n");
		int choice;
		cin >> choice;
		switch (choice){
		case 1:
			int y;
			cin >> y;
			Fib.insert(y);
			break;
		case 2:
			Fib.extractMin();
			break;
		default:
			break;
		}
		cout << "Minimum Element: " << Fib.getMin() << "\tSize : " << Fib.size() << endl;
	}
	while (1);
}
