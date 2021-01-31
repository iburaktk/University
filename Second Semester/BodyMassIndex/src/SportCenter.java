/**
 * 
 * @author İbrahim Burak Tanrıkulu
 *
 */


public class SportCenter {
	
	public String sportCenterName;
	static Member[] memberArray = new Member[10];
	static int count = 0;
	
	
	public SportCenter(String sportCenterName)
	{
		this.sportCenterName = sportCenterName;
	}
	
	
	public static void addMember(String name,String surname,float weight,float height)
	{
		if (count == 10)
		{
			System.out.printf("Reached member limit. Can't add member.");
			return;
		}
		memberArray[count] = new Member(name,surname,weight,height);
		memberArray[count].setName(name);
		memberArray[count].setSurname(surname);
		memberArray[count].setWeight(weight);
		memberArray[count].setHeight(height);
		memberArray[count].setMemberId(count);
		count++;
	}
	
	public static String search(String name,String surname)
	{
		if (count == 0)
		{
			return "There is no member yet. Please firstly add a member.\n";
		}
		int i;
		for (i=0;i<count;i++)
		{
			if (name.equals(memberArray[i].getName()) && surname.equals(memberArray[i].getSurname()))
			{
				return memberArray[i].WeightStatus() + "\n";
			}
		}
		return "Member cannot found.\n";
	}
	
	public static int printAllMembers()
	{
		if (count == 0)
		{
			System.out.println("There is no member yet. Please firstly add a member.\n");
			return 0;
		}
		for (int i=0;i<count;i++)
		{
			memberArray[i].Display();
		}
		return 1;
	}
	
	

}
