/**
 * 
 * @author İbrahim Burak Tanrıkulu
 *
 */


public class Member {
	
	
	private int memberId;
	private String name;
	private String surname;
	private float height;
	private float weight;
	
	private static float staticHeight;
	private static float staticWeight;
	
	public Member(String name1,String surname1,float weight1,float height1)
	{
		name = name1;
		surname = surname1;
		height = height1;
		weight = weight1;
		staticHeight = height1;
		staticWeight = weight1;
	}

	public int getMemberId() {
		return memberId;
	}
	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public float getHeight() {
		return height;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	public float getWeight() {
		return weight;
	}
	public void setWeight(float weight) {
		this.weight = weight;
	}
	
	public static float BMI()
	{
		return (staticWeight / (staticHeight * staticHeight));
	}
	public static String WeightStatus()
	{
		return ((BMI() < 18.5) ? "Underweight" : (BMI() < 25) ? "Normal" : (BMI() < 30) ? "Overweight" : (BMI() < 35) ? "Obese" : "Extremely obese" );
	}
	
	public void Display()
	{
		System.out.printf("Id:%d Name:%s Surname:%s Weight:%.1f Height:%.2f\n",memberId,name,surname,weight,height);
	}
	
	
}
