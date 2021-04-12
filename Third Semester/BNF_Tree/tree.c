#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <time.h>

typedef struct {
    char* terminalSymbol;
}noChildNode;

typedef struct {
    void* firstChild;
    int childAmountOfFirstChild;
}oneChildNode;

typedef struct {
    void* firstChild;
    int childAmountOfFirstChild;
    void* secondChild;
    int childAmountOfSecondChild;
}twoChildrenNode;

typedef struct {
    void* firstChild;
    int childAmountOfFirstChild;
    void* secondChild;
    int childAmountOfSecondChild;
    void* thirdChild;
    int childAmountOfThirdChild;
}threeChildrenNode;

// function prototypes
void* recursiveChildCreator(void* head,char* type);
void recursiveTreePrinter(void* head,int numberOfChild);

// terminal symbol arrays and number of symbols in these arrays.
char** opSymbols;
char** pre_opSymbols;
char** rel_opSymbols;
char** set_opSymbols;
char** varSymbols;
int numberOfOpSymbols;
int numberOfPre_opSymbols;
int numberOfRel_opSymbols;
int numberOfSet_opSymbols;
int numberOfVarSymbols;


int main(void)
{
    //reading input files.
    FILE* op = fopen("op","r");
    FILE* pre_op = fopen("pre_op","r");
    FILE* rel_op = fopen("rel_op","r");
    FILE* set_op = fopen("set_op","r");
    FILE* var = fopen("var","r");
    char* opInput = malloc(10* sizeof(char));
    char* pre_opInput = malloc(10* sizeof(char));
    char* rel_opInput = malloc(10* sizeof(char));
    char* set_opInput = malloc(10* sizeof(char));
    char* varInput = malloc(10* sizeof(char));
    opSymbols = malloc(sizeof(char*));
    numberOfOpSymbols = 0;
    pre_opSymbols = malloc(sizeof(char*));
    numberOfPre_opSymbols = 0;
    rel_opSymbols = malloc(sizeof(char*));
    numberOfRel_opSymbols = 0;
    set_opSymbols = malloc(sizeof(char*));
    numberOfSet_opSymbols = 0;
    varSymbols = malloc(sizeof(char*));
    numberOfVarSymbols = 0;
    int i=0;
    while (!feof(op))
    {
        fscanf(op,"%[^\n]\n",opInput);
        opSymbols = realloc(opSymbols,(i+1)*sizeof(char*));
        opSymbols[i] = malloc(10*sizeof(char));
        strcpy(opSymbols[i],opInput);
        numberOfOpSymbols++;
        i++;
    }
    free(opInput);
    fclose(op);
    i=0;
    while (!feof(pre_op))
    {
        fscanf(pre_op,"%[^\n]\n",pre_opInput);
        pre_opSymbols = realloc(pre_opSymbols,(i+1)* sizeof(char*));
        pre_opSymbols[i] = malloc(10*sizeof(char));
        strcpy(pre_opSymbols[i],pre_opInput);
        numberOfPre_opSymbols++;
        i++;
    }
    free(pre_opInput);
    fclose(pre_op);
    i=0;
    while (!feof(rel_op))
    {
        fscanf(rel_op,"%[^\n]\n",rel_opInput);
        rel_opSymbols = realloc(rel_opSymbols,(i+1)* sizeof(char*));
        rel_opSymbols[i] = malloc(10*sizeof(char));
        strcpy(rel_opSymbols[i],rel_opInput);
        numberOfRel_opSymbols++;
        i++;
    }
    free(rel_opInput);
    fclose(rel_op);
    i=0;
    while (!feof(set_op))
    {
        fscanf(set_op,"%[^\n]\n",set_opInput);
        set_opSymbols = realloc(set_opSymbols,(i+1)* sizeof(char*));
        set_opSymbols[i] = malloc(10*sizeof(char));
        strcpy(set_opSymbols[i],set_opInput);
        numberOfSet_opSymbols++;
        i++;
    }
    free(set_opInput);
    fclose(set_op);
    i=0;
    while (!feof(var))
    {
        fscanf(var,"%[^\n]\n",varInput);
        varSymbols = realloc(varSymbols,(i+1)* sizeof(char*));
        varSymbols[i] = malloc(10*sizeof(char));
        strcpy(varSymbols[i],varInput);
        numberOfVarSymbols++;
        i++;
    }
    free(varInput);
    fclose(var);

    srand(time(NULL));

    //recursively creating tree
    threeChildrenNode* root = recursiveChildCreator(root,"cond");

    //printing tree
    printf("if(");
    recursiveTreePrinter(root,3);
    printf(") { }\n");
}

