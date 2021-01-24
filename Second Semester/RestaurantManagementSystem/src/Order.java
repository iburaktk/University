
public class Order {
	
	final int MAX_ITEM = 10;
	
	Item[] order = new Item[MAX_ITEM];
	Item[] stock = new Item[MAX_ITEM];
	Worker waiter;
	String items;
	int stockCount;
	int itemCount = 0;
	
	public Order(String items,Item[] stock,int stockCount)
	{
		this.stockCount = stockCount;
		this.items = items;
		this.stock = stock;
	}
	
	
	void prepareOrder ()
	{
		String[] itemAndNum;
		itemAndNum = items.split(":");
		int len = itemAndNum.length;
		for (int i=0;i<len;i++)
		{
			String[] itemOrNum;
			itemOrNum = itemAndNum[i].split("-");
			for (int j=0;j<Integer.valueOf(itemOrNum[1]);j++)
			{
				int k;
				for (k=0;k<stockCount;k++)
				{
					if (stock[k].getName().equals(itemOrNum[0]))
					{
						if (stock[k].getAmount()>0)
						{
							System.out.printf("Item %s added into order\n",itemOrNum[0]);
							stock[k].setAmount(stock[k].getAmount()-1);
							Item anOrder = new Item(itemOrNum[0],stock[k].getCost(),1);
							order[itemCount] = anOrder;
							itemCount++;
							break;
						}
						else 
						{
							System.out.printf("Sorry! No %s in the stock!\n",itemOrNum[0]);
							break;
						}
					}
					else if (k==stockCount-1)
					{
						System.out.printf("Unknown item %s\n",itemOrNum[0]);
						break;
					}
				}
			}
		}
	}


	public Item[] getOrder() {
		return order;
	}


	public void setOrder(Item[] order) {
		this.order = order;
	}


	public String getItems() {
		return items;
	}


	public void setItems(String items) {
		this.items = items;
	}	
}
