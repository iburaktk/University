
public class Player {

	public String name;
	public int money;
	public int currentLocation;
	public int inJail = 0;
	public int jailCount = 0;
	public int railroadHave = 0;
	
	public Player(String name,int money)
	{
		this.name = name;
		this.money = money;
		this.currentLocation = 0;
	}
	
	public Square[] ownedSquares = new Square[28];
	public int ownedSquaresNum = 0;
	
	public void buy(Square property)
	{
		money -= property.cost;
		ownedSquares[ownedSquaresNum] = property;
		ownedSquaresNum++;
		if (property.type.equals("Railroad"))
		{
			railroadHave++;
		}
	}
	
	public void collectRent(int rent)
	{
		money += rent;
	}
	
	public void payRent(int rent)
	{
		money -= rent;
	}
	
	public void jail()
	{
		inJail = 1;
	}
	public void free()
	{
		inJail = 0;
		jailCount = 0;
	}
	public String showProperties()
	{
		String properties = "";
		for (int i=0;i<ownedSquaresNum;i++)
		{
			String temp;
			temp = properties + ownedSquares[i].name;
			properties = temp;
			if (i != ownedSquaresNum-1)
			{
				temp = properties + ",";
				properties = temp;
			}
		}
		return properties;
	}
	
}
