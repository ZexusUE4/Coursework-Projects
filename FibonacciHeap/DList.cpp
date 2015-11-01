#include "DList.h"
#include "Node.h"
#include <cstddef>

template <class T>
DList<T>::DList()
{
	start = new Node<T>(NULL);
	end = new Node<T>(NULL);

	_size = 0;
	start->setNext(end);
	end->setPrev(start);
}

template <class T>
DList<T>::~DList()
{
}

template <class T>
void DList<T>::attach(Node<T>* toAttach){
	Node<T>* last = end->getPrev();
	last->setNext(toAttach);
	toAttach->setNext(end);
	end->setPrev(toAttach);
	toAttach->setPrev(last);
	_size++;

}

template <class T>
void DList<T>::erase(Node<T>* toErase){
	Node<T>* prev = toErase->getPrev();
	prev->setNext(toErase->getNext());
	toErase->getNext()->setPrev(prev);
	_size--;

}

template <class T>
void DList<T>::clear(){
	start->setNext(end);
	end->setPrev(start);
	_size = 0;
}

template <class T>
Node<T>* DList<T>::getStart(){
	return start;
}

template <class T>
Node<T>* DList<T>::getEnd(){
	return end;
}

template<class T>
size_t DList<T>::size(){
	return (size_t)_size;
}
