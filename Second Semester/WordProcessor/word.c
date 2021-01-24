#include <stdio.h>
#include <stdlib.h>
#include <string.h>


typedef struct file_
{
    char *filename;
    char *text;
    int filenumber;
}file;

void Grow(file ** growingfiles,int NumberOfFiles);
void Create(char ** Lines, file * files, int NumberOfFiles);
void Append(char ** Lines, file * files, int NumberOfFiles);
void Print(char ** Lines, file * files, int NumberOfFiles);
void Replace(char ** Lines, file * files, int NumberOfFiles);
void Remove(char ** Lines, file * files, int NumberOfFiles);
void Delete(char ** Lines, file * files, int NumberOfFiles);


int main(int argc, char *argv[]) {
    if (argc < 2) {
        printf("One argument excepted.\n");
        exit(EXIT_SUCCESS);
    } else if (argc > 2) {
        printf("Too many arguments\n");
        exit(EXIT_SUCCESS);
    }
    if (!fopen(argv[1], "r+")) {
        printf("Can't opened file.\n");
        exit(EXIT_SUCCESS);
    }
    FILE *Commands;
    Commands = fopen(argv[1], "r"); // opened file for reading
    int totalLine = 0;
    int maxLineLength = 0;
    int i = 0;
    while (!feof(Commands)) {
        ++i;
        char ch;
        ch = fgetc(Commands);
        if (ch == '\n') {
            totalLine += 1;
            if (i > maxLineLength) {
                maxLineLength = i;
            }
            i = 0;
        }
    }
    char CommandsArr[totalLine][maxLineLength];
    char *commandLine = malloc(10 * sizeof(char));
    int line = 0;
    i = 0;
    rewind(Commands);
    while (1 > 0) {
        char ch;
        ch = fgetc(Commands);
        if (ch == '\n' || ch == EOF) {
            *(commandLine + i) = '\0';
            i = 0;
            strcpy(CommandsArr[line], commandLine);
            free(commandLine);
            commandLine = malloc(10 * sizeof(char));
            line += 1;
            if (ch == EOF) { break; }
            else { continue; }
        }
        if (i % 10 == 0 && i != 0) {
            commandLine = realloc(commandLine, (i + 10) * sizeof(char));
        }
        *(commandLine + i) = ch;
        i++;
    }
    free(commandLine);
    fclose(Commands);
    char ***Lines = (char ***) malloc(totalLine * sizeof(char ***));
    for (int i = 0; i < totalLine; i++) {
        Lines[i] = (char **) malloc(4 * sizeof(char **));
        int k = 0;
        for (int j = 0; j < 4; j++) {
            int l = 0;
            char *TheWord = malloc(11 * sizeof(char *));
            char ch = CommandsArr[i][k];
            while (ch != '-' && ch != '\0') {
                if (l % 10 == 0 && l != 0) {
                    TheWord = realloc(TheWord, (l + 20) * sizeof(char *));
                }
                TheWord[l] = ch;
                k++;
                l++;
                ch = CommandsArr[i][k];
            }
            if (TheWord[l - 1] == ' ') {
                TheWord = realloc(TheWord,(l)*sizeof(char));
                TheWord[l - 1] = '\0';
            } else {
                TheWord[l] = '\0';
            }
            k++;
            int TheWordLen = strlen(TheWord)+1;
            Lines[i][j] = malloc(TheWordLen * sizeof(char));
            strcpy(Lines[i][j], TheWord);
            if (ch == '\0') { break; }
        }
    }
    file *files = (file *) malloc(1 * sizeof(file));
    int NumberOfFiles = 0;
    for (int i = 0; i < totalLine; i++)
    {
        if (strcmp(Lines[i][0], "create") == 0) {
            NumberOfFiles += 1;
            Grow(&files,NumberOfFiles);
            Create(Lines[i], files, NumberOfFiles);
        } else if (strcmp(Lines[i][0], "append") == 0) {
            Append(Lines[i], files, NumberOfFiles);
        } else if (strcmp(Lines[i][0], "print") == 0) {
            Print(Lines[i], files, NumberOfFiles);
        } else if (strcmp(Lines[i][0], "replace") == 0) {
            Replace(Lines[i], files, NumberOfFiles);
        } else if (strcmp(Lines[i][0], "remove") == 0) {
            Remove(Lines[i], files, NumberOfFiles);
        } else if (strcmp(Lines[i][0], "delete") == 0) {
            Delete(Lines[i], files, NumberOfFiles);
            NumberOfFiles -= 1;
            Grow(&files,NumberOfFiles);
        } else {
            printf("Invalid command!\n");
        }
    }
    free(Lines);
}

