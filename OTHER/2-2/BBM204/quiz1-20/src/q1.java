
public class q1 {
	
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
	
	static class node { 
		
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
	
	node getRightHead(node head) // left side's head is already "head", lets find right side's head.
	{
		node nodePtr = head;
		node rightHead = head;
		while(nodePtr.next != null && nodePtr.next.next != null)
		{
			nodePtr = nodePtr.next.next;
			rightHead = rightHead.next;
		}
		nodePtr = rightHead;
		rightHead = rightHead.next;
		nodePtr.next = null; // we cut the connection between left and right
		return rightHead;
	}
	
	node sort(node head)
	{
		if (head.next == null) // if there is 1 element, no need to sort.
		{
			return head;
		}
		if (head.next.next != null) // if there are 3 or more elements, then divide it again.
		{
			return mergeSort(head);
		}
		if (head.value > head.next.value)
		{
			node nodePtr = head.next;
			nodePtr.next = head;
			nodePtr.next.next = null;
			return nodePtr;
		}
		return head;
	}

	node merge(node left, node right)
	{
		node returnNode , leftPtr = left , rightPtr = right;
		if (leftPtr.value <= rightPtr.value)
		{
			returnNode = leftPtr;
			leftPtr = leftPtr.next;
		}
		else 
		{
			returnNode = rightPtr;
			rightPtr = rightPtr.next;
		}
		node nodePtr = returnNode;
		while (leftPtr != null || rightPtr != null )
		{
			if (leftPtr == null)
			{
				nodePtr.next = rightPtr;
				rightPtr = rightPtr.next;
			}
			else if (rightPtr == null)
			{
				nodePtr.next = leftPtr;
				leftPtr = leftPtr.next;
			}
			else 
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
		node rightHead = getRightHead(head);
		
		head = sort(head);
		rightHead = sort(rightHead);
		
		head = merge(head,rightHead);
		
		return head;
	}
}