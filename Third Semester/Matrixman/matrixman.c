#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

// data structures

struct Vector
{
    char* vectorName;
    int* vectorValues;
    int length;
};

struct Matrix
{
    char* matrixName;
    int** matrixValues;
    int x;
    int y;
};

// prototypes of functions

int toInt(char* string);
bool intCheck(char* myString);
void printVec(struct Vector theVector);
void printMat(struct Matrix theMatrix);
void freeVec(struct Vector *theVector);
void freeMat(struct Matrix *theMatrix);
struct Vector veczeros(char* filename, int length);
struct Matrix matzeros(char* filename, int row, int col);
struct Vector vecread(char* filename, char* fileLocation);
struct Matrix matread(char* filename, char* fileLocation);
struct Matrix vecstack(struct Vector vector1, struct Vector vector2, char* direction, char* filename);
void matstack(struct Matrix *matrix1, struct Matrix *matrix2, char where);
void mvstack(struct Matrix *matrix,struct Vector *vector,char where);
void pad(struct Matrix *matrix, int x, int y, char* mode);
void padval(struct Matrix *matrix, int x, int y, int value);
struct Vector vecslice(struct Vector vector, int start, int stop, char* name);
struct Vector matslicecol(struct Matrix matrix, int column, int start, int stop, char* name);
struct Vector matslicerow(struct Matrix matrix, int row, int start, int stop, char* name);
struct Matrix matslice(struct Matrix matrix, int y1, int y2, int x1, int x2, char* name);
void add(struct Matrix *matrix1, struct Matrix *matrix2);
void multiply(struct Matrix *matrix1, struct Matrix *matrix2);
void subtract(struct Matrix *matrix1, struct Matrix *matrix2);


