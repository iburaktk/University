import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) throws IOException {
		
		File fixtures = new File(args[0]);
		Scanner input = new Scanner(fixtures);
	
		int footballTeamsNum = 0;
		int basketballTeamsNum = 0;
		int volleyballTeamsNum = 0;
		while(input.hasNext())
		{
			String match = input.nextLine();
			if (match.charAt(0) == 'F')
			{
				footballTeamsNum++;
			}
			else if (match.charAt(0) == 'B')
			{
				basketballTeamsNum++;
			}
			else if (match.charAt(0) == 'V')
			{
				volleyballTeamsNum++;
			}
		}
		
		Team[] footballTeams = new footballTeam[footballTeamsNum];
		Team[] basketballTeams = new basketballTeam[basketballTeamsNum];
		Team[] volleyballTeams = new volleyballTeam[volleyballTeamsNum];
		int footballTeamsCount=0,basketballTeamsCount=0,volleyballTeamsCount=0;
		Team[][] teams = {footballTeams,basketballTeams,volleyballTeams};
		input = new Scanner(fixtures);
		while (input.hasNext())
		{
			String match = input.nextLine();
			int whichSport = (match.charAt(0) == 'F') ? 0: (match.charAt(0) == 'B') ? 1 : (match.charAt(0) == 'V') ? 2 : 3 ;
			String[] match1 = match.split("\t");
			int teamsNum = (whichSport == 0) ? footballTeamsCount : (whichSport == 1) ? basketballTeamsCount : volleyballTeamsCount ;
			int location1 = -1;
			for (int i=0;i<teamsNum;i++)
			{
				if (match1[1].equals(teams[whichSport][i].name))
				{
					location1 = i;
					break;
				}
			}
			if (location1 == -1)
			{
				location1 = (whichSport == 0) ? footballTeamsCount : (whichSport == 1) ? basketballTeamsCount : volleyballTeamsCount;
				if (whichSport==0)
				{
					teams[whichSport][location1] = new footballTeam();
					footballTeamsCount++;
				}
				if (whichSport==1)
				{
					teams[whichSport][location1] = new basketballTeam();
					basketballTeamsCount++;
				}
				if (whichSport==2)
				{
					teams[whichSport][location1] = new volleyballTeam();
					volleyballTeamsCount++;
				}
				teams[whichSport][location1].setName(match1[1]);
			}
			int location2 = -1;
			for (int i=0;i<teamsNum;i++)
			{
				if (match1[2].equals(teams[whichSport][i].name))
				{
					location2 = i;
					break;
				}
			}
			if (location2 == -1)
			{
				location2 = (whichSport == 0) ? footballTeamsCount : (whichSport == 1) ? basketballTeamsCount : volleyballTeamsCount;
				if (whichSport==0)
				{
					teams[whichSport][location2] = new footballTeam();
					footballTeamsCount++;
				}
				if (whichSport==1)
				{
					teams[whichSport][location2] = new basketballTeam();
					basketballTeamsCount++;
				}
				if (whichSport==2)
				{
					teams[whichSport][location2] = new volleyballTeam();
					volleyballTeamsCount++;
				}
				teams[whichSport][location2].name = match1[2];
			}
			String score = match1[3];
			if (whichSport == 0)
			{
				teams[0][location1].increaseNumberOfGoalsFor(Character.getNumericValue(score.charAt(0)));
				teams[0][location2].increaseNumberOfGoalsFor(Character.getNumericValue(score.charAt(4)));
				teams[0][location1].increaseNumberOfGoalsAgainst(Character.getNumericValue(score.charAt(4)));
				teams[0][location2].increaseNumberOfGoalsAgainst(Character.getNumericValue(score.charAt(0)));
				teams[0][location1].increasePlayedMatches();
				teams[0][location2].increasePlayedMatches();
				if (score.charAt(0) > score.charAt(4))
				{
					teams[0][location1].increaseMatchesWon();
				}
				else if (score.charAt(0) == score.charAt(4))
				{
					teams[0][location1].increaseMatchesDraw();
					teams[0][location2].increaseMatchesDraw();
				}
				else 
				{
					teams[0][location2].increaseMatchesWon();
				}
			}
			else if (whichSport == 1)
			{
				String[] scores = score.split(" : ");
				teams[1][location1].increaseNumberOfPointsFor(Integer.valueOf(scores[0]));
				teams[1][location2].increaseNumberOfPointsFor(Integer.valueOf(scores[1]));
				teams[1][location1].increaseNumberOfPointsAgainst(Integer.valueOf(scores[1]));
				teams[1][location2].increaseNumberOfPointsAgainst(Integer.valueOf(scores[0]));
				teams[1][location1].increasePlayedMatches();
				teams[1][location2].increasePlayedMatches();
				if (Integer.valueOf(scores[0]) > Integer.valueOf(scores[1]))
				{

					teams[1][location1].increaseMatchesWon();
				}
				else 
				{
					teams[1][location2].increaseMatchesWon();
				}
				
			}
			else if (whichSport == 2)
			{
				teams[2][location1].increaseNumberOfSetsFor(Character.getNumericValue(score.charAt(0)));
				teams[2][location2].increaseNumberOfSetsFor(Character.getNumericValue(score.charAt(4)));
				teams[2][location1].increaseNumberOfSetsAgainst(Character.getNumericValue(score.charAt(4)));
				teams[2][location2].increaseNumberOfSetsAgainst(Character.getNumericValue(score.charAt(0)));
				teams[2][location1].increasePlayedMatches();
				teams[2][location2].increasePlayedMatches();
				if (score.charAt(0) == '3' && Character.getNumericValue(score.charAt(4)) < 2 )
				{					
					teams[2][location1].increaseWonEasily();
					teams[2][location1].increaseMatchesWon();
				}
				else if (score.charAt(0) == '3' && score.charAt(4) == '2')
				{
					teams[2][location1].increaseMatchesWon();
					teams[2][location2].increaseLoseNearly();
				}
				else if (score.charAt(0) == '2' && score.charAt(4) == '3')
				{
					teams[2][location1].increaseLoseNearly();
					teams[2][location2].increaseMatchesWon();
				}
				else 
				{
					teams[2][location2].increaseWonEasily();
					teams[2][location2].increaseMatchesWon();
				}
			}
		}
		for (int i=0;i<footballTeamsCount;i++)
		{
			int temp = 0;
			int score = 0;
			for (int j=i;j<footballTeamsCount;j++)
			{
				if (teams[0][j].calculatePoint() > score)
				{
					score = teams[0][j].calculatePoint();
					temp = j;
				}
				else if (teams[0][j].calculatePoint() == score)
				{
					int avarage1 = teams[0][temp].numberOfGoalsFor-teams[0][temp].numberOfGoalsAgainst;
					int avarage2 = teams[0][j].numberOfGoalsFor-teams[0][j].numberOfGoalsAgainst;
					if (avarage1<avarage2)
					{
						temp = j;
					}
				}
			}
			Team tempTeam = teams[0][i];
			teams[0][i] = teams[0][temp];
			teams[0][temp] = tempTeam;
		}
		PrintWriter out = new PrintWriter(new FileWriter(args[1]));
		for (int i=0;i<footballTeamsCount;i++)
		{
			out.printf("%d.\t%s\t%d\t%d\t%d\t%d\t%d:%d\t%d",i+1,teams[0][i].name,teams[0][i].playedMatches,teams[0][i].matchesWon,teams[0][i].matchesDraw,teams[0][i].playedMatches-teams[0][i].matchesWon-teams[0][i].matchesDraw,teams[0][i].numberOfGoalsFor,teams[0][i].numberOfGoalsAgainst,teams[0][i].calculatePoint());
			if (i != footballTeamsCount-1)
			{
				out.print("\n");
			}		
		}
		out.close();
		for (int i=0;i<basketballTeamsCount;i++)
		{
			int temp = 0;
			int score = 0;
			for (int j=i;j<basketballTeamsCount;j++)
			{
				if (teams[1][j].calculatePoint() > score)
				{
					score = teams[1][j].calculatePoint();
					temp = j;
				}
				else if (teams[1][j].calculatePoint() == score)
				{
					int avarage1 = teams[1][temp].numberOfPointsFor-teams[1][temp].numberOfPointsAgainst;
					int avarage2 = teams[1][j].numberOfPointsFor-teams[1][j].numberOfPointsAgainst;
					if (avarage1<avarage2)
					{
						temp = j;
					}
				}
			}
			Team tempTeam = teams[1][i];
			teams[1][i] = teams[1][temp];
			teams[1][temp] = tempTeam;
		}
		out = new PrintWriter(new FileWriter(args[2]));
		for (int i=0;i<basketballTeamsCount;i++)
		{
			out.printf("%d.\t%s\t%d\t%d\t%d\t%d:%d\t%d",i+1,teams[1][i].name,teams[1][i].playedMatches,teams[1][i].matchesWon,teams[1][i].playedMatches-teams[1][i].matchesWon,teams[1][i].numberOfPointsFor,teams[1][i].numberOfPointsAgainst,teams[1][i].calculatePoint());
			if (i != basketballTeamsCount-1)
			{
				out.print("\n");
			}
		}
		out.close();
		for (int i=0;i<volleyballTeamsCount;i++)
		{
			int temp = 0;
			int score = 0;
			for (int j=i;j<volleyballTeamsCount;j++)
			{
				if (teams[2][j].calculatePoint() > score)
				{
					score = teams[2][j].calculatePoint();
					temp = j;
				}
				else if (teams[2][j].calculatePoint() == score)
				{
					int avarage1 = teams[2][temp].numberOfSetsFor-teams[2][temp].numberOfSetsAgainst;
					int avarage2 = teams[2][j].numberOfSetsFor-teams[2][j].numberOfSetsAgainst;
					if (avarage1<avarage2)
					{
						temp = j;
					}
				}
			}
			Team tempTeam = teams[2][i];
			teams[2][i] = teams[2][temp];
			teams[2][temp] = tempTeam;
		}
		out = new PrintWriter(new FileWriter(args[3]));
		for (int i=0;i<volleyballTeamsCount;i++)
		{
			out.printf("%d.\t%s\t%d\t%d\t%d\t%d:%d\t%d",i+1,teams[2][i].name,teams[2][i].playedMatches,teams[2][i].matchesWon,teams[2][i].playedMatches-teams[2][i].matchesWon,teams[2][i].numberOfSetsFor,teams[2][i].numberOfSetsAgainst,teams[2][i].calculatePoint());
			if (i != volleyballTeamsCount-1)
			{
				out.print("\n");
			}
		}
		out.close();
	}
}

