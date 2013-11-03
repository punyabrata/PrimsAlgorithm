package com.ADS.mst;

public class SimpleScheme {
//	#Edges in the Graph
	public int numEdges;
//	Array of edges with their costs
	public EdgeCost[] costArray;
	
	/* Method: Constructor
	 * Input: Integer
	 * Output: Nil
	 * Description:	This parameterized constructor takes number of edges as input
	 * The same will be the size of the cost-array used in the Simple Scheme 
	 */
	public SimpleScheme(int numEdges) {
		this.numEdges = numEdges;
		costArray = new EdgeCost[numEdges];
		for (int i = 0 ; i < numEdges ; i++) {
			costArray[i] = null;
		}
	}
	
	/* Method: 	addEdge()
	 * Input:	EdgeCost
	 * Output:	Void
	 * Description:	This method adds the edge to the cost-array
	 */		
	public void addEdge(EdgeCost e, int index) {
		costArray[index] = e;
	}
	
	/* Method: 	extractLocalMin()
	 * Input:	Nil
	 * Output:	EdgeCost
	 * Description:	This method extracts the Edge, with minimum cost, in the cost-array
	 */		
	public EdgeCost extractLocalMin() {
		int min = 1001;
		EdgeCost minEdge = null;
//		Traversing until there is no edge while ignoring already used edges
		for (int i = 0 ; i < numEdges && costArray[i] != null ; i++) {
			EdgeCost eCost = costArray[i];
			if (!eCost.isUsed && !eCost.isIgnored && min > eCost.cost) {
				min = eCost.cost;
				minEdge = eCost;
			}
		}
		return minEdge;
	}

	/* Method: 	invalidateEdge()
	 * Input:	EdgeCost
	 * Output:	Void
	 * Description:	This method invalidates an Edge by making 'isUsed = true'
	 */	
	public void invalidateEdge(EdgeCost e) {
		e.isUsed = true;
	}
	
	/* Method: 	ignoreEdge()
	 * Input:	EdgeCost
	 * Output:	Void
	 * Description:	This method ignores an Edge by making 'isIgnored = true'
	 */	
	public void ignoreEdge(EdgeCost e) {
		e.isIgnored = true;
	}
	
	/* Method: 	doesEdgeExist()
	 * Input:	EdgeCost
	 * Output:	Boolean
	 * Description:	This method checks whether an edge already exists in the cost-array
	 */	
	public Boolean doesEdgeExist(EdgeCost e) {
//		Checking for each edge in the costArray
		for (int i = 0 ; i < numEdges && costArray[i] != null ; i++) {
			EdgeCost eC = costArray[i];
//			If the two end-points and the cost are identical, the edge actually exists
//			However, the end-points may alternate
			if (
					(
					(e.v1.key == eC.v1.key && e.v2.key == eC.v2.key)
					||
					(e.v1.key == eC.v2.key && e.v2.key == eC.v1.key)
					)
					&& 
					(e.cost == eC.cost)
				) {
				return true;
			}
		}
//		Edge does not exist
		return false;
	}

}

class EdgeCost {
//	End-Points
	Vertex v1;
	Vertex v2;
//	Cost
	int cost;
//	Whether used in the MST
	Boolean isUsed;
//	Whether use of this edge creates a cycle while computing MST
	Boolean isIgnored;
	public EdgeCost(Vertex v1, Vertex v2, int cost, Boolean used, Boolean ignored) {
		this.v1 = v1;
		this.v2 = v2;
		this.cost = cost;
		this.isUsed = used;
		this.isIgnored = ignored;
	}
}