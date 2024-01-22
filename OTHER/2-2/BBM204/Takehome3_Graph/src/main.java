import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Stack;

// verilen directed graphýn unique topological orderinge sahip olup olmadýðýný hamilton path ile bul.
//if hamilton path exists, topological sort order is unique

public class main
{
	
	public static void main(String[] args) throws FileNotFoundException
	{
		File outputFile = new File(args[3]);
		System.setOut(new PrintStream(outputFile));
		
		// For Question 1, we will use graph "G1".
		
		Graph G1 = new Graph(args[0],false);
		
		int parallelEdges = 0;
		for(int i=0;i<G1.getV();i++)
		{
			// we will do changes on graph. so we must clone it.
			ArrayList<Integer> temp = new ArrayList<Integer>(G1.adj.get(i));
			for (int j : temp)
			{
				G1.adj.get(i).remove(0);			// Firstly, we will delete this edge.
				if (G1.adj.get(i).contains(j))		// If there is still an edge between pair of this
				{									// vertices, then that edge is parallel edge.
					parallelEdges++;
				}
			}
		}
		parallelEdges = parallelEdges / 2;			// 1-5 and 5-1 are same. So we divide by 2.
		System.out.print("Q1\n" + parallelEdges + "\n");
		
		// For Question 2, we will use graph "G2".
		
		System.out.println("Q2");
		Graph G2 = new Graph(args[1],false);
		
		for(int v=0; v<G2.getV();v++)				// Firstly, sorting edges.
		{
			Collections.sort(G2.adj.get(v));
		}
		
		boolean marked[];
		boolean bridgeFound = false;
		
		for (int i=0;i<G2.getV();i++)
		{
			for (int j=0;j<G2.adj.get(i).size();j++)
			{
				marked = new boolean[G2.getV()];
				if (G2.adj.get(i).get(j) < i)		// We already looked for that edge. 
					continue;
				int target = G2.adj.get(i).get(j);			// We will delete this edge and we will
				G2.adj.get(i).removeIf(n -> n==target);		// try to reach other side of this edge.
				if ( dfsBasedBridgeChecker(G2, marked, i, target) == true )
				{
					bridgeFound = true;							// if we couldn't reached the other side
					System.out.print(i + "-" + target + " ");	// then this edge is a bridge.
				}
				G2.adj.get(i).add(target);				// We checked this edge. We must re-add edge 
				Collections.sort(G2.adj.get(i));		// for checking other edges too.
			}
		}
		if (!bridgeFound)
			System.out.print("Graph is two-edge connected");
		System.out.print("\n");
		
		// For Question 3, we will use graph "G3".
		
		Graph G3 = new Graph(args[2],true);
		
		Stack<Integer> topologicalSorted = new Stack<Integer>();
		marked = new boolean[G3.getV()];
		for (int v=0;v<G3.getV();v++)						// Firstly, we will do topological sort.
			if (!marked[v])									// We will use reverse DFS postorder.
				topologicalSorter(G3,v,marked,topologicalSorted);
		
		// If there is hamiltonian path, then there is unique topological order.
		System.out.println("Q3\n" + isThatGraphHamiltonian(G3, topologicalSorted));
		
		
	}
	static boolean dfsBasedBridgeChecker(Graph G, boolean[] marked,int v, int target) 
	{
		// Cut the connection  -->  Try to reach other side with dfs.
		marked[v] = true;
		if (G.adj.get(v).contains(target))
		{
			return false;	// We are reached the other side. Then this edge isn't a bridge.
		}
		for (int w : G.adj.get(v))
			if(!marked[w])
			{
				if ( dfsBasedBridgeChecker(G, marked, w, target) == false ) // We are already reached.
					return false;
			}
		return true;		// We couldn't reached the other side. Then this edge is a bridge.
	}
	
	static void topologicalSorter(Graph G, int v, boolean[] marked, Stack<Integer> topologicalSorted)
	{
		// reverse DFS postorder gives us topological sort of the graph.
		marked[v] = true;
		for (int w : G.adj.get(v))
			if (!marked[w])
				topologicalSorter(G,w,marked,topologicalSorted);
		topologicalSorted.push(v);
	}
	
	static boolean isThatGraphHamiltonian(Graph G, Stack<Integer> path)
	{
		// We already have topological sort. We will walk on this path. 
		int start = path.pop();
		while (!path.isEmpty())
		{
			int target = path.pop();
			if (!G.adj.get(start).contains(target))
				return false;// if the path doesn't contain next vertex, then this path isn't hamiltonian
			start = target;
		}
		return true; 		// we succesfully finished our path. 
	}
}

class Graph
{
	private final int V;
	public ArrayList<ArrayList<Integer>> adj;	// adjacency list of the graph
	
	public Graph(String inputFileName, boolean directed) throws FileNotFoundException
	{
		// constructor of the graph.
		int E;
		File inputFile = new File(inputFileName);
		Scanner read = new Scanner(inputFile);
		
		V = read.nextInt();
		adj = new ArrayList<ArrayList<Integer>>(V);
		for (int v=0;v<V;v++)
			adj.add(new ArrayList<Integer>());
		
		E = read.nextInt();
		for (int i=0;i<E;i++)
		{
			int a,b;
			a = read.nextInt();
			b = read.nextInt();
			addEdge(a,b,directed);
		}
		
	}
	
	public int getV()	// gives amount of vertices
	{
		return V;
	}
	
	void addEdge(int v, int w, boolean directed)	// adds edges.
	{
		adj.get(v).add(w);
		if (!directed)
			adj.get(w).add(v);
	}
	
}
