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
		File outputFile = new File(args[1]);
		System.setOut(new PrintStream(outputFile));

		System.out.println("Q2 output:");
		
		Graph graph = new Graph(10);
		graph.printGraph();
		Prim prim = new Prim(graph);
		System.out.print("MST : ");
		for (Edge edge : prim.mst())
			System.out.print(edge.either() + "-" + edge.other()+" ");	
		System.out.println();
		
		System.out.println("Q3 output:");
		
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

class Block
{
	public int value;
	public int distTo;
	
}

class Prim		// 
{
	private boolean[] marked;
	private ArrayList<Edge> mst;
	private ArrayList<Edge> pq;
	
	public Prim(Graph graph)
	{
		pq = new ArrayList<Edge>();
		mst = new ArrayList<Edge>();
		marked = new boolean[graph.getV()];
		visit(graph, 0);
		
		while (!pq.isEmpty())
		{
			Edge e = delMin();
			int v = e.either(), w = e.other();
			if (marked[v] && marked[w]) continue;
			mst.add(e);
			if (!marked[v]) visit(graph, v);
			if (!marked[w]) visit(graph, w);
		}
	}
	
	private void visit(Graph graph, int v)
	{
		marked[v] = true;
		for(Edge e : graph.adj[v])
			if (!marked[e.other()])
				pq.add(e);
		if (graph.getV() == v+1)
			return;
		System.out.print("lazy PQ on "+(v+1)+"th step : ");
		printlazyPQ();
		System.out.print("eager PQ on "+(v+1)+"th step: ");
		printEagerPQ();
		System.out.println();
	}
	
	private void printlazyPQ()
	{
		Collections.sort(pq,new sortEdge());
		int i=0;
		for (Edge edge : pq)
		{
			i++;
			System.out.print(edge.either()+"-"+edge.other()+" ");
		}
		System.out.println(" : "+i);
	}
	private void printEagerPQ()
	{
		int i = pq.get(0).either();
		for (Edge edge : pq)
			if (edge.either() == i)
				System.out.print(edge.either()+"-"+edge.other()+" ");
		System.out.println();
	}
	
	public Iterable<Edge> mst()
	{
		return mst;
	}
	
	public Edge delMin()
	{
		Collections.sort(pq,new sortEdge());
		Edge edge = pq.get(0);
		pq.remove(0);
		return edge;
	}
}

class sortEdge implements Comparator<Edge>		// comparator class for sorting edges.
{
	public int compare(Edge a, Edge b)
	{
		return  (int) a.getWeight() - (int) b.getWeight();
	}
}

class Graph
{
	private final int V;
	public ArrayList<Edge>[] adj;	// adjacency list of the graph
	
	@SuppressWarnings("unchecked")		// gives warning at array of arraylist.
	public Graph(int V)
	{
		// constructor of the graph.
		this.V = V;
		adj = new ArrayList[V];
		for (int i=0;i<V;i++)
			adj[i] = new ArrayList<Edge>();
		int weight = 92;		// decreasing weights for worst case.
		for (int i=0;i<V;i++)
		{
			for (int j=i+1;j<V;j++)
			{
				adj[i].add(new Edge(i,j, weight));
				adj[j].add(new Edge(j,i, weight));
				weight += 1;
			}
			weight -= 17;
		}
	}
	
	public int getV()	// gives amount of vertices
	{
		return V;
	}
	
	void printGraph()
	{
		for (int i=0;i<V;i++)
		{
			System.out.print(i+":");
			for (Edge edge : adj[i])
				System.out.print(" "+edge.other()+"("+(int)edge.getWeight()+")");
			System.out.println();
		}	
	}
	
}

class Edge
{
	private int v;
	private int w;
	private double weight;
	
	public Edge(int v, int w, double weight)
	{
		this.v = v;
		this.w = w;
		this.weight = weight;
	}
	
	public int either()
	{
		return v;
	}
	
	public int other()
	{
		return w;
	}
	
	public double getWeight()
	{
		return weight;
	}
}