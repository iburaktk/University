import java.io.*;
import java.util.*;

public class Main{

    public static void main(String [ ] args) {
    	ArrayList<Character> finalText = new ArrayList<Character>();
    	Map<Character, String> nonTerminalToTerminal = new HashMap<Character,String>();

    	File inputBNF = new File(args[0]);
    	Scanner input = null;
    	try {input = new Scanner(inputBNF);} catch (FileNotFoundException e) {e.printStackTrace();}
    	int symbolAmount = 0;
    	String firstSymbol = "";
    	while (input.hasNext()) //creating map for translating nonTerminal to Terminal
    	{
    		symbolAmount++;
    		String line = input.nextLine();
    		String[] data = line.split("->");
    		nonTerminalToTerminal.put(line.charAt(0),data[1]);
    		if (symbolAmount == 1) { firstSymbol = String.valueOf(line.charAt(0)); }
    	}
    	
    	recursion(nonTerminalToTerminal,firstSymbol,finalText); // using recursion for creating finalText
    	
    	for (char character : finalText) // printing finalText
    	{
    		System.out.print(character);
    	}
    }
    
    private static void recursion(Map<Character, String> nonTerminalToTerminal, String expansion,ArrayList<Character> textPart)
    {
    	if (expansion.length() == 1) // for first nonTerminal symbol
    	{
    		recursion(nonTerminalToTerminal, "(" + nonTerminalToTerminal.get(expansion.charAt(0)) + ")", textPart);
    	}
    	else 
    	{
    		for (int i=0;i<expansion.length();i++)
        	{
        		if (i == 0)
        		{
        			textPart.add('(');
        		}
        		else if (i == expansion.length())
        		{
        			textPart.add(')');
        		}
        		else if (expansion.charAt(i) < 91 && expansion.charAt(i) > 42) // Checking nonTerminal or Terminal
        		{
        			recursion(nonTerminalToTerminal, "(" + nonTerminalToTerminal.get(expansion.charAt(i)) + ")", textPart);
        		}
        		else 
    			{
					textPart.add(expansion.charAt(i));
				}
        	}
		}
    }
}
