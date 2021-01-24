
public class Item {
public Item(String name,float cost,int amount) 
{
	this.name = name;
	this.cost = cost;
	this.amount = amount;
}

public String name = "";
public float cost = 0;
private int amount = 0;
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public float getCost() {
	return cost;
}
public void setCost(float cost) {
	this.cost = cost;
}
public int getAmount() {
	return amount;
}
public void setAmount(int amount) {
	this.amount = amount;
}
}
