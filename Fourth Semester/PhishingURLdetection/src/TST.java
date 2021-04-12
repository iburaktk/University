import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class TST
{
	public Node root;
	int weightNumber;
	int truePositive,falseNegative,trueNegative,falsePositive,unpredictablePhishing,unpredictableLegitimate;
	
	public TST(Scanner scanner1, Scanner scanner2, Scanner scanner3, Scanner scanner4, int N_GRAM_NUMBER)
	{
		root = new Node('\0');
		weightNumber = 0;
		truePositive = 0;
		trueNegative = 0;
		falseNegative = 0;
		falsePositive = 0;
		unpredictablePhishing = 0;
		unpredictableLegitimate = 0;
		
		int i=0,j=0,k=0;
		
		while (scanner1.hasNext())
		{
			i++;
			String line = scanner1.nextLine();
			if (line.isEmpty())
				continue;
			line = line.replaceAll("https", "");
			line = line.replaceAll("http", "");
			line = line.replaceAll("www", "");
			Locale trLocale = Locale.forLanguageTag("tr_TR");
			line = line.toLowerCase(trLocale);
			while (line.length() > N_GRAM_NUMBER-1)
			{
				root.addWord(line.substring(0, N_GRAM_NUMBER),-1); 
				line = line.substring(1);
			}
		}
		System.out.println("Legitimate training file has been loaded with ["+i+"] instances");
		j=i;
		i=0;
		
		while (scanner3.hasNext())
		{
			i++;
			scanner3.nextLine();
		}
		System.out.println("Legitimate test file has been loaded with ["+i+"] instances");
		i=0;
		
		while (scanner2.hasNext())
		{
			i++;
			String line = scanner2.nextLine();
			if (line.isEmpty())
				continue;
			line = line.replaceAll("https", "");
			line = line.replaceAll("http", "");
			line = line.replaceAll("www", "");
			Locale trLocale = Locale.forLanguageTag("tr_TR");
			line = line.toLowerCase(trLocale);
			while (line.length() > N_GRAM_NUMBER-1)
			{
				root.addWord(line.substring(0, N_GRAM_NUMBER),1); 
				line = line.substring(1);
			}
		}
		System.out.println("Phishing training file has been loaded with ["+i+"] instances");
		k=i;
		i=0;
		
		while (scanner4.hasNext())
		{
			i++;
			scanner4.nextLine();
		}
		System.out.println("Phishing test file has been loaded with ["+i+"] instances");
		
		System.out.println("TST has been loaded with "+j+" n-grams");
		System.out.println("TST has been loaded with "+k+" n-grams");
	}
	
	public void testIt(Scanner legitimateTest, Scanner phishingTest, int N_GRAM_NUMBER)
	{
		while (legitimateTest.hasNext())
		{
			float sumOfWeights = 0;
			String line = legitimateTest.nextLine();
			if (line.isEmpty())
				continue;
			line = line.replaceAll("https", "");
			line = line.replaceAll("http", "");
			line = line.replaceAll("www", "");			
			Locale trLocale = Locale.forLanguageTag("tr_TR");
			line = line.toLowerCase(trLocale);
			while (line.length() >= N_GRAM_NUMBER)
			{
				String word = line.substring(0, N_GRAM_NUMBER);		// Create n-gram.
				sumOfWeights += findAndGetWeightIfExists(word);		// Gets weights and sums up.			
				line = line.substring(1);
			}
			if (sumOfWeights < 0.0f)
				trueNegative++;
			else if (sumOfWeights > 0.0f)
				falsePositive++;
			else 
				unpredictableLegitimate++;				
		}
		
		while (phishingTest.hasNext())
		{
			float sumOfWeights = 0;
			String line = phishingTest.nextLine();
			if (line.isEmpty())
				continue;
			line = line.replaceAll("https", "");
			line = line.replaceAll("http", "");
			line = line.replaceAll("www", "");
			Locale trLocale = Locale.forLanguageTag("tr_TR");
			line = line.toLowerCase(trLocale);
			while (line.length() >= N_GRAM_NUMBER)
			{
				String word = line.substring(0, N_GRAM_NUMBER);		// Create n-gram.
				sumOfWeights += findAndGetWeightIfExists(word);		// Gets weights and sums up.
				line = line.substring(1);
			}
			if (sumOfWeights > 0.0f)
				truePositive++;
			else if (sumOfWeights < 0.0f)
				falseNegative++;
			else 
				unpredictablePhishing++;			
		}
		System.out.println("TP:"+truePositive+" FN:"+falseNegative+" TN:"+trueNegative+" FP:"+falsePositive+" Unpredictable Phishing:"+unpredictablePhishing+" Unpredictable Legitimate:"+unpredictableLegitimate);
		float accuracy = (float)(truePositive + trueNegative) / (float)(truePositive+trueNegative+falseNegative+falsePositive+unpredictableLegitimate+unpredictablePhishing);
		System.out.println("Accurracy: "+accuracy);
		
	}
	
	private float findAndGetWeightIfExists(String word)
	{
		Node node = root;
		while (! node.isEnd)
		{
			boolean found = false;
			for (Node child : node.childeren)			
				if (child.data == word.charAt(0))		// Check. Is this n-gram exists in TST?
				{
					node = child;
					word = word.substring(1);
					found = true;
					break;
				}
			if (found == false)							// If it is not exists, then return 0.
				return 0.0f;
		}
		return node.weight;								// return its weight.
	}
}

