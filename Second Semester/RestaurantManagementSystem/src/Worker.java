
public class Worker {

	private String name;
	private float salary;
	public int authorization;
	
	public Worker(String name, float salary, int authorization)
	{
		this.name = name;
		this.salary = salary;
		this.authorization = authorization;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getSalary() {
		if (authorization == 1)
		{
			return (float) (salary + salary*tableCreated*(0.1));
		}
		else if (authorization == 2)
		{
			return (float) (salary + salary*orderOperated*(0.05));
		}
		else 
		{
			return 0;
		}
	}
	public void setSalary(float salary) {
		this.salary = salary;
	}

	final int ALLOWED_MAX_TABLES = (authorization == 1) ? 2 : 0 ;
	public int tableCreated;
	final int MAX_TABLE_SERVICE = (authorization == 2) ? 3 : 0 ;
	public int orderOperated;
	
}