int main(int argc, char* argv[])
{
    //functions implemented and not implemented 5

    struct Vector * vectors = malloc(NULL); // struct array for vectors
    int numberOfVectors = 0;
    struct Matrix * matrices = malloc(NULL); // struct array for matrices
    int numberOfMatrices = 0;

    stdout = fopen(argv[3],"w+");
    char* filelocation = malloc(strlen(argv[1]) * sizeof(char));
    strcpy(filelocation,argv[1]);
    FILE* inputFile = fopen(argv[2],"r");
    char* inputLine = malloc(50 * sizeof(char));
    while(!feof(inputFile))
    {
        fscanf(inputFile,"%[^\n]\n",inputLine);
        char* command = strtok(inputLine," \n");
        if(command == NULL) { printf("error\n"); continue; }
        if (!strcmp(command,"veczeros"))
        {
            command = strtok(NULL," \r\n");
            if(command == NULL) { printf("error\n"); continue; }
            int sameVectorName = 0;
            for(int i=0;i<numberOfVectors;i++) // searching for vector that has same name
            {
                if(!strcmp((*(vectors+i)).vectorName,command)) { sameVectorName = 1; }
            }
            char* vectorName = malloc(strlen(command)* sizeof(char)); // taking vector's name
            strcpy(vectorName,command);
            command = strtok(NULL," \r\n");
            if(command == NULL || sameVectorName == 1 || toInt(command) < 1 || intCheck(command) == false) { printf("error\n"); free(vectorName); continue; }
            int vectorLength = toInt(command); // taking vector's length
            if(strtok(NULL," \n") != NULL) { printf("error\n"); free(vectorName); continue; }
            numberOfVectors++;
            vectors = realloc(vectors, numberOfVectors * sizeof(struct Vector)); // adding to vectors array
            *(vectors+numberOfVectors-1) = veczeros(vectorName,vectorLength);
            free(vectorName);
        }
        else if (!strcmp(command,"matzeros"))
        {
            command = strtok(NULL," \r\n");
            if (command == NULL) { printf("error\n"); continue;}
            int sameMatrixName = 0;
            for(int i=0;i<numberOfMatrices;i++)
            {
                if(!strcmp((*(matrices+i)).matrixName,command)) { sameMatrixName = 1;} // searching for matrix that has same name
            }
            char* matrixName = malloc(strlen(command) * sizeof(char)); // taking matrix's name
            strcpy(matrixName,command);
            command = strtok(NULL," \r\n");
            if(command == NULL || sameMatrixName == 1 || toInt(command) < 1 || intCheck(command) == false) { printf("error\n"); free(matrixName); continue;}
            int rowMatrix = toInt(command);
            command = strtok(NULL," \r\n");
            if(command == NULL || toInt(command) < 1 || intCheck(command) == false) { printf("error\n"); free(matrixName); continue; }
            int columnMatrix = toInt(command);
            if(strtok(NULL," \n") != NULL) { printf("error\n"); free(matrixName); continue; }
            numberOfMatrices++;
            matrices = realloc(matrices,numberOfMatrices* sizeof(struct Matrix)); // adding to matrices array
            *(matrices+numberOfMatrices-1) = matzeros(matrixName,rowMatrix,columnMatrix);
            free(matrixName);
        }
        else if (!strcmp(command,"vecread"))
        {
            command = strtok(NULL," \r\n");
            if(command == NULL) { printf("error\n"); continue; }

            int sameVectorName = 0;
            for(int i=0;i<numberOfVectors;i++) // searching for vector that has same name
            {
                if(!strcmp((*(vectors+i)).vectorName,command)) { sameVectorName = 1; }
            }
            char* vectorName = malloc(strlen(command)* sizeof(char)); // taking vector's name
            strcpy(vectorName,command);
            if(strtok(NULL," \n") != NULL || sameVectorName == 1) { printf("error\n"); free(vectorName); continue; }
            char* locationOfFile = malloc(strlen(filelocation)* sizeof(char));
            strcpy(locationOfFile,filelocation);
            strcat(locationOfFile,vectorName);
            FILE* theFile = fopen(locationOfFile,"r"); // checking for file exists or not
            if(theFile == NULL) { printf("error\n"); free(vectorName); free(locationOfFile); continue;}
            fclose(theFile);
            numberOfVectors++;
            vectors = realloc(vectors, numberOfVectors * sizeof(struct Vector)); // adding to vectors array
            *(vectors+numberOfVectors-1) = vecread(vectorName,locationOfFile);
            free(vectorName);
            free(locationOfFile);
        }
        else if (!strcmp(command,"matread"))
        {
            command = strtok(NULL," \r\n");
            if(command == NULL) { printf("error\n"); continue; }
            int sameMatrixName = 0;
            for(int i=0;i<numberOfMatrices;i++) // searching for matrix that has same name
            {
                if(!strcmp((*(matrices+i)).matrixName,command)) { sameMatrixName = 1; }
            }
            char* matrixName = malloc(strlen(command)* sizeof(char)); // taking matrix's name
            strcpy(matrixName,command);
            if(strtok(NULL," \n") != NULL || sameMatrixName == 1) { printf("error\n"); free(matrixName); continue; }
            char* locationOfFile = malloc(strlen(filelocation)* sizeof(char));
            strcpy(locationOfFile,filelocation);
            strcat(locationOfFile,matrixName);
            FILE* theFile = fopen(locationOfFile,"r"); // checking for file exists or not
            if(theFile == NULL) { printf("error\n"); free(matrixName); free(locationOfFile); continue;}
            fclose(theFile);-
                    numberOfMatrices++;
            matrices = realloc(matrices, numberOfMatrices * sizeof(struct Matrix)); // adding to matrices array
            *(matrices+numberOfMatrices-1) = matread(matrixName,locationOfFile);
            free(matrixName);
            free(locationOfFile);
        }
        else if (!strcmp(command,"vecstack"))
        {
            command = strtok(NULL," \r\n");
            if(command == NULL) { printf("error\n"); continue; }
            int numberOfVector1 = -1;
            for(int i=0;i<numberOfVectors;i++) // searching location of matrix
            {
                if(!strcmp((*(vectors+i)).vectorName,command)) {numberOfVector1 = i;}
            }
            command = strtok(NULL," \r\n");
            if(command == NULL || numberOfVector1 == -1) { printf("error\n"); continue; }
            int numberOfVector2 = -1;
            for(int i=0;i<numberOfVectors;i++) // searching location of vector
            {
                if(!strcmp((*(vectors+i)).vectorName,command)) {numberOfVector2 = i;}
            }
            command = strtok(NULL," \r\n");
            if(command == NULL || numberOfVector2 == -1) { printf("error\n"); continue; } //
            char* direction = malloc(strlen(command)* sizeof(char));
            strcpy(direction,command);
            command = strtok(NULL," \r\n");
            if(command == NULL || (strcmp(direction,"row") && strcmp(direction,"column"))) { printf("error\n"); free(direction); continue; }
            char* matrixName = malloc(strlen(command) * sizeof(char)); // taking matrix's name
            strcpy(matrixName,command);
            int sameMatrixName = 0;
            for(int i=0;i<numberOfMatrices;i++) // searching for matrix that has same name
            {
                if(!strcmp((*(matrices+i)).matrixName,command)) { sameMatrixName = 1; }
            }
            if(strtok(NULL," \n") != NULL || sameMatrixName == 1 || (*(vectors+numberOfVector1)).length != (*(vectors+numberOfVector2)).length) { printf("error\n"); free(direction); free(matrixName); continue; }
            numberOfMatrices++;
            matrices = realloc(matrices,numberOfMatrices* sizeof(struct Matrix)); // adding matrices array
            *(matrices+numberOfMatrices-1) = vecstack(*(vectors+numberOfVector1) , *(vectors+numberOfVector2) , direction , matrixName );
            free(matrixName);
            free(direction);
        }
        else if (!strcmp(command,"matstack"))
        {
            command = strtok(NULL," \r\n");
            if(command == NULL) { printf("error\n"); continue; }
            int numberOfMatrix1 = -1;
            for(int i=0;i<numberOfMatrices;i++) // searching location of matrix1
            {
                if(!strcmp((*(matrices+i)).matrixName,command)) {numberOfMatrix1 = i;}
            }
            command = strtok(NULL," \r\n");
            if(command == NULL || numberOfMatrix1 == -1) { printf("error\n"); continue; }
            int numberOfMatrix2 = -1;
            for(int i=0;i<numberOfMatrices;i++) // searching for location of matrix2
            {
                if(!strcmp((*(matrices+i)).matrixName,command)) {numberOfMatrix2 = i;}
            }
            command = strtok(NULL," \r\n");
            if(command == NULL || numberOfMatrix2 == -1 || command[1] != '\0' || (command[0] != 'r' && command[0] != 'd')) { printf("error\n"); continue; }

            if (strtok(NULL," \n") != NULL
                || (command[0] != 'd' && (*(matrices+numberOfMatrix1)).x != (*(matrices+numberOfMatrix2)).x )
                || (command[0] != 'r' && (*(matrices+numberOfMatrix1)).y != (*(matrices+numberOfMatrix2)).y))
            { printf("error\n"); continue; }
            matstack(matrices+numberOfMatrix1,matrices+numberOfMatrix2,command[0]); // concatenating matrices
        }
        else if (!strcmp(command,"mvstack"))
        {
            command = strtok(NULL," \r\n");
            if(command == NULL) { printf("error\n"); continue; }
            int numberOfMatrix = -1;
            for(int i=0;i<numberOfMatrices;i++) // searching location of matrix
            {
                if(!strcmp((*(matrices+i)).matrixName,command)) {numberOfMatrix = i;}
            }
            command = strtok(NULL," \r\n");
            if(command == NULL || numberOfMatrix == -1) { printf("error\n"); continue; }
            int numberOfVector = -1;
            for(int i=0;i<numberOfVectors;i++) // searching location of vector
            {
                if(!strcmp((*(vectors+i)).vectorName,command)) {numberOfVector = i;}
            }
            command = strtok(NULL," \r\n");
            if(command == NULL || numberOfVector == -1 || command[1] != '\0' || (command[0] != 'r' && command[0] != 'd')) { printf("error\n"); continue; }
            if (strtok(NULL," \n") != NULL || (command[0] == 'r' && (*(matrices+numberOfMatrix)).x != (*(vectors+numberOfVector)).length )
                || (command[0] == 'd' && (*(matrices+numberOfMatrix)).y != (*(vectors+numberOfVector)).length))
            { printf("error\n"); continue; }
            mvstack(matrices+numberOfMatrix,vectors+numberOfVector,command[0]); // concatenating matrix and vector
        }
        else if (!strcmp(command,"pad"))
        {
            command = strtok(NULL," \r\n");
            if(command == NULL) { printf("error\n"); continue; }
            int numberOfMatrix = -1;
            for(int i=0;i<numberOfMatrices;i++) // searching location of matrix
            {
                if(!strcmp((*(matrices+i)).matrixName,command)) {numberOfMatrix = i;}
            }
            command = strtok(NULL," \r\n");
            if(command == NULL || numberOfMatrix == -1 || toInt(command) < 0 || intCheck(command) == false) { printf("error\n"); continue; }
            int row = toInt(command);
            command = strtok(NULL," \r\n");
            if(command == NULL || toInt(command) < 0 || intCheck(command) == false) { printf("error\n"); continue; }
            int column = toInt(command);
            command = strtok(NULL," \r\n");
            if(command == NULL || (strcmp(command,"maximum") && strcmp(command,"minimum"))) { printf("error\n"); continue; }
            char* mode = malloc(strlen(command)*sizeof(char));
            strcpy(mode,command);
            if (strtok(NULL," \n") != NULL) { printf("error\n"); continue; }
            pad(matrices+numberOfMatrix,row,column,mode); // padding
            free(mode);
        }
        else if (!strcmp(command,"padval"))
        {
            command = strtok(NULL," \r\n");
            if(command == NULL) { printf("error\n"); continue; }
            int numberOfMatrix = -1;
            for(int i=0;i<numberOfMatrices;i++) // searching location of matrix
            {
                if(!strcmp((*(matrices+i)).matrixName,command)) {numberOfMatrix = i;}
            }
            command = strtok(NULL," \r\n");
            if(command == NULL || numberOfMatrix == -1 || toInt(command) < 0 || intCheck(command) == false) { printf("error\n"); continue; }
            int row = toInt(command);
            command = strtok(NULL," \r\n");
            if(command == NULL || toInt(command) < 0 || intCheck(command) == false) { printf("error\n"); continue; }
            int column = toInt(command);
            command = strtok(NULL," \r\n");
            if(command == NULL || intCheck(command) == false) { printf("error\n"); continue; }
            int value = toInt(command);
            if (strtok(NULL," \n") != NULL) { printf("error\n"); continue; }
            padval(matrices+numberOfMatrix,row,column,value); // padding
        }
        else if (!strcmp(command,"vecslice"))
        {
            command = strtok(NULL," \r\n");
            if(command == NULL) { printf("error\n"); continue; }
            int numberOfVector = -1;
            for(int i=0;i<numberOfVectors;i++) // searching location of vector
            {
                if(!strcmp((*(vectors+i)).vectorName,command)) {numberOfVector = i;}
            }
            command = strtok(NULL," \r\n");
            if(command == NULL || numberOfVector == -1 || toInt(command) <= -1 || intCheck == NULL || toInt(command) >= (*(vectors+numberOfVector)).length ) { printf("error\n"); continue; }
            int start = toInt(command);
            command = strtok(NULL," \r\n");
            if(command == NULL || intCheck(command) == false || toInt(command) <= 0 || toInt(command) > (*(vectors+numberOfVector)).length ) { printf("error\n"); continue; }
            int stop = toInt(command);
            command = strtok(NULL," \r\n");
            if(command == NULL) { printf("error\n"); continue; }
            char* vectorName = malloc(strlen(command)); // taking vector's name
            strcpy(vectorName,command);
            int sameVectorName = 0;
            for(int i=0;i<numberOfVectors;i++) // searching for vector that has same name
            {
                if(!strcmp((*(vectors+i)).vectorName,command)) { sameVectorName = 1; }
            }
            if(strtok(NULL," \n") != NULL || sameVectorName == 1) { printf("error\n"); continue;}
            numberOfVectors++;
            vectors = realloc(vectors, numberOfVectors * sizeof(struct Vector)); // adding vectors array
            *(vectors+numberOfVectors-1) = vecslice(*(vectors+numberOfVector) , start , stop , vectorName);
            free(vectorName);
        }
        else if (!strcmp(command,"matslicecol"))
        {
            command = strtok(NULL," \r\n");
            if(command == NULL) { printf("error\n"); continue;}
            int numberOfMatrix = -1;
            for(int i=0;i<numberOfMatrices;i++) // searching location of matrix
            {
                if(!strcmp((*(matrices+i)).matrixName,command)) {numberOfMatrix = i;}
            }
            command = strtok(NULL," \r\n");
            if(command == NULL || numberOfMatrix == -1 || toInt(command) <= 0 || intCheck(command) == false || toInt(command) > (*(matrices+numberOfMatrix)).y ) { printf("error\n"); continue; }
            int indexColumn = toInt(command);
            command = strtok(NULL," \r\n");
            if(command == NULL || toInt(command) < 0 || intCheck(command) == false || toInt(command) >= (*(matrices+numberOfMatrix)).x ) { printf("error\n"); continue;}
            int start = toInt(command);
            command = strtok(NULL," \r\n");
            if(command == NULL || toInt(command) < 1 || intCheck(command) == false || toInt(command) > (*(matrices+numberOfMatrix)).x ) { printf("error\n"); continue; }
            int stop = toInt(command);
            command = strtok(NULL," \r\n");
            char* vectorName = malloc(strlen(command)* sizeof(char)); // taking vector's name
            strcpy(vectorName,command);
            int sameVectorName = 0;
            for(int i=0;i<numberOfVectors;i++) // searching for vector that has same name
            {
                if(!strcmp((*(vectors+i)).vectorName,command)) { sameVectorName = 1; }
            }
            if(strtok(NULL," \n") != NULL || sameVectorName == 1) { printf("error\n"); continue; }
            numberOfVectors++;
            vectors = realloc(vectors, numberOfVectors * sizeof(struct Vector)); // adding vectors array
            *(vectors+numberOfVectors-1) = matslicecol(*(matrices+numberOfMatrix) , indexColumn, start, stop, vectorName);
            free(vectorName);
        }
        else if (!strcmp(command,"matslicerow"))
        {
            command = strtok(NULL," \r\n");
            if(command == NULL) { printf("error\n"); continue;}
            int numberOfMatrix = -1;
            for(int i=0;i<numberOfMatrices;i++) // searching location of matrix
            {
                if(!strcmp((*(matrices+i)).matrixName,command)) {numberOfMatrix = i;}
            }
            command = strtok(NULL," \r\n");
            if(command == NULL || numberOfMatrix == -1 || intCheck(command) == false || toInt(command) <= 0 || toInt(command) > (*(matrices+numberOfMatrix)).x ) { printf("error\n"); continue; }
            int indexRow = toInt(command);
            command = strtok(NULL," \r\n");
            if(command == NULL || toInt(command) < 0 || intCheck(command) == false || toInt(command) >= (*(matrices+numberOfMatrix)).y ) { printf("error\n"); continue;}
            int start = toInt(command);
            command = strtok(NULL," \r\n");
            if(command == NULL || toInt(command) < 1 || intCheck(command) == false || toInt(command) > (*(matrices+numberOfMatrix)).y ) { printf("error\n"); continue; }
            int stop = toInt(command);
            command = strtok(NULL," \r\n");
            char* vectorName = malloc(strlen(command)* sizeof(char)); // taking vector's name
            strcpy(vectorName,command);
            int sameVectorName = 0;
            for(int i=0;i<numberOfVectors;i++) // searching for vector that has same name
            {
                if(!strcmp((*(vectors+i)).vectorName,command)) { sameVectorName = 1; }
            }
            if(strtok(NULL," \n") != NULL || sameVectorName == 1) { printf("error\n"); continue; }
            numberOfVectors++;
            vectors = realloc(vectors, numberOfVectors * sizeof(struct Vector)); // adding vectors array
            *(vectors+numberOfVectors-1) = matslicerow(*(matrices+numberOfMatrix) , indexRow, start, stop, vectorName);
            free(vectorName);
        }
        else if (!strcmp(command,"matslice"))
        {
            command = strtok(NULL," \r\n");
            if(command == NULL) { printf("error\n"); continue;}
            int numberOfMatrix = -1;
            for(int i=0;i<numberOfMatrices;i++) // searching location of matrix
            {
                if(!strcmp((*(matrices+i)).matrixName,command)) {numberOfMatrix = i;}
            }
            command = strtok(NULL," \r\n");
            int maxColumn = (*(matrices+numberOfMatrix)).y;
            if(command == NULL || toInt(command) < 0 || intCheck(command) == false || toInt(command) >= maxColumn ) { printf("error\n"); continue; }
            int y1 = toInt(command);
            command = strtok(NULL," \r\n");
            if(command == NULL || toInt(command) <= 0 || intCheck(command) == false || toInt(command) > maxColumn) { printf("error\n"); continue; }
            int y2 = toInt(command);
            command = strtok(NULL," \r\n");
            int maxRow = (*(matrices+numberOfMatrix)).x;
            if(command == NULL || toInt(command) < 0 || intCheck(command) == false || toInt(command) >= maxRow) { printf("error\n"); continue; }
            int x1 = toInt(command);
            command = strtok(NULL," \r\n");
            if(command == NULL || toInt(command) <= 0 || intCheck(command) == false || toInt(command) > maxRow) { printf("error\n"); continue; }
            int x2 = toInt(command);
            command = strtok(NULL," \r\n");
            if(command == NULL) { printf("error\n"); continue; }
            char* matrixName = malloc(strlen(command)* sizeof(char)); // taking matrix's name
            strcpy(matrixName,command);
            int sameMatrixName = 0;
            for(int i=0;i<numberOfMatrices;i++) // searching for matrix that has same name
            {
                if(!strcmp((*(matrices+i)).matrixName,command)) { sameMatrixName = 1; }
            }
            if(strtok(NULL," \n") != NULL || sameMatrixName == 1) { printf("error\n"); free(matrixName); continue; }
            numberOfMatrices++;
            matrices = realloc(matrices, numberOfMatrices * sizeof(struct Matrix)); // adding matrices array
            *(matrices+numberOfMatrices-1) = matslice(*(matrices+numberOfMatrix) ,y1-1,y2-1,x1,x2,matrixName);
            free(matrixName);
        }
        else if(!strcmp(command,"add"))
        {
            command = strtok(NULL," \r\n");
            if(command == NULL) { printf("error\n"); continue; }
            int numberOfMatrix1 = -1;
            for(int i=0;i<numberOfMatrices;i++) // searching location of matrix1
            {
                if(!strcmp((*(matrices+i)).matrixName,command)) {numberOfMatrix1 = i;}
            }
            command = strtok(NULL," \r\n");
            if(command == NULL || numberOfMatrix1 == -1) { printf("error\n"); continue; }
            int numberOfMatrix2 = -1;
            for(int i=0;i<numberOfMatrices;i++) // searching location of matrix2
            {
                if(!strcmp((*(matrices+i)).matrixName,command)) {numberOfMatrix2 = i;}
            }
            if(strtok(NULL," \n") != NULL || numberOfMatrix2 == -1) { printf("error\n"); continue; }
            add(matrices+numberOfMatrix1,matrices+numberOfMatrix2); // adding
        }
        else if(!strcmp(command,"multiply"))
        {
            command = strtok(NULL," \r\n");
            if(command == NULL) { printf("error\n"); continue; }
            int numberOfMatrix1 = -1;
            for(int i=0;i<numberOfMatrices;i++) // searching location of matrix1
            {
                if(!strcmp((*(matrices+i)).matrixName,command)) {numberOfMatrix1 = i;}
            }
            command = strtok(NULL," \r\n");
            if(command == NULL || numberOfMatrix1 == -1) { printf("error\n"); continue; }
            int numberOfMatrix2 = -1;
            for(int i=0;i<numberOfMatrices;i++) // searching location of matrix2
            {
                if(!strcmp((*(matrices+i)).matrixName,command)) {numberOfMatrix2 = i;}
            }
            if(strtok(NULL," \n") != NULL || numberOfMatrix2 == -1) { printf("error\n"); continue; }
            multiply(matrices+numberOfMatrix1,matrices+numberOfMatrix2); // multipling
        }
        else if(!strcmp(command,"subtract"))
        {
            command = strtok(NULL," \r\n");
            if(command == NULL) { printf("error\n"); continue; }
            int numberOfMatrix1 = -1;
            for(int i=0;i<numberOfMatrices;i++) // searching location of matrix1
            {
                if(!strcmp((*(matrices+i)).matrixName,command)) {numberOfMatrix1 = i;}
            }
            command = strtok(NULL," \r\n");
            if(command == NULL || numberOfMatrix1 == -1) { printf("error\n"); continue; }
            int numberOfMatrix2 = -1;
            for(int i=0;i<numberOfMatrices;i++) // searching location of matrix2
            {
                if(!strcmp((*(matrices+i)).matrixName,command)) {numberOfMatrix2 = i;}
            }
            if(strtok(NULL," \n") != NULL || numberOfMatrix2 == -1) { printf("error\n"); continue; }
            subtract(matrices+numberOfMatrix1,matrices+numberOfMatrix2); // subtracting
        }
        else
        { printf("error\n"); }
    }
    fclose(inputFile);
    free(filelocation);
    free(inputLine);
    for (int i=0;i<numberOfVectors;i++) // free'ing allocated memory on vector array
    {
        freeVec(vectors+i);
    }
    free(vectors);
    for(int i=0;i<numberOfMatrices;i++) // free'ing allocated memory on matrices array
    {
        freeMat(matrices+i);
    }
    free(matrices);
    fclose(stdout);
    return 0;
}

