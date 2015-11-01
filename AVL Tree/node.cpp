#include "node.h"
using namespace std;


template<class C>
node<C>::node(C v){
    value = v;
}

template<class C>
int node<C>::leftHeight(){
    if(left == NULL)
        return -1;
    else
        return left->height;
}

template<class C>
int node<C>::rightHeight(){
    if(right == NULL)
        return -1;
    else
        return right->height;
}

template<class C>
void node<C>::traverse(node<C> *cur){
    cout << cur->value << " " << cur->height << endl;
    if(cur->left){
        cout << "Left\n";
        traverse(cur->left);
    }
    if(cur->right){
        cout << "Right\n";
        traverse(cur->right);
    }
    cout << "UP\n";
}

template<class C>
void node<C>::traverse(){
    traverse(this);
}

template<class C>
int node<C>::getBalance(){
    return leftHeight() - rightHeight();
}
