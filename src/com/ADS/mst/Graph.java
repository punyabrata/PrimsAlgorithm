package com.ADS.mst;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

class Neighbor {
//	Vertex-# of the neighbor
	public int vertexNum;
//	Weight of the edge connected between this and the actual vertex	
	public int edgeWeight;
//	Next neighbor connected to the actual vertex	
	public Neighbor next;
//	Constructor
	public Neighbor(int vnum, int weight, Neighbor nbr) {
		this.vertexNum = vnum;
		this.edgeWeight = weight;
		this.next = nbr;
	}
}

class Vertex {
//	Key at the vertex
	public int key;
//	Pointer to the first neighbor in the Adjacency Linked List
	public Neighbor adjList;
//	Constructor
	public Vertex(int key, Neighbor neighbors) {
		this.key = key;
		this.adjList = neighbors;
	}
}

public class Graph {
//	Array of vertices
	private Vertex[] vertices;
//	#Edges
	private int numEdges;
	
	/* Method: 	Constructor
	 * Input:	Integer, Float
	 * Output:	Void
	 * Description:	This parameterized constructor uses the following things:
	 * Integer 'n' to denote the number of vertices; Float 'd' to denote the density
	 */
	public Graph(int n, float d) {
//		#Vertices
		int numVertices = n;
		System.out.println("No. of Vertices: " + numVertices);
		vertices = new Vertex[numVertices];
		for (int i = 0 ; i < numVertices ; i++) {
//			New vertex with no adjacency list created yet
			Vertex v = new Vertex(i,null);
			vertices[i] = v;
		}		
//		#Density
		System.out.println("Density: " + d);
//		#Edges
		float product = (n*(n - 1)/2)*d/100;
		int numEdges = (int) Math.ceil(product);
//		Assigning it to the property member
		this.numEdges = numEdges;
		System.out.println("No. of Edges: " + numEdges);
		do {
	//		Form Edges
			ArrayList<int[]> listOfEdges = new ArrayList<int[]>();
			int[] edgeTriplet;
	//		Looping for the number of edges
			for (int i = 0 ; i < numEdges ; i++) {
	//			Left end-point
				int v1 = RandomNumberGenerator.getRandomNumber(numVertices);
	//			Right end-point
				int v2 = RandomNumberGenerator.getRandomNumber(numVertices);
	//			v1 and v2 cannot be the same
				while (v1 == v2) {
					v2 = RandomNumberGenerator.getRandomNumber(numVertices);
				}
	//			Cost
				int cost = RandomNumberGenerator.getRandomNumber(1000) + 1;
	//			Checking in the ArrayList whether the edge already exists
				Boolean check = false;
				for (int j = 0 ; j < listOfEdges.size() ; j++) {
					int[] edge = listOfEdges.get(j);
					if ((v1 == edge[0] && v2 == edge[1]) || (v1 == edge[1] && v2 == edge[0])) {
						check = true;
						break;
					}
				}
	//			Edge already exists
				if (check) {
					i--;
				} else {
	//				New Edge
					edgeTriplet = new int[3];
					edgeTriplet[0] = v1;
					edgeTriplet[1] = v2;
					edgeTriplet[2] = cost;
	//				Adding the edge to 'listOfEdges'
					listOfEdges.add(edgeTriplet);
				}
			}
	//		Constructing the Graph
//			#Connections
			for (int i = 0 ; i < numEdges ; i++) {
//				Left and right end-points of an edge and its cost
				int v1 = 0;
				int v2 = 0;
				int c = 0;
				v1 = listOfEdges.get(i)[0];
				v2 = listOfEdges.get(i)[1];
				c = listOfEdges.get(i)[2];
//				Since there is no order for the neighbors
//				Adding v2 to the front of v1's adjacency list
				vertices[v1].adjList = new Neighbor(v2, c, vertices[v1].adjList);
//				Adding v1 to the front of v2's adjacency list
				vertices[v2].adjList = new Neighbor(v1, c, vertices[v2].adjList);
			}			
		} while (!this.traverseDFS());
	}
	