void Grow(file ** newfiles,int NumberOfFiles)
{
    file *temp = realloc(*newfiles, NumberOfFiles*sizeof(file));
    *newfiles = temp;
}

void Shrink(file ** newfiles,int NumberOfFiles)
{
    file *temp = realloc(*newfiles, NumberOfFiles*sizeof(file));
    *newfiles = temp;
}

void Create(char ** Line,file * files, int NumberOfFiles)
{
    printf("%s -%s -%s\n",Line[0],Line[1],Line[2]);
    if (Line[1][0] == 'n')
    {
        files[NumberOfFiles-1].filename = malloc(strlen(Line[1])*sizeof(char));
        files[NumberOfFiles-1].text = malloc(strlen(Line[2])*sizeof(char));
        for (int i=0;i<strlen(&Line[1][0]);i++)
        {
            files[NumberOfFiles-1].filename[i] = Line[1][i+2];
            if (Line[1][i+2] == '\0'){break;}
        }
        if (strlen(&Line[2][0]) > 1)
        {
            for (int i=0;i<strlen(&Line[2][0]);i++)
            {
                files[NumberOfFiles-1].text[i] = Line[2][i+2];
                if (Line[2][i+2] == '\0'){break;}
            }
        }
        else
        {
            files[NumberOfFiles-1].text[0] = '\0';
        }
    }
    else if (Line[1][0] == 't')
    {
        files[NumberOfFiles-1].text = malloc(strlen(Line[1])*sizeof(char));
        files[NumberOfFiles-1].filename = malloc(strlen(Line[2])*sizeof(char));
        if (strlen(&Line[1][0]) > 2)
        {
            for (int i=0;i<strlen(&Line[1][0]);i++)
            {
                files[NumberOfFiles-1].text[i] = Line[1][i+2];
                if (Line[1][i+2] == '\0'){break;}
            }
        }
        else
        {
            files[NumberOfFiles-1].text[0] = '\0';
        }
        for (int i=0;i<strlen(&Line[2][0]);i++)
        {
            files[NumberOfFiles-1].filename[i] = Line[2][i+2];
            if (Line[2][i+2] == '\0'){break;}
        }
    }
    if (NumberOfFiles == 1)
    {
        files[NumberOfFiles-1].filenumber = 1;
    }
    else
    {
        files[NumberOfFiles-1].filenumber = files[NumberOfFiles-2].filenumber +1;
    }
}

void Append(char ** Line,file * files,int NumberOfFiles)
{
    printf("%s -%s -%s\n",Line[0],Line[1],Line[2]);
    char *filename = malloc(20*sizeof(char));
    char *newWords = malloc(200*sizeof(char));
    if (Line[1][0] == 'n')
    {
        for (int i=0;i<strlen(&Line[1][0]);i++)
        {
            filename[i] = Line[1][i+2];
            if (Line[1][i+2] == '\0'){break;}
        }
        for (int i=0;i<strlen(&Line[2][0]);i++)
        {
            newWords[i] = Line[2][i+2];
            if (Line[2][i+2] == '\0'){break;}
        }
    }
    else if (Line[1][0] == 't')
    {
        for (int i=0;i<strlen(&Line[1][0]);i++)
        {
            newWords[i] = Line[1][i+2];
            if (Line[1][i+2] == '\0'){break;}
        }
        for (int i=0;i<strlen(&Line[2][0]);i++)
        {
            filename[i] = Line[2][i+2];
            if (Line[2][i+2] == '\0'){break;}
        }
    }
    int i = 0;
    for (i=0;i<NumberOfFiles;i++)
    {
        if (strcmp(files[i].filename,filename) == 0)
        {
            break;
        }
    }
    files[i].text = realloc(files[i].text,(strlen(files[i].text)+strlen(newWords))*sizeof(char));
    int oldLen = strlen(files[i].text);
    int j;
    for (j=oldLen;j<oldLen+strlen(newWords)+1;j++)
    {
        files[i].text[j] = newWords[j-oldLen];
    }
    files[i].text[j+1] = '\0';
}