void* recursiveChildCreator(void* head,char* type) // creates tree
{
    if (!strcmp(type,"cond"))
    {
        head = malloc(sizeof(threeChildrenNode));
        threeChildrenNode* currentNode = head;
        int i = rand() % 2;
        if (i == 0) // <cond> = <cond><set-op><cond>
        {
            currentNode->firstChild = recursiveChildCreator(currentNode->firstChild,"cond");
            currentNode->childAmountOfFirstChild = 3;
            currentNode->secondChild = recursiveChildCreator(currentNode->secondChild,"set_op");
            currentNode->childAmountOfSecondChild = 0;
            currentNode->thirdChild = recursiveChildCreator(currentNode->thirdChild,"cond");
            currentNode->childAmountOfThirdChild = 3;
        }
        else // <cond> = <expr><rel-op><expr>
        {
            i = rand() % 3;
            if (i == 0) // <expr> = <expr><op><expr>
            {
                currentNode->firstChild = recursiveChildCreator(currentNode->firstChild,"expr3");
                currentNode->childAmountOfFirstChild = 3;
            }
            else if (i == 1) // <expr> = <pre-op><expr>
            {
                currentNode->firstChild = recursiveChildCreator(currentNode->firstChild,"expr2");
                currentNode->childAmountOfFirstChild = 2;
            }
            else if (i == 2) // <expr> = <var>
            {
                currentNode->firstChild = recursiveChildCreator(currentNode->firstChild,"expr1");
                currentNode->childAmountOfFirstChild = 1;
            }
            currentNode->secondChild = recursiveChildCreator(currentNode->secondChild,"rel_op");
            currentNode->childAmountOfSecondChild = 0;
            i = rand() % 3;
            if (i == 0) // <expr> = <expr><op><expr>
            {
                currentNode->thirdChild = recursiveChildCreator(currentNode->thirdChild,"expr3");
                currentNode->childAmountOfThirdChild = 3;
            }
            else if (i == 1) // <expr> = <pre-op><expr>
            {
                currentNode->thirdChild = recursiveChildCreator(currentNode->thirdChild,"expr2");
                currentNode->childAmountOfThirdChild = 2;
            }
            else if (i == 2) // <expr> = <var>
            {
                currentNode->thirdChild = recursiveChildCreator(currentNode->thirdChild,"expr1");
                currentNode->childAmountOfThirdChild = 1;
            }
        }
    }
    else if (!strcmp(type,"expr3"))
    {
        head = malloc(sizeof(threeChildrenNode));
        threeChildrenNode* currentNode = head;
        int i = rand() % 3;
        if (i == 0) // <expr> = <expr><op><expr>
        {
            currentNode->firstChild = recursiveChildCreator(currentNode->firstChild,"expr3");
            currentNode->childAmountOfFirstChild = 3;
        }
        else if (i == 1) // <expr> = <pre-op><expr>
        {
            currentNode->firstChild = recursiveChildCreator(currentNode->firstChild,"expr2");
            currentNode->childAmountOfFirstChild = 2;
        }
        else if (i == 2) // <expr> = <var>
        {
            currentNode->firstChild = recursiveChildCreator(currentNode->firstChild,"expr1");
            currentNode->childAmountOfFirstChild = 1;
        }
        currentNode->secondChild = recursiveChildCreator(currentNode->secondChild,"op");
        currentNode->childAmountOfSecondChild = 0;
        i = rand() % 3;
        if (i == 0) // <expr> = <expr><op><expr>
        {
            currentNode->thirdChild = recursiveChildCreator(currentNode->thirdChild,"expr3");
            currentNode->childAmountOfThirdChild = 3;
        }
        else if (i == 1) // <expr> = <pre-op><expr>
        {
            currentNode->thirdChild = recursiveChildCreator(currentNode->thirdChild,"expr2");
            currentNode->childAmountOfThirdChild = 2;
        }
        else if (i == 2) // <expr> = <var>
        {
            currentNode->thirdChild = recursiveChildCreator(currentNode->thirdChild,"expr1");
            currentNode->childAmountOfThirdChild = 1;
        }
    }
    else if (!strcmp(type,"expr2"))
    {
        head = malloc(sizeof(twoChildrenNode));
        twoChildrenNode* currentNode = head;
        currentNode->firstChild = recursiveChildCreator(currentNode->firstChild,"pre_op");
        currentNode->childAmountOfFirstChild = 0;
        int i = rand() % 3;
        if (i == 0) // <expr> = <expr><op><expr>
        {
            currentNode->secondChild = recursiveChildCreator(currentNode->secondChild,"expr3");
            currentNode->childAmountOfSecondChild = 3;
        }
        else if (i == 1) // <expr> = <pre-op><expr>
        {
            currentNode->secondChild = recursiveChildCreator(currentNode->secondChild,"expr2");
            currentNode->childAmountOfSecondChild = 2;
        }
        else if (i == 2) // <expr> = <var>
        {
            currentNode->secondChild = recursiveChildCreator(currentNode->secondChild,"expr1");
            currentNode->childAmountOfSecondChild = 1;
        }
    }
    else if (!strcmp(type,"expr1"))  // <expr> = <var>
    {
        head = malloc(sizeof(oneChildNode));
        oneChildNode* currentNode = head;
        currentNode->firstChild = recursiveChildCreator(currentNode->firstChild,"var");
        currentNode->childAmountOfFirstChild = 0;
    }
    else if (!strcmp(type,"op"))
    {
        head = malloc(sizeof(noChildNode));
        noChildNode* currentNode = head;
        currentNode->terminalSymbol = malloc(10* sizeof(char));
        int i = rand() % numberOfOpSymbols;
        strcpy(currentNode->terminalSymbol,opSymbols[i]);
    }
    else if (!strcmp(type,"pre_op"))
    {
        head = malloc(sizeof(noChildNode));
        noChildNode* currentNode = head;
        currentNode->terminalSymbol = malloc(10* sizeof(char));
        int i = rand() % numberOfPre_opSymbols;
        strcpy(currentNode->terminalSymbol,pre_opSymbols[i]);
    }
    else if (!strcmp(type,"rel_op"))
    {
        head = malloc(sizeof(noChildNode));
        noChildNode* currentNode = head;
        currentNode->terminalSymbol = malloc(10* sizeof(char));
        int i = rand() % numberOfRel_opSymbols;
        strcpy(currentNode->terminalSymbol,rel_opSymbols[i]);
    }
    else if (!strcmp(type,"set_op"))
    {
        head = malloc(sizeof(noChildNode));
        noChildNode* currentNode = head;
        currentNode->terminalSymbol = malloc(10* sizeof(char));
        int i = rand() % numberOfSet_opSymbols;
        strcpy(currentNode->terminalSymbol,set_opSymbols[i]);
    }
    else if (!strcmp(type,"var"))
    {
        head = malloc(sizeof(noChildNode));
        noChildNode* currentNode = head;
        currentNode->terminalSymbol = malloc(10* sizeof(char));
        int i = rand() % numberOfVarSymbols;
        strcpy(currentNode->terminalSymbol,varSymbols[i]);
    }
    return head;
}

