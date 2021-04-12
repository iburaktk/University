#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct {
    int value;
    struct gen* next;
} gen;

typedef struct {
    gen* firstGen;
    int fitness;
    int rank;
    struct chromosome* nextChromosome;
} chromosome;

typedef struct {
    chromosome* firstChromosome;
} generation;

void myStrtok(char* myString,char delimeter,char** targetString);
int toInt(char* string);
chromosome* sortGenerationRec(chromosome* head,int numberOfChromosomes);
void calculateFitness(chromosome* currentChromosome);
void storeBestChromosome(chromosome* target, chromosome* data);
void printGeneration(chromosome* currentChromosome);
void printChromosome(chromosome* currentChromosome);
void xover(chromosome* firstChromosome,int firstPair,int secondPair,int start, int stop);

int main(int argc, int* argv[])
{
    //taking arguments and opening files for input reading
    int PROB_SIZE = toInt(argv[1]); // number of genes in a chromosome
    int POP_SIZE = toInt(argv[2]); // number of chromosomes of this creature
    int MAX_GEN = toInt(argv[3]); // number of generations
    FILE* populationInput = fopen("population","r");
    FILE* selectionInput = fopen("selection","r");
    FILE* xoverInput = fopen("xover","r");
    FILE* mutateInput = fopen("mutate","r");

    //creating data
    generation* currentGeneration = malloc(sizeof(generation));

    chromosome* bestChromosome = malloc(sizeof(chromosome));
    bestChromosome->firstGen = malloc(sizeof(gen));
    gen* genPointer = bestChromosome->firstGen;
    for(int i=1;i<PROB_SIZE;i++)
    {
        genPointer->next = malloc(sizeof(gen));
        genPointer = genPointer->next;
    }

    //initialization first generation
    printf("GENERATION: 0\n");
    chromosome* currentChromosome = malloc(sizeof(chromosome));
    currentGeneration->firstChromosome = currentChromosome;
    for(int i=0;i<POP_SIZE;i++)
    {
        char* chromosomeInputLine = malloc((PROB_SIZE*2)*sizeof(char));
        fscanf(populationInput,"%[^\n]\n",chromosomeInputLine);
        char* currentGen = strtok_r(chromosomeInputLine,":\r", &chromosomeInputLine);
        currentChromosome->firstGen = malloc(sizeof(gen));
        gen* genPointer = currentChromosome->firstGen;
        for (int j=0;j<PROB_SIZE;j++)
        {
            genPointer->value = toInt(currentGen);
            if (j != PROB_SIZE -1)
            {
                genPointer->next = malloc(sizeof(gen));
            }
            genPointer = genPointer->next;
            currentGen = strtok_r(NULL,":\r", &chromosomeInputLine);
        }
        calculateFitness(currentChromosome);
        if (i == 0) // initialization of bestChromosome
        {
            storeBestChromosome(bestChromosome,currentChromosome);
        }
        else if (bestChromosome->fitness > currentChromosome->fitness) // refreshing bestChromosome
        {
            storeBestChromosome(bestChromosome,currentChromosome);
        }
        if (i != POP_SIZE -1) // we dont want allocate memory for NULL
        {
            currentChromosome->nextChromosome = malloc(sizeof(chromosome));
        }
        currentChromosome = currentChromosome->nextChromosome;
    }
    fclose(populationInput);

    //printing first generation
    currentGeneration->firstChromosome = sortGenerationRec(currentGeneration->firstChromosome,POP_SIZE);
    printGeneration(currentGeneration->firstChromosome);
    printChromosome(bestChromosome);

    //allocating variables, we will use they in evolution
    char* xoverInputLine = malloc(6* sizeof(char));
    char* xoverLocation = malloc(3* sizeof(char));
    char* selectionInputLine = malloc((POP_SIZE*3)*sizeof(char));
    char* chromosomePairs = malloc(6* sizeof(char));
    char* pair = malloc(3* sizeof(char));
    char* mutateInputLine = malloc(3* sizeof(char));

    //evolution
    for (int i=0;i<MAX_GEN;i++)
    {
        printf("GENERATION %d\n",i+1);

        //selection and xover
        fscanf(xoverInput,"%[^\n]\n",xoverInputLine);
        myStrtok(xoverInputLine,':',&xoverLocation);
        int xoverStart = toInt(xoverLocation);
        myStrtok(xoverInputLine,':',&xoverLocation);
        int xoverStop = toInt(xoverLocation);
        fscanf(selectionInput,"%[^\n]\n",selectionInputLine);
        for(int j=0;j<POP_SIZE/2;j++) // there is (numberOfChromosome/2) pairs
        {
            myStrtok(selectionInputLine,' ',&chromosomePairs);
            myStrtok(chromosomePairs,':',&pair);
            int firstPair = toInt(pair);
            myStrtok(chromosomePairs,':',&pair);
            int secondPair = toInt(pair);
            if (firstPair > secondPair)
            {
                int temp = firstPair;
                firstPair = secondPair;
                secondPair = temp;
            }
            xover(currentGeneration->firstChromosome,firstPair,secondPair,xoverStart,xoverStop);
        }

        //mutating
        fscanf(mutateInput,"%s\n",mutateInputLine);
        int mutate = toInt(mutateInputLine);
        chromosome* chromosomePointer = currentGeneration->firstChromosome;
        for(int i=0;i<POP_SIZE;i++)
        {
            genPointer = chromosomePointer->firstGen;
            for(int j=1;j<mutate;j++)
            {
                genPointer = genPointer->next;
            }
            if (genPointer->value == 1)
            {
                genPointer->value = 0;
            }
            else
            {
                genPointer->value = 1;
            }
            chromosomePointer = chromosomePointer->nextChromosome;
        }

        //printing generation to screen
        calculateFitness(currentGeneration->firstChromosome);
        currentGeneration->firstChromosome = sortGenerationRec(currentGeneration->firstChromosome,POP_SIZE);
        if (currentGeneration->firstChromosome->fitness < bestChromosome->fitness)
        {
            storeBestChromosome(bestChromosome,currentGeneration->firstChromosome);
        }
        printGeneration(currentGeneration->firstChromosome);
        printChromosome(bestChromosome);
    }
    free(xoverLocation);
    free(xoverInputLine);
    free(selectionInputLine);
    free(mutateInputLine);
    free(chromosomePairs);
    free(pair);
    fclose(selectionInput);
    fclose(xoverInput);
    fclose(mutateInput);
}