int toInt(char* string) // function that turns string to integer
{
    int count=0,value=0,isNegative=0;
    if (string[count] == '-'){count=1;isNegative=1;}
    while(string[count] != '\0' && string[count] != '\r')
    {
        value *= 10;
        value += string[count]-48;
        count++;
    }
    if(isNegative){return (value * -1);}
    return value;
}

bool intCheck(char* myString) // checks this char is number or not
{
    for(int i=0;i<strlen(myString);i++)
    {
        if ((((myString[i] - 48) < 0) && ((57 - myString[i]) > 9)) && !(i == 0 && myString[i] == '-'))
            return false;
    }
    return true;
}


void printVec(struct Vector theVector) // function that prints vector
{
    printf("%d",*(theVector.vectorValues));
    for(int i=1;i<theVector.length;i++)
    {
        printf(" %d",*(theVector.vectorValues+i));
    }
    fprintf(stdout,"\n");
}

void printMat(struct Matrix theMatrix) // function that prints matrix
{
    for(int i=0;i<theMatrix.x;i++)
    {
        printf("%d",*(*(theMatrix.matrixValues+i)));
        for(int j=1;j<theMatrix.y;j++)
        {
            printf(" %d",*(*(theMatrix.matrixValues+i)+j));
        }
        fprintf(stdout,"\n");
    }
}

