/**
 * Hashtable for seperate chaining.
 */

public class MyHashTable
{
	private final int TABLE_SIZE;
	public int comparison = 0;
	private LinkedList[] EmployeeData;	// we will use array of linkedlist in this hashtable
	
	public MyHashTable(int size)
	{
		TABLE_SIZE = size;
		EmployeeData = new LinkedList[TABLE_SIZE];
	}
	
	private int hash(int key)
	{
		return (key % TABLE_SIZE);
	}

	public void put(Employee employee, int phoneNumber)
	{
		int hashCode = hash(phoneNumber);
		EmployeeData[hashCode] = LinkedList.add(EmployeeData[hashCode], employee);
	}
	
	public Employee get(int phoneNumber)
	{
		int hashCode = hash(phoneNumber);
		if (EmployeeData[hashCode] == null)
			return null;
		LinkedList temp = EmployeeData[hashCode];
		while (temp != null)
		{
			comparison++;
			Employee employee = (Employee) temp.value;
			if (employee.getPhoneNumber() == phoneNumber)
				return employee;
			temp = temp.next;
		}
		return null;
	}
	
	public void print()
	{
		for (int i=0;i<TABLE_SIZE;i++)
		{
			String nextPhoneNumber;
			Employee employee;
			if (EmployeeData[i] == null)
				nextPhoneNumber = "Null";
			else 
			{
				employee = (Employee) EmployeeData[i].value;
				nextPhoneNumber = String.valueOf(employee.getPhoneNumber());
			}
			System.out.print("[Chain "+i+"]: "+nextPhoneNumber);
			if (nextPhoneNumber == "Null")
				System.out.println();
			else 
			{
				LinkedList tempLinkedList = EmployeeData[i];
				while (tempLinkedList.next != null)
				{
					tempLinkedList = tempLinkedList.next;
					employee = (Employee) tempLinkedList.value;
					System.out.print("---->"+employee.getPhoneNumber());
				}
				System.out.println();
			}
		}
	}
}

// linkedlist class for hashtable.
class LinkedList
{
	Object value;	// we will use employee objects.
	LinkedList next;
	
	public LinkedList (Object value)
	{
		this.value = value;
		next = null;
	}
	
	public static LinkedList add(LinkedList list, Object data)	// adding new object to end of the list
	{
		if (list == null)	// if this list is empty, then create a list with an object.
			list = new LinkedList(data);
		else
		{
			LinkedList newNode = new LinkedList(data);	// create new node.
			LinkedList temp = list;
			while (temp.next != null)	// find end of the list
				temp = temp.next;
			temp.next = newNode;		// add object to the list.
		}
		return list;
	}
}