void myStrtok(char* myString,char delimeter,char** targetString) // mine strtok_r function
{
    int i;
    for (i=0;i<strlen(myString);i++)
    {
        if(myString[i] == delimeter || myString[i] == '\0')
        {
            break;
        }
    }
    for(int j=0;j<i;j++)
    {
        (*targetString)[j] = myString[j];
    }
    (*targetString)[i] = '\0';
    int k=(strlen(myString)-i);
    for(int j=0;j<k;j++)
    {
        myString[j] = myString[j+i+1];
    }
}

int toInt(char* string) // function that turns string to integer
{
    int count=0,value=0;
    while(string[count] != '\0' && string[count] != '\r')
    {
        value *= 10;
        value += string[count]-48;
        count++;
    }
    return value;
}

chromosome* sortGenerationRec(chromosome* head,int numberOfChromosomes) // sorts chromosomes and returns head of queue
{
    if (numberOfChromosomes == 1) // 1 chromosome left, enqueue it.
    {
        return head;
    }
    chromosome* ptr = head;
    chromosome* bestPtr = ptr;
    chromosome* temp;
    int bestFitness = head->fitness;
    for (int i=1;i<numberOfChromosomes;i++) // find best chromosome
    {
        temp = ptr->nextChromosome;
        if (temp->fitness < bestFitness)
        {
            bestFitness = temp->fitness;
            bestPtr = ptr;
        }
        ptr = ptr->nextChromosome;
    }
    if (bestFitness != head->fitness) // if best chromosome is not first, then requeue the queue
    {
        ptr = head;
        head = bestPtr->nextChromosome;
        temp = bestPtr->nextChromosome;
        bestPtr->nextChromosome = temp->nextChromosome;
    }
    else // if best chromosome is already first, continue with others
        ptr = head->nextChromosome;
    head->nextChromosome = sortGenerationRec(ptr,numberOfChromosomes-1); // enqueue'ing
    return head;
}

