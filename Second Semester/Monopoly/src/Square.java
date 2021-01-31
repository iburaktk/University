
public class Square {
	
	public String name;
	public int cost;
	public String type;
	public Player owner;
	
	public Square(String name, int cost, String type, Player owner)
	{
		this.name = name;
		this.cost = cost;
		this.type = type;
		this.owner = owner;
	}
	public Square(String name)
	{
		this.type = "Action Square";
		this.name = name;
		this.cost = 0;
	}
	
	
	public void sell(Player owner)
	{
		this.owner = owner;
	}
	

}
