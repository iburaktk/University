import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

public class main
{
	
	public static void main(String[] args) throws FileNotFoundException
	{
		File outputFile = new File("output.txt");
		System.setOut(new PrintStream(outputFile));
		
		float loadFactor1 = Float.valueOf(args[1]);
		float loadFactor2 = Float.valueOf(args[2]);
		if (loadFactor2 > 1.0)
		{
			System.out.println("Load Factor 2 cannot be more than 1!");
			System.exit(0);
		}
		int tableSize1;
		int tableSize2;
		
		String input;
		File inputFile = new File(args[0]);
		Scanner in = new Scanner(inputFile);
		
		int employeeAmount = -1;	// first line is not an employee, so employeeAmount = -1
		while (in.hasNext())
		{
			in.nextLine();
			employeeAmount++;
		}
		tableSize1 = (int) (employeeAmount/loadFactor1);
		tableSize2 = (int) (employeeAmount/loadFactor2);
		// creating 3 hashtable for each operation
		MyHashTable seperateChaining = new MyHashTable(tableSize1);
		HashTable linearProbing = new HashTable(tableSize2);
		HashTable doubleHashing = new HashTable(tableSize2);
		
		in = new Scanner(inputFile);
		in.nextLine();
		while (in.hasNext())
		{
			// creating employee
			Employee newEmployee = new Employee();
			newEmployee.setEmployeeCode(in.next());
			newEmployee.setNRIC(in.next());
			int phoneNumber = Integer.valueOf(in.next());
			newEmployee.setPhoneNumber(phoneNumber);
			try 
			{
				// we will try putting this employee to hashtables.
				seperateChaining.put(newEmployee,phoneNumber);
				linearProbing.put(newEmployee,phoneNumber,false);
				doubleHashing.put(newEmployee,phoneNumber,true);
			} catch (Exception e) {	// if something goes wrong, throw exception.w
				e.printStackTrace();
				System.exit(0);
			} 
			
		}
		
		// Operations completed. Now, lets print it.
		System.out.println(args[0]+",LF="+args[1]+",LF2="+args[2]+","+args[3]);
		System.out.println("PART1");
		seperateChaining.print();
		System.out.println("PART2");
		System.out.println("Hashtable for Linear Probing");
		linearProbing.print();
		System.out.println("Hashtable for Double Hashing");
		doubleHashing.print();
		
		long startTime = System.nanoTime();
		if (seperateChaining.get(Integer.valueOf(args[3])) != null)
		{
			long endTime = System.nanoTime();
			System.out.println("SEPARATE CHAINING:\n"
							+ "Key found with " + seperateChaining.comparison + " comparisons\n"
							+ "CPU time taken to search = " + (endTime - startTime) + " ns");
		}
		startTime = System.nanoTime();
		if (linearProbing.get(Integer.valueOf(args[3]),false) != null)
		{
			long endTime = System.nanoTime();
			System.out.println("LINEAR PROBING:\n"
							+ "Key found with " + linearProbing.comparison + " comparisons\n"
							+ "CPU time taken to search = " + (endTime - startTime) + " ns");
		}
		startTime = System.nanoTime();
		if (doubleHashing.get(Integer.valueOf(args[3]), true) != null)
		{
			long endTime = System.nanoTime();
			System.out.println("DOUBLE HASHING:\n"
							+ "Key found with " + doubleHashing.comparison + " comparisons\n"
							+ "CPU time taken to search = " + (endTime - startTime) + " ns");
		}
		
	}
}