	/* Method: 	Constructor
	 * Input:	String
	 * Output:	Nil
	 * Description:	This is the parameterized constructor which accepts an input file
	 */
	public Graph(String fileName) throws FileNotFoundException, NoSuchElementException {
		if ("".equals(fileName)) {
			System.out.println("No file entered.");
			System.exit(0);
		}
		Scanner sc = new Scanner(new File(fileName));
//		#Vertices
		int numVertices = sc.nextInt();
		System.out.println("No. of Vertices: " + numVertices);
		vertices = new Vertex[numVertices];
		for (int i = 0 ; i < numVertices ; i++) {
//			New vertex with no adjacency list created yet
			Vertex v = new Vertex(i,null);
			vertices[i] = v;
		}
//		#Edges
		int numEdges = sc.nextInt();
//		Assigning it to the property member
		this.numEdges = numEdges;		
		System.out.println("No. of Edges: " + numEdges);
		
//		#Connections
		for (int i = 0 ; i < numEdges ; i++) {
//			Left and right end-points of an edge and its cost
			int v1 = 0;
			int v2 = 0;
			int c = 0;
			if (sc.hasNextInt()) {
				v1 = sc.nextInt();
//				System.out.print("V1 = " + v1 + "		");
			} else {
				System.out.println("Error in edge definition");
				System.exit(0);
			}
			if (sc.hasNextInt()) {
				v2 = sc.nextInt();
//				System.out.print("V2 = " + v2 + "		");
			} else {
				System.out.println("Error in edge definition");
				System.exit(0);
			}
			if (sc.hasNextInt()) {
				c = sc.nextInt();
//				System.out.print("Cost = " + c);
			} else {
				System.out.println("Error in edge definition");
				System.exit(0);
			}
			System.out.println();
//			Since there is no order for the neighbors
//			Adding v2 to the front of v1's adjacency list
			vertices[v1].adjList = new Neighbor(v2, c, vertices[v1].adjList);
//			Adding v1 to the front of v2's adjacency list
			vertices[v2].adjList = new Neighbor(v1, c, vertices[v2].adjList);
		}
	}
	
	/* Method: 	printGraph()
	 * Input:	Nil
	 * Output:	Void
	 * Description:	This method prints the Graph
	 */
	public void printGraph() {
		System.out.println();
		System.out.println("Adjacency List Representation: ");
		System.out.println();
		for (int v = 0 ; v < vertices.length ; v++) {
			System.out.print(vertices[v].key);
			for (Neighbor n = vertices[v].adjList ; n != null ; n = n.next) {
				System.out.print(" ---> " + vertices[n.vertexNum].key + "," + n.edgeWeight);
			}
			System.out.println();
			System.out.println();
		}
	}
	
	/* Method: 	traverseDFS()
	 * Input:	Nil
	 * Output:	Void
	 * Description:	This method performs the DFS traversal of the Graph
	 */
	public Boolean traverseDFS() {
//		Stack used for DFS traversal
		Stack<Vertex> dfsStack = new Stack<Vertex>();
//		Boolean visited array, initialized with all 'false'
		Boolean[] visitedArray = new Boolean[vertices.length];
		for (int i = 0 ; i < vertices.length ; i++) {
			visitedArray[i] = false;
		}
//		ArrayList to store the visit order of the vertices
		ArrayList<Integer> results = new ArrayList<Integer>();
//		-- Begin processing vertices --
//		Vertex Counter
		int current = 0;
//		Processing alphabetically smallest unvisited vertex
//		Keeping in result 
		results.add(vertices[current].key);
//		Marking it as visited
		visitedArray[vertices[current].key] = true;
//		Pushing it on to the stack
		dfsStack.push(vertices[current]);
//		Check until Stack is empty
		while (!dfsStack.isEmpty()) {
//			Used to find alphabetically smallest vertex in the neighbor list
			int minVertex = 1001;
//			Checking for all the neighbors of the current vertex and finding the minimum
			for (Neighbor n = vertices[current].adjList ; n != null ; n = n.next) {
//				If the vertex in the neighbor list is already visited, not counting it
				if (visitedArray[n.vertexNum]) {
//					Ignore already visited vertices
				} else {
//					Set 'minVertex' to the current one
					if ((n.vertexNum) < minVertex) {
						minVertex = n.vertexNum;
					}
				}
			}
//			If all the vertices in the neighbor list is visited, popping from stack
			if (minVertex == 1001) {
				Vertex v = dfsStack.pop();
				current = v.key;
			} else {
//				Definite alphabetically smallest neighbor is found
				current = minVertex;
//				Keeping in result
				results.add(vertices[current].key);
//				Marking it as visited
				visitedArray[vertices[current].key] = true;
//				Pushing it on to the stack
				dfsStack.push(vertices[current]);				
			}
		}
		System.out.println();
		System.out.println("DFS Traversal order");
		System.out.println();		
		//Checking if all the vertices are visited
		for (int i = 0 ; i < vertices.length ; i++) {
			if (visitedArray[i] == false) {
//				Even if a single vertex is unvisited
				System.out.println();
				System.out.println("The graph is NOT connected!");
				return false;
			}
			System.out.print(results.get(i) + " -- ");			
		}
		System.out.println();
//		If all the vertices are visited
		System.out.println("The graph is connected!");
		return true;
	}	
	