void freeVec(struct Vector *theVector) // function that free's vector
{
    free(theVector->vectorName);
    free(theVector->vectorValues);
}

void freeMat(struct Matrix *theMatrix) // function that free's matrix
{
    free(theMatrix->matrixName);
    for(int i=0;i<theMatrix->x;i++)
    {
        free(*(theMatrix->matrixValues+i));
    }
    free(theMatrix->matrixValues);
}

struct Vector veczeros(char* filename, int length) // creates a vector with zeros
{
    struct Vector newVector;
    newVector.vectorName = malloc(strlen(filename) * sizeof(char));
    strcpy(newVector.vectorName,filename);
    newVector.vectorValues = malloc(length * sizeof(int));
    int i;
    for (i = 0;i<length;i++)
    {
        newVector.vectorValues[i] = 0;
    }
    printf("created vector %s %d\n",newVector.vectorName,length);
    newVector.length = length;
    printVec(newVector);
    return newVector;
}

struct Matrix matzeros(char* filename, int x, int y) // creates a matrix with zeros
{
    struct Matrix newMatrix;
    newMatrix.matrixName = malloc(strlen(filename) * sizeof(char));
    strcpy(newMatrix.matrixName,filename);
    newMatrix.matrixValues = malloc(x * sizeof(int*));
    int i,j;
    for(i=0;i<x;i++)
    {
        *(newMatrix.matrixValues+i) = malloc(y * sizeof(int));
        for(j=0;j<y;j++)
        {
            *(*(newMatrix.matrixValues+i)+j) = 0;
        }
    }
    printf("created matrix %s %d %d\n",newMatrix.matrixName, x, y);
    newMatrix.x=x;
    newMatrix.y=y;
    printMat(newMatrix);
    return newMatrix;
}

