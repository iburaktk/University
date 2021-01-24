#include <stdio.h>

void sort(int * List)
{
    for (int i=0;i<10;i++)
    {
        for (int j=0;j<10;j++)
        {
            if (i<j)
            {
                if (List[i]<List[j])
                {
                    int temp = List[i];
                    List[i] = List[j];
                    List[j] = temp;
                }
                if (List[i]>List[j])
                {
                    int temp = List[i];
                    List[i] = List[j];
                    List[j] = temp;
                }
            }
        }
    }
}

char * str(int * List, char * List1)
{
    sprintf(List1,"[%d, %d, %d, %d, %d, %d, %d, %d, %d, %d]",List[0],List[1],List[2],List[3],List[4],List[5],List[6],List[7],List[8],List[9]);
}

char * str2(int * List, char * List1)
{
    sprintf(List1,"[%d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d]",List[0],List[1],List[2],List[3],List[4],List[5],List[6],List[7],List[8],List[9],List[10],List[11],List[12],List[13],List[14],List[15],List[16],List[17],List[18],List[19]);
}

void Concatenate(int * ListA,int * ListB,int * ListC)
{
    for (int i=0;i<10;i++)
    {
        ListC[i]=ListA[i];
    }
    for (int i=0;i<10;i++)
    {
        ListC[i+10]=ListB[i];
    }
}

void SortedConcatenate(int * ListA, int * ListB, int * ListC)
{
    int i=0,j=0,k=0;
    while (k<20)
    {
        if (i!=10 && j!=10)
        {
            if (ListA[i]<ListB[j])
            {
                ListC[k]=ListA[i];
                i+=1;
                k+=1;
                continue;
            }
            if (ListA[i]>ListB[j])
            {
                ListC[k]=ListB[j];
                j+=1;
                k+=1;
                continue;
            }
        }
        if (i==10)
        {
            ListC[k]=ListB[j];
            j+=1;
            k+=1;
        }
        if (j==10)
        {
            ListC[k]=ListA[i];
            i+=1;
            k+=1;
        }
    }
}

int main()
{
    int ListA[10] = {12, 29, 15, 8, 36, 6, 9, 2, 4, 7};
    int ListB[10] = {39, 41, 1, 3, 27, 14, 5, 11, 90, 43};
    int *A = ListA;
    int *B = ListB;
    char List1[300];
    str(ListA,List1);
    printf("List A: %s\n",List1);
    str(ListB,List1);
    printf("List B: %s\n",List1);
    sort(A);
    sort(B);
    str(ListA,List1);
    printf("Sorted List A: %s\n",List1);
    str(ListB,List1);
    printf("Sorted List B: %s\n",List1);
    int ListC[20];
    Concatenate(ListA,ListB,ListC);
    str2(ListC,List1);
    printf("List C: %s\n",List1);
    SortedConcatenate(ListA,ListB,ListC);
    str2(ListC,List1);
    printf("Sorted List C: %s\n", List1);
    return 0;
}
//İbrahim Burak Tanrıkulu
