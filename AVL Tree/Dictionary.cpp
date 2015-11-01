#include "Dictionary.h"
#include<fstream>

void Dictionary::LoadDictionary(){
    ifstream inp ("dictionary.txt");
    string line;
    if(inp.is_open()){
        while(getline(inp,line)){
            if(T.insert(line) == NULL){
                cout << "Error : " << line << " Already exists!\n";
            }
        }
    }
    else{
        cout << "Error : Reading dictionary.txt!\n";
    }
    printHeight();
    return;
}

void Dictionary::PrintSize(){
    cout << "Tree Size : " << T.size() << endl;
}

void Dictionary::InsertWord(string word){
    if(T.insert(word) == NULL){
        cout << "Error : " << word << " Already exists!\n";
    }
    cout << "Size: " << T.size();
    cout << "\t" << "Height: " << T.getHeight() << endl;
}

bool Dictionary::LookUp(string word){
    if(T.find(word) == NULL){
        cout << word << " NO\n";
        return false;
    }
    else{
        cout << "YES\n";
        return true;
    }
}
bool Dictionary::RemoveWord(string word){
    bool ret = 0;
    if(T.remove(word) == NULL){
        cout << "Error: " << word << " Not found to be deleted!\n";
        ret = 0;
    }
    else{
        cout << word << " Successfully removed!\n";
        ret = 1;
    }
    cout << "Size: " << T.size() << "\tHeight: " << T.getHeight() << endl;
    return ret;
}
void Dictionary::BatchLookUp(){
    ifstream inp ("queries.txt");
    string line;
    int found_count = 0;
    if(inp.is_open()){
        while(getline(inp,line)){
            if(T.find(line) != NULL){
                found_count++;
            }
            else{
                cout << line << "Not found!\n";
            }
        }
    }
    else{
        cout << "Error : Reading queries.txt!\n";
    }
    cout << "Found Words count = " << found_count << endl;
}
void Dictionary::BatchDelete(){
    ifstream inp ("deletions.txt");
    string line;
    int found_count = 0;
    if(inp.is_open()){
        while(getline(inp,line)){
            found_count += RemoveWord(line);
        }
    }
    else{
        cout << "Error : Reading deletions.txt!\n";
    }

    cout << "Removed Words count = " << found_count << endl;
}
void Dictionary::printHeight(){
    cout << "Size: " << T.size() << "\tHeight: " << T.getHeight() << endl;
}
