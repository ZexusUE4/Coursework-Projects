#include "AVLTree.cpp"

class Dictionary{
public:

    void LoadDictionary();
    void PrintSize();
    void InsertWord(string word);
    bool LookUp(string word);
    bool RemoveWord(string word);
    void BatchLookUp();
    void BatchDelete();
    void printHeight();

private:
    AVLTree<string> T;
};
