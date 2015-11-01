#pragma once
#include <vector>
#include <cstddef>
using namespace std;

template <class T>
class Node
{
public:
	Node(T val);
	~Node();
	T key;
	vector<Node<T>*> child;
	Node<T>* getNext();
	Node<T>* getPrev();
	void setNext(Node<T>* n);
	void setPrev(Node<T>* p);

private:
	Node<T>* next;
	Node<T>* prev;
};

