import java.io.IOException;
import java.util.*;
public class Main {
	
	public static void main(String[] args) {
		int choose = 0 ;
		Scanner input = new Scanner(System.in);
		while (choose != 4)
		{
			System.out.println("1-Add a new member\n2-Display all members\n3-Search\n4-Exit\nEnter your choose");
			choose = input.nextInt();
			if (choose == 1)
			{				
				String name;
				String surname;
				float height;
				float weight;
				
				System.out.println("Enter name");
				name = input.next();
				System.out.println("Enter surname");
				surname = input.next();
				System.out.println("Enter weight");
				weight = input.nextFloat();
				System.out.println("Enter height");
				height = input.nextFloat();
				
				SportCenter.addMember(name,surname,weight,height);
			}
			else if (choose == 2)
			{
				SportCenter.printAllMembers();
			}
			else if (choose == 3)
			{
				String name;
				String surname;
				System.out.println("Enter name");
				name = input.next();
				System.out.println("Enter surname");
				surname = input.next();
				
				System.out.printf("%s",SportCenter.search(name,surname));
			}
		}
		return;
	}

}