struct Vector vecread(char* filename, char* fileLocation) // reads a vector from file
{
    struct Vector newVector;
    newVector.vectorName = malloc((strlen(filename) -3) * sizeof(char));
    filename = strtok(filename,".");
    strcpy(newVector.vectorName,filename);
    FILE* theFile = fopen(fileLocation, "r");
    char* value = malloc(5*sizeof(char));
    int count = 0;
    newVector.vectorValues = malloc(sizeof(int));
    while (!feof(theFile))
    {
        fscanf(theFile,"%s",value);
        newVector.vectorValues = realloc(newVector.vectorValues, (sizeof(newVector.vectorValues)+1) * sizeof(int));
        newVector.vectorValues[count] = toInt(value);
        count++;
    }
    printf("read vector %s.vec %d\n",newVector.vectorName,count);
    newVector.length = count;
    printVec(newVector);
    free(value);
    fclose(theFile);
    return newVector;
}

struct Matrix matread(char* filename, char* fileLocation) // reads a matrix from file
{
    struct Matrix newMatrix;
    newMatrix.matrixName = malloc((strlen(filename) -3) *sizeof(char));
    filename = strtok(filename,".");
    strcpy(newMatrix.matrixName,filename);
    FILE* theFile = fopen(fileLocation, "r");
    char* rowValues = malloc(50*sizeof(char));
    int row = 0, column;
    newMatrix.matrixValues = malloc(NULL);
    while (!feof(theFile))
    {
        column = 0;
        newMatrix.matrixValues = realloc(newMatrix.matrixValues, (row+1)*sizeof(int*));
        fscanf(theFile,"%[^\n]\n",rowValues);
        *(newMatrix.matrixValues+row) = malloc(NULL);
        char* value = strtok(rowValues," \n");
        while(value != NULL)
        {
            column++;
            *(newMatrix.matrixValues+row) = realloc(*((newMatrix.matrixValues)+row),column*sizeof(int));
            *(*(newMatrix.matrixValues+row)+column-1) = toInt(value);
            value = strtok(NULL," \n");
        }
        row++;
    }
    printf("read matrix %s.mat %d %d\n",newMatrix.matrixName,row,column);
    free(rowValues);
    fclose(theFile);
    newMatrix.x=row;
    newMatrix.y=column;
    printMat(newMatrix);
    return newMatrix;
}