void Print(char ** Line, file * files, int NumberOfFiles)
{
    if (Line[1][0] == 'a')
    {
        printf("%s -%s\n",Line[0],Line[1]);
        for (int i=0;i<NumberOfFiles;i++)
        {
            printf("Filename %d:  %s\n",files[i].filenumber,files[i].filename);
        }
    }
    else if (Line[1][0] == 'c')
    {
        printf("%s -%s\n",Line[0],Line[1]);
        for (int i=0;i<NumberOfFiles;i++)
        {
            printf("Num:  %d\nName:  ",files[i].filenumber);
            for (int j=0;j<strlen(files[i].filename);j++)
            {
                if (files[i].filename[j] != '.')
                {
                    printf("%c",files[i].filename[j]);
                }
                else
                {
                    printf("\n");
                    break;
                }
            }
            if (files[i].text[0] != '\0')
            {
                printf("Text:  %s\n",files[i].text);
            }
            else
            {
                printf("Text:  Empty File\n");
            }
        }
    }
    else if (Line[1][0] == 'e')
    {
        printf("%s -%s\n",Line[0],Line[1]);
        char *type = malloc(5*sizeof(char));
        for (int i=0;i<strlen(&Line[1][0]);i++)
        {
            type[i] = Line[1][i+2];
            if (Line[1][i+2] == '\0'){break;}
        }
        int TypeLen = strlen(type);
        for (int i=0;i<NumberOfFiles;i++)
        {
            int Boolean = 1;
            for (int j=0;j<TypeLen;j++)
            {
                if (files[i].filename[strlen(files[i].filename)-TypeLen+j] != type[j])
                {
                    Boolean = 0;
                    break;
                }
            }
            if (Boolean == 1)
            {
                printf("Filename %d:  ",files[i].filenumber);
                for (int j=0;j<strlen(files[i].filename);j++)
                {
                    if (files[i].filename[j] != '.')
                    {
                        printf("%c",files[i].filename[j]);
                    }
                    else
                    {
                        printf("\n");
                        break;
                    }
                }
                if (files[i].text[0] != '\0')
                {
                    printf("Text:  %s\n",files[i].text);
                }
                else
                {
                    printf("Text:  Empty File\n");
                }
            }
        }

    }
    else if (Line[1][0] == 'n')
    {
        char *filename = malloc(20* sizeof(char));
        for (int i=0;i<strlen(&Line[1][0]);i++)
        {
            filename[i] = Line[1][i+2];
            if (Line[1][i+2] == '\0'){break;}
        }
        int i = 0;
        for (i=0;i<NumberOfFiles;i++)
        {
            if (strcmp(files[i].filename,filename) == 0)
            {
                break;
            }
        }
        if (Line[2][0] == 't')
        {
            printf("%s -%s -%s\n",Line[0],Line[1],Line[2]);
            if (files[i].text[0] == '\0')
            {
                printf("Text:  Empty File\n");
            }
            else
            {
                printf("Text:  %s\n",files[i].text);
            }
        }
        else if (Line[2][0] == 'c' && Line[2][1] == 'w')
        {
            printf("%s -%s -%s\n",Line[0],Line[1],Line[2]);
            char *word = malloc(20* sizeof(char));
            for (int j=3;j<strlen(&Line[2][0])+1;j++)
            {
                word[j-3] = Line[2][j];
            }
            int NumOfOccur = 0;
            for (int j=0;j<(strlen(files[i].text)-strlen(word));j++)
            {
                if (files[i].text[j] == word[0])
                {
                    int Boolean = 1;
                    for (int k=1;k<strlen(word);k++)
                    {
                        if (files[i].text[j+k] != word[k])
                        {
                            Boolean = 0;
                        }
                    }
                    if (Boolean == 1)
                    {
                        NumOfOccur += 1;
                    }
                }
            }
            printf("Text:  %s\n",files[i].text);
            printf("Number Of Occurrence of \"%s\" : %d\n",word,NumOfOccur);
        }
        else if (Line[2][0] == 'c' && Line[2][1] == 's')
        {
            printf("%s -%s -%s\n",Line[0],Line[1],Line[2]);
            int NumOfSentences = 0;
            for (int j=0;j<strlen(files[i].text);j++)
            {
                if (files[i].text[j] == '.' || files[i].text[j] == '!' || files[i].text[j] == '?')
                {
                    NumOfSentences += 1;
                }
            }
            printf("Number Of Sentences :  %d\n",NumOfSentences);
        }
        free(filename);
    }
}


