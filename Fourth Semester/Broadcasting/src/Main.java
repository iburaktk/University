import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Queue;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;
import java.util.ArrayDeque;

public class Main
{
	
	public static void main(String[] args) throws FileNotFoundException
	{
		File outputFile = new File(args[1]);
		System.setOut(new PrintStream(outputFile));
				
		File inputFile = new File(args[0]);
		Scanner input = new Scanner(inputFile);
		String firstLine = input.nextLine();
		
		Graph graph = new Graph(firstLine);		// construct the graph with first line
		
		String source = input.nextLine();
		while (input.hasNext())					// add edges
		{
			String firstNode = input.next();
			String secondNode = input.next();
			graph.addEdge(firstNode,secondNode);
		}
		
		System.out.println("Graph structure:");		// print the graph
		for (Node node : graph.nodes.values())
		{
			System.out.print(node.getNodeName()+"("+ node.getCapacity() + ")-->");
			boolean first = true;
			for (Node neighbour : node.getNeighbours())
			{
				if (first)
				{
					System.out.print(neighbour.getNodeName());
					first = false;
				}
				else 
					System.out.print(" " + neighbour.getNodeName());
			}
			System.out.println();
		}
		
		System.out.println("Broadcast steps:");
		Queue<Node> queue = new ArrayDeque<Node>();		// i used queue for broadcasting for right order
		Stack<Node> stack = new Stack<Node>();			// i used stack for message passing for right order
		queue.add(graph.nodes.get(source));
		stack.push(graph.nodes.get(source));
		
		while(!queue.isEmpty())		// we will add new nodes to this queue.
		{
			Node node = queue.poll();
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(node.getNodeName()+"-->");
			boolean first = true;
			for (Node neighbour : new ArrayList<Node>(node.getNeighbours()))
			{
				if(neighbour.sendMessage(node))		// if sending message successes, print and add to queue
				{
					if (first)
					{
						stringBuilder.append(neighbour.getNodeName());
						first = false;
					}
					else 
						stringBuilder.append(" "+neighbour.getNodeName());
					queue.add(neighbour);
					stack.push(neighbour);
				}
			}
			if (stringBuilder.toString().compareTo(node.getNodeName()+"-->") == 0)
				continue;
			System.out.println(stringBuilder.toString());
		}
		System.out.println("Message passing:");
		Node node = stack.pop();
		while (!stack.isEmpty())		// child to parent
		{
			node.updateBestNode();		// taking message, comparing, and updating best nodes
			System.out.print(node.getNodeName() + "--->");
			StringBuilder stringBuilder = new StringBuilder();
			for (Node bestNode : node.getBestNodes())
			{
				stringBuilder.insert(0,"["+bestNode.getNodeName()+","+bestNode.getCapacity()+"]");
			}
			
			System.out.println(stringBuilder.toString()+"--->"+node.getParentNode().getNodeName());
			node = stack.pop();
		}
		node.updateBestNode();		// updating source (final)
		System.out.print("Best node-->");
		boolean first = true;
		StringBuilder stringBuilder = new StringBuilder();
		for (Node node2 : graph.nodes.get(source).getBestNodes())
		{
			if (!first)
				stringBuilder.insert(0,node2.getNodeName()+", ");
			else 
			{
				stringBuilder.append(node2.getNodeName());
				first = false;
			}
		}
		System.out.println(stringBuilder.toString());
		
		int bestHeight = 100;		// i hope that you wont try with 100 height tree :D
		StringBuilder possibleRoots = new StringBuilder(); 
		for (Node node3 : graph.nodes.values())		// find each node's height and return minimum ones.
		{
			graph.resetParent();
			int height = node3.findHeight();
			if (height < bestHeight)		// new minimum root. we must wipe string and add new root.
			{
				if (possibleRoots.length() > 0)
					possibleRoots.delete(0, possibleRoots.length());
				bestHeight = height;
				possibleRoots.append(node3.getNodeName());
			}
			else if (height == bestHeight)		// there are more than one possible root. we must append it to our output.
				possibleRoots.insert(0,node3.getNodeName()+", ");
		}
		System.out.print("Possible roots-->"+possibleRoots.toString());
	}
	
}