struct Matrix vecstack(struct Vector vector1, struct Vector vector2, char* direction, char* filename) // concatenates two vectors and creates a matrix
{
    if (!strcmp(direction,"row"))
    {
        struct Matrix newMatrix;
        newMatrix.matrixName = malloc(strlen(filename)*sizeof(char));
        strcpy(newMatrix.matrixName,filename);
        newMatrix.x = 2;
        newMatrix.y = vector1.length;
        newMatrix.matrixValues = (int **) malloc(2*sizeof(int*));
        *(newMatrix.matrixValues) = (int*) malloc(vector1.length * sizeof(int));
        *(newMatrix.matrixValues+1) = (int*) malloc(vector1.length * sizeof(int));
        for(int i=0;i<vector1.length;i++)
        {
            *(*(newMatrix.matrixValues)+i) = *(vector1.vectorValues+i);
            *(*(newMatrix.matrixValues+1)+i) = *(vector2.vectorValues+i);
        }
        printf("vectors concatenated %s %d %d\n", newMatrix.matrixName,newMatrix.x,newMatrix.y);
        printMat(newMatrix);
        return newMatrix;
    }
    else if (!strcmp("column",direction))
    {
        struct Matrix newMatrix;
        newMatrix.matrixName = malloc(strlen(filename)*sizeof(char));
        strcpy(newMatrix.matrixName,filename);
        newMatrix.x = vector1.length;
        newMatrix.y = 2;
        newMatrix.matrixValues = (int **) malloc(vector1.length*sizeof(int*));
        for(int i=0;i<vector1.length;i++)
        {
            *(newMatrix.matrixValues+i) = malloc(2*sizeof(int));
        }
        for(int i=0;i<vector1.length;i++)
        {
            *(*(newMatrix.matrixValues+i)) = *(vector1.vectorValues+i);
            *(*(newMatrix.matrixValues+i)+1) = *(vector2.vectorValues+i);
        }
        printf("vectors concatenated %s %d %d\n", newMatrix.matrixName,newMatrix.x,newMatrix.y);
        printMat(newMatrix);
        return newMatrix;
    }
    else {printf("error\n");}
}

