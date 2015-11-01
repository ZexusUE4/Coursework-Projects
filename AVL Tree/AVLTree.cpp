#include "AVLTree.h"
#include<stack>
using namespace std;

template<class C>
AVLTree<C>::AVLTree(){
}


template<class C>
long long AVLTree<C>::size(){
    return _size;
}

template<class C>
node<C>* AVLTree<C>::insert(C x,node<C> *cur){
    if(cur == NULL){
        success = true;
        return new node<C>(x);
    }
    if(x < cur->value){
        cur-> left = insert(x,cur->left);
    }
    else if(x > cur-> value){
        cur-> right = insert(x,cur->right);
    }
    else{
        return cur;
    }
    //Updates the node's height
    cur->height = max(1+cur->leftHeight(),1+cur->rightHeight());

    //returns the node after balancing it if required
    return balance(cur);
}

//Returns the root if the insertin was successful,
//else it returns NULL if the inserted node already exists
template<class C>
node<C>* AVLTree<C>::insert(C x){
    if(root == NULL){
        _size++;
        root = new node<C>(x);
        return root;
    }
    root = insert(x,root);
    if(success){
        _size++;
        success = false;
        return root;
    }
    else
        return NULL;
}

template<class C>
void AVLTree<C>::traverse(){
    if(root)
        traverse(root);
}

template<class C>
void AVLTree<C>::traverse(node<C> *cur){
//    cout << cur->value << " " << cur->height << endl;
//    if(cur->left){
//        cout << "Left\n";
//        traverse(cur->left);
//    }
//    if(cur->right){
//        cout << "Right\n";
//        traverse(cur->right);
//    }
//    cout << "UP\n";
    stack<node<C>*> st;
    st.push(cur);
    while(st.size()){
        node<C>* cur = st.top(); st.pop();
        while(cur != NULL){
            st.push(cur);
            cur = cur->left;
        }
        if(st.empty()) return;
        cur = st.top(); st.pop();
        if(cur == NULL)
            continue;
        cout << cur-> value << " ";
        st.push(cur->right);
    }
}

//performs required rotations on this node and returns it
template<class C>
node<C>* AVLTree<C>::balance(node<C>* cur){
    int _balance = cur->leftHeight() - cur->rightHeight();

    //Left left case
    if(_balance > 1 && cur->left->getBalance() >= 0){
        return rotRight(cur);
    }
    //Right right case
    if(_balance < -1 && cur->right->getBalance() <= 0){
        return rotLeft(cur);
    }
    //Left Right case
    if(_balance > 1 && cur->left->getBalance() < 0){
        cur->left = rotLeft(cur->left);
        return rotRight(cur);
    }
    //Right Left case
    if(_balance < -1 && cur->right->getBalance() > 0){
        cur->right = rotRight(cur->right);
        return rotLeft(cur);
    }
    return cur;
}

template<class C>
node<C>* AVLTree<C>::rotRight(node<C> *cur){

    node<C>* y = cur->left;
    node<C>* T3 = y->right;
    y->right = cur;
    cur-> left = T3;

    //Update the height of the rotated nodes
    cur -> height = max(cur->leftHeight(),cur->rightHeight())+1;
    y-> height = max(y->leftHeight(),y->rightHeight())+1;

    //returns the new root
    return y;

}
template<class C>
node<C>* AVLTree<C>::rotLeft(node<C> *cur)
{
    node<C>* y = cur->right;
    node<C>* T3 = y->left;
    y->left = cur;
    cur->right = T3;

    //Update the height of the rotated nodes
    cur -> height = max(cur->leftHeight(),cur->rightHeight())+1;
    y-> height = max(y->leftHeight(),y->rightHeight())+1;

    //returns the new root
    return y;
}

template<class C>
node<C>* AVLTree<C>::getSuccessor(node<C>* cur){
    if(cur-> right == NULL)
        return NULL;
    cur = cur->right;
    while(true){
        if(cur->left == NULL)
            break;
        cur = cur->left;
    }
    return cur;
}

template<class C>
node<C>* AVLTree<C>::getPredecessor(node<C>* cur){
    if(cur->left == NULL)
        return NULL;
    cur = cur->left;
    while(true){
        if(cur->right == NULL)
            break;
        cur = cur->right;
    }
    return cur;
}

template<class C>
node<C>* AVLTree<C>::find(C x){
    if(root == NULL)
        return NULL;
    return find(x,root);
}

//returns a pointer to the target node or NULL if not found
template<class C>
node<C>* AVLTree<C>::find(C x,node<C>* cur){
    if(cur == NULL)
        return cur;
    if(cur->value == x)
        return cur;
    if(x < cur->value){
        return find(x,cur->left);
    }
    if(x > cur->value)
        return find(x,cur->right);
    return NULL;
}

template<class C>
node<C>* AVLTree<C>::removeAndReplace(node<C>* cur){
    success = true;
    if(cur->height == 0)
        return NULL;
    if(cur->left == NULL)
        return cur->right;
    if(cur->right == NULL)
        return cur->left;

    node<C>* rep = getSuccessor(cur);

    cur->value = rep->value;

    //removes the Successor node after replacing the deleted node with it
    cur->right = remove(rep->value,cur->right);

    return balance(cur);
}

template<class C>
node<C>* AVLTree<C>::remove(C x){
    root = remove(x,root);
    if(success){
        _size--;
        success = false;
        return root;
    }
    else
        return NULL;
}

template<class C>
node<C>* AVLTree<C>::remove(C x,node<C>* cur){
    if(cur == NULL)
        return cur;
    if(x < cur->value)
        cur-> left = remove(x,cur->left);
    else if(x > cur->value)
        cur-> right = remove(x,cur->right);
    else
        cur = removeAndReplace(cur);
    if(cur)
        cur->height = max(cur->leftHeight(),cur->rightHeight()) + 1;
    return cur;
}

template<class C>
int AVLTree<C>::getHeight(){
    if(root)
        return root->height;
    //empty tree
    return -1;
}

