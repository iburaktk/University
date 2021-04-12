import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

public class Main
{
	public static void main(String[] args) throws FileNotFoundException
	{
		Maze maze = new Maze(args[0]);
		maze.findShortestPath();
	}	
}

class Maze
{
	private Block[][] maze;
	public int n;
	
	public Maze(String inputFileName) throws FileNotFoundException
	{
		File inputFile = new File(inputFileName);
		Scanner read = new Scanner(inputFile);
		int n = read.nextInt();
		read.nextLine();
		
		this.n = n;
		maze = new Block[n][n];
		for (int i=0;i<n;i++)
			for (int j=0;j<n;j++)
				maze[i][j] = new Block();
		
		int i=0;
		while (read.hasNext())
		{
			String line = read.nextLine();
			int j=0;
			for (String value : line.split(","))
			{
				maze[i][j].value = Integer.valueOf(value);
				j++;
			}
			i++;
		}
	}
	
	public void printMaze()
	{
		System.out.println("Maze: ");
		for (int i=0;i<n;i++)
		{
			for (int j=0;j<n;j++)
				System.out.print("\t"+maze[i][j].value);
			System.out.println();
		}
		System.out.println("Maze distances: (Dijkstra's algorithm)");
		for (int i=0;i<n;i++)
		{
			for (int j=0;j<n;j++)
				System.out.print("\t"+maze[i][j].distTo);
			System.out.println();
		}
	}
	
	public void findShortestPath()
	{
		calculateDistance();
		printMaze();
		int i=n-1,j=n-1;
		StringBuilder output = new StringBuilder();
		int location = n*n-1;
		while(location != 0)
		{
			if (i == 0)
			{
				output.insert(0," "+(location-1)+"->"+location);
				j--;
			}
			else if (j == 0)
			{
				output.insert(0," "+(location-n)+"->"+location);
				i--;
			}
			else 
			{
				int leftValue = maze[i][j-1].distTo;
				int topValue = maze[i-1][j].distTo;
				if (topValue <= leftValue)
				{
					output.insert(0," "+(location-n)+"->"+location);
					i--;
				}
				else if (leftValue < topValue)
				{
					output.insert(0," "+(location-1)+"->"+location);
					j--;
				}
				
			}
			location = i*n + j;
		}
		System.out.println("Path:"+output.toString());	
		
	}
	
	public void calculateDistance()
	{
		for (int i=0;i<n;i++)
		{
			for (int j=0;j<n;j++)
			{
				if (i == 0 && j == 0)
					maze[0][0].distTo = maze[0][0].value;
				else if (i == 0)
					maze[i][j].distTo = maze[i][j-1].distTo + maze[i][j].value;
				else if (j == 0)
					maze[i][j].distTo = maze[i-1][j].distTo + maze[i][j].value;
				else 
				{
					int leftValue = maze[i][j-1].distTo;
					int topValue = maze[i-1][j].distTo;
					if (leftValue >= topValue)
						maze[i][j].distTo = topValue + maze[i][j].value;
					else 
						maze[i][j].distTo = leftValue + maze[i][j].value;
				}
			}
		}
	}
	
}