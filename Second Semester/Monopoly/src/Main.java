import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import org.json.simple.*;
import org.json.simple.parser.*;


public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		
		Player[] players = new Player[3];
		players[1] = new Player("1",1000000);
		players[2] = new Player("2",1000000);
		players[0] = new Player("Bank",100000);
		
		Square[] squares = new Square[40];
		squares[0] = new Square("Go");
		squares[10] = new Square("Jail");
		squares[20] = new Square("Free Parking");
		squares[30] = new Square("Go to jail");
		squares[4] = new Square("Income Tax");
		squares[38] = new Square("Super Tax");
		squares[2] = new Square("Community Chest");
		squares[17] = new Square("Community Chest");
		squares[33] = new Square("Community Chest");
		squares[7] = new Square("Chance");
		squares[22] = new Square("Chance");
		squares[36] = new Square("Chance");
		
		JSONParser parser = new JSONParser();
		
		try {
			
			JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("src\\property.json"));
			Square[] lands = new Square[22];
			for (int i=0;i<21;i++)
			{
				int landLocation = Integer.valueOf((String) ((JSONObject)parser.parse(jsonObject.get("1").toString().replace("[","").split("},")[i]+"}")).get("id"));
				String landName = (String)((JSONObject)parser.parse(jsonObject.get("1").toString().replace("[","").split("},")[i]+"}")).get("name");
				int landCost = Integer.valueOf((String) ((JSONObject)parser.parse(jsonObject.get("1").toString().replace("[","").split("},")[i]+"}")).get("cost"));
				Square aLand = new Square(landName, landCost, "Land", players[0]);
				squares[landLocation-1] = aLand;
			}
			Square aLand = new Square((String)((JSONObject)parser.parse(jsonObject.get("1").toString().replace("]","").split("},")[21])).get("name"), Integer.valueOf((String) ((JSONObject)parser.parse(jsonObject.get("1").toString().replace("]","").split("},")[21])).get("cost")),"Land", players[0]);
			squares[Integer.valueOf((String) ((JSONObject)parser.parse(jsonObject.get("1").toString().replace("]","").split("},")[21])).get("id")) -1] = aLand;
			
			Square[] railroads = new Square[22];
			for (int i=0;i<3;i++)
			{
				int railroadLocation = Integer.valueOf((String) ((JSONObject)parser.parse(jsonObject.get("2").toString().replace("[","").split("},")[i]+"}")).get("id"));
				String railroadName = (String)((JSONObject)parser.parse(jsonObject.get("2").toString().replace("[","").split("},")[i]+"}")).get("name");
				int railroadCost = Integer.valueOf((String) ((JSONObject)parser.parse(jsonObject.get("2").toString().replace("[","").split("},")[i]+"}")).get("cost"));
				Square aRailroad = new Square(railroadName, railroadCost, "Railroad", players[0]);
				squares[railroadLocation-1] = aRailroad;
			}
			Square aRailroad = new Square((String)((JSONObject)parser.parse(jsonObject.get("2").toString().replace("]","").split("},")[3])).get("name"), Integer.valueOf((String) ((JSONObject)parser.parse(jsonObject.get("2").toString().replace("]","").split("},")[3])).get("cost")), "Railroad", players[0]);
			squares[Integer.valueOf((String) ((JSONObject)parser.parse(jsonObject.get("2").toString().replace("]","").split("},")[3])).get("id")) -1] = aRailroad;
			
			Square[] companies = new Square[22];
			int companyLocation = Integer.valueOf((String) ((JSONObject)parser.parse(jsonObject.get("3").toString().replace("[","").split("},")[0]+"}")).get("id"));
			String companyName = (String)((JSONObject)parser.parse(jsonObject.get("3").toString().replace("[","").split("},")[0]+"}")).get("name");
			int companyCost = Integer.valueOf((String) ((JSONObject)parser.parse(jsonObject.get("3").toString().replace("[","").split("},")[0]+"}")).get("cost"));
			Square aCompany = new Square(companyName, companyCost, "Company", players[0]);
			squares[companyLocation-1] = aCompany;
			aCompany = new Square((String)((JSONObject)parser.parse(jsonObject.get("3").toString().replace("]","").split("},")[1])).get("name"), Integer.valueOf((String) ((JSONObject)parser.parse(jsonObject.get("3").toString().replace("]","").split("},")[1])).get("cost")), "Company", players[0]);
			squares[Integer.valueOf((String) ((JSONObject)parser.parse(jsonObject.get("3").toString().replace("]","").split("},")[1])).get("id")) -1] = aCompany;
						
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		
		/*for (int i=0;i<40;i++)
		{
			System.out.printf("%d\t%-25s %d\t\t%-20s\n",i+1,squares[i].name,squares[i].cost,squares[i].type);
		}*/
		
		Card[] chance = new Card[6];
		Card[] communityChest = new Card[8];
		int chanceCount = 0;
		int communityChestCount = 0;
		
		
		try {
			JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("src\\list.json"));
			chance = new Card[6];
			for (int i=0;i<6;i++)
			{
				String chances = (String) ((JSONObject) parser.parse(jsonObject.get("chanceList").toString().replace("[","").replace("]","").split(",")[i])).get("item");
				Card aCard = new Card(chances);
				chance[i] = aCard;
			}
			
			communityChest = new Card[8];
			for (int i=0;i<8;i++)
			{
				String communityChests = (String) ((JSONObject) parser.parse(jsonObject.get("communityChestList").toString().replace("[","").replace("]","").split(",")[i])).get("item");
				Card aCard = new Card(communityChests);
				communityChest[i] = aCard;
			}
			
			
			/*for (int i=0;i<chanceCardNum;i++)
			{
				System.out.printf("Chance: %s\n",chance[i].message);
			}
			for (int i=0;i<communityChestCardNum;i++)
			{
				System.out.printf("Community Chest: %s\n",communityChest[i].message);
			}*/
			
		}catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		
		String winner = "";
		
		File commands = new File("src\\command6.txt");
		Scanner input = new Scanner(commands);
		while (input.hasNext())
		{
			String aCommand = input.nextLine();
			if ("show()".equals(aCommand))
			{
				if (players[1].money>players[2].money)
				{
					winner = "Player 1";
				}
				else if (players[1].money == players[2].money)
				{
					winner = "scoreless";
				}
				else 
				{
					winner = "Player 2";
				}
				System.out.printf("-----------------------------------------------------------------------------------------------------------\n"
						+ "Player 1\t%d\thave: %s\n"
						+ "Player 2\t%d\thave: %s\n"
						+ "Banker\t%d\n"
						+ "Winner %s\n"
						+ "-----------------------------------------------------------------------------------------------------------\n"
				,players[1].money,players[1].showProperties(),players[2].money,players[2].showProperties(),players[0].money,winner);
			}
			else 
			{
				String[] command = aCommand.split(";");
				String playerName = command[0];
				int dice = Integer.valueOf(command[1]);
				int playerNum;
				int otherPlayerNum;
				if("Player 1".equals(playerName))
				{
					playerNum = 1;
					otherPlayerNum = 2;
				}
				else 
				{
					playerNum = 2;
					otherPlayerNum = 1;
				}
				players[playerNum].currentLocation += dice;
				if (players[playerNum].currentLocation / 40.0 >= 1)
				{
					players[playerNum].money += 200;
					players[0].money -= 200;
				}
				players[playerNum].currentLocation = players[playerNum].currentLocation % 40;
				if (players[playerNum].inJail == 1)
				{
					players[playerNum].currentLocation = 10;
					players[playerNum].jailCount++;
					System.out.printf("%s\t%d\t%d\t%d\t%d\t%s in jail (count=%d)\n",playerName,dice,players[playerNum].currentLocation+1,players[1].money,players[2].money,playerName,players[playerNum].jailCount);
					if (players[playerNum].jailCount == 3)
					{
						players[playerNum].free();
					}
				}
				else if (squares[players[playerNum].currentLocation].type.equals("Action Square"))
				{
					if (squares[players[playerNum].currentLocation].name.equals("Go"))
					{
						System.out.printf("%s\t%d\t%d\t%d\t%d\t%s is in GO square\n",playerName,dice,players[playerNum].currentLocation+1,players[1].money,players[2].money,playerName);
					}
					else if (squares[players[playerNum].currentLocation].name.equals("Go to jail"))
					{
						players[playerNum].currentLocation = 10;
						players[playerNum].jail();
						System.out.printf("%s\t%d\t%d\t%d\t%d\t%s went to jail\n",playerName,dice,players[playerNum].currentLocation+1,players[1].money,players[2].money,playerName);
					}
					else if (squares[players[playerNum].currentLocation].name.equals("Jail"))
					{
						players[playerNum].jail();
						System.out.printf("%s\t%d\t%d\t%d\t%d\t%s went to jail\n",playerName,dice,players[playerNum].currentLocation+1,players[1].money,players[2].money,playerName);
					}
					else if (squares[players[playerNum].currentLocation].name.equals("Free Parking"))
					{
						System.out.printf("%s\t%d\t%d\t%d\t%d\t%s is in Free Parking\n",playerName,dice,players[playerNum].currentLocation+1,players[1].money,players[2].money,playerName);
					}
					else if (squares[players[playerNum].currentLocation].name.equals("Income Tax") || squares[players[playerNum].currentLocation].name.equals("Super Tax"))
					{
						players[playerNum].money -= 100;
						players[0].money += 100;
						System.out.printf("%s\t%d\t%d\t%d\t%d\t%s paid Tax\n",playerName,dice,players[playerNum].currentLocation+1,players[1].money,players[2].money,playerName);
					}
					else if (squares[players[playerNum].currentLocation].name.equals("Chance"))
					{
						String message = chance[chanceCount].message;
						chanceCount++;
						chanceCount = chanceCount % 6;
						if ("Advance to Go (Collect $200)".equals(message)) 
						{
							players[playerNum].currentLocation = 0;
							players[playerNum].money+=200;
							players[0].money -= 200;
							System.out.printf("%s\t%d\t%d\t%d\t%d\t%s draw %s\n",playerName,dice,players[playerNum].currentLocation+1,players[1].money,players[2].money,playerName,message);
						}
						else if ("Advance to Leicester Square".equals(message))
						{
							players[playerNum].currentLocation = 26;
							System.out.printf("%s\t%d\t%d\t%d\t%d\t%s draw %s ",playerName,dice,players[playerNum].currentLocation+1,players[1].money,players[2].money,playerName,message);
							if (squares[26].owner == players[0])
							{
								if (players[playerNum].money >= squares[26].cost)
								{
									squares[26].owner = players[playerNum];
									players[playerNum].buy(squares[26]);
									players[0].money += squares[26].cost;
									System.out.printf("Player %d bought Leicester Square\n",playerNum);
								}
								else 
								{
									System.out.printf("%s\t%d\t%d\t%d\t%d\t%s goes bankrupt\n",playerName,dice,players[playerNum].currentLocation+1,players[1].money,players[2].money,playerName);
									break;
								}
							}
							else if (squares[26].owner != players[playerNum]) 
							{
								int rent = (int) (squares[26].cost * 0.3);
								players[playerNum].money -= rent;
								players[otherPlayerNum].money += rent;
								System.out.printf("Player %d paid rent for Leicester Square\n",playerNum);
								//kira �deyebiliyor mu bak
							}
							else { System.out.printf("Player %d is in Leicester Square\n",playerNum); }
						}
						else if ("Go back 3 spaces".equals(message))
						{
							players[playerNum].currentLocation -= 3;
							System.out.printf("%s\t%d\t%d\t%d\t%d\t%s draw %s\n",playerName,dice,players[playerNum].currentLocation+1,players[1].money,players[2].money,playerName,message);
						}
						else if ("Pay poor tax of $15".equals(message))
						{
							players[playerNum].money -= 15;
							players[0].money += 15;
							System.out.printf("%s\t%d\t%d\t%d\t%d\t%s draw %s\n",playerName,dice,players[playerNum].currentLocation+1,players[1].money,players[2].money,playerName,message);
						}
						else if ("Your building loan matures - collect $150".equals(message))
						{
							players[playerNum].money += 150;
							players[0].money += 150;
							System.out.printf("%s\t%d\t%d\t%d\t%d\t%s draw %s\n",playerName,dice,players[playerNum].currentLocation+1,players[1].money,players[2].money,playerName,message);
						}
						else if ("You have won a crossword competition - collect $100 ".equals(message))
						{
							players[playerNum].money += 100;
							players[0].money -= 100;
							System.out.printf("%s\t%d\t%d\t%d\t%d\t%s draw %s\n",playerName,dice,players[playerNum].currentLocation+1,players[1].money,players[2].money,playerName,message);
						}
					}
					else if (squares[players[playerNum].currentLocation].name.equals("Community Chest"))
					{
						String message = communityChest[communityChestCount].message;
						communityChestCount++;
						communityChestCount = communityChestCount % 8;
						if ("Advance to Go (Collect $200)".equals(message))
						{
							players[playerNum].currentLocation = 0;
							players[playerNum].money+=200;
							players[0].money -= 200;
							System.out.printf("%s\t%d\t%d\t%d\t%d\t%s draw %s\n",playerName,dice,players[playerNum].currentLocation+1,players[1].money,players[2].money,playerName,message);
						}
						else if ("Bank error in your favor - collect $75".equals(message))
						{
							players[playerNum].money += 75;
							players[0].money -= 75;
							System.out.printf("%s\t%d\t%d\t%d\t%d\t%s draw %s\n",playerName,dice,players[playerNum].currentLocation+1,players[1].money,players[2].money,playerName,message);
						}
						else if ("Doctor's fees - Pay $50".equals(message))
						{
							players[playerNum].money -= 50;
							players[0].money += 50;
							System.out.printf("%s\t%d\t%d\t%d\t%d\t%s draw %s\n",playerName,dice,players[playerNum].currentLocation+1,players[1].money,players[2].money,playerName,message);
						}
						else if ("It is your birthday Collect $10 from each player".equals(message))
						{
							players[playerNum].money += 10;
							players[otherPlayerNum].money -= 10;
							System.out.printf("%s\t%d\t%d\t%d\t%d\t%s draw %s\n",playerName,dice,players[playerNum].currentLocation+1,players[1].money,players[2].money,playerName,message);
						}
						else if ("Grand Opera Night - collect $50 from every player for opening night seats".equals(message))
						{
							players[playerNum].money += 50;
							players[otherPlayerNum].money -= 50;
							System.out.printf("%s\t%d\t%d\t%d\t%d\t%s draw %s\n",playerName,dice,players[playerNum].currentLocation+1,players[1].money,players[2].money,playerName,message);
						}
						else if ("Income Tax refund - collect $20".equals(message))
						{
							players[playerNum].money += 20;
							players[0].money -= 20;
							System.out.printf("%s\t%d\t%d\t%d\t%d\t%s draw %s\n",playerName,dice,players[playerNum].currentLocation+1,players[1].money,players[2].money,playerName,message);
						}
						else if ("Life Insurance Matures - collect $100".equals(message))
						{
							players[playerNum].money += 100;
							players[0].money -= 100;
							System.out.printf("%s\t%d\t%d\t%d\t%d\t%s draw %s\n",playerName,dice,players[playerNum].currentLocation+1,players[1].money,players[2].money,playerName,message);
						}
						else if ("Pay Hospital Fees of $100".equals(message))
						{
							players[playerNum].money -= 100;
							players[0].money += 100;
							System.out.printf("%s\t%d\t%d\t%d\t%d\t%s draw %s\n",playerName,dice,players[playerNum].currentLocation+1,players[1].money,players[2].money,playerName,message);
						}
						else if ("Pay School Fees of $50".equals(message))
						{
							players[playerNum].money -= 50;
							players[0].money += 50;
							System.out.printf("%s\t%d\t%d\t%d\t%d\t%s draw %s\n",playerName,dice,players[playerNum].currentLocation+1,players[1].money,players[2].money,playerName,message);
						}
						else if ("You inherit $100".equals(message))
						{
							players[playerNum].money += 100;
							players[0].money -= 100;
							System.out.printf("%s\t%d\t%d\t%d\t%d\t%s draw %s\n",playerName,dice,players[playerNum].currentLocation+1,players[1].money,players[2].money,playerName,message);
						}
						else if ("From sale of stock you get $50".equals(message))
						{
							players[playerNum].money += 50;
							players[0].money -= 50;
							System.out.printf("%s\t%d\t%d\t%d\t%d\t%s draw %s\n",playerName,dice,players[playerNum].currentLocation+1,players[1].money,players[2].money,playerName,message);
						}
					}
				}
				else if (squares[players[playerNum].currentLocation].owner == players[0])
				{
					if (players[playerNum].money >= squares[players[playerNum].currentLocation].cost)
					{
						squares[players[playerNum].currentLocation].owner = players[playerNum];
						players[playerNum].buy(squares[players[playerNum].currentLocation]);
						players[0].money += squares[players[playerNum].currentLocation].cost;
						System.out.printf("%s\t%d\t%d\t%d\t%d\t%s bought %s\n",playerName,dice,players[playerNum].currentLocation+1,players[1].money,players[2].money,playerName,squares[players[playerNum].currentLocation].name);
					}
					else 
					{
						System.out.printf("%s\t%d\t%d\t%d\t%d\t%s goes bankrupt\n",playerName,dice,players[playerNum].currentLocation+1,players[1].money,players[2].money,playerName);
						break;
					}
				}
				// railroad dan bor� almas�na falan bak yani bankrupt olcak m� diye
				else 
				{
					if (squares[players[playerNum].currentLocation].owner != players[playerNum])
					{
						int rent = 0;
						if (squares[players[playerNum].currentLocation].type.equals("Land"))
						{
							if (squares[players[playerNum].currentLocation].cost <= 2000)
							{
								rent = (int) (squares[players[playerNum].currentLocation].cost * 0.4);
							}
							else if (squares[players[playerNum].currentLocation].cost <= 3000)
							{
								rent = (int) (squares[players[playerNum].currentLocation].cost * 0.3);
							}
							else if (squares[players[playerNum].currentLocation].cost <= 4000)
							{
								rent = (int) (squares[players[playerNum].currentLocation].cost * 0.35);
							}
							else {
								rent = 0;
							}
							players[playerNum].money -= rent;
							players[otherPlayerNum].money += rent;
						}
						else if (squares[players[playerNum].currentLocation].type.equals("Company"))
						{
							rent = 4*dice;
							players[playerNum].money -= rent;
							players[otherPlayerNum].money += rent;
						}
						else if (squares[players[playerNum].currentLocation].type.equals("Raildroad"))
						{
							rent = 25*players[otherPlayerNum].railroadHave;
							players[playerNum].money -= rent;
							players[otherPlayerNum].money += 25*players[otherPlayerNum].railroadHave;
						}
						System.out.printf("%s\t%d\t%d\t%d\t%d\t%s paid rent for %s\n",playerName,dice,players[playerNum].currentLocation+1,players[1].money,players[2].money,playerName,squares[players[playerNum].currentLocation].name);
						if (players[playerNum].money < 0)
						{
							players[playerNum].money += rent;
							players[otherPlayerNum].money -= rent;
							//buraya bak bence
							break;
						}
					}
					else 
					{
						System.out.printf("%s\t%d\t%d\t%d\t%d\t%s is in %s\n",playerName,dice,players[playerNum].currentLocation+1,players[1].money,players[2].money,playerName,squares[players[playerNum].currentLocation].name);
					}
				}
			}
		}
		if (players[1].money>players[2].money)
		{
			winner = "Player 1";
		}
		else if (players[1].money == players[2].money)
		{
			winner = "scoreless";
		}
		else 
		{
			winner = "Player 2";
		}
		
		System.out.printf("-----------------------------------------------------------------------------------------------------------\n"
						+ "Player 1\t%d\thave: %s\n"
						+ "Player 2\t%d\thave: %s\n"
						+ "Banker\t%d\n"
						+ "Winner %s\n"
						+ "-----------------------------------------------------------------------------------------------------------\n"
			,players[1].money,players[1].showProperties(),players[2].money,players[2].showProperties(),players[0].money,winner);
	
	}
}