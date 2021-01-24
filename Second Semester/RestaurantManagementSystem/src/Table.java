
public class Table {

	public int tableId;
	public int capacity;
	public int inService;
	public final int MAX_ORDER = 5;
	private Worker creator;
	public Worker waiter;
	Order order;
	int[] itemAmount = new int[MAX_ORDER];
	int orderCount = 0;
	int isCheckOut = 1;
	
	public Table(int tableId, int capacity, Worker creator)
	{
		this.tableId = tableId;
		this.capacity = capacity;
		this.creator = creator;
		this.inService = 0;
	}

	public int getInService() {
		return inService;
	}

	public void setInService(int inService) {
		this.inService = inService;
	}

	public Worker getWaiter() {
		return waiter;
	}

	public void setWaiter(Worker waiter) {
		this.waiter = waiter;
	}
	
	void takeOrder(Order order)
	{
		if(isCheckOut == 1)
		{
			this.order = order;
			this.order.prepareOrder();
			itemAmount[0] = order.itemCount;
			orderCount++;
			isCheckOut=0;
		}
		else 
		{
			System.out.printf("You must use check_out command firstly before a new order!\n");
		}
	}

	void addToOrder(String items)
	{
		order.setItems(items);
		int oldNum = order.itemCount;
		order.prepareOrder();
		itemAmount[orderCount] = order.itemCount - oldNum;
		orderCount++;
	}
	
	void checkOut()
	{
		Item[] status = new Item[10];
		int statusCount = 1;
		for (int i=0;i<order.itemCount;i++)
		{
			if (i==0)
			{
				Item anItem = new Item(order.order[i].getName(),order.order[i].getCost(),1);
				status[0] = anItem;
				continue;
			}
			for (int k=0;k<statusCount;k++)
			{
				if (order.order[i].getName().equals(status[k].getName()))
				{
					status[k].setAmount(status[k].getAmount()+1);
					break;
				}
				else if (k==statusCount-1)
				{
					statusCount++;
					Item anItem = new Item(order.order[i].getName(),order.order[i].getCost(),1);
					status[statusCount-1] = anItem;
					break;
				}
			}
		}
		float sum = 0;
		for (int i=0;i<statusCount;i++)
		{
			sum += status[i].getCost()*status[i].getAmount();
			System.out.printf("%s:\t%.3f (x %d) %.3f $\n",status[i].getName(),status[i].getCost(),status[i].getAmount(),status[i].getCost()*status[i].getAmount());
		}
		System.out.printf("Total:\t%.3f $\n",sum);
		orderCount = 0;
		isCheckOut = 1;
		inService = 0;
	}
	
}
