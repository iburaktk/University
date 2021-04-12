/**
 * Hashtable for linear probing and double hashing.
 */

public class HashTable
{
	private final int TABLE_SIZE;
	public int comparison = 0;
	private Employee[] EmployeeData;	// we will use employee array in this hashtable
	
	public HashTable(int size)
	{
		TABLE_SIZE = size;
		EmployeeData = new Employee[TABLE_SIZE];
	}
	
	private int hash1(int key)
	{
		return (key % TABLE_SIZE);
	}
	
	private int hash2(int key)
	{
		return 1 + (key % (TABLE_SIZE - 1));
	}
	
	private int doubleHashFunction(int key, int i)
	{
		return (hash1(key) + i * hash2(key)) % TABLE_SIZE ;
	}
	
	public void put(Employee employee, int phoneNumber, boolean doubleHashing) throws Exception
	{
		int i = 0;	// coefficent for double hashing.
		int hashCode = doubleHashFunction(phoneNumber, i);
		if(doubleHashing)	// searching appropriate location with double hashing.
		{
			while(EmployeeData[hashCode] != null)
			{
				hashCode = doubleHashFunction(phoneNumber, ++i);
				if (i == TABLE_SIZE)
					throw new Exception("InfiniteLoopException: Double Hashing");
			}
			EmployeeData[hashCode] = employee;
		}
		else 	// searching appropriate location with linear probing
		{
			while (EmployeeData[hashCode] != null)
				hashCode = (hashCode + 1) % TABLE_SIZE;
			EmployeeData[hashCode] = employee;
		}
	}
	
	public Employee get(int phoneNumber,boolean doubleHashing)
	{
		int i = 0;	// coefficent for double hashing.
		int hashCode = doubleHashFunction(phoneNumber, i);
		if(doubleHashing)
		{
			while(EmployeeData[hashCode] != null)
			{
				comparison++;	// comparison amount
				if (EmployeeData[hashCode].getPhoneNumber() == phoneNumber)
					return EmployeeData[hashCode];
				hashCode = doubleHashFunction(phoneNumber, ++i);
			}
			return null;
		}
		else 
		{
			while (EmployeeData[hashCode] != null)
			{
				comparison++;
				if (EmployeeData[hashCode].getPhoneNumber() == phoneNumber)
					return EmployeeData[hashCode];
				hashCode = (hashCode + 1) % TABLE_SIZE;
			}
			return null;
		}
	}
	
	public void print() 
	{
		for (int i=0;i<TABLE_SIZE;i++)
		{
			System.out.print("["+i+"]--->");
			if (EmployeeData[i] == null)
				System.out.println("null");
			else
				System.out.println(EmployeeData[i].getPhoneNumber());
		}
	}
}