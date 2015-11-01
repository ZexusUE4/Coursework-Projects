#include "FibonacciHeap.h"
#include "DList.cpp"
#include "Node.cpp"
#include <iostream>

template <class T>
FibonacciHeap<T>::FibonacciHeap()
{
	_size = 0;
	min = rootList.getEnd();
}

template <class T>
FibonacciHeap<T>::~FibonacciHeap()
{

}

template <class T>
void FibonacciHeap<T>::insert(T val){
	Node<T>* toInsert = new Node<T>(val);
	rootList.attach(toInsert);
	if (min == rootList.getEnd() || min->key > val){
		min = toInsert;
	}
	_size++;
}

template <class T>
T FibonacciHeap<T>::getMin(){
	if (min == rootList.getEnd())
		return NULL;
	return min->key;
}

template <class T>
void FibonacciHeap<T>::consolidate(){
	unordered_map<int, Node<T>*> deg;

	for (Node<T>* i = rootList.getStart()->getNext(); i != rootList.getEnd();i = i->getNext()){
		Node<T>* x = i;
		int d = x->child.size();
		while (deg[d] != NULL){
			Node<T>* y = deg[d];
			if (x->key > y->key)
				swap(x, y);
			x->child.push_back(y);
			deg[d] = NULL;
			d++;
		}
		deg[d] = x;
	}

	min = rootList.getEnd();
	rootList.clear();

	for (typename unordered_map<int, Node<T>*>::iterator it = deg.begin(); it != deg.end(); it++){
		Node<T>* t = it->second;
		if (t == NULL)
			continue;
		rootList.attach(t);
		if (min == rootList.getEnd() || t->key < min->key){
			min = t;
		}
	}
}

template <class T>
T FibonacciHeap<T>::extractMin(){
	Node<T>* ret = min;
	if (min != rootList.getEnd()){
		for (int i = 0; i < ret->child.size(); i++){
			rootList.attach(ret->child[i]);
		}

		rootList.erase(ret);
		if (rootList.size() == 0)
			min = rootList.getEnd();
		else{
			min = rootList.getStart()->getNext();
			consolidate();
		}
		_size--;
		return ret->key;
	}
}

template <class T>
long long FibonacciHeap<T>::size(){
	return _size;
}

template <class T>
long long FibonacciHeap<T>::rootsNum(){
	return rootList.size();
}