class Node
{
	public char data;
	private int phishOccurrence;
	private int legitimateOccurrence;
	public float weight;
	public boolean isEnd;
	int sortedWordAmount;
	PrintStream stdout;
	public String temp;
	
	public Map<Character,Integer> hashMap;
	public ArrayList<Node> childeren;
	
	public Node(char data)
	{
		this.data = data;
		isEnd = false;
		phishOccurrence = 0;
		legitimateOccurrence = 0;
		hashMap = new HashMap<Character,Integer>();
		childeren = new ArrayList<Node>();
		stdout = System.out;
	}
	
	public int getPhishOccurrence()
	{
		return phishOccurrence;
	}
	
	private void increasePhishOccurrence()
	{
		phishOccurrence++;
	}
	
	public int getLegitimateOccurrence()
	{
		return legitimateOccurrence;
	}
	
	private void increaseLegitimateOccurrence()
	{
		legitimateOccurrence++;
	}
	
	public float getWeight()
	{
		if (phishOccurrence>0 && legitimateOccurrence==0)
			weight = 1.0f;
		else if (phishOccurrence==0 && legitimateOccurrence>0)
			weight = -1.0f;
		else if (phishOccurrence>0 && legitimateOccurrence>0)
		{
			if (phishOccurrence > legitimateOccurrence)
				weight = (float)legitimateOccurrence / (float)phishOccurrence ;
			else if (phishOccurrence < legitimateOccurrence)
				weight = - (float)phishOccurrence / (float)legitimateOccurrence;
			else 
				weight = 0.0f;
		}
		return weight;
	}
	
	public void addWord(String word,int i)
	{
		if (word.isEmpty())
		{
			isEnd = true;
			if (i == -1)
				increaseLegitimateOccurrence();
			if (i == 1)
				increasePhishOccurrence();
			return;
		}
		if (hashMap.containsKey(word.charAt(0)))		// If this character already exist, go to child.
		{
			char temp = (char) word.charAt(0);
			childeren.get(hashMap.get(temp)).addWord(word.substring(1), i);
		}
		else 											// else, add new .
		{
			char temp = word.charAt(0);
			Node newNode = new Node(temp);
			hashMap.put(temp, childeren.size());
			childeren.add(newNode);
			newNode.addWord(word.substring(1), i);
		}
		return;
	}
	
	private void sortMostImportantLegitimate()			// Firstly, sort childeren and initialize yourself.
	{
		sortedWordAmount = 0;
		if (isEnd)
			return;
		int i=0;
		for (Node node : childeren)
		{
			hashMap.replace(node.data,i++);
			node.sortMostImportantLegitimate();
		}
		Collections.sort(childeren,new compareLegitimateOccurrences());
		legitimateOccurrence = childeren.get(0).legitimateOccurrence;	// You're at parent node. Set occurrence with biggest child's occurrence.
	}
	
	private String refreshMostImportantLegitimate(Node parent)	// When printed most important, refresh values to second.
	{
		if (isEnd)		// if you're at leaf; add character to string and set second important's value to parent.
		{
			parent.sortedWordAmount++;
			if (parent.childeren.size() == parent.sortedWordAmount)
				parent.legitimateOccurrence = -1;	// if all childeren already printed, set parent to -1. So that parent goes to the end of the ordered list.
			else 
				parent.legitimateOccurrence = parent.childeren.get(parent.sortedWordAmount).getLegitimateOccurrence();
			StringBuilder newString = new StringBuilder();
			newString.append(data);
			return newString.toString();
		}
		else 		// recursively traverse to child, get value from child and add your data to it. return to parent.
		{
			StringBuilder newString = new StringBuilder();
			if (childeren.get(0).isEnd)
				newString.append(childeren.get(sortedWordAmount).refreshMostImportantLegitimate(this));
			else 
				newString.append(childeren.get(0).refreshMostImportantLegitimate(this));
			newString.insert(0, data);
			Collections.sort(parent.childeren, new compareLegitimateOccurrences());
			parent.legitimateOccurrence = parent.childeren.get(0).getLegitimateOccurrence();
			return newString.toString();
			
		}
	}
	