	/* Method: 	findMSTSimple()
	 * Input:	Nil
	 * Output:	Void
	 * Description:	This method computes the minimum spanning tree of the graph using Simple Scheme
	 * It uses PRIM'S ALGORITHM to find the minimum spanning tree
	 */
	public void findMSTSimple () {
		if (vertices.length < 1 || !this.traverseDFS()) {
			System.out.println("MST not possible");
			return;
		}
//		Required to check if the vertex is already visited
		Boolean[] VerticesCheck = new Boolean[vertices.length];
		for (int i = 0 ; i < vertices.length ; i++) {
			VerticesCheck[i] = false;
		}		
//		Count the vertices which are visited
		int verticesCount = 0;
//		Initialize Simple Scheme
		SimpleScheme sScheme = new SimpleScheme(numEdges);
/*		Start with the alphabetically smallest vertex which is already stored in 'vertices'
		And then keep checking until VerticesCheck has all true*/
		int current = 0;
//		Incrementing the number of vertices visited when current vertex is found
		verticesCount++;
		int indexCount = 0;
		int totalCost = 0;
		do {
//			Taking the vertex out
			Vertex v = vertices[current];
//			Visiting it
			VerticesCheck[current] = true;
//			Checking for every neighbor in the adjacency list
			for (Neighbor n = v.adjList ; n != null ; n = n.next) {
//				Creating a new edge with Start, End, Cost, isUsed
				EdgeCost e = new EdgeCost(v, vertices[n.vertexNum], n.edgeWeight, false, false);
//				Checking if the edge already exists in the Simple Scheme cost-array
				if (!sScheme.doesEdgeExist(e)) {
//					Edge doesn't exist
					sScheme.addEdge(e, indexCount);
					indexCount++;
				}
			}
//			Finding Min-Edge
			EdgeCost e = sScheme.extractLocalMin();
/*			As long as both the end-points of the min-edge are already present 
			in the 'VerticesCheck' array, ignoring that vertex and generating
			new local min-edge. This will avoid a cycle in the minimum spanning tree*/
			while (VerticesCheck[e.v1.key] && VerticesCheck[e.v2.key]) {
				sScheme.ignoreEdge(e);
				e = sScheme.extractLocalMin();
			}
			totalCost += e.cost;
//			Invalidating the Edge
			sScheme.invalidateEdge(e);
//			Getting the new current vertex from the minimum edge chosen
//			One of the end-points of this must be already visited
			int key = e.v1.key;
//			If v1 is visited, choose v2, else chose v1
			if (VerticesCheck[key]) {
				current = e.v2.key;
			} else {
				current = e.v1.key;
			}
//			Incrementing the number of vertices visited when current vertex is found
			verticesCount++;			
		} while (verticesCount != vertices.length);
//		Printing the minimum spanning tree with its cost
		System.out.println();
		System.out.println("The minimum spanning tree will consist of the following edges");
		System.out.println("Cost is followed by the edges");
		System.out.println();
		System.out.println(totalCost);
		for (int i = 0 ; i < numEdges && sScheme.costArray[i] != null ; i++) {
			EdgeCost eC = sScheme.costArray[i];
			if (eC.isUsed)
				System.out.println(eC.v1.key + " " + eC.v2.key);
		}
		System.out.println();
//		System.out.println("Cost of the minimum spanning tree: " + totalCost);
	}
	
	/* Method: 	findMSTFHeap()
	 * Input:	Nil
	 * Output:	Void
	 * Description:	This method computes the minimum spanning tree of the graph using
	 * the Fibonacci Heap Scheme
	 * It uses PRIM'S ALGORITHM to find the minimum spanning tree
	 */
	public void findMSTFHeap () {
		if (vertices.length < 1 || !this.traverseDFS()) {
			System.out.println("MST not possible");
			return;
		}
//		Instantiating the Fibonacci Heap Scheme
		FHeapScheme fHScheme = new FHeapScheme(this);
//		Getting the final minimum spanning tree in the form of a HashMap<Integer, FibonacciEdge>
		HashMap<Integer, FibonacciEdge> result = fHScheme.computeMST();
//		Printing the minimum spanning tree with its cost
		System.out.println("The minimum spanning tree will consist of the following edges");
		System.out.println();
		Set<Integer> verticesKeys = result.keySet();
		int totalCost = 0;
		for (int key : verticesKeys) {
			FibonacciEdge f = result.get(key);
			if (f != null) {
				System.out.println(f.v1 + " " + f.v2);
				totalCost += result.get(key).cost;
			}
		}
		System.out.println();
		System.out.println("Cost of the minimum spanning tree: " + totalCost);		
	}
	
	/* Method: 	getVertices()
	 * Input:	Nil
	 * Output:	Array of Vertices
	 * Description:	This method is a getter for 'vertices'
	 */
	public Vertex[] getVertices() {
		return vertices;
	}
}