class Graph
{
	public Map<String, Node> nodes;		// nodes array
	
	public Graph(String firstLineInput)
	{
		// constructor of the graph.
		nodes = new HashMap<String, Node>();
		Scanner read = new Scanner(firstLineInput);
		while (read.hasNext())
		{
			String nodeName = read.next();
			int capacity = read.nextInt();
			nodes.put(nodeName, new Node(nodeName, capacity));		// creating nodes.
		}
	}
	
	public void addEdge(String firstNode, String secondNode)		// adds edges.
	{
		nodes.get(firstNode).addNeighbour(nodes.get(secondNode));
		nodes.get(secondNode).addNeighbour(nodes.get(firstNode));
	}
	
	public void resetParent()		// when setting parents, deleted the parent from neighbours list. reset it.
	{
		for (Node node : nodes.values())
		{
			if (node.getParentNode() != null)
			{
				node.addNeighbour(node.getParentNode());
				node.setParentNode(null);
			}
		}
	}
	
	
}

class Node
{	
	private String nodeName;
	private int capacity;
	private Node parentNode;
	private ArrayList<Node> bestNodes;
	public boolean messageRecieved;
	private ArrayList<Node> neighbours;		// i stored neighbour data in each node. like adjacency list
	
	public Node(String nodeName, int capacity)
	{
		this.nodeName = nodeName;
		this.capacity = capacity;
		parentNode = null;
		bestNodes = new ArrayList<Node>();
		messageRecieved = false;
		neighbours = new ArrayList<Node>();
	}
	
	public int findHeight()		// finds height of the tree of rooted by this node.
	{
		int bestHeight = 0;
		for (Node node : neighbours)
		{
			int height;
			node.setParentNode(this);		// setting child's parent node to this node.
			node.neighbours.remove(this);	// removing this node from child's neighbours list
			height = node.findHeight();		// recursively find height.
			if (height > bestHeight)
				bestHeight = height;
		}
		return bestHeight + 1;
	}
	
	public boolean sendMessage(Node senderNode)		// takes message from parent, sets parent and sends message to neighbours.
	{
		if (messageRecieved)
		{
			neighbours.remove(senderNode);
			senderNode.neighbours.remove(this);
			return false;
		}
		else
		{
			setParentNode(senderNode);
			messageRecieved = true;
			return true;
		}
	}
	
	public void addNeighbour(Node newNode)		// adds edges
	{
		neighbours.add(newNode);
	}
	
	public String getNodeName()
	{
		return nodeName;
	}
	
	public int getCapacity()
	{
		return capacity;
	}
	
	public Node getParentNode()
	{
		return parentNode;
	}
	
	public void setParentNode(Node parentNode)		// sets parent and removes from childeren list.
	{
		this.parentNode = parentNode;
		neighbours.remove(parentNode);
	}
	
	public void updateBestNode()
	{
		if (bestNodes.isEmpty())		// if this node is first to message, then best node is itself
		{
			parentNode.bestNodes.add(this);
			bestNodes.add(this);
		}
		else 
		{
			int bestCapacity = getCapacity();
			for (Node node : bestNodes)
			{
				if (node.getCapacity() > bestCapacity)
				{
					bestCapacity = node.getCapacity();		// finding maximum capacity
				}
			}
			bestNodes.add(this);
			for (Node node : new ArrayList<Node>(bestNodes))
			{
				if (node.getCapacity() < bestCapacity)
				{
					bestNodes.remove(node);		// removing lesser capacity nodes.
				}
			}
			if (parentNode != null)		// check for source node (source has not got parent)
				parentNode.bestNodes.addAll(bestNodes);
		}
	}
	
	public Iterable<Node> getBestNodes()
	{
		return bestNodes;
	}
	
	public ArrayList<Node> getNeighbours()
	{
		return neighbours;
	}
}