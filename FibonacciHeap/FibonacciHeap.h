#pragma once
#include "Node.h"
#include<unordered_map>
#include "DList.h"
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
	long long size();
	long long rootsNum();
	void consolidate();

private:
	DList<T> rootList;
	Node<T>* min;
	int _size;
};

