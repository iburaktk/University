import java.util.Map;
import java.util.Queue;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Collections;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.StringBuilder;

public class HW5
{
	public static void main(String[] args) throws FileNotFoundException
	{
		File inputFile = new File(args[0]);
		Scanner input = new Scanner(inputFile);
		int carrierType = input.nextInt();
		int numberOfPoints = input.nextInt();
		int numberOfRoads = input.nextInt();
		int targetPoint = input.nextInt();
		
		Graph country = new Graph(input,numberOfPoints,numberOfRoads);
		country.travel(carrierType,targetPoint);
	}
}

class Graph
{
	Node startNode;					// 1
	Map<Integer,Node> points;		// map to get node.
	
	public Graph(Scanner input,int numberOfPoints, int numberOfRoads)
	{
		points = new HashMap<Integer,Node>();
		for (int i=0;i<numberOfRoads;i++)
		{
			int point1Name = input.nextInt();
			int point2Name = input.nextInt();
			int duration = input.nextInt();
			Node point1 = null;
			if (points.containsKey(point1Name))
				point1 = points.get(point1Name);	// get point.
			else
			{
				point1 = new Node(point1Name);		// create new point.
				points.put(point1Name, point1);
			}
			Node point2 = null;
			if (points.containsKey(point2Name))
				point2 = points.get(point2Name);	// get point
			else
			{
				point2 = new Node(point2Name);		// create new point
				points.put(point2Name, point2);
			}
			addEdge(point1, point2, duration);		// add edge between this points.
		}
		startNode = points.get(1);
		if (startNode == null)						// we must have a point named "1".
		{
			System.out.println("-1");
			System.exit(0);
		}
	}
	
	private void addEdge(Node point1, Node point2, int duration)	// adds edge to points's neighbour data.
	{
		Edge edge = new Edge(point1, point2, duration);
		point1.neighbours.add(edge);
		point2.neighbours.add(edge);
	}
	
	public void travel(int carrierType,int targetPoint)
	{
		if (carrierType == 1)
		{
			if (!shorthestPathDjkstra(targetPoint))			// travel.
			{
				System.out.println("-1");					// if didn't found, print "-1"
				return;
			}
			StringBuilder stringBuilder = new StringBuilder();
			int temp = targetPoint;
			while (temp != 1)								// printing travel reversely.
			{
				stringBuilder.insert(0, "->"+temp);
				temp = points.get(temp).edgeTo;
			}
			stringBuilder.insert(0, "1");
			System.out.println(points.get(targetPoint).distTo+"\n"+stringBuilder.toString());
		}
		else if (carrierType == 2)
		{
			if (!shorthestPathDjkstra(targetPoint))			// travel.
			{
				System.out.println("-1");					// if didn't found, print "-1"
				return;
			}
			int temp = points.get(targetPoint).edgeTo, maxDuration = 0, distTo = points.get(targetPoint).distTo;
			while (temp != 0)								// we found route, travel and find maximum duration.
			{
				if (distTo - points.get(temp).distTo > maxDuration)
					maxDuration = distTo - points.get(temp).distTo;
				distTo = points.get(temp).distTo;
				temp = points.get(temp).edgeTo;
			}
			System.out.println(maxDuration);				// print it.
		}
		else if (carrierType == 3)
		{
			if (!minimumRoadDjkstra(targetPoint))			// travel
			{
				System.out.println("-1");					// if didn't found, print "-1"
				return;
			}
			StringBuilder stringBuilder = new StringBuilder();
			int temp = targetPoint;
			while (temp != 1)								// printing travel reversely.
			{
				stringBuilder.insert(0, "->"+temp);
				temp = points.get(temp).edgeTo;
			}
			stringBuilder.insert(0, "1");
			System.out.println(points.get(targetPoint).distTo+"\n"+stringBuilder.toString());
		}
		else if (carrierType == 4)
		{
			if (!shorthestRoadTravel(targetPoint))			// travel
			{
				System.out.println("-1");					// if didn't found, print "-1"
				return;
			}
			// we printed our travel in function. because in this travel, we may visit a point more than once.
		}
		else System.out.println("error");
	}
	