void recursiveTreePrinter(void* head,int numberOfChild) // prints the tree
{
    if (numberOfChild == 3) // <cond><set-op><cond> or <expr><rel-op><expr> or <expr><op><expr>
    {
        threeChildrenNode* currentNode = head;
        printf("(");
        recursiveTreePrinter(currentNode->firstChild,currentNode->childAmountOfFirstChild);
        recursiveTreePrinter(currentNode->secondChild,currentNode->childAmountOfSecondChild);
        recursiveTreePrinter(currentNode->thirdChild,currentNode->childAmountOfThirdChild);
        printf(")");
    }
    else if (numberOfChild == 2) // <pre-op><exp>
    {
        twoChildrenNode* currentNode = head;
        recursiveTreePrinter(currentNode->firstChild,currentNode->childAmountOfFirstChild);
        printf("(");
        recursiveTreePrinter(currentNode->secondChild,currentNode->childAmountOfSecondChild);
        printf(")");
    }
    else if (numberOfChild == 1) // <var>
    {
        oneChildNode* currentNode = head;
        recursiveTreePrinter(currentNode->firstChild,currentNode->childAmountOfFirstChild);
    }
    else if (numberOfChild == 0) // terminal symbols
    {
        noChildNode* currentNode = head;
        printf("%s",currentNode->terminalSymbol);
    }
}

// İbrahim Burak Tanrıkulu