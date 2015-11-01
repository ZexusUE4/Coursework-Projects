#pragma once
#include "Node.h"
#include<unordered_map>
#include<unordered_set>

template <class T>
class FibonacciHeap
{
public:
	FibonacciHeap();
	~FibonacciHeap();
	void insert(T key);
	T extractMin();
	T getMin();
	int size();
	void consolidate();

private:
	unordered_set<Node<T>*> rootList;
	Node<T>* min;
	int _size;
};

