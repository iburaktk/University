import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main
{
	public static void main(String[] args) throws FileNotFoundException
	{
		int N_GRAM_NUMBER = 3;
		int FEATURE_SIZE = 5000;
		
		File legitimateTrain = new File("../legitimate-train.txt");
		Scanner legitimateTrainInput = new Scanner(legitimateTrain);
		File phishingTrain = new File("../phishing-train.txt");
		Scanner phishingTrainInput = new Scanner(phishingTrain);
		File legitimateTest = new File("../legitimate-test.txt");
		Scanner legitimateTestInput = new Scanner(legitimateTest);
		File phishingTest = new File("../phishing-test.txt");
		Scanner phishingTestInput = new Scanner(phishingTest);
		
		// All data in TST. We are giving scanners as argument. So constructor method can construct.
		TST theTST = new TST(legitimateTrainInput,phishingTrainInput,legitimateTestInput,phishingTestInput,N_GRAM_NUMBER);
		
		// Finding and printing most important phishing n-grams. 
		int minimumOccurrenceOfPhishing = theTST.root.printMostImportantPhishing(FEATURE_SIZE); // minimum frequency
		String lastMostImportantPhishing = theTST.root.temp;									// last n-gram with minimum frequency
		System.out.println(FEATURE_SIZE+" strong phishing n-grams have been saved to the file\"strong_phishing_features.txt\"");
		
		// Finding and printing most important legitimate n-grams. 
		int minimumOccurrenceOfLegitimate = theTST.root.printMostImportantLegitimate(FEATURE_SIZE); // minimum frequency
		String lastMostImportantLegitimate = theTST.root.temp;										// last n-gram with minimum frequency
		System.out.println(FEATURE_SIZE+" strong legitimate n-grams have been saved to the file\"strong_legitimate_features.txt\"");
			
		// Finding and printing weights of all n-grams. 
		int numberOfWords = theTST.root.printAllFeatureWeights();	// amount of all n-grams
		System.out.println(numberOfWords+" n-grams + weights have been saved to the file \"all_feature_weights.txt\"");
		
		// Firstly looks frequency. If frequency less than minimum, removes. else looks for alphabetic order respect to last n-gram.
		int removedWords = theTST.root.removeInsignificantOnes(minimumOccurrenceOfLegitimate,minimumOccurrenceOfPhishing,lastMostImportantLegitimate,lastMostImportantPhishing,true,true);
		System.out.println(removedWords+" insignificant n-grams have been removed from the TST");
		
		// Calculates weights of n-grams and sums up weights. Determines legitimate or phishing.
		theTST.testIt(new Scanner(legitimateTest), new Scanner(phishingTest),N_GRAM_NUMBER);
	}
	
	
}