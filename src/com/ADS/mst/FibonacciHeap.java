package com.ADS.mst;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class FibonacciHeap {
//	Min-Item in the Fibonacci Heap
	public FibonacciNode minItem;
	
	/* Method: Constructor
	 * Input: Nil
	 * Output: Nil
	 * Description:	This non-parameterized constructor initializes the minItem to null
	 */
	public FibonacciHeap() {
		minItem = null;
	}
	
	/* Method: 	insert()
	 * Input:	FibonacciNode
	 * Output:	Void
	 * Description:	This method inserts a new node into the Fibonacci Heap
	 * The new node is inserted to the top level doubly circular linked list of the heap
	 * While inserting, if the top level is empty, we insert it as a first node and make it minItem
	 * Else, we check the new minItem in O(1) time and set the minItem accordingly
	 */	
	public void insert(FibonacciNode fibNode) {
//		Making it a top level singleton Min_Tree
		fibNode.topLevel = true;
//		Setting parent and childCut to 'null'
		fibNode.parent = null;
		fibNode.childCut = null;
//		If minItem is null, set the current node as minItem
		if (minItem == null) {
//			Making it doubly-circular
			fibNode.next = fibNode;
			fibNode.previous = fibNode;
//			Setting as minItem
			minItem = fibNode;
		} else{
//			If there is already a minItem, add the current node after the minItem
//			Set the doubly-circular connections
			fibNode.next = minItem.next;
			fibNode.previous = minItem;
			minItem.next.previous = fibNode;
			minItem.next = fibNode;
//			Setting the new minItem if current node has less key than the minItem does
			if (minItem.key > fibNode.key) {
				minItem = fibNode;
			}
		}
	}

	/* Method: 	extractMin()
	 * Input:	Nil
	 * Output:	FibonacciNode
	 * Description:	This method extracts the minItem from the tree
	 * After this extraction operation, all the subtrees of this minItem, and all the other
	 * top level Min-Trees are processed with the following operation.
	 * The roots of all the above Min-Trees are broken from their doubly linked list chain
	 * All these roots are pushed into an ArrayList of FibonacciNode-s
	 * The ArrayList is sent to the method pairwiseCombine()
	 * The minItem is returned
	 */		
	public FibonacciNode extractMin() {
		FibonacciNode min = minItem;
//		If minItem is null, i.e., the tree is empty, return 'null'
		if (min == null) {
			return null;
		}
//		ArrayList of minTrees to be combined pairwise
		ArrayList<FibonacciNode> minTrees = new ArrayList<FibonacciNode>();
//		Adding every child of the minItem to the ArrayList
		if (minItem.child != null) {
//			Child of minItem present
			FibonacciNode minChild = minItem.child;
//			Adding child to the ArrayList while setting parent to 'null'
			minChild.parent = null;
			minTrees.add(minChild);
//			Adding other nodes if present
			FibonacciNode minChildNext = minChild.next;
			while (minChildNext != minChild) {
//				Adding to the ArrayList while setting parent to 'null'
				minChildNext.parent = null;
				minTrees.add(minChildNext);
//				Moving forward
				minChildNext = minChildNext.next;
			}
		}
//		Adding other top level Min-Trees, if present, to the ArrayList
		FibonacciNode minItemNext = minItem.next;
		while (minItemNext.graphKey != minItem.graphKey) {
//			Adding to the ArrayList while setting top level to 'false'
			minItemNext.topLevel = false;
			minTrees.add(minItemNext);
//			Moving forward
			minItemNext = minItemNext.next;
			
		}
//		Sending the ArrayList of Min-Trees to the pairwiseCombine method
		if (!minTrees.isEmpty()) {
//			Breaking the doubly circular connections
			for (FibonacciNode fNode : minTrees) {
				fNode.previous = fNode;
				fNode.next = fNode;
			}
//			When a list of Trees are sent to be combined pairwise
//			there is no min element, therefore setting it to 'null'
			minItem = null;
//			Sending the ArrayList
			pairwiseCombine(minTrees);
		} else {
//			If the last element is extracted, minItem must be set to 'null'
			minItem = null;			
		}
//		Returning the minItem
		return min;
	}
	
	/* Method: 	pairwiseCombine()
	 * Input:	ArrayList<FibonacciNode>
	 * Output:	Void
	 * Description:	This method performs the pairwise-combine operation for the minTrees
	 * It maintains a Tree Table which keeps track of the degree of the tree
	 * The method traverses through the minTrees one by one
	 * It marks the degree of the current minTree to 'true' in the designated cell
	 * that points to that minTree
	 * If the mark was already 'true',
	 * the method combines the two trees by calling combineMintrees()
	 * When all the minTrees are checked for combination, we connect the final minTrees
	 * using doubly circular linked list and set the minItem pointer
	 */		
	private void pairwiseCombine(ArrayList<FibonacciNode> minTrees) {
//		System.out.println("minTrees.size(): " + minTrees.size());
//		Tree Table
		HashMap<Integer, FibonacciNode> treeTable = new HashMap<Integer, FibonacciNode>();
//		Checking for every Min-Tree in the ArrayList
		for (FibonacciNode fNode : minTrees) {
//			As long as Min-Tree of same degree already present
			while (treeTable.containsKey(fNode.degree) && treeTable.get(fNode.degree) != null) {
				FibonacciNode previousMinTree = treeTable.get(fNode.degree);
//				Remove Key
				treeTable.remove(fNode.degree);
//				Combine the two Min-Trees
				fNode = combineMintrees(previousMinTree, fNode);
			}
//			Min-Tree of a new degree
			treeTable.put(fNode.degree, fNode);
		}
//		Linking the final Min-Trees by inserting them one by one
		Set<Integer> uniqueDegrees = treeTable.keySet();
		for (Integer degree : uniqueDegrees) {
			FibonacciNode fibNode = treeTable.get(degree);
			insert(fibNode);
		}
	}
	
	/* Method: 	combineMintrees()
	 * Input:	FibonacciNode, FibonacciNode
	 * Output:	FibonacciNode
	 * Description:	This method combines two Min-Trees
	 * It checks for the key of minTree1 and minTree2, whichever has the bigger key,
	 * that becomes the child of the other
	 * The final Min-Tree has got its degree increased by 1
	 */		
	private FibonacciNode combineMintrees(FibonacciNode minTree1, FibonacciNode minTree2) {
//		If minTree1 has the bigger key
		if (minTree1.key > minTree2.key) {
			return setPointers(minTree1, minTree2);
		} else {
//			minTree2 has the bigger key
			return setPointers(minTree2, minTree1);
		}
	}
	
	/* Method: 	setPointers()
	 * Input:	FibonacciNode, FibonacciNode
	 * Output:	FibonacciNode
	 * Description:	This method makes t1 the child of t2
	 * It also sets the parent, child, next and previous pointers accordingly
	 */			
	private FibonacciNode setPointers(FibonacciNode t1, FibonacciNode t2) {
//		t1 will be the child of t2 which will be the final Min-Tree
		t1.parent = t2;
//		ChildCut value is set to false when t1 became the child of t2
		t1.childCut = false;
//		If t2 already has a child
		if (t2.child != null) {
			FibonacciNode minTreeChild = t2.child;
//			Setting the doubly-circular connections
			t1.next = minTreeChild.next;
			t1.previous = minTreeChild;
			minTreeChild.next.previous = t1;
			minTreeChild.next = t1;
		} else {
//			minTree1 is the first child of minTree2
			t2.child = t1;
		}
		t2.degree++;
		return t2;		
	}

	/* Method: 	decreaseKey()
	 * Input:	FibonacciNode, int
	 * Output:	Void
	 * Description:	This method sets a new key 'newKey' to a given FibonacciNode
	 * It performs Cascading Cut if the Min-Tree property is violated
	 * It sets new minItem pointer if the given FibonacciNode is a root node
	 */		
	public void decreaseKey(FibonacciNode fibNode, int newKey) {
//		Set new key
		fibNode.key = newKey;
//		If top level root node, check the minItem pointer
		if (fibNode.topLevel) {
//			System.out.println("minItem: " + (minItem != null ? minItem.graphKey : null));
//			System.out.println("fibNode.key: " + fibNode.key);
			if (minItem.key > fibNode.key) {
//				Set the current node/root to be the minItem
				minItem = fibNode;
			}
		} else {
//			Check with its parent and make cascading cut if required
			FibonacciNode fibParent = fibNode.parent;
			if (fibNode.key < fibParent.key) {
//				Pointer Changes - Removing the node from its parent
//				Next and Previous Nodes
				fibNode.previous.next = fibNode.next;
				fibNode.next.previous = fibNode.previous;
//				Self
				fibNode.next = null;
				fibNode.previous = null;				
//				Insert fibNode to the top level list
				fibNode.parent = null;
				insert(fibNode);
				fibParent.degree--;
//				Child pointer should be null if degree becomes zero (0)
				if (fibParent.degree == 0) {
					fibParent.child = null;
				}
//				-- Cascading cut operation --
//				Traverse up till a top level node using the parent pointer
//				If the childCut value of the parent is 'false', stop
				while (!fibParent.topLevel && fibParent.childCut) {
//					Storing the parent in a temporary variable
					FibonacciNode fibonacciParent = fibParent.parent;
//					Pointer Changes - Removing the node from its parent
//					Next and Previous Nodes
					fibParent.previous.next = fibParent.next;
					fibParent.next.previous = fibParent.previous;
//					Self
					fibParent.next = null;
					fibParent.previous = null;					
//					Inserting the node to the top level
					fibParent.parent = null;
					insert(fibParent);
//					Decreasing its degree by 1
					fibonacciParent.degree--;
//					Child pointer should be null if degree becomes zero (0)
					if (fibonacciParent.degree == 0) {
						fibonacciParent.child = null;
					}					
//					Setting it back to the fibParent variable
					fibParent = fibonacciParent;
				}
//				Setting the childCut value to 'true' for the last parent if it isn't a root node
				if (!fibParent.topLevel)
					fibParent.childCut = true;
			}
		}
	}
	
	/* Method: 	printFibonacciHeap()
	 * Input:	Nil
	 * Output:	Void
	 * Description:	This method prints the entire Fibonacci Heap
	 */		
	public void printFibonacciHeap() {
//		Passing the minItem of the Fibonacci Heap
		FibonacciNode min = minItem;
		if (min != null)
			System.out.println("Min Item: " + minItem.key);
		else
			System.out.println("No Min Item is present...");
		printHeap(min);
	}
	
	/* Method: 	printHeap()
	 * Input:	FibonacciNode
	 * Output:	Void
	 * Description:	This method prints the Heap given a start node
	 */	
	private void printHeap(FibonacciNode fibNode) {
//		Process if fibNode not null
		if (fibNode != null) {
//			ArrayList to contain the nodes in one level
			ArrayList<FibonacciNode> fibonacciLevelList = new ArrayList<FibonacciNode>();
//			Adding the current node to the ArrayList
			fibonacciLevelList.add(fibNode);
//			Adding the other nodes to the ArrayList
			FibonacciNode fibNext = fibNode.next;
			for (FibonacciNode f = fibNext ; f != fibNode ; f = f.next) {
				fibonacciLevelList.add(f);
			}
//			Printing the list
			printList(fibonacciLevelList);
			for (FibonacciNode f : fibonacciLevelList) {
//				Recursive call with the child of the current node
				printHeap(f.child);				
			}
		} else {
//			Return null if fibNode is null
			return;
		}
	}

	/* Method: 	printList()
	 * Input:	FibonacciNode
	 * Output:	Void
	 * Description:	This method prints the circular doubly linked list at every level
	 */		
	private void printList(ArrayList<FibonacciNode> fibonacciLevelList) {
		for (FibonacciNode f : fibonacciLevelList) {
			System.out.print(f.graphKey + " | " + f.key + " | " + f.degree + " | " + 
					(f.child == null ? f.child : f.child.key) + " | " + 
					(f.parent == null? f.parent : f.parent.key) + " | " + 
					f.childCut + " | " + f.next.key + " | " + f.previous.key + " | " + 
					f.topLevel + " --> ");
		}
		System.out.println();
	}
}

