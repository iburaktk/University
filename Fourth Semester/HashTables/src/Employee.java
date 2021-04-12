public class Employee
{
	private String employeeCode;
	private String numNRIC;
	private int phoneNumber;
	
	public Employee()
	{
		
	}
	
	public Employee(String employeeCode, String numNRIC, int phoneNumber)
	{
		this.employeeCode = employeeCode;
		this.numNRIC = numNRIC;
		this.phoneNumber = phoneNumber;
	}
	
	public String getEmployeeCode() {
		return employeeCode;
	}
	
	public String getNumNRIC() {
		return numNRIC;
	}
	
	public int getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	
	public void setNRIC(String numNRIC) {
		this.numNRIC = numNRIC;
	}
	
	public void setPhoneNumber(int phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}