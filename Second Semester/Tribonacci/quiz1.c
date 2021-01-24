#include <stdio.h>
#include <stdlib.h>

int main()
{
	printf("Enter a number: "); // taking a number from user
	int number;
	scanf("%d",&number);
	int *tribo = malloc(number * sizeof(int));
	int i;	
	for (i=0;i<number;i++)
	{
		if (i==0)
		{
			*(tribo) = 0; // first element is 0
		}
		else if (i==1)
		{
			*(tribo+1) = 1; // second element is 1
		}
		else if (i==2)
		{
			*(tribo+2) = 2; // third element is 2
		}
		else // element(n) = element(n-1) + element(n-2) + element(n-3)
		{
			*(tribo+i) = *(tribo+i-1) + *(tribo+i-2) + *(tribo+i-3);
		}
	}
	for (i = 0;i<number;i++) // printing tribonacci numbers
	{
		if (i==number-1)
		{
			printf("%d",*(tribo+i));
		}
		else
		{
			printf("%d ",*(tribo+i));
		}
	}
	free(tribo); 
}
// İbrahim Burak Tanrıkulu