void matstack(struct Matrix *matrix1, struct Matrix *matrix2, char where) // concatenates second matrix to first matrix
{
    if ((where == 'r') && (matrix1->x == matrix2->x))
    {
        for(int i=0;i<matrix1->x;i++)
        {
            *(matrix1->matrixValues+i) = realloc(*(matrix1->matrixValues+i), (matrix1->y + matrix2->y) * sizeof(int));
        }
        for(int i=0;i<matrix1->x;i++)
        {
            for(int j=0;j<matrix2->y;j++)
            {
                *(*(matrix1->matrixValues+i)+matrix1->y+j) = *(*(matrix2->matrixValues+i)+j);
            }
        }
        matrix1->y += matrix2->y;
        printf("matrices concatenated %s %d %d\n", matrix1->matrixName, matrix1->x, matrix1->y);
        printMat(*matrix1);
    }
    else if ((where == 'd') && (matrix1->y == matrix2->y))
    {
        matrix1->matrixValues = realloc(matrix1->matrixValues, (matrix1->x + matrix2->x) * sizeof(int *));
        for (int i=matrix1->x;i<(matrix1->x + matrix2->x);i++)
        {
            *(matrix1->matrixValues+i) = malloc(matrix1->y * sizeof(int));
        }
        for (int i=0;i<matrix2->x;i++)
        {
            for(int j=0;j<matrix1->y;j++)
            {
                *(*(matrix1->matrixValues+i+matrix1->x)+j) = *(*(matrix2->matrixValues+i)+j);
            }
        }
        matrix1->x += matrix2->x;
        printf("matrices concatenated %s %d %d\n", matrix1->matrixName, matrix1->x, matrix1->y);
        printMat(*matrix1);
    }
    else {printf("error\n");}
}

void mvstack(struct Matrix *matrix,struct Vector *vector,char where) // concatenates vector to matrix
{
    if ((where == 'r') && (matrix->x == vector->length))
    {
        for(int i=0;i<matrix->x;i++)
        {
            *(matrix->matrixValues+i) = (int *) realloc(*(matrix->matrixValues+i), (matrix->y +1)*sizeof(int));
        }
        for(int i=0;i<matrix->x;i++)
        {
            *(*(matrix->matrixValues+i)+matrix->y) = *(vector->vectorValues+i);
        }
        matrix->y += 1;
        printf("matrix and vector concatenated %s %d %d\n", matrix->matrixName, matrix->x, matrix->y);
        printMat(*matrix);
    }
    else if ((where == 'd') && (matrix->y == vector->length))
    {
        matrix->matrixValues = realloc(matrix->matrixValues, (matrix->x +1)*sizeof(int*));
        *(matrix->matrixValues + matrix->x) = malloc(matrix->y * sizeof(int));
        for(int i=0;i<matrix->y;i++)
        {
            *(*(matrix->matrixValues+matrix->x)+i) = *(vector->vectorValues+i);
        }
        matrix->x += 1;
        printf("matrix and vector concatenated %s %d %d\n", matrix->matrixName, matrix->x, matrix->y);
        printMat(*matrix);
    }
    else {printf("error\n");}
}

void pad(struct Matrix *matrix, int x, int y, char* mode) // pads matrix with maximum or minimum values
{
    matrix->matrixValues = realloc(matrix->matrixValues, (matrix->x + x) * sizeof(int *));
    for(int i=0;i<matrix->x;i++)
    {
        *(matrix->matrixValues+i) = realloc(*(matrix->matrixValues+i), (matrix->y + y) * sizeof(int));
    }
    for(int i=0;i<x;i++)
    {
        *(matrix->matrixValues+i+matrix->x) = malloc((matrix->y + y)*sizeof(int));
    }
    for(int h=0;h<y;h++)
    {
        for(int i=0;i<matrix->x;i++)
        {
            int myNumber = *(*(matrix->matrixValues+i));
            if (!strcmp(mode,"maximum")) {
                for (int j = 1; j < matrix->y; j++)
                {
                    if (*(*(matrix->matrixValues+i)+j) > myNumber)
                    { myNumber = *(*(matrix->matrixValues+i)+j); }
                }
            }
            if (!strcmp(mode,"minimum")) {
                for (int j = 1; j < matrix->y; j++)
                {
                    if (*(*(matrix->matrixValues+i)+j) < myNumber)
                    { myNumber = *(*(matrix->matrixValues+i)+j); }
                }
            }
            *(*(matrix->matrixValues+i)+matrix->y) = myNumber;
        }
        matrix->y++;
    }
    for(int h=0;h<x;h++)
    {
        for(int j=0;j<matrix->y;j++)
        {
            int myNumber = *(*(matrix->matrixValues)+j);
            if(!strcmp(mode,"maximum"))
            {
                for (int i=1;i<matrix->x;i++)
                {
                    if ( *(*(matrix->matrixValues+i)+j) > myNumber)
                    { myNumber = *(*(matrix->matrixValues+i)+j); }
                }
            }
            if(!strcmp(mode,"minimum"))
            {
                for (int i=1;i<matrix->x;i++)
                {
                    if ( *(*(matrix->matrixValues+i)+j) < myNumber)
                    { myNumber = *(*(matrix->matrixValues+i)+j); }
                }
            }
            *(*(matrix->matrixValues+matrix->x)+j) = myNumber;
        }
        matrix->x++;
    }
    printf("matrix paded %s %d %d\n",matrix->matrixName, matrix->x, matrix->y);
    printMat(*matrix);
}

void padval(struct Matrix *matrix, int x, int y, int value) // pads matrix with given value
{
    matrix->matrixValues = realloc(matrix->matrixValues, (matrix->x + x) * sizeof(int *));
    for(int i=0;i<matrix->x;i++)
    {
        *(matrix->matrixValues+i) = realloc(*(matrix->matrixValues+i), (matrix->y + y) * sizeof(int));
    }
    for(int i=0;i<x;i++)
    {
        *(matrix->matrixValues+i+matrix->x) = malloc((matrix->y + y)* sizeof(int));
    }
    for(int h=0;h<y;h++)
    {
        for(int i=0;i<matrix->x;i++)
        {
            *(*(matrix->matrixValues+i)+matrix->y) = value;
        }
        matrix->y++;
    }
    for(int h=0;h<x;h++)
    {
        for(int j=0;j<matrix->y;j++)
        {
            *(*(matrix->matrixValues+matrix->x)+j) = value;
        }
        matrix->x++;
    }
    printf("matrix paded %s %d %d\n",matrix->matrixName, matrix->x, matrix->y);
    printMat(*matrix);
}

