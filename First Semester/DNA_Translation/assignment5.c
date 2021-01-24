#include <stdio.h>
#include <stdlib.h>
#include <string.h>

char translation(char codon[4]); 		// Prototypes of functions
int mystrcmp(char first[20],char second[20]);

int main(int argc, char *argv[])
{
    if (argc < 2)
    {
        printf("One argument excepted.\n");
        exit(EXIT_SUCCESS);
    }
    else if (argc > 2)
    {
        printf("Too many arguments\n");
        exit(EXIT_SUCCESS);
    }
	FILE *mRNA;
    if (!fopen(argv[1],"r")) // opened file for reading
    {
        printf("Can't opened file.\n");
        exit(EXIT_SUCCESS);
    }
    mRNA = fopen(argv[1],"r");
    char mRNA_chain[31];
    int i=0;
    while (!feof(mRNA))
    {
        mRNA_chain[i] = fgetc(mRNA);
        i+=1;
    }
    mRNA_chain[30]='\0';
    char protein[20]; // amino acids sequence of input file
    int i = 0;
    for (i<10;i++)
    {
        char codon[4]; // codon
        int j = 0;
        for (j<3;j++) // 3 nucleotides equals to 1 codon
        {
            char nucleotide = mRNA_chain[(i*3)+j];
            codon[j] = nucleotide;
        }
        char aminoAcid[2]= {translation(codon),'\0'};
        if (aminoAcid[0]=='M') // start codon
        {
            protein[0]='M';
        }
        else
        {
            protein[2*i]=aminoAcid[0];
            protein[2*i-1]='-';
        }
    }
	if (protein[0]=='M' && protein[18]=='X') // Is there start and stop codons in input?
	{
	    // defining my proteins
	    // if you want define new protein here, you can use translation function.
        char MyProteinA[20] = "M-V-A-E-G-T-K-R-I-X";//AUGGUGGCGGAGGGGACGAAGAGGAUCUAA
        char MyProteinB[20] = "M-G-E-A-V-R-K-T-I-X";//AUGGGAGAAGCAGUAAGAAAAACAAUAUAG
        char MyProteinC[20] = "M-F-S-Y-C-L-P-Q-R-X";//AUGUUUUCCUAUUGCCUGCCACAACGCUGA
        char MyProteinD[20] = "M-F-L-V-P-T-Y-D-H-X";//AUGUUCUUGGUCCCUACUUACGAUCAUUAA
        char MyProteinE[20] = "M-F-S-Y-C-L-P-K-R-X";//AUGUUUUCCUAUUGCCUGCCAAAACGCUGA
        // comparing my proteins and input protein
        if (mystrcmp(protein,MyProteinA))
        {
            MyProteinA[17] = '\0';
            printf("MyProteinA is identified in sequence.\n"
                   "The amino acids of MyProteinA: %s\n",MyProteinA);
        }
        else if (mystrcmp(protein,MyProteinB))
        {
            MyProteinB[17] = '\0';
            printf("MyProteinB is identified in sequence.\n"
                   "The amino acids of MyProteinB: %s\n",MyProteinB);
        }
        else if (mystrcmp(protein,MyProteinC))
        {
            MyProteinC[17] = '\0';
            printf("MyProteinC is identified in sequence.\n"
                   "The amino acids of MyProteinC: %s\n",MyProteinC);
        }
        else if (mystrcmp(protein,MyProteinD))
        {
            MyProteinD[17] = '\0';
            printf("MyProteinD is identified in sequence.\n"
                   "The amino acids of MyProteinD: %s\n",MyProteinD);
        }
        else if (mystrcmp(protein,MyProteinE))
        {
            MyProteinE[17] = '\0';
            printf("MyProteinE is identified in sequence.\n"
                   "The amino acids of MyProteinE: %s\n",MyProteinE);
        }
        else // input protein is not my protein. probably new protein.
        {
            printf("It is not a known protein.\n"
                   "It is probably a new protein.\n");
        }
    }
    else // input has not got start or stop codon. so this is not a protein.
    {
        printf("It is not a known protein.\nIt is not a protein.\n");
    }
    return 0;
}

char translation(char codon[4]) // codon to amino acid
{
    char aminoacid;
    // U is Uracil nucleotide ( in DNA it is Thymine)
    // C is Cytosine nucleotide
    // A is Adenine nucleotide
    // G is Guanine nucleotide
    // controlling this nucleotide sequence is which amino acid,
    if (codon[1]=='U')
    {
        if (codon[0]=='U' && (codon[2]=='U' || codon[2]=='C')){
            aminoacid = 'F';}
        else if ((codon[0]=='U' && (codon[2]=='A' || codon[2]=='G')) || codon[0]=='C'){
            aminoacid = 'L';}
        else if (codon[0]=='A' && codon[2]!='G'){
            aminoacid = 'I';}
        else if (codon[0]=='A' && codon[2]=='G'){
            aminoacid = 'M';}
        else if (codon[0]=='G'){
            aminoacid = 'V';}
    }
    else if (codon[1]=='C')
    {
        if (codon[0]=='U'){
            aminoacid = 'S';}
        else if (codon[0]=='C'){
            aminoacid = 'P';}
        else if (codon[0]=='A'){
            aminoacid = 'T';}
        else if (codon[0]=='G'){
            aminoacid = 'A';}
    }
    else if (codon[1]=='A')
    {
        if (codon[0]=='U' && (codon[2]=='U' || codon[2]=='C')){
            aminoacid = 'Y';}
        else if (codon[0]=='U' && (codon[2]=='A' || codon[2]=='G')){
            aminoacid = 'X';} // stop codon
        else if (codon[0]=='C' && (codon[2]=='U' || codon[2]=='C')){
            aminoacid = 'H';}
        else if (codon[0]=='C' && (codon[2]=='A' || codon[2]=='G')){
            aminoacid = 'Q';}
        else if (codon[0]=='A' && (codon[2]=='U' || codon[2]=='C')){
            aminoacid = 'N';}
        else if (codon[0]=='A' && (codon[2]=='A' || codon[2]=='G')){
            aminoacid = 'K';}
        else if (codon[0]=='G' && (codon[2]=='U' || codon[2]=='C')){
            aminoacid = 'D';}
        else if (codon[0]=='G' && (codon[2]=='A' || codon[2]=='G')){
            aminoacid = 'E';}
    }
    else if (codon[1]=='G') {
        if (codon[0] == 'U' && (codon[2] == 'U' || codon[2] == 'C')) {
            aminoacid = 'C';
        } else if (codon[0] == 'U' && codon[2] == 'A') {
            aminoacid = 'X'; // stop codon
        } else if (codon[0] == 'U' && codon[2] == 'G') {
            aminoacid = 'W';
        } else if (codon[0] == 'C' || (codon[0] == 'A' && (codon[2] == 'A' || codon[2] == 'G'))) {
            aminoacid = 'R';
        } else if (codon[0] == 'A' && (codon[2] == 'U' || codon[2] == 'C')) {
            aminoacid = 'S';
        } else if (codon[0] == 'G') {
            aminoacid = 'G';
        }
    }
    else { printf("ERROR!"); }
    return aminoacid;
}

int mystrcmp(char first[20],char second[20])
{
    int i = 0
    for (i<17;i++)
    {
        if (first[i]==second[i])
        { continue; }
        else
        { return 0; }
    }
    return 1;
}

//İbrahim Burak Tanrıkulu