void calculateFitness(chromosome* currentChromosome) // calculates fitness of chromosomes
{
    chromosome* chromosomePointer = currentChromosome;
    while(chromosomePointer != NULL)
    {
        gen* genPointer = chromosomePointer->firstGen;
        int fitness = 0;
        while(genPointer != NULL)
        {
            fitness *= 2;
            fitness += genPointer->value;
            genPointer = genPointer->next;
        }
        chromosomePointer->fitness = fitness;
        chromosomePointer = chromosomePointer->nextChromosome;
    }
}

void storeBestChromosome(chromosome* target, chromosome* data) // copies the data of best chromosome
{
    target->fitness = data->fitness;
    gen* genPointer1 = data->firstGen;
    gen* genPointer2 = target->firstGen;
    while (genPointer1 != NULL)
    {
        genPointer2->value = genPointer1->value;
        genPointer1 = genPointer1->next;
        genPointer2 = genPointer2->next;
    }
}

void printGeneration(chromosome* currentChromosome) // prints all chromosomes
{
    while (currentChromosome != NULL)
    {
        printChromosome(currentChromosome);
        currentChromosome = currentChromosome->nextChromosome;
    }
    printf("Best chromosome found so far: ");
}

void printChromosome(chromosome* currentChromosome) // prints current chromosomes
{
    gen* currentGen = currentChromosome->firstGen;
    printf("%d",currentGen->value);
    currentGen = currentGen->next;
    while (currentGen != NULL)
    {
        printf(":%d",currentGen->value);
        currentGen = currentGen->next;
    }
    printf(" -> %d\n",currentChromosome->fitness);
}

void xover(chromosome* firstChromosome,int firstPair,int secondPair,int start, int stop) // selection and xover
{
    chromosome* chromosomePair1 = firstChromosome;
    chromosome* chromosomePair2;
    for(int i=1;i<firstPair;i++) // finding adress of first pair of chromosome
    {
        chromosomePair1 = chromosomePair1->nextChromosome;
    }
    chromosomePair2 = chromosomePair1;
    for(int i=0;i<(secondPair-firstPair);i++) // finding adress of second pair of chromosome
    {
        chromosomePair2 = chromosomePair2->nextChromosome;
    }
    gen* genPointer1 = chromosomePair1->firstGen;
    gen* genPointer2 = chromosomePair2->firstGen;
    if (start == 1)
    {
	//changing links at start position
        gen* temp = chromosomePair1->firstGen;
        chromosomePair1->firstGen = chromosomePair2->firstGen;
        chromosomePair2->firstGen = temp;
        for(int i=0;i<(stop-start);i++)
        {
            genPointer1 = genPointer1->next;
            genPointer2 = genPointer2->next;
        }
	//changing links at stop position
        temp = genPointer1->next;
        genPointer1->next = genPointer2->next;
        genPointer2->next = temp;
    }
    else
    {
        for(int i=1;i<start-1;i++) // finding starting position for xover
        {
            genPointer1 = genPointer1->next;
            genPointer2 = genPointer2->next;
        }
	//changing links at start position
        gen* temp = genPointer1->next;
        genPointer1->next = genPointer2->next;
        genPointer2->next = temp;
        for(int i=0;i<(stop-start+1);i++) 
        {
            genPointer1 = genPointer1->next;
            genPointer2 = genPointer2->next;
        }
	//changing links at stop position
        temp = genPointer1->next;
        genPointer1->next = genPointer2->next;
        genPointer2->next = temp;
    }

}