void Replace(char ** Lines,file * files, int NumberOfFiles)
{
    printf("%s -%s -%s -%s\n",Lines[0],Lines[1],Lines[2],Lines[3]);
    if (Lines[1][1] == 'w')
    {
        if (Lines[2][0] == 'n' && Lines[2][1] == ' ')
        {
            char *temp = malloc(20*sizeof(char));
            strcpy(temp,Lines[1]);
            strcpy(Lines[1],Lines[2]);
            strcpy(Lines[2],temp);
            free(temp);
        }
        else if (Lines[3][0] == 'n' && Lines[3][1] == ' ')
        {
            char *temp = malloc(20*sizeof(char));
            strcpy(temp,Lines[1]);
            strcpy(Lines[1],Lines[3]);
            strcpy(Lines[3],temp);
            free(temp);
        }
    }
    if (Lines[2][0] != 'o')
    {
        char *temp = malloc(20*sizeof(char));
        strcpy(temp,Lines[2]);
        strcpy(Lines[2],Lines[3]);
        strcpy(Lines[3],temp);
        free(temp);
    }
    char *filename = malloc(20* sizeof(char));
    for (int i=0;i<strlen(&Lines[1][0]);i++)
    {
        filename[i] = Lines[1][i+2];
        if (Lines[1][i+2] == '\0'){break;}
    }
    int i = 0;
    for (i=0;i<NumberOfFiles;i++)
    {
        if (strcmp(files[i].filename,filename) == 0)
        {
            break;
        }
    }
    free(filename);
    char *oldWord = malloc(20*sizeof(char));
    for (int j=0;j<strlen(&Lines[2][0]);j++)
    {
        oldWord[j] = Lines[2][j+3];
        if (Lines[2][j+3] == '\0'){break;}
    }
    int oldWordLen = strlen(oldWord);
    char *newWord = malloc(20*sizeof(char));
    for (int j=0;j<strlen(&Lines[3][0]);j++)
    {
        newWord[j] = Lines[3][j+3];
        if (Lines[3][j+3] == '\0'){break;}
    }
    int newWordLen = strlen(newWord);
    //files[i].text = realloc(files[i].text,(strlen(files[i].text) + (newWordLen-oldWordLen))*sizeof(char));
    char * temp = realloc(files[i].text,(strlen(files[i].text) + (newWordLen-oldWordLen))*sizeof(char));
    files[i].text = temp;
    char * aWord = malloc(20*sizeof(char));
    for (int j=0;j<strlen(files[i].text);j++)
    {
        if (files[i].text[j] == oldWord[0])
        {
            int Boolean = 1;
            for (int l=0;l<strlen(oldWord);l++)
            {
                if (files[i].text[j+l] != oldWord[l])
                {
                    Boolean = 0;
                }
            }
            if (Boolean == 1)
            {
                if (oldWordLen == newWordLen)
                {
                    for (int k=0;k<newWordLen;k++)
                    {
                        files[i].text[j+k] = newWord[k];
                    }
                }
                else
                {
                    char *temp = malloc(strlen(files[i].text) * sizeof(char));
                    strcpy(temp,files[i].text);
                    //char * temp = realloc(files[i].text,s)
                    for (int k=0;k<newWordLen;k++)
                    {
                        files[i].text[j+k] = newWord[k];
                    }
                    for (int k=j;k<strlen(temp);k++)
                    {
                        files[i].text[k+newWordLen] = temp[k+oldWordLen];
                    }
                }
            }
        }
    }
}


