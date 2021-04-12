#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct{
   char* passengerName;
   int priority;
   int wantedClass;
   struct passenger* next;
} passenger;

typedef struct {
   char* flightName;
   char** seats;
   passenger* queues;
   int closed;
   int soldBusinessTicket;
   int soldEconomyTicket;
   int soldStandardTicket;
   int personsInBusinessQueue;
   int personsInEconomyQueue;
   int personsInStandardQueue;
   //queues[0] = businessQueue      seats[0] = businessSeats    ticketSoldPassengers[0] = soldBusinessTickets
   //queues[1] = economyQueue       seats[1] = economySeats     ticketSoldPassengers[1] = soldEconomyTickets
   //queues[2] = standardQueue      seats[2] = standardSeats    ticketSoldPassengers[2] = soldStandardTickets
} flight;

void freePassengerInfo(passenger* thePassenger);
void freeFlightInfo(flight* theFlight);
int toInt(char* string);
int intCheck(char* myString){for(int i=0;i<strlen(myString);i++){if ((((myString[i] - 48) < 0) || ((57 - myString[i]) < 0))){return 0;}} return 1;};
void addseat(char** string, int number);


int main (int argc, char* argv[])
{
    stdout = fopen(argv[2],"w+"); // for printing to output file

    flight* flights = malloc(NULL); //flight and seats data
    int numberOfFlights = 0;
    int** numberOfSeats = malloc(NULL);

    passenger** ticketSoldPassengers = malloc(NULL); // tickets data
    int* businessTicket = malloc(NULL);
    int* economyTicket = malloc(NULL);
    int* standardTicket = malloc(NULL);
    int* standartBusinessTicket = malloc(NULL);
    int* standartEconomyTicket = malloc(NULL);

    FILE* inputFile = fopen(argv[1],"r"); // input file
    while(!feof(inputFile))
    {
        char* inputLine = malloc(50*sizeof(char));
        fscanf(inputFile,"%[^\n]\n",inputLine);
        char* inputLineCopy = malloc(50*sizeof(char)); strcpy(inputLineCopy,inputLine);
        char* command = strtok(inputLine," \r");
        if (!strcmp(command,"addseat"))
        {
            command = strtok(NULL," \r");
            int flightLocation = -1;
            for(int i=0;i<numberOfFlights;i++) // Is this flight exists?
            {
                if (!strcmp(flights[i].flightName, command))
                {
                    flightLocation = i;
                    break;
                }
            }
            char* class = malloc (20* sizeof(char)); char* command2; command2 = strtok_r(inputLineCopy," \r",&inputLineCopy); command2 = strtok_r(NULL," \r",&inputLineCopy); command2 = strtok_r(NULL," \r",&inputLineCopy); strcpy(class,command2); command2 = strtok_r(NULL," \r",&inputLineCopy);
            if (flightLocation == -1 && (!(strcmp(class,"business") && strcmp(class,"economy") && strcmp(class,"standard")) && intCheck(command2) && strtok_r(NULL," \r",&inputLineCopy) == NULL)) // this flight is new. we must allocate.
            {
                numberOfFlights++;
                businessTicket = realloc(businessTicket,numberOfFlights* sizeof(int));
                economyTicket = realloc(economyTicket,numberOfFlights* sizeof(int));
                standardTicket = realloc(standardTicket,numberOfFlights* sizeof(int));
                standartBusinessTicket = realloc(standartBusinessTicket,numberOfFlights* sizeof(int));
                standartEconomyTicket = realloc(standartEconomyTicket,numberOfFlights* sizeof(int));
                numberOfSeats = realloc(numberOfSeats,numberOfFlights* sizeof(int*));
                numberOfSeats[numberOfFlights - 1] = malloc(3* sizeof(int)); numberOfSeats[numberOfFlights - 1][2] = 0;
                ticketSoldPassengers = realloc(ticketSoldPassengers,numberOfFlights* sizeof(passenger*));
                ticketSoldPassengers[numberOfFlights - 1] = malloc(3* sizeof(passenger));
                flights = realloc(flights,numberOfFlights * sizeof(flight));
                flights[numberOfFlights - 1].flightName = malloc(strlen(command) * sizeof(char));
                strcpy(flights[numberOfFlights - 1].flightName, command);
                flights[numberOfFlights - 1].personsInBusinessQueue = 0;
                flights[numberOfFlights - 1].personsInEconomyQueue = 0;
                flights[numberOfFlights - 1].personsInStandardQueue = 0;
                flights[numberOfFlights - 1].queues = malloc(3* sizeof(passenger));
                flights[numberOfFlights - 1].seats = malloc(3*sizeof(int *));
                flights[numberOfFlights - 1].seats[0] = malloc(1 * sizeof(char));
                flights[numberOfFlights - 1].seats[1] = malloc(1 * sizeof(char));
                flights[numberOfFlights - 1].seats[2] = malloc(1 * sizeof(char));
                strcpy(flights[numberOfFlights - 1].seats[0], "");
                strcmp(flights[numberOfFlights - 1].seats[1], "");
                strcmp(flights[numberOfFlights - 1].seats[2], "");
                flightLocation = numberOfFlights - 1;
                flights[flightLocation].closed = 0;
            }
            else if (flightLocation == -1 || flights[flightLocation].closed == 1 || !intCheck(command2) || (strcmp(class,"business") && strcmp(class,"economy") && strcmp(class,"standard"))) {fprintf(stdout,"error\n"); free(inputLine); continue;} command = strtok(NULL," \r");
            if (!strcmp(command,"business")) // adding business seats
            {
                command = strtok(NULL," \r");
                addseat(&(flights[flightLocation].seats[0]),toInt(command));
                numberOfSeats[flightLocation][0] += toInt(command);
                printf("addseats %s %d %d %d\n",flights[flightLocation].flightName, numberOfSeats[flightLocation][0],numberOfSeats[flightLocation][1],numberOfSeats[flightLocation][2]);
            }
            else if (!strcmp(command,"economy")) // adding economy seats
            {
                command = strtok(NULL," \r");
                for (int i=0;i<toInt(command);i++)
                {
                    flights[flightLocation].seats[1] = realloc(flights[flightLocation].seats[1], (strlen(flights[flightLocation].seats[1])+2) * sizeof(char));
                    strcat(flights[flightLocation].seats[1],"s");
                }
                numberOfSeats[flightLocation][1] += toInt(command);
                printf("addseats %s %d %d %d\n",flights[flightLocation].flightName, numberOfSeats[flightLocation][0],numberOfSeats[flightLocation][1],numberOfSeats[flightLocation][2]);
            }
            else if (!strcmp(command,"standard")) // adding standard seats
            {
                command = strtok(NULL," \r");
                for (int i=0;i<toInt(command);i++)
                {
                    flights[flightLocation].seats[2] = realloc(flights[flightLocation].seats[2], (strlen(flights[flightLocation].seats[2])+2) * sizeof(char));
                    strcat(flights[flightLocation].seats[2],"s");
                }
                numberOfSeats[flightLocation][2] += toInt(command);
                printf("addseats %s %d %d %d\n",flights[flightLocation].flightName, numberOfSeats[flightLocation][0],numberOfSeats[flightLocation][1],numberOfSeats[flightLocation][2]);
            }
            else { fprintf(stdout,"error\n"); free(inputLine); continue;}
        }
        else if (!strcmp(command,"enqueue"))
        {
            command = strtok(NULL," \r");
            int flightLocation = -1;
            for(int i=0;i<numberOfFlights;i++) //Is this flight exists?
            {
                if (!strcmp(flights[i].flightName, command))
                {
                    flightLocation = i;
                    break;
                }
            }
            if (flightLocation == -1 || flights[flightLocation].closed == 1) { fprintf(stdout,"error\n"); free(inputLine); continue; }
            command = strtok(NULL," \r");
            if (!strcmp(command,"business"))
            {
                command = strtok(NULL," \r");
                char* passengerName = command;
                int priority = 0;
                command = strtok(NULL," \r");
                if (command == NULL) {}
                else if (!strcmp(command,"diplomat")) // priority check
                {
                    priority = 1;
                }
                else { fprintf(stdout,"error\n"); free(inputLine); continue; }
                if (priority == 1) // prior passenger
                {
                    if (flights[flightLocation].personsInBusinessQueue == 0) // this passenger is first passenger
                    {
                        flights[flightLocation].queues[0].passengerName = malloc((strlen(passengerName)+1)* sizeof(char));
                        strcpy(flights[flightLocation].queues[0].passengerName, passengerName);
                        flights[flightLocation].queues[0].priority = 1;
                        flights[flightLocation].queues[0].wantedClass = 2;
                        flights[flightLocation].queues[0].next = NULL;
                    }
                    else if (flights[flightLocation].queues[0].priority == 0) // this passenger goes to head.
                    {
                        passenger* newPassenger = malloc(sizeof(passenger));
                        newPassenger->passengerName = malloc((strlen(flights[flightLocation].queues[0].passengerName)+1) * sizeof(char));
                        strcpy(newPassenger->passengerName, flights[flightLocation].queues[0].passengerName);
                        newPassenger->priority = 0;
                        newPassenger->wantedClass = flights[flightLocation].queues[0].wantedClass;
                        newPassenger->next = flights[flightLocation].queues[0].next;
                        flights[flightLocation].queues[0].passengerName = realloc(flights[flightLocation].queues[0].passengerName,(strlen(passengerName)+1)*sizeof(char));
                        strcpy(flights[flightLocation].queues[0].passengerName,passengerName);
                        flights[flightLocation].queues[0].priority = 1;
                        flights[flightLocation].queues[0].wantedClass = 2;
                        flights[flightLocation].queues[0].next = newPassenger;
                    }
                    else
                    {
                        passenger* newPassenger = malloc(sizeof(passenger));
                        newPassenger->passengerName = malloc((strlen(passengerName)+1)* sizeof(char));
                        strcpy(newPassenger->passengerName,passengerName);
                        newPassenger->priority = 1;
                        newPassenger->wantedClass = 2;
                        passenger* lastPriorPassenger = &(flights[flightLocation].queues[0]);
                        passenger* NonPriorPassenger = &(flights[flightLocation].queues[0]);
                        while (NonPriorPassenger->priority == 1)
                        {
                            lastPriorPassenger = NonPriorPassenger;
                            NonPriorPassenger = NonPriorPassenger->next;
                            if (NonPriorPassenger == NULL) { break; }
                        }
                        lastPriorPassenger->next = newPassenger;
                        newPassenger->next = NonPriorPassenger;
                    }
                }
                if (priority == 0) // normal passenger
                {
                    if (flights[flightLocation].personsInBusinessQueue == 0) // first passenger
                    {
                        flights[flightLocation].queues[0].passengerName = malloc((strlen(passengerName)+1)* sizeof(char));
                        strcpy(flights[flightLocation].queues[0].passengerName, passengerName);
                        flights[flightLocation].queues[0].priority = 0;
                        flights[flightLocation].queues[0].wantedClass = 2;
                        flights[flightLocation].queues[0].next = NULL;
                    }
                    else
                    {
                        passenger* thisPassenger = malloc(sizeof(passenger));
                        thisPassenger->passengerName = malloc((strlen(passengerName)+1) * sizeof(char));
                        strcpy(thisPassenger->passengerName,passengerName);
                        thisPassenger->priority = 0;
                        thisPassenger->wantedClass = 2;
                        thisPassenger->next = NULL;
                        passenger* endOfQueue = &(flights[flightLocation].queues[0]);
                        while(endOfQueue->next != NULL)
                        {
                            endOfQueue = endOfQueue->next;
                        }
                        endOfQueue->next = thisPassenger;
                    }
                }
                flights[flightLocation].personsInBusinessQueue++;
                printf("queue %s %s business %d\n",flights[flightLocation].flightName,passengerName,flights[flightLocation].personsInBusinessQueue);
            }
            else if (!strcmp(command,"economy"))
            {
                command = strtok(NULL," \r");
                int priority = 0;
                char* passengerName = command;
                command = strtok(NULL," \r");
                if (command == NULL) {}
                else if (!strcmp(command,"veteran")) // priority check
                {
                    priority = 1;
                }
                else { fprintf(stdout,"error\n"); free(inputLine); continue; }
                if (priority == 1)
                {
                    if (flights[flightLocation].personsInEconomyQueue == 0) // first passenger
                    {
                        flights[flightLocation].queues[1].passengerName = malloc((strlen(passengerName)+1)* sizeof(char));
                        strcpy(flights[flightLocation].queues[1].passengerName, passengerName);
                        flights[flightLocation].queues[1].priority = 1;
                        flights[flightLocation].queues[1].wantedClass = 2;
                        flights[flightLocation].queues[1].next = NULL;
                    }
                    else if (flights[flightLocation].queues[1].priority == 0) // goes to head
                    {
                        passenger* newPassenger = malloc(sizeof(passenger));
                        newPassenger->passengerName = malloc((strlen(flights[flightLocation].queues[1].passengerName)+1) * sizeof(char));
                        strcpy(newPassenger->passengerName, flights[flightLocation].queues[1].passengerName);
                        newPassenger->priority = 0;
                        newPassenger->next = flights[flightLocation].queues[1].next;
                        flights[flightLocation].queues[1].passengerName = realloc(flights[flightLocation].queues[1].passengerName,(strlen(passengerName)+1)*sizeof(char));
                        strcpy(flights[flightLocation].queues[1].passengerName,passengerName);
                        flights[flightLocation].queues[1].priority = 1;
                        flights[flightLocation].queues[1].wantedClass = 1;
                        flights[flightLocation].queues[1].next = newPassenger;
                    }
                    else
                    {
                        passenger* newPassenger = malloc(sizeof(passenger));
                        newPassenger->passengerName = malloc((strlen(passengerName)+1)* sizeof(char));
                        strcpy(newPassenger->passengerName,passengerName);
                        newPassenger->priority = 1;
                        newPassenger->wantedClass = 1;
                        passenger* lastPriorPassenger;
                        passenger* NonPriorPassenger = &(flights[flightLocation].queues[1]);
                        while (NonPriorPassenger->priority == 1)
                        {
                            lastPriorPassenger = NonPriorPassenger;
                            NonPriorPassenger = NonPriorPassenger->next;
                        }
                        lastPriorPassenger->next = newPassenger;
                        newPassenger->next = NonPriorPassenger;
                    }
                }
                if (priority == 0) // normal passenger
                {
                    if (flights[flightLocation].personsInEconomyQueue == 0) // first passenger
                    {
                        flights[flightLocation].queues[1].passengerName = malloc((strlen(passengerName)+1)* sizeof(char));
                        strcpy(flights[flightLocation].queues[1].passengerName, passengerName);
                        flights[flightLocation].queues[1].priority = 0;
                        flights[flightLocation].queues[1].wantedClass = 1;
                        flights[flightLocation].queues[1].next = NULL;
                    }
                    else
                    {
                        passenger* thisPassenger = malloc(sizeof(passenger));
                        thisPassenger->passengerName = malloc(strlen((passengerName)+1) * sizeof(char));
                        strcpy(thisPassenger->passengerName,passengerName);
                        thisPassenger->priority = 0;
                        thisPassenger->wantedClass = 1;
                        thisPassenger->next = NULL;
                        passenger* endOfQueue = &(flights[flightLocation].queues[1]);
                        while(endOfQueue->next != NULL)
                        {
                            endOfQueue = endOfQueue->next;
                        }
                        endOfQueue->next = thisPassenger;
                    }
                }
                flights[flightLocation].personsInEconomyQueue++;
                printf("queue %s %s economy %d\n",flights[flightLocation].flightName,passengerName,flights[flightLocation].personsInEconomyQueue);
            }
            else if (!strcmp(command,"standard"))
            {
                command = strtok(NULL," \r");
                if (strtok(NULL," \r") != NULL) { fprintf(stdout,"error\n"); free(inputLine); continue; }
                if (flights[flightLocation].personsInStandardQueue == 0) // first passenger
                {
                    flights[flightLocation].queues[2].passengerName = malloc((strlen(command)+1)* sizeof(char));
                    strcpy(flights[flightLocation].queues[2].passengerName, command);
                    flights[flightLocation].queues[2].priority = 0;
                    flights[flightLocation].queues[2].wantedClass = 0;
                    flights[flightLocation].queues[2].next = NULL;
                }
                else
                {
                    passenger* thisPassenger = malloc(sizeof(passenger));
                    thisPassenger->passengerName = malloc((strlen(command)+1) * sizeof(char));
                    strcpy(thisPassenger->passengerName,command);
                    thisPassenger->priority = 0;
                    thisPassenger->wantedClass = 0;
                    thisPassenger->next = NULL;
                    passenger* endOfQueue = &(flights[flightLocation].queues[1]);
                    while(endOfQueue->next != NULL)
                    {
                        endOfQueue = endOfQueue->next;
                    }
                    endOfQueue->next = thisPassenger;
                }
                flights[flightLocation].personsInStandardQueue++;
                printf("queue %s %s standard %d\n",flights[flightLocation].flightName,command,flights[flightLocation].personsInStandardQueue);
            }
            else { fprintf(stdout,"error\n"); free(inputLine); continue;}
        }
        else if (!strcmp(command,"sell"))
        {
            command = strtok(NULL," \r");
            int flightLocation = -1;
            for(int i=0;i<numberOfFlights;i++) // Is this flight exists?
            {
                if (!strcmp(flights[i].flightName, command))
                {
                    flightLocation = i;
                    break;
                }
            }
            if (flightLocation == -1 || strtok(NULL," \r") != NULL || flights[flightLocation].closed == 1) { fprintf(stdout,"error\n"); free(inputLine); continue; }
            int businessTicketCount = 0;
            int economyTicketCount = 0;
            int standardTicketCount = 0;
            int standartBusinessCount = 0;
            int standartEconomyCount = 0;
            passenger* currentPassenger = &(flights[flightLocation].queues[0]);
            for(int i=0;i<strlen(flights[flightLocation].seats[0]);i++) // selling tickets to business
            {
                if (currentPassenger == NULL || flights[flightLocation].personsInBusinessQueue == 0) { break; }
                if (businessTicket[flightLocation] == 0)
                {
                    ticketSoldPassengers[flightLocation][0].passengerName = malloc((strlen(currentPassenger->passengerName)+1)*sizeof(char));
                    strcpy(ticketSoldPassengers[flightLocation][0].passengerName,currentPassenger->passengerName);
                    ticketSoldPassengers[flightLocation][0].wantedClass = 2;
                    ticketSoldPassengers[flightLocation][0].next = NULL;
                }
                else
                {
                    passenger* thisPassenger = &(ticketSoldPassengers[flightLocation][0]);
                    while(thisPassenger->next != NULL)
                    {
                        thisPassenger = thisPassenger->next;
                    }
                    passenger* newPassenger = malloc(sizeof(passenger));
                    newPassenger->passengerName = malloc((strlen(currentPassenger->passengerName)+1)* sizeof(char));
                    strcpy(newPassenger->passengerName,currentPassenger->passengerName);
                    newPassenger->wantedClass = 2;
                    newPassenger->next = NULL;
                    thisPassenger->next = newPassenger;
                }
                if (flights[flightLocation].personsInBusinessQueue != 1)
                {
                    passenger* tmp = currentPassenger->next;
                    currentPassenger->passengerName = realloc(currentPassenger->passengerName,(strlen(tmp->passengerName)+1)* sizeof(char));
                    strcpy(currentPassenger->passengerName,tmp->passengerName);
                    currentPassenger->priority = tmp->priority;
                    currentPassenger->next = tmp->next;
                    free(tmp->passengerName);
                    free(tmp);
                }
                else
                {
                    free(currentPassenger->passengerName);
                }
                flights[flightLocation].personsInBusinessQueue--;
                businessTicketCount++;
                businessTicket[flightLocation]++;
            }
            currentPassenger = &(flights[flightLocation].queues[1]);
            for(int i=0;i<strlen(flights[flightLocation].seats[1]);i++) // selling tickets to economy
            {
                if (currentPassenger == NULL || flights[flightLocation].personsInEconomyQueue == 0) { break; }
                if (economyTicket[flightLocation] == 0)
                {
                    ticketSoldPassengers[flightLocation][1].passengerName = malloc((strlen(currentPassenger->passengerName)+1)*sizeof(char));
                    strcpy(ticketSoldPassengers[flightLocation][1].passengerName,currentPassenger->passengerName);
                    ticketSoldPassengers[flightLocation][1].wantedClass = 1;
                    ticketSoldPassengers[flightLocation][1].next = NULL;
                }
                else
                {
                    passenger* thisPassenger = &(ticketSoldPassengers[flightLocation][1]);
                    while(thisPassenger->next != NULL)
                    {
                        thisPassenger = thisPassenger->next;
                    }
                    passenger* newPassenger = malloc(sizeof(passenger));
                    newPassenger->passengerName = malloc((strlen(currentPassenger->passengerName)+1)* sizeof(char));
                    strcpy(newPassenger->passengerName,currentPassenger->passengerName);
                    newPassenger->wantedClass = 1;
                    newPassenger->next = NULL;
                    thisPassenger->next = newPassenger;
                }
                if (flights[flightLocation].personsInEconomyQueue != 1)
                {
                    passenger* tmp = currentPassenger->next;
                    currentPassenger->passengerName = realloc(currentPassenger->passengerName,(strlen(tmp->passengerName)+1)* sizeof(char));
                    strcpy(currentPassenger->passengerName,tmp->passengerName);
                    currentPassenger->priority = tmp->priority;
                    currentPassenger->next = tmp->next;
                    free(tmp->passengerName);
                    free(tmp);
                }
                else
                {
                    free(currentPassenger->passengerName);
                }
                flights[flightLocation].personsInEconomyQueue--;
                economyTicketCount++;
                economyTicket[flightLocation]++;
            }
            currentPassenger = &(flights[flightLocation].queues[2]);
            for(int i=0;i<strlen(flights[flightLocation].seats[2]);i++) // selling tickets to standard
            {
                if (currentPassenger == NULL || flights[flightLocation].personsInStandardQueue == 0) { break; }
                if (standardTicket[flightLocation] == 0)
                {
                    ticketSoldPassengers[flightLocation][2].passengerName = malloc((strlen(currentPassenger->passengerName)+1)*sizeof(char));
                    strcpy(ticketSoldPassengers[flightLocation][2].passengerName,currentPassenger->passengerName);
                    ticketSoldPassengers[flightLocation][2].wantedClass = 0;
                    ticketSoldPassengers[flightLocation][2].next = NULL;
                }
                else
                {
                    passenger* thisPassenger = &(ticketSoldPassengers[flightLocation][2]);
                    while(thisPassenger->next != NULL)
                    {
                        thisPassenger = thisPassenger->next;
                    }
                    passenger* newPassenger = malloc(sizeof(passenger));
                    newPassenger->passengerName = malloc((strlen(currentPassenger->passengerName)+1)* sizeof(char));
                    strcpy(newPassenger->passengerName,currentPassenger->passengerName);
                    newPassenger->wantedClass = 0;
                    newPassenger->next = NULL;
                    thisPassenger->next = newPassenger;
                }
                if (flights[flightLocation].personsInStandardQueue != 1)
                {
                    passenger* tmp = currentPassenger->next;
                    currentPassenger->passengerName = realloc(currentPassenger->passengerName,(strlen(tmp->passengerName)+1)* sizeof(char));
                    strcpy(currentPassenger->passengerName,tmp->passengerName);
                    currentPassenger->priority = tmp->priority;
                    currentPassenger->next = tmp->next;
                    free(tmp->passengerName);
                    free(tmp);
                }
                else
                {
                    free(currentPassenger->passengerName);
                }
                flights[flightLocation].personsInStandardQueue--;
                standardTicketCount++;
                standardTicket[flightLocation]++;
            }
            flights[flightLocation].seats[2][strlen(flights[flightLocation].seats[2])-standardTicketCount] = '\0';
            currentPassenger = &(flights[flightLocation].queues[0]);
            for(int i=0;i<strlen(flights[flightLocation].seats[2]);i++) // selling tickets to standard for business passenger
            {
                if (currentPassenger == NULL || flights[flightLocation].personsInBusinessQueue == 0) { break; }
                if (standardTicket[flightLocation] == 0)
                {
                    ticketSoldPassengers[flightLocation][2].passengerName = malloc((strlen(currentPassenger->passengerName)+1)*sizeof(char));
                    strcpy(ticketSoldPassengers[flightLocation][2].passengerName,currentPassenger->passengerName);
                    ticketSoldPassengers[flightLocation][2].wantedClass = 2;
                    ticketSoldPassengers[flightLocation][2].next = NULL;
                }
                else
                {
                    passenger* thisPassenger = &(ticketSoldPassengers[flightLocation][2]);
                    while(thisPassenger->next != NULL)
                    {
                        thisPassenger = thisPassenger->next;
                    }
                    passenger* newPassenger = malloc(sizeof(passenger));
                    newPassenger->passengerName = malloc((strlen(currentPassenger->passengerName)+1)* sizeof(char));
                    strcpy(newPassenger->passengerName,currentPassenger->passengerName);
                    newPassenger->wantedClass = 2;
                    newPassenger->next = NULL;
                    thisPassenger->next = newPassenger;
                }
                if (flights[flightLocation].personsInBusinessQueue != 1)
                {
                    passenger* tmp = currentPassenger->next;
                    currentPassenger->passengerName = realloc(currentPassenger->passengerName,(strlen(tmp->passengerName)+1)* sizeof(char));
                    strcpy(currentPassenger->passengerName,tmp->passengerName);
                    currentPassenger->priority = tmp->priority;
                    currentPassenger->next = tmp->next;
                    free(tmp->passengerName);
                    free(tmp);
                }
                else
                {
                    free(currentPassenger->passengerName);
                }
                flights[flightLocation].personsInBusinessQueue--;
                standardTicketCount++;
                standardTicket[flightLocation]++;
                standartBusinessCount++;
                standartBusinessTicket[flightLocation]++;
            }
            flights[flightLocation].seats[0][strlen(flights[flightLocation].seats[0])-businessTicketCount] = '\0';
            flights[flightLocation].seats[2][strlen(flights[flightLocation].seats[2])-standartBusinessCount] = '\0';
            currentPassenger = &(flights[flightLocation].queues[1]);
            for(int i=0;i<strlen(flights[flightLocation].seats[2]);i++) // selling tickets to standard for business passengers
            {
                if (currentPassenger == NULL || flights[flightLocation].personsInEconomyQueue == 0) { break; }
                if (standardTicket[flightLocation] == 0)
                {
                    ticketSoldPassengers[flightLocation][2].passengerName = malloc((strlen(currentPassenger->passengerName)+1)*sizeof(char));
                    strcpy(ticketSoldPassengers[flightLocation][2].passengerName,currentPassenger->passengerName);
                    ticketSoldPassengers[flightLocation][2].wantedClass = 1;
                    ticketSoldPassengers[flightLocation][2].next = NULL;
                }
                else
                {
                    passenger* thisPassenger = &(ticketSoldPassengers[flightLocation][2]);
                    while(thisPassenger->next != NULL)
                    {
                        thisPassenger = thisPassenger->next;
                    }
                    passenger* newPassenger = malloc(sizeof(passenger));
                    newPassenger->passengerName = malloc((strlen(currentPassenger->passengerName)+1)* sizeof(char));
                    strcpy(newPassenger->passengerName,currentPassenger->passengerName);
                    newPassenger->wantedClass = 1;
                    newPassenger->next = NULL;
                    thisPassenger->next = newPassenger;
                }
                if (flights[flightLocation].personsInEconomyQueue != 1)
                {
                    passenger* tmp = currentPassenger->next;
                    currentPassenger->passengerName = realloc(currentPassenger->passengerName,(strlen(tmp->passengerName)+1)* sizeof(char));
                    strcpy(currentPassenger->passengerName,tmp->passengerName);
                    currentPassenger->priority = tmp->priority;
                    currentPassenger->next = tmp->next;
                    free(tmp->passengerName);
                    free(tmp);
                }
                else
                {
                    free(currentPassenger->passengerName);
                }
                flights[flightLocation].personsInEconomyQueue--;
                standardTicketCount++;
                standardTicket[flightLocation]++;
                standartEconomyCount++;
                standartEconomyTicket[flightLocation]++;
            }
            flights[flightLocation].seats[1][strlen(flights[flightLocation].seats[1])-economyTicketCount] = '\0';
            flights[flightLocation].seats[2][strlen(flights[flightLocation].seats[2])-standartEconomyCount] = '\0';
            flights[flightLocation].soldBusinessTicket += businessTicketCount;
            flights[flightLocation].soldEconomyTicket += economyTicketCount;
            flights[flightLocation].soldStandardTicket += standardTicketCount;
            printf("sold %s %d %d %d\n",flights[flightLocation].flightName,flights[flightLocation].soldBusinessTicket,flights[flightLocation].soldEconomyTicket,flights[flightLocation].soldStandardTicket);
        }
        else if (!strcmp(command,"close"))
        {
            command = strtok(NULL," \r");
            int flightLocation = -1;
            for(int i=0;i<numberOfFlights;i++) // Is this flight exists?
            {
                if (!strcmp(flights[i].flightName, command))
                {
                    flightLocation = i;
                    break;
                }
            }
            if (flightLocation == -1 || flights[flightLocation].closed == 1 || strtok(NULL," \r") != NULL) { fprintf(stdout,"error\n"); free(inputLine); continue; }
            flights[flightLocation].closed = 1; // flight closed
            int totalTickets = flights[flightLocation].soldBusinessTicket + flights[flightLocation].soldEconomyTicket + flights[flightLocation].soldStandardTicket;
            int totalWaitingPassengers = flights[flightLocation].personsInBusinessQueue + flights[flightLocation].personsInEconomyQueue + flights[flightLocation].personsInStandardQueue;
            printf("closed %s %d %d\n",flights[flightLocation].flightName,totalTickets,totalWaitingPassengers);
            passenger* waitingPassenger = &(flights[flightLocation].queues[0]);
            for(int i=0;i<flights[flightLocation].personsInBusinessQueue;i++) // printing waiting passengers
            {
                printf("waiting %s\n",waitingPassenger->passengerName);
                waitingPassenger = waitingPassenger->next;
            }
            waitingPassenger = &(flights[flightLocation].queues[1]);
            for(int i=0;i<flights[flightLocation].personsInEconomyQueue;i++)
            {
                printf("waiting %s\n",waitingPassenger->passengerName);
                waitingPassenger = waitingPassenger->next;
            }
            waitingPassenger = &(flights[flightLocation].queues[2]);
            for(int i=0;i<flights[flightLocation].personsInStandardQueue;i++)
            {
                printf("waiting %s\n",waitingPassenger->passengerName);
                waitingPassenger = waitingPassenger->next;
            }
        }
        else if (!strcmp(command,"report"))
        {
            command = strtok(NULL," \r");
            int flightLocation = -1;
            for(int i=0;i<numberOfFlights;i++) // Is this flight exists?
            {
                if (!strcmp(flights[i].flightName, command))
                {
                    flightLocation = i;
                    break;
                }
            }
            if (flightLocation == -1 || strtok(NULL," \r") != NULL) { fprintf(stdout,"error\n"); free(inputLine); continue; }
            printf("report %s\n",flights[flightLocation].flightName); // printing report
            printf("business %d\n",flights[flightLocation].soldBusinessTicket);
            if (flights[flightLocation].soldBusinessTicket != 0)
            {
                passenger* ptrP = &(ticketSoldPassengers[flightLocation][0]);
                while(ptrP != NULL)
                {
                    fprintf(stdout,"%s\n",ptrP->passengerName);
                    ptrP = ptrP->next;
                }
            }
            printf("economy %d\n",flights[flightLocation].soldEconomyTicket);
            if (flights[flightLocation].soldEconomyTicket != 0)
            {
                passenger* ptrP = &(ticketSoldPassengers[flightLocation][1]);
                while(ptrP != NULL)
                {
                    fprintf(stdout,"%s\n",ptrP->passengerName);
                    ptrP = ptrP->next;
                }
            }
            printf("standard %d\n",flights[flightLocation].soldStandardTicket);
            if (flights[flightLocation].soldStandardTicket != 0)
            {
                passenger* ptrP = &(ticketSoldPassengers[flightLocation][2]);
                while(ptrP != NULL)
                {
                    fprintf(stdout,"%s\n",ptrP->passengerName);
                    ptrP = ptrP->next;
                }
            }
            printf("end of report %s\n",flights[flightLocation].flightName);
        }
        else if (!strcmp(command,"info"))
        {
            command = strtok(NULL," \r");
            if (strtok(NULL," \r") != NULL) { fprintf(stdout,"error\n"); free(inputLine); continue; }
            passenger* thePassenger;
            int flightLocation, class, found = 0;
            for(int i=0;i<numberOfFlights;i++) // searching
            {
                for(int j=0;j<3;j++)
                {
                    passenger * ptr = &(flights[i].queues[j]);
                    while (ptr != NULL && found != 1) // searching in queues
                    {
                        if((j == 0 && flights[i].personsInBusinessQueue == 0) || (j == 1 && flights[i].personsInEconomyQueue == 0) || (j == 2 && flights[i].personsInStandardQueue == 0))
                        { break; }
                        if (!strcmp(ptr->passengerName,command))
                        { thePassenger = ptr; found = 1; class = 3; flightLocation = i; break; }
                        if (found == 1) { break; }
                        ptr = ptr->next;
                    }
                    ptr = &(ticketSoldPassengers[i][j]);
                    while (ptr != NULL && found != 1) // searching in tickets
                    {
                        if((j == 0 && flights[i].soldBusinessTicket == 0) || (j == 1 && flights[i].soldEconomyTicket == 0) || (j == 2 && flights[i].soldStandardTicket == 0))
                        { break; }
                        if (!strcmp(ptr->passengerName, command))
                        { thePassenger = ptr; found = 1; class = j; flightLocation = i; break; }
                        if (found == 1) { break; }
                        ptr = ptr->next;
                    }
                }
                if(found == 1) {break;}
            }
            if (found == 1) // passenger found. printing info
            {
                char* wantedClass;
                char* soldClass;
                if (thePassenger->wantedClass == 0) { wantedClass = "standard"; }
                else if (thePassenger->wantedClass == 1) { wantedClass = "economy"; }
                else if (thePassenger->wantedClass == 2) { wantedClass = "business"; }
                else { fprintf(stdout,"error\n");}
                if (class == 0) { soldClass = "business"; }
                else if (class == 1) { soldClass = "economy"; }
                else if (class == 2) { soldClass = "standard"; }
                else { soldClass = "none"; }
                printf("info %s %s %s %s\n", thePassenger->passengerName, flights[flightLocation].flightName, wantedClass, soldClass);
            }
            else { fprintf(stdout,"error\n"); free(inputLine); continue; }
        }
        else {fprintf(stdout,"error\n");} free(inputLine);
    }

    // free'ing variables
    fclose(inputFile);
    free(businessTicket);
    free(economyTicket);
    free(standardTicket);
    free(standartBusinessTicket);
    free(standartEconomyTicket);
    for(int i=0;i<numberOfFlights;i++)
    {
        free(numberOfSeats[i]);
    }
    free(numberOfSeats);
    for(int i=0;i<numberOfFlights;i++)
    {
        for(int j=0;j<3;j++)
        {
            freePassengerInfo(&(ticketSoldPassengers[i][j]));
        }
        free(ticketSoldPassengers[i]);
    }
    free(ticketSoldPassengers);

    for(int i=0;i<numberOfFlights;i++)
    {
        //freeFlightInfo(&(flights[i]));
    }
    free(flights);
    fclose(stdout);
}

void freeFlightInfo(flight* theFlight) // function for free'ing flight data
{
    free(theFlight->flightName);
    for(int i=0;i<3;i++)
    {
        free(theFlight->seats[i]);
        freePassengerInfo(&(theFlight->queues[i]));
    }
    free(theFlight->seats);
    free(theFlight->queues);
}

void freePassengerInfo(passenger* thePassenger) // function for free'ing passenger data
{
    if (thePassenger == NULL)
    {
        return;
    }
    free(thePassenger->passengerName);
    freePassengerInfo(thePassenger->next);
    free(thePassenger->next);
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

void addseat(char** string, int number) // function for adding seats
{
    for (int i=0;i<number;i++)
    {
        *string = realloc(*string, (strlen(*string)+2) * sizeof(char));
        strcat(*string,"s");
    }
}