	public int printMostImportantLegitimate(int FEATURE_SIZE) throws FileNotFoundException
	{
		File outputFile = new File("../strong_legitimate_features.txt");
		System.setOut(new PrintStream(outputFile));
		try 
		{
			PrintStream out = new PrintStream(System.out,true,"UTF-8");		// for printing turkish letters.
			System.setOut(out);
		}
		catch (Exception e){}
		sortMostImportantLegitimate();
		System.out.println("Most important legitimate n_grams");
		int i=1;
		int importantLegitimateOccurrence = 0;
		String nGramWord = null;
		while (i<FEATURE_SIZE+1)
		{
			importantLegitimateOccurrence  = childeren.get(0).getLegitimateOccurrence();
			nGramWord = childeren.get(0).refreshMostImportantLegitimate(this);
			System.out.println(i+". "+nGramWord+" - freq: "+importantLegitimateOccurrence);
			i++;
		}
		temp = nGramWord;		// set last most important legitimate
		System.setOut(stdout);
		return importantLegitimateOccurrence;	
	}
	
	private void sortMostImportantPhishing()	// Firstly, sort childeren and initialize yourself.
	{
		sortedWordAmount = 0;
		if (isEnd)
			return;
		int i=0;
		for (Node node : childeren)
		{
			hashMap.replace(node.data,i++);
			node.sortMostImportantPhishing();
		}
		Collections.sort(childeren,new comparePhishOccurrences());
		phishOccurrence = childeren.get(0).phishOccurrence;	// You're at parent node. Set occurrence with biggest child's occurrence.
	}
	
	private String refreshMostImportantPhishing(Node parent)	// When printed most important, refresh values to second.
	{
		if (isEnd)		// if you're at leaf; add character to string and set second important's value to parent.
		{
			parent.sortedWordAmount++;
			if (parent.childeren.size() == parent.sortedWordAmount)
				parent.phishOccurrence = -1;	// if all childeren already printed, set parent to -1. So that parent goes to the end of the ordered list.
			else 
				parent.phishOccurrence = parent.childeren.get(parent.sortedWordAmount).getPhishOccurrence();
			StringBuilder newString = new StringBuilder();
			newString.append(data);
			return newString.toString();
		}
		else 		// recursively traverse to child, get value from child and add your data to it. return to parent.
		{
			StringBuilder newString = new StringBuilder();
			if (childeren.get(0).isEnd)
				newString.append(childeren.get(sortedWordAmount).refreshMostImportantPhishing(this));
			else 
				newString.append(childeren.get(0).refreshMostImportantPhishing(this));
			newString.insert(0, data);
			Collections.sort(parent.childeren, new comparePhishOccurrences());
			parent.phishOccurrence = parent.childeren.get(0).getPhishOccurrence();
			return newString.toString();
		}
	}
	
	public int printMostImportantPhishing(int FEATURE_SIZE) throws FileNotFoundException
	{
		File outputFile = new File("../strong_phishing_features.txt");
		System.setOut(new PrintStream(outputFile));
		try 
		{
			PrintStream out = new PrintStream(System.out,true,"UTF-8");		// for printing turkish letters.
			System.setOut(out);
		}
		catch (Exception e){}
		sortMostImportantPhishing();
		System.out.println("Most important phishing n_grams");
		int i=1;
		int importantPhishingOccurrence = 0;
		String nGramWord = null;
		while (i<FEATURE_SIZE+1)
		{
			importantPhishingOccurrence = childeren.get(0).getPhishOccurrence();
			nGramWord = childeren.get(0).refreshMostImportantPhishing(this);
			System.out.println(i+". "+nGramWord+" - freq: "+importantPhishingOccurrence);
			i++;
		}
		temp = nGramWord;		// set last most important legitimate
		System.setOut(stdout);
		return importantPhishingOccurrence;
	}
	
	private void sortWeights(Comparator<Node> comparator)	// Firstly, sort childeren and initialize yourself.
	{
		sortedWordAmount = 0;
		if (isEnd)
		{
			weight = getWeight();
			return;
		}
		int i=0;
		for (Node node : childeren)
		{
			hashMap.replace(node.data,i++);
			node.sortWeights(comparator);
		}
		Collections.sort(childeren,comparator);
		weight = childeren.get(0).weight;	// You're at parent node. Set weight with biggest child's weight.
	}
	
