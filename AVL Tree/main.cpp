#include <bits/stdc++.h>
#include "Dictionary.h"

using namespace std;

int main()
{
    Dictionary D;
    while(1){
        printf("Enter Operation to Perform:\n1-Load Dictionary.\n2-Print Dictionary Size.\n3-Insert Word.\n4-Look-Up Word.\n5-Remove a word.\n6-Batch Look-Up.\n7-Batch Remove.\n");
        string word,choices;
        cin >> choices;
        int choice = choices[0]-'0';
        switch(choice){
        case 1:
            D.LoadDictionary(); break;
        case 2:
            D.printHeight(); break;
        case 3:
            printf("Enter the word : ");
            cin >> word;
            D.InsertWord(word);
            break;
        case 4:
            printf("Enter the word : ");
            cin >> word;
            D.LookUp(word);
            break;
        case 5:
            printf("Enter the word : ");
            cin >> word;
            D.RemoveWord(word);
            break;
        case 6:
            D.BatchLookUp();
            break;
        case 7:
            D.BatchDelete();
            break;
        default:
            cout << "Invalid choice!\n";
            break;

        }
    }
}