	private boolean shorthestPathDjkstra(int targetPoint)	// finding points's distances to point "1" with djkstra's algorithm.
	{
		boolean found = false;
		startNode.distTo = 0;
		Queue<Integer> queue = new ArrayDeque<Integer>();	// we will add points in order to this queue.
		queue.add(1);
		while (!queue.isEmpty())
		{
			int i = (Integer) queue.poll();
			Collections.sort(points.get(i).neighbours, new edgeComparator());	// sort neighbour edges respect to road durations.
			for (Edge edge : points.get(i).neighbours)							// for getting shorthest road first.
			{
				if (!edge.used)
				{
					Node node1 = points.get(i);
					Node node2 = edge.getNeighbour(node1);
					if (node1.distTo + edge.duration < node2.distTo)	// if distance is lesser; then, new distance is it.
					{
						node2.distTo = node1.distTo + edge.duration;
						node2.edgeTo = i;				// stored possible travel route in this variable.
					}
					edge.used = true;
					if (node2.nodeName != targetPoint)		// if that node is not target, then add to queue.
						queue.add(node2.nodeName);
					else found = true;						// we found at least one path to the target.
				}
			}
		}
		return found;
	}
	
	private boolean minimumRoadDjkstra(int targetPoint)		// finding number of passed points with djkstra's algorithm.
	{								// in this function, we will use distTo variable to store number of passed points.
		boolean found = false;
		startNode.distTo = 0;
		Queue<Integer> queue = new ArrayDeque<Integer>();	// we will add points in order to this queue.
		queue.add(1);
		while (!queue.isEmpty())
		{
			int i = (Integer) queue.poll();
			for (Edge edge : points.get(i).neighbours)		// order is not important.
			{
				if (!edge.used)
				{
					Node node1 = points.get(i);
					Node node2 = edge.getNeighbour(node1);
					if (node1.distTo + 1 < node2.distTo)	// if number of passed points lesser, change it.
					{
						node2.distTo = node1.distTo + 1;
						node2.edgeTo = i;
					}
					edge.used = true;
					if (node2.nodeName != targetPoint)		// if that node is not target, then add to queue.
						queue.add(node2.nodeName);
					else found = true;						// we found at least one path to the target.
				}
			}
		}
		return found;
	}
	
	private boolean shorthestRoadTravel(int targetPoint)	// Basically, go to shorthest road.
	{
		StringBuilder stringBuilder = new StringBuilder();	// we will print travel in function.
		stringBuilder.append("1");							// print starting point.
		boolean found = false;
		int i=1;
		startNode.distTo = 0;
		while (found == false)
		{
			Collections.sort(points.get(i).neighbours, new edgeComparator());	// sorting edges respect to durations.
			for (Edge edge : points.get(i).neighbours)
			{
				if (!edge.used)
				{
					Node node1 = points.get(i);
					Node node2 = edge.getNeighbour(node1);
					stringBuilder.append("->"+node2.nodeName);					// printing current point.
					node2.distTo = node1.distTo + edge.duration;
					node2.edgeTo = i;
					i = node2.nodeName;
					edge.used = true;
					if (node2.nodeName == targetPoint)							// if reached to target, break.
						found = true;
					break;
				}
			}
		}
		if (found)		// printing travel.
		{
			System.out.println(points.get(targetPoint).distTo + "\n" + stringBuilder.toString());
		}
		return found;
	}
}

class Node
{	
	public int nodeName;					// node name
	public int distTo;						// distance to starting node
	public int edgeTo;						// route of travel
	public ArrayList<Edge> neighbours;		// stored neighbour data in each node. like adjacency list
	
	public Node(int nodeName)
	{
		this.nodeName = nodeName;
		neighbours = new ArrayList<Edge>();
		distTo = 2147483647;				// positive infinite --> max distance.
		edgeTo = 0;							// we didn't know route of travel yet.
	}
}

class Edge
{
	public Node node1, node2;
	public int duration;
	public boolean used;
	
	public Edge(Node node1, Node node2, int duration)
	{
		this.node1 = node1;
		this.node2 = node2;
		this.duration = duration;
		used = false;
	}
	
	public Node getNeighbour(Node node)		// get other point of the edge.
	{
		if (node == node1)
			return node2;
		else 
			return node1;
	}
}

class edgeComparator implements Comparator<Edge>		// sorts list respect to edge's durations.
{
	public int compare(Edge edge1, Edge edge2) 
	{
		return edge1.duration - edge2.duration;
	}
}

