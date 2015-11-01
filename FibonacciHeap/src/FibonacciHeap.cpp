#include "FibonacciHeap.h"

template <class T>
FibonacciHeap<T>::FibonacciHeap()
{
	_size = 0;
	min = NULL;
}

template <class T>
FibonacciHeap<T>::~FibonacciHeap()
{
}

template <class T>
void FibonacciHeap<T>::insert(T val){
	Node<T>* toInsert = new Node(val);
	rootList.insert(toInsert);
	if (min->key == NULL || min->key > val)
		min = toInsert;
	_size++;
}

template <class T>
T FibonacciHeap<T>::getMin(){
	return min->key;
}

template <class T>
void FibonacciHeap<T>::consolidate(){
	unordered_map<int, Node<T>*> deg;

	for (int i = 0; i < rootList.size(); i++){
		Node<T>* x = rootList[i];
		int d = x->child.size();
		while (deg[d] != NULL){
			Node<T>* y = deg[d];
			if (x->key > y->key)
				swap(x, y);
			x->child.push_back(y);
			deg[d] = NULL;
			d++;
		}
		A[d] = x;
	}
	min = NULL;
	rootList.clear();

	for (unordered_map<int, Node<T>*>::iterator it = deg.begin(); it != deg.end(); it++){
		Node<T>* t = it->s;
		if (min == NULL || t->key < min->key)
			min = t;
		rootList.insert(t);
	}
}

template <class T>
T FibonacciHeap<T>::extractMin(){
	Node<T>* ret = min;
	if (ret == NULL){
		return NULL;
	}
	for (int i = 0; i < ret->child.size(); i++){
		rootList.insert(ret->child[i]);
	}

	rootList.remove(ret);
	if (rootList.size() == 0)
		min = NULL;
	else{
		min = *(rootList.begin());
		consolidate();
	}
	_size--;
}

