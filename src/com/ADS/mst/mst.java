package com.ADS.mst;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class mst {
	/* Method: main()
	 * Input: CommandLine Arguments
	 * Output: Void
	 * Description: This method is the main method() 
	 */	
	public static void main(String args[]) throws FileNotFoundException{
		long start = 0;
		long stop = 0;
		start = System.currentTimeMillis();
		
//		The Algorithm
		Graph graph;
		if ("-r".equals(args[0])) {
			graph = new Graph(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
//			graph.printGraph();
			graph.traverseDFS();
			graph.findMSTSimple();
//			graph.findMSTFHeap();
		} else if ("-s".equals(args[0]) || "-f".equals(args[0])) {
			graph = new Graph(args[1]);
			graph.printGraph();
			graph.traverseDFS();
			if ("-s".equals(args[0])) {
				graph.findMSTSimple();
			} else if ("-f".equals(args[0])) {
				graph.findMSTFHeap();
			}
		}
		
		stop = System.currentTimeMillis();
//		Execution Time
		double time = stop - start;
		System.out.println("Total time taken: " + time/1000 + " s");
	}
}