	private String refreshWeights(Node parent)	// When printed most important, refresh values to second.
	{
		if (isEnd)		// if you're at leaf; add character to string and set second important's value to parent.
		{
			parent.sortedWordAmount++;
			if (parent.childeren.size() == parent.sortedWordAmount)
				parent.weight = -2;		// if all childeren already printed, set parent to -2. So that parent goes to the end of the ordered list.
			else 
				parent.weight = parent.childeren.get(parent.sortedWordAmount).getWeight();
			StringBuilder newString = new StringBuilder();
			newString.append(data);
			return newString.toString();
		}
		else 		// recursively traverse to child, get value from child and add your data to it. return to parent.
		{
			
			StringBuilder newString = new StringBuilder();
			if (childeren.get(0).isEnd)
				newString.append(childeren.get(sortedWordAmount).refreshWeights(this));
			else
				newString.append(childeren.get(0).refreshWeights(this));
			newString.insert(0, data);			
			Collections.sort(parent.childeren, new compareWeights());
			parent.weight = parent.childeren.get(0).weight;
			return newString.toString();
		}
	}
	
	public int printAllFeatureWeights() throws FileNotFoundException
	{
		File outputFile = new File("../all_feature_weights.txt");
		System.setOut(new PrintStream(outputFile));
		try 
		{
			PrintStream out = new PrintStream(System.out,true,"UTF-8");		// for printing turkish letters.
			System.setOut(out);
		}
		catch (Exception e){}
		sortWeights(new compareWeights());
		System.out.println("All N-Gram Weights");
		int i=0;
		while (weight != -2)
		{
			float weight = childeren.get(0).weight;
			String nGramWord = childeren.get(0).refreshWeights(this);
			System.out.println(nGramWord+" - weight: "+weight);
			i++;
		}
		System.setOut(stdout);
		return i;
	}
	
	public int removeInsignificantOnes(int legitimateMin, int phishingMin, String lastLegitimate, String lastPhishing, boolean legitimateLessThan, boolean phishingLessThan)
	{
		int i=0,j=0;
		while (i != childeren.size())
		{
			Node node = childeren.get(i);
			if (node.isEnd)
			{
				if (node.getLegitimateOccurrence() < legitimateMin && node.getPhishOccurrence() < phishingMin)
				{								// if frequency is low, then its not important. Delete it.
					childeren.remove(i);
					j++;
				}
				else if (node.getLegitimateOccurrence() == legitimateMin && legitimateLessThan)
				{								// if legitimate frequency is on lowest important, Check for alphabetic order.
					if (Character.compare(node.data, lastLegitimate.charAt(0)) < 1)
						i++;
					else
					{
						childeren.remove(i);
						j++;
					}
				}
				else if (node.getPhishOccurrence() == phishingMin && phishingLessThan)
				{								// if phishing frequency is on lowest important, Check for alphabetic order.
					if (Character.compare(node.data, lastPhishing.charAt(0)) < 1)
						i++;
					else
					{
						childeren.remove(i);
						j++;
					}
				}
				else i++;
			}
			else 	// recursively traverse to child. check for alphabetic order.
			{
				boolean temp1 = Character.compare(node.data, lastLegitimate.charAt(0)) < 1;		// for alphabetic ordering.
				boolean temp2 = Character.compare(node.data, lastPhishing.charAt(0)) < 1;
				if (!legitimateLessThan)
					temp1 = false;
				if (!phishingLessThan)
					temp2 = false;
				j += node.removeInsignificantOnes(legitimateMin, phishingMin,lastLegitimate.substring(1), lastPhishing.substring(1), temp1, temp2);
				if (node.childeren.size() == 0)		// if all childeren removed, then remove this too.
				{
					childeren.remove(node);
				}
				else i++;
			}
		}
		return j;
	}
}

class compareLegitimateOccurrences implements Comparator<Node>
{
	public int compare(Node b, Node a)
	{
		int diff = a.getLegitimateOccurrence() - b.getLegitimateOccurrence();
		if (diff != 0)
			return diff;
		else
			return Character.compare(b.data, a.data);
	}
}

class comparePhishOccurrences implements Comparator<Node>
{
	public int compare(Node b, Node a)
	{
		int diff = a.getPhishOccurrence() - b.getPhishOccurrence();
		if (diff != 0)
			return diff;
		else
			return Character.compare(b.data, a.data);
	}
}

class compareWeights implements Comparator<Node>
{
	public int compare(Node a, Node b)
	{
		if (Float.sum(a.weight, -b.weight) > 0.0f)
			return -1;
		else if (Float.sum(a.weight, -b.weight) < 0.0f)
			return 1;
		else 
			return Character.compare(a.data, b.data);
	}
}
