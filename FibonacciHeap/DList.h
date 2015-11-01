#pragma once
#include"Node.h"

template<class T>
class DList
{
public:
	DList();
	~DList();
	void attach(Node<T>* toAttach);
	void erase(Node<T>* toErase);
	Node<T>* getStart();
	Node<T>* getEnd();
	size_t size();
	void clear();

private:
	long long _size;
	Node<T>* start;
	Node<T>* end;
};

