#include "node.cpp"

template<class C>
class AVLTree
{
public:
    node<C>* root = NULL;
    bool success = false;
    long long _size = 0;

    AVLTree();

    long long size();
    node<C>* insert(C x);
    void traverse();
    node<C>* find(C x);
    node<C>* remove(C x);
    node<C>* getPredecessor(node<C>* cur);
    node<C>* getSuccessor(node<C>* cur);
    int getHeight();

private:
    node<C>* insert(C x,node<C> *cur);
    node<C>* balance(node<C>* cur);
    node<C>* rotLeft(node<C> *root);
    node<C>* rotRight(node<C> *root);
    node<C>* find(C x,node<C>* cur);
    node<C>* remove(C x,node<C>* cur);
    node<C>* removeAndReplace(node<C>* cur);
    void traverse(node<C> *cur);
};
