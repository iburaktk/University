
public class q4 {
	
	public static void main(String[] args) 
	{ 

		//Creating our list
		linkedList list = new linkedList(); 
		
		list.add(85);
		list.add(45);
		list.add(100); 
		list.add(58); 
		list.add(31);
		list.add(855);
		list.add(201); 
		list.add(39); 
		list.add(46);
		list.add(26);
		list.add(55);
		list.add(30);
		list.add(18);
		list.add(11);
		list.add(999);
		list.add(99);
		list.add(79);
		list.add(27);
		list.add(174); 
		list.add(4);
		list.add(98);

		System.out.println("Before merge sort:");
		list.printList(list.head);
		
		list.head = list.mergeSort(list.head);
		
		System.out.println("After merge sort:"); 
		list.printList(list.head);
	} 
}

class linkedList { 
	node head;
	
	void setHead(node head)
	{
		this.head = head;
	}
	
	static class node
	{
		
		int value; 
		node next; 
		
		public node(int value) 
		{ 
			this.value = value; 
		} 
	} 
	
	void add(int newValue)
	{
		node newNode = new node(newValue);
		newNode.next = head;
		setHead(newNode);
	}
	
	void printList(node head)
	{
		node nodePtr = head;
		while (nodePtr != null)
		{
			System.out.printf("%d ",nodePtr.value);
			nodePtr = nodePtr.next;
		}
		System.out.println();
	}
	
	node sort(node head)
	{
		if (head.next == null) // if there is 1 element, no need to sort.
		{
			return head;
		}
		else if (head.next.next == null) // if there are 2 elements, we compare once.
		{
			if (head.value > head.next.value)
			{
				node nodePtr = head.next;
				nodePtr.next = head;
				nodePtr.next.next = null;
				return nodePtr;
			}
			return head;
		}
		else if (head.next.next.next != null) // if there are 4 or more elements, then divide it again.
		{
			return mergeSort(head);
		}
		else if (head.value < head.next.value && head.next.value < head.next.next.value)                                      //123
		{
			return head;
		}
		else if (head.value < head.next.value && head.next.value > head.next.next.value && head.value < head.next.next.value) //132
		{
			node nodePtr = head.next;
			head.next = nodePtr.next;
			head.next.next = nodePtr;
			nodePtr.next = null;
			return head;
		}
		else if (head.value > head.next.value && head.next.value < head.next.next.value && head.value < head.next.next.value) //213
		{
			node nodePtr = head.next;
			head.next = head.next.next;
			nodePtr.next = head;
			return nodePtr;
		}
		else if (head.value < head.next.value && head.next.value > head.next.next.value && head.value > head.next.next.value) //231
		{
			node nodePtr = head.next.next;
			head.next.next = null;
			nodePtr.next = head;
			return nodePtr;
		}
		else if (head.value > head.next.value && head.next.value < head.next.next.value && head.value > head.next.next.value) //312
		{
			node nodePtr = head.next;
			head.next = null;
			nodePtr.next.next = head;
			return nodePtr;
		}
		else if (head.value > head.next.value && head.next.value > head.next.next.value)									  //321
		{
			node nodePtr = head.next.next;
			nodePtr.next = head.next;
			head.next = null;
			nodePtr.next.next = head;
			return nodePtr;
		}
		return null;
	}

	node merge(node left, node middle, node right)
	{
		node returnNode = null , leftPtr = left , middlePtr = middle , rightPtr = right;
		//determining first node
		if (leftPtr.value <= middlePtr.value && leftPtr.value <= rightPtr.value)
		{
			returnNode = leftPtr;
			leftPtr = leftPtr.next;
		}
		else if (middlePtr.value < leftPtr.value && middlePtr.value <= rightPtr.value)
		{
			returnNode = middlePtr;
			middlePtr = middlePtr.next;
		}
		else if (rightPtr.value < leftPtr.value && rightPtr.value < middlePtr.value)
		{
			returnNode = rightPtr;
			rightPtr = rightPtr.next;
		}
		// merging other nodes with loop
		node nodePtr = returnNode;
		while (leftPtr != null || rightPtr != null || middlePtr != null )
		{
			if (leftPtr == null && middlePtr == null)
			{
				nodePtr.next = rightPtr;
				rightPtr = rightPtr.next;
			}
			else if (leftPtr == null && rightPtr == null)
			{
				nodePtr.next = middlePtr;
				middlePtr = middlePtr.next;
			}
			else if (middlePtr == null && rightPtr == null)
			{
				nodePtr.next = leftPtr;
				leftPtr = leftPtr.next;
			}
			else if (leftPtr == null)
			{
				if (middlePtr.value <= rightPtr.value)
				{
					nodePtr.next = middlePtr;
					middlePtr = middlePtr.next;
				}
				else
				{
					nodePtr.next = rightPtr;
					rightPtr = rightPtr.next;
				}
			}
			else if (middlePtr == null)
			{
				if (leftPtr.value <= rightPtr.value)
				{
					nodePtr.next = leftPtr;
					leftPtr = leftPtr.next;
				}
				else
				{
					nodePtr.next = rightPtr;
					rightPtr = rightPtr.next;
				}
			}
			else if (rightPtr == null)
			{
				if (leftPtr.value <= middlePtr.value)
				{
					nodePtr.next = leftPtr;
					leftPtr = leftPtr.next;
				}
				else
				{
					nodePtr.next = middlePtr;
					middlePtr = middlePtr.next;
				}
			}
			else 
			{
				if (leftPtr.value <= middlePtr.value && leftPtr.value <= rightPtr.value)
				{
					nodePtr.next = leftPtr;
					leftPtr = leftPtr.next;
				}
				else if (middlePtr.value < leftPtr.value && middlePtr.value <= rightPtr.value)
				{
					nodePtr.next = middlePtr;
					middlePtr = middlePtr.next;
				}
				else if (rightPtr.value < leftPtr.value && rightPtr.value < middlePtr.value)
				{
					nodePtr.next = rightPtr;
					rightPtr = rightPtr.next;
				}
			}
			nodePtr = nodePtr.next;
		}
		return returnNode;
	}
	
	node mergeSort(node head) 
	{
		if (head.next == null)
		{
			return head;
		}
		// dividing to 3
		node nodePtr = head;
		node middlePart = head;
		node lastPart = head;
		while(nodePtr.next != null && nodePtr.next.next != null && nodePtr.next.next.next != null)
		{
			nodePtr = nodePtr.next.next.next;
			lastPart = lastPart.next.next;
			middlePart = middlePart.next;
		}
		nodePtr = middlePart;
		middlePart = middlePart.next;
		nodePtr.next = null; // we cut the connection between left and middle
		nodePtr = lastPart;
		lastPart = lastPart.next;
		nodePtr.next = null; // we cut the connection between middle and right
		
		head = sort(head);
		middlePart = sort(middlePart);
		lastPart = sort(lastPart);
		
		head = merge(head,middlePart,lastPart);
		
		return head;
	}
}