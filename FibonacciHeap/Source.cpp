#include<iostream>
#include<vector>
#include <fstream>
#include "BellmanFord.h"
#include "Dijkstra.h"
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

	/*BellmanFord BF(readFile("10000Test.txt"));
	cout << "Done Reading\n";
	while (1){
		int start, end;
		cin >> start >> end;
		cout << BF.getShortestPathYen(start, end) << " ";
		cout << BF.getShortestPath(start, end) << endl;
	}*/

	/*vector<vector<pair<int, double> > > graph = readFile("1000Test.txt");
	Dijkstra dj(graph);
	BellmanFord bf(graph);
	cout << "Done Reading\n";
	while (1){
		int start, end;
		cin >> start >> end;
		cout << dj.getShortestPathFib(start, end) << " ";
		cout << dj.getShortestPath(start, end) << endl;
	}*/





	//Fibonacci Test
	
	FibonacciHeap<int> F;
	while (1){
		printf("1- Insert\n2- Remove Min\n");
		int choice;
		cin >> choice;
		switch (choice){
		case 1:
			int y;
			cin >> y;
			F.insert(y);
			break;
		case 2:
			F.extractMin();
			break;
		default:
			break;
		}
		cout << "Minimum Element: " << F.getMin();
		cout << "\tRootsNum : " << F.rootsNum();
		cout << "\tSize : " << F.size() << endl;
	}
	
	while (1);
}