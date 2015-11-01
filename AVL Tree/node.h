#include<iostream>

template<class C>
class node
{
public:
    C value;
    int height = 0;
    node* parent = NULL;
    node* left = NULL;
    node* right = NULL;

    node(C v);

    int leftHeight();
    int rightHeight();
    void traverse();
    int getBalance();
private:
    void traverse(node<C> *cur);
};