struct Vector vecslice(struct Vector vector, int start, int stop, char* name) // slices vector and creates new vector
{
    struct Vector newVector;
    newVector.vectorName = malloc(strlen(name) * sizeof(char));
    strcpy(newVector.vectorName,name);
    newVector.length = stop-start;
    newVector.vectorValues = malloc((stop-start) * sizeof(int));
    for(int i=start;i<stop;i++)
    {
        *(newVector.vectorValues+i-start) =  *(vector.vectorValues+i);
    }
    printf("vector sliced %s %d\n",newVector.vectorName,newVector.length);
    printVec(newVector);
    return newVector;
}

struct Vector matslicecol(struct Matrix matrix, int column, int start, int stop, char* name) // slices matrix's given column and creates new vector
{
    if((start < stop) && (start >= 0) && (stop <= matrix.x) && (column >= 0) && (column < matrix.y))
    {
        struct Vector newVector;
        newVector.vectorName = malloc(strlen(name) * sizeof(char));
        strcpy(newVector.vectorName,name);
        newVector.length = stop-start;
        newVector.vectorValues = malloc((stop-start) * sizeof(int));
        for(int i=start;i<stop;i++)
        {
            *(newVector.vectorValues+i-start) = *(*(matrix.matrixValues+i)+column);
        }
        printf("vector sliced %s %d\n",newVector.vectorName,newVector.length);
        printVec(newVector);
        return newVector;
    }
    else { printf("error\n"); }
}

struct Vector matslicerow(struct Matrix matrix, int row, int start, int stop, char* name) // slices matrix's given row and creates new vector
{
    if((start < stop) && (start >= 0) && (stop <= matrix.y) && (row >= 0) && (row < matrix.x))
    {
        struct Vector newVector;
        newVector.vectorName = malloc(strlen(name) * sizeof(char));
        strcpy(newVector.vectorName,name);
        newVector.length = stop-start;
        newVector.vectorValues = malloc((stop-start) * sizeof(int));
        for(int i=start;i<stop;i++)
        {
            *(newVector.vectorValues+i-start) = *(*(matrix.matrixValues+row)+i);
        }
        printf("vector sliced %s %d\n",newVector.vectorName,newVector.length);
        printVec(newVector);
        return newVector;
    }
    else { printf("error\n"); }
}

struct Matrix matslice(struct Matrix matrix, int y1, int y2, int x1, int x2, char* name) // slices matrix and creates a smaller new matrix
{
    if (1)
    {
        struct Matrix newMatrix;
        newMatrix.matrixName = malloc(strlen(name) * sizeof(char));
        strcpy(newMatrix.matrixName,name);
        newMatrix.x = x2 - x1;
        newMatrix.y = y2 - y1;
        newMatrix.matrixValues = malloc(newMatrix.x * sizeof(int *));
        for(int i=0;i<newMatrix.x;i++)
        {
            *(newMatrix.matrixValues+i) = malloc(newMatrix.y * sizeof(int));
        }
        for(int i=x1;i<x2;i++)
        {
            for(int j=y1;j<y2;j++)
            {
                *(*(newMatrix.matrixValues+i-x1)+j-y1) = *(*(matrix.matrixValues+i)+j+1);
            }
        }
        printf("matrix sliced %s %d %d\n",newMatrix.matrixName,newMatrix.x,newMatrix.y);
        printMat(newMatrix);
        return newMatrix;
    }
    else {printf("error\n"); }
}

void add(struct Matrix *matrix1, struct Matrix *matrix2) // adds two matrices
{
    if ((matrix1->x == matrix2->x) && (matrix1->y == matrix2->y))
    {
        for (int i=0;i<matrix1->x;i++)
        {
            for (int j=0;j<matrix1->y;j++)
            {
                *(*(matrix1->matrixValues+i)+j) += *(*(matrix2->matrixValues+i)+j);
            }
        }
        printf("add %s %s\n",matrix1->matrixName,matrix2->matrixName);
        printMat(*matrix1);
    }
    else { printf("error\n"); }
}

void multiply(struct Matrix *matrix1, struct Matrix *matrix2) // multiplies two matrices
{
    if ((matrix1->x == matrix2->x) && (matrix1->y == matrix2->y))
    {
        for (int i=0;i<matrix1->x;i++)
        {
            for (int j=0;j<matrix1->y;j++)
            {
                *(*(matrix1->matrixValues+i)+j) *= *(*(matrix2->matrixValues+i)+j);
            }
        }
        printf("multiply %s %s\n",matrix1->matrixName,matrix2->matrixName);
        printMat(*matrix1);
    }
    else { printf("error\n"); }
}

void subtract(struct Matrix *matrix1, struct Matrix *matrix2) // subtracts two matrices
{
    if ((matrix1->x == matrix2->x) && (matrix1->y == matrix2->y))
    {
        for (int i=0;i<matrix1->x;i++)
        {
            for (int j=0;j<matrix1->y;j++)
            {
                *(*(matrix1->matrixValues+i)+j) -= *(*(matrix2->matrixValues+i)+j);
            }
        }
        printf("subtract %s %s\n",matrix1->matrixName,matrix2->matrixName);
        printMat(*matrix1);
    }
    else { printf("error\n"); }
}

//İbrahim Burak Tanrıkulu