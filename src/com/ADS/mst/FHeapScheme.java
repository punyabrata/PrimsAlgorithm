package com.ADS.mst;

import java.util.HashMap;

public class FHeapScheme {
//	Array of Fibonacci Vertex
	public FibonacciVertex[] fVertices;
//	The Fibonacci Heap
	public FibonacciHeap fHeap = new FibonacciHeap();
//	The Graph
	public Graph graph;
	
	/* Method: Constructor
	 * Input: Graph
	 * Output: Nil
	 * Description:	This parameterized constructor takes the Graph as input and initializes it 
	 */
	public FHeapScheme(Graph graph) {
//		The Graph
		this.graph = graph;
//		Vertices of the Graph
		Vertex[] vertices = graph.getVertices();
//		Initializing the array of Fibonacci Vertex
		fVertices = new FibonacciVertex[vertices.length];
//		Initializing Start vertex
		FibonacciVertex s = new FibonacciVertex(vertices[0].key, vertices[0].key, null, null);
//		Adding to the array
		fVertices[0] = s;
//		Initializing Data Structure
		for (int i = 1 ; i < vertices.length ; i++) {
//			Fibonacci Vertex
			FibonacciVertex f;
//			Graph Key
			int graphKey = vertices[i].key;
//			Fibonacci Vertex Key
			int k = 10001;
//			Fibonacci Vertex Edge
			FibonacciEdge e = null;
//			Fibonacci Node
			FibonacciNode fNode = null;
//			Checking the adjacency list of the start vertex
			for (Neighbor n = vertices[0].adjList ; n != null ; n = n.next) {
//				An edge present between start vertex and the current vertex
				if (n.vertexNum == vertices[i].key) {
					k = n.edgeWeight;
					e = new FibonacciEdge(vertices[i].key, vertices[0].key, n.edgeWeight);
					break;
				}
			}
//			New Fibonacci Node
			fNode = new FibonacciNode(k, 0, null, null, null, null, null, true, graphKey);
//			New Fibonacci Vertex
			f = new FibonacciVertex(graphKey, k, e, fNode);
//			Adding to the array
			fVertices[i] = f;
//			Inserting the node to the Fibonacci Heap
			fHeap.insert(fNode);
		}
	}

	/* Method: 	computeMST()
	 * Input:	Nil
	 * Output:	HashMap<Integer, FibonacciEdge>
	 * Description:	This method computes the minimum spanning tree using the Fibonacci Heap
	 */		
	public HashMap<Integer, FibonacciEdge> computeMST() {
//		Final data structure (HashMap) that stores the vertices one by one using their graphKey
//		and the corresponding edges (FibonacciEdge-s) too
//		These edges will be the final edges of the minimum spanning tree
		HashMap<Integer, FibonacciEdge> vertexWithEdge = new HashMap<Integer, FibonacciEdge>();
//		Putting the starting vertex (Vertex with graphKey = 0)
		vertexWithEdge.put(fVertices[0].graphKey, null);
//		System.out.println(vertexWithEdge.containsKey(0));
//		System.out.println("fVertices.length : " + fVertices.length);
//		Looping for the other vertices
		for (int i = 1 ; i < fVertices.length ; i++) {
//			Extracting the Min using the extractMin() method
			FibonacciNode min = fHeap.extractMin();
			System.out.println();
//			this.printFHeapScheme();
			System.out.println();
//			System.out.println("min.graphKey: " + min.graphKey);
//			System.out.println("min.key: " + min.key);
//			Putting it onto the HashMap
			int key = min.graphKey;
			FibonacciEdge value = fVertices[min.graphKey].edge;
			vertexWithEdge.put(key, value);
//			Checking for the neighbors from the adjacency list of the current vertex
			for (Neighbor n = this.graph.getVertices()[key].adjList ; n != null ; n = n.next) {
//				If the neighbor hasn't already been put into the final HashMap and if its key is
//				greater than the weight of its edge connecting to the current vertex
				if (!(vertexWithEdge.containsKey(n.vertexNum)) && (fVertices[n.vertexNum].key > n.edgeWeight)) {
//					Set its edge to be the current edge and set its key as the weight of this edge
					fVertices[n.vertexNum].edge = new FibonacciEdge(n.vertexNum, key, n.edgeWeight);
//					To set the new key, calling the decreaseKey() method
//					System.out.println("Decrease Key Required for: " + fVertices[n.vertexNum].fNode.graphKey + " with item: " + n.edgeWeight);					
					fHeap.decreaseKey(fVertices[n.vertexNum].fNode, n.edgeWeight);
				}
			}
		}
		return vertexWithEdge;
	}
	
	/* Method: 	printFHeapScheme()
	 * Input:	Nil
	 * Output:	Nil
	 * Description:	This method prints the Fibonacci Vertices and the corresponding Fibonacci Heap
	 */		
	public void printFHeapScheme() {
//		Printing Fibonacci Vertices
		System.out.println("Printing Fibonacci Vertices from the FHeap Scheme");
		for (int i = 0 ; i < fVertices.length ; i++) {
			System.out.print(fVertices[i].graphKey + 
								" | " + fVertices[i].key + 
								" | " + (fVertices[i].edge != null ? 
											(fVertices[i].edge.v1 + ", " + 
													fVertices[i].edge.cost + ", " + 
													fVertices[i].edge.v2) : 
												null) + " --> ");
		}
		System.out.println();
		fHeap.printFibonacciHeap();
	}
}

class FibonacciVertex {
//	Key from the Graph
	int graphKey;
//	Key
	int key;
//	Edge from the source vertex to self
	FibonacciEdge edge;
//	Pointer to a Fibonacci Node
	FibonacciNode fNode;
//	Constructor
	public FibonacciVertex(int graphKey, int key, FibonacciEdge edge, FibonacciNode fNode) {
		this.graphKey = graphKey;
		this.key = key;
		this.edge = edge;
		this.fNode = fNode;
	}
}

class FibonacciEdge {
//	Two end-points of an edge and its cost
	int v1;
	int v2;
	int cost;
//	Constructor
	public FibonacciEdge(int v1, int v2, int cost) {
		this.v1 = v1;
		this.v2 = v2;
		this.cost = cost;
	}
}