class FibonacciNode {
//	Key
	public int key;
//	#Children
	public int degree;
//	Pointer to the child FibonacciNode
//	Leaves of the Min-Trees will have it as 'null'
	public FibonacciNode child;
	/* Pointer to the parent FibonacciNode
	 * In a certain level of a Min-Tree only one of the nodes in that level
	 * will have the parent pointer
	 * Roots of the Min-Trees will have their parent pointer set to 'null'   
	 */
	public FibonacciNode parent;
	/* 1) Roots, which are in the top level of the Fibonacci Heap, of the Min-Trees
	 * will have this value set to 'null'
	 * 2) If a Fibonacci node has NOT lost a single child since it has become the child of its
	 * parent, the childCut value will be 'false'
	 * 3) If a Fibonacci node loses ONE child since it has become the child of its parent
	 * the childCut value will be set to 'true'
	 * 4) If a Fibonacci node loses MORE THAN ONE child since it has become the child of its parent
	 * the node will be upgraded to the top level
	 * 5) In the top level the childCut value of this node will automatically be set to 'null'
	 * */
	public Boolean childCut;
//	Pointer to the next node in the doubly linked list
//	in the level the current Fibonacci node resides
	public FibonacciNode next;
//	Pointer to the previous node in the doubly linked list
//	in the level the current Fibonacci node resides	
	public FibonacciNode previous;
//	Set to 'true' for the top level nodes, i.e., the roots of the Min-Trees, otherwise false
	public Boolean topLevel;
//	Unique Identifier
	public int graphKey;
	
//	Constructor
	public FibonacciNode(int key, int degree, FibonacciNode child, FibonacciNode parent,
							Boolean childCut, FibonacciNode next, FibonacciNode previous,
							Boolean topLevel, int graphKey) {
		this.key = key;
		this.degree = degree;
		this.child = child;
		this.parent = parent;
		this.childCut = childCut;
		this.next = next;
		this.previous = previous;
		this.topLevel = topLevel;
		this.graphKey = graphKey;
	}
}