void Remove(char ** Lines, file * files, int NumberOfFiles)
{
    printf("%s -%s -%s -%s\n",Lines[0], Lines[1], Lines[2], Lines[3]);
    char *filename = malloc(20* sizeof(char));
    for (int i=0;i<strlen(&Lines[1][0]);i++)
    {
        filename[i] = Lines[1][i+2];
        if (Lines[1][i+2] == '\0'){break;}
    }
    int i = 0;
    for (i=0;i<NumberOfFiles;i++)
    {
        if (strcmp(files[i].filename,filename) == 0)
        {
            break;
        }
    }
    free(filename);
    char *Start = malloc(4*sizeof(char));
    int j;
    for (j=0;j<strlen(Lines[2]);j++)
    {
        Start[j] = Lines[2][j+2];
    }
    char *Length = malloc(4*sizeof(char));
    for (j=0;j<strlen(Lines[3]);j++)
    {
        Length[j] = Lines[3][j+2];
    }
    int start=0,length=0;
    for (j=0;j<4;j++)
    {
        start *= 10;
        start += Start[j]-48;
        if (Start[j+1] == '\0'){break;}
    }
    for (j=0;j<4;j++)
    {
        length *= 10;
        length += Length[j]-48;
        if (Length[j+1] == '\0'){break;}
    }
    free(Start);
    free(Length);
    for (j=start;j<(strlen(files[i].text)-length);j++)
    {
        files[i].text[j] = files[i].text[j+length];
    }
    files[i].text[j] = '\0';
    files[i].text = realloc(files[i].text,(strlen(files[i].text)-length)*sizeof(char));
}


void Delete(char ** Lines, file * files, int NumberOfFiles)
{
    printf("%s -%s\n",Lines[0],Lines[1]);
    char *filename = malloc(20* sizeof(char));
    for (int i=0;i<strlen(&Lines[1][0]);i++)
    {
        filename[i] = Lines[1][i+2];
        if (Lines[1][i+2] == '\0'){break;}
    }
    int i = 0;
    for (i=0;i<NumberOfFiles;i++)
    {
        if (strcmp(files[i].filename,filename) == 0)
        {
            break;
        }
    }
    free(filename);
    for (int j=i;j<NumberOfFiles-1;j++)
    {
        //printf("Old : %s. New : %s. Next : %s\n",files[j].filename,files[j+1].filename,files[j+2].filename);
        //printf("OldLen : %d. NewLen : %d\n",strlen(files[j].text),strlen(files[j+1].text));
        /*char * temp = realloc(files[j].filename,strlen(files[j+1].filename)*sizeof(char));
        files[j].filename = temp;
        strcpy(files[j].filename,files[j+1].filename);
        free(temp);
        temp = realloc(files[j].text,strlen(files[j+1].text)*sizeof(char));
        files[j].text = temp;
        strcpy(files[j].text,files[j+1].text);
        files[j].filenumber = files[j+1].filenumber;
        free(temp);*/
        /*printf("%d:%d-%d\n",j,strlen(files[j+1].filename),strlen(files[j+1].text));
        file temp;

        int nameLen = strlen(files[j+1].filename);
        temp.filename = realloc(files[j].filename,nameLen*sizeof(char));
        strcpy(temp.filename,files[j+1].filename);

        int textLen = strlen(files[j+1].text);
        temp.text = realloc(files[j].text,textLen*sizeof(char));
        strcpy(temp.text,files[j+1].text);

        printf("%s %d\n", temp.filename,textLen);
        temp.filenumber = files[j+1].filenumber;
        files[j],temp;
        printf("%s %d\n", files[j].filename,textLen);
        free(temp.text);
        free(temp.filename);*/
        files[j] = files[j+1];
    }

}

//İbrahim Burak Tanrıkulu