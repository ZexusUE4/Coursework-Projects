#include "Node.h"

template<class T>
Node<T>::Node(T val)
{
	key = val;
	child = vector<Node<T>*>(0);
	next = NULL;
	prev = NULL;
}


template<class T>
Node<T>::~Node()
{

}

template<class T>
Node<T>* Node<T>::getNext(){
	return next;
}

template<class T>
Node<T>* Node<T>::getPrev(){
	return prev;
}

template<class T>
void Node<T>::setNext(Node<T>* n){
	next = n;
}

template<class T>
void Node<T>::setPrev(Node<T>* p){
	prev = p;
}