class Team{
	public String name = "";
	public int playedMatches;
	public int matchesWon;
	public int matchesDraw;
	public int numberOfGoalsFor = 0;
	public int numberOfGoalsAgainst = 0;
	public int numberOfPointsFor;
	public int numberOfPointsAgainst;
	public int wonEasily;
	public int loseNearly;
	public int numberOfSetsFor;
	public int numberOfSetsAgainst;
	public void setName(String name) {
		this.name = name;
	}
	public void increasePlayedMatches() {
		playedMatches++;
	}
	public void increaseMatchesWon() {
		matchesWon++;
	}
	public void increaseMatchesDraw() {
		matchesDraw++;
	}
	public void increaseNumberOfGoalsFor(int numberOfGoalsFor) {
		this.numberOfGoalsFor += numberOfGoalsFor;
	}
	public void increaseNumberOfGoalsAgainst(int numberOfGoalsAgainst) {
		this.numberOfGoalsAgainst += numberOfGoalsAgainst;
	}
	public void increaseNumberOfPointsFor(int numberOfPointsFor) {
		this.numberOfPointsFor += numberOfPointsFor;
	}
	public void increaseNumberOfPointsAgainst(int numberOfPointsAgainst) {
		this.numberOfPointsAgainst += numberOfPointsAgainst;
	}
	public void increaseWonEasily() {
		wonEasily++;
	}
	public void increaseLoseNearly() {
		loseNearly++;
	}
	public void increaseNumberOfSetsFor(int numberOfSetsFor) {
		this.numberOfSetsFor += numberOfSetsFor;
	}
	public void increaseNumberOfSetsAgainst(int numberOfSetsAgainst) {
		this.numberOfSetsAgainst += numberOfSetsAgainst;
	}
	public int calculatePoint()
	{
		return 0;
	}
}
class footballTeam extends Team {
	public void increaseMatchesDraw() {
		this.matchesDraw++;
	}
	public void increaseNumberOfGoalsFor(int numberOfGoalsFor) {
		this.numberOfGoalsFor += numberOfGoalsFor;
	}
	public void increaseNumberOfGoalsAgainst(int numberOfGoalsAgainst) {
		this.numberOfGoalsAgainst += numberOfGoalsAgainst;
	}
	public int calculatePoint()
	{
		return matchesWon*3 + matchesDraw*1;
	}
}
class basketballTeam extends Team {
	
	public void increaseNumberOfPointsFor(int numberOfPointsFor) {
		this.numberOfPointsFor += numberOfPointsFor;
	}
	public void increaseNumberOfPointsAgainst(int numberOfPointsAgainst) {
		this.numberOfPointsAgainst += numberOfPointsAgainst;
	}
	public int calculatePoint()
	{
		return matchesWon*2 + (playedMatches-matchesWon)*1;
	}
}
class volleyballTeam extends Team {

	public void increaseWonEasily() {
		wonEasily++;
	}
	public void increaseLoseNearly() {
		loseNearly++;
	}
	public void increaseNumberOfSetsFor(int numberOfSetsFor) {
		this.numberOfSetsFor += numberOfSetsFor;
	}
	public void increaseNumberOfSetsAgainst(int numberOfSetsAgainst) {
		this.numberOfSetsAgainst += numberOfSetsAgainst;
	}
	public int calculatePoint()
	{
		return wonEasily*3 + (matchesWon-wonEasily)*2 + loseNearly*1;
	}
}

//İbrahim Burak Tanrıkulu

