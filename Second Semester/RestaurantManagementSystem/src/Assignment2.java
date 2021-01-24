import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Assignment2 {

	public static void main(String[] args) throws IOException {
		
		final int MAX_EMPLOYER = 5;
		final int MAX_WAITER = 5;
		final int MAX_TABLE = 5;
		final int MAX_ITEM = 10;
		
		Item[] stock = new Item[MAX_ITEM];
		Table[] tables = new Table[MAX_TABLE];
		Worker[] employers = new Worker[MAX_EMPLOYER];
		Worker[] waiters = new Worker[MAX_WAITER];
		
		
		String Command;
		File setup = new File("setup1.dat");
		Scanner input = new Scanner(setup);
		int stockCount=0,employerCount=0,waiterCount=0;
		while(input.hasNext())
		{
			Command = input.next();
			if ("add_item".equals(Command))
			{
				Command = input.next();
				String[] order;
				order = Command.split(";");
				Item newOrder = new Item(order[0],Float.valueOf(order[1]),Integer.valueOf(order[2]));
				stock[stockCount] = newOrder;
				stockCount++;
			}
			else if ("add_employer".equals(Command))
			{
				Command = input.next();
				String[] employer;
				employer = Command.split(";");
				Worker newWorker = new Worker(employer[0], Float.valueOf(employer[1]), 1);
				employers[employerCount] = newWorker;
				employerCount++;
			}
			else if ("add_waiter".equals(Command))
			{
				Command = input.next();
				String[] waiter;
				waiter = Command.split(";");
				Worker newWorker = new Worker(waiter[0],Float.valueOf(waiter[1]),2);
				waiters[waiterCount] = newWorker;
				waiterCount++;
			}
			else 
			{
				System.out.println("Invalid command!\n");
			}
		}
		input.close();
		File commandsFile = new File("commands1.dat");
		input = new Scanner(commandsFile);
		int tableCount=0;
		while (input.hasNext())
		{
			Command = input.next();
			if ("create_table".equals(Command))
			{
				System.out.printf("**********************************\nPROGRESSING COMMAND: %s\n",Command);
				Command = input.next();
				String[] table;
				table = Command.split(";");
				int i;
				for (i=0;i<employerCount;i++)
				{
					if (table[0].equals(employers[i].getName()))
					{
						break;
					}
					if (i == employerCount-1)
					{
						i=-1;
						break;
					}
				}
				if (i != -1)
				{
					if (tableCount + 1 != MAX_TABLE)
					{
						if (employers[i].tableCreated +1 == employers[i].ALLOWED_MAX_TABLES)
						{
							System.out.printf("%s has already created %d tables!",employers[i].getName(),employers[i].ALLOWED_MAX_TABLES);
						}
						else 
						{
							Table newTable = new Table(tableCount, Integer.valueOf(table[1]), employers[i]);
							tables[tableCount] = newTable;
							tableCount++;
							employers[i].tableCreated++;
							System.out.printf("A new table has succesfully been added\n");
							
						}
					}
					else 
					{
						System.out.printf("Not allowed to exceed max. number of tables, %d\n",MAX_TABLE);
					}
				}
				else 
				{
					System.out.printf("There is no employer named %s\n",table[0]);
				}
			}
			else if("new_order".equals(Command))
			{
				System.out.printf("**********************************\nPROGRESSING COMMAND: %s\n",Command);
				Command = input.next();
				String[] order;
				order = Command.split(";");
				for (int i=0;i<tableCount;i++)
				{
					if ((tables[i].capacity >= Integer.valueOf(order[1])) && (tables[i].inService == 0)) 
					{
						tables[i].inService = 1;
						System.out.printf("Table (= ID %d) has been taken into service\n",tables[i].tableId);
						int j;
						for (j=0;j<waiterCount;j++)
						{
							if (order[0].equals(waiters[j].getName()))
							{
								if (waiters[j].orderOperated != waiters[j].MAX_TABLE_SERVICE-1)
								{
									Order theOrder = new Order(order[2],stock,stockCount);
									tables[i].setWaiter(waiters[j]);
									tables[i].takeOrder(theOrder);
									waiters[j].orderOperated++;
								}
								else
								{
									System.out.printf("Not allowed to service max. number of tables, %d\n",waiters[i].MAX_TABLE_SERVICE);
								}
								break;
							}
							else if (j == waiterCount-1)
							{
								System.out.printf("There is no waiter named %s\n",order[0]);
							}
						}
						break;
					}
					else if (i == tableCount-1)
					{
						System.out.printf("There is no appropriate table for this order!\n");
					}
				}
				
			}
			else if ("add_order".equals(Command))
			{
				System.out.printf("**********************************\nPROGRESSING COMMAND: %s\n",Command);
				Command = input.next();
				String[] order;
				order = Command.split(";");
				int i;
				for (i=0;i<tableCount;i++)
				{
					if (Integer.valueOf(order[1]) == tables[i].tableId)
					{
						break;
					}
					else if (i==tableCount-1) 
					{
						System.out.printf("There is no table with this table ID\n");
						i=-1;
						break;
					}
				}
				if (i == -1) { }
				else if(tables[i].inService != 0 && ! tables[i].waiter.getName().equals(order[0]))
				{
					System.out.printf("This table is either not in service now or %s cannot be assigned this table!\n",tables[i].waiter);
				}
				else
				{
					if (tables[i].orderCount+1 != tables[i].MAX_ORDER)
					{
						if (tables[i].waiter.orderOperated+1 == tables[i].waiter.MAX_TABLE_SERVICE)
						{
							System.out.printf("Not allowed to service max. number of tables, %d\n",waiters[i].MAX_TABLE_SERVICE);
						}
						else
						{
							tables[i].addToOrder(order[2]);
							tables[i].waiter.orderOperated++;
						}
					}
					else 
					{
						System.out.printf("Not allowed to exceed max number of orders!\n");
					}
				}
			}
			else if ("check_out".equals(Command))
			{
				System.out.printf("**********************************\nPROGRESSING COMMAND: %s\n",Command);
				Command = input.next();
				String[] check;
				check = Command.split(";");
				int i;
				for (i=0;i<waiterCount;i++)
				{
					if(check[0].equals(waiters[i].getName()))
					{
						break;
					}
					else if (i == waiterCount-1)
					{
						System.out.printf("There is no waiter named %s\n",check[0]);
						i=-1;
						break;
					}
				}
				if (i != -1)
				{
					if((tables[Integer.valueOf(check[1])].inService == 1) && (check[0].equals(tables[Integer.valueOf(check[1])].waiter.getName())))
					{
						tables[Integer.valueOf(check[1])].checkOut();
					}
					else 
					{
						System.out.printf("This table is either not in service now or %s cannot be assigned this table!\n",check[0]);
					}
				}
			}
			else if ("stock_status".equals(Command))
			{
				System.out.printf("**********************************\nPROGRESSING COMMAND: %s\n",Command);
				for (int i=0;i<stockCount;i++)
				{
					System.out.printf("%s:\t%d\n",stock[i].getName(),stock[i].getAmount());
				}
			}
			else if ("get_table_status".equals(Command))
			{
				System.out.printf("**********************************\nPROGRESSING COMMAND: %s\n",Command);
				for (int i=0;i<tableCount;i++)
				{
					if (tables[i].inService == 0)
					{
						System.out.printf("Table %d: Free\n",i);
					}
					else 
					{
						System.out.printf("Table %d: Reserved (%s)\n",i,tables[i].waiter.getName());
					}
				}
			}
			else if ("get_order_status".equals(Command))
			{
				System.out.printf("**********************************\nPROGRESSING COMMAND: %s\n",Command);
				for (int i=0;i<tableCount;i++)
				{
					System.out.printf("Table: %d\n\t%d order(s)\n",i,tables[i].orderCount);
					for (int j=0;j<tables[i].orderCount;j++)
					{
						System.out.printf("\t\t%d item(s)\n",tables[i].itemAmount[j]);
					}
				}
			}
			else if ("get_employer_salary".equals(Command))
			{
				System.out.printf("**********************************\nPROGRESSING COMMAND: %s\n",Command);
				for (int i=0;i<employerCount;i++)
				{
					System.out.printf("Salary for %s: %.1f\n",employers[i].getName(),employers[i].getSalary());
				}
			}
			else if ("get_waiter_salary".equals(Command))
			{
				System.out.printf("**********************************\nPROGRESSING COMMAND: %s\n",Command);
				for (int i=0;i<waiterCount;i++)
				{
					System.out.printf("Salary for %s: %.1f\n",waiters[i].getName(),waiters[i].getSalary());
				}
			}
		}
	}

}
