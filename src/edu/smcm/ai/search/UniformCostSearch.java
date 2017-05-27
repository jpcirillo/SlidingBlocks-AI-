package edu.smcm.ai.search;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Implementation of the Uniform Cost Search. See Russell and Norvig, Artificial
 * Intelligence: A Modern Approach, Third Edition, p 84. The frontier is
 * represented using both a PriorityQueue and a Map&lt;State, Node&gt; to make
 * determining membership quicker.
 */
public class UniformCostSearch extends Search {

	/**
	 * Set of states explored so far.
	 */
	private Set<State> explored;

	/**
	 * A Map that associates States with Nodes
	 */
	private Map<State, Node> frontier_map;

	/**
	 * The queue of Nodes in the frontier
	 */
	private PriorityQueue<Node> frontier_queue;

	/**
	 * Counter for the number of nodes generated
	 */
	private int nodes_generated;

	/**
	 * Default Constructor.
	 */
	public UniformCostSearch() {
		this.explored = new HashSet<State>();
		this.frontier_map = new HashMap<State, Node>();
		this.frontier_queue = new PriorityQueue<Node>();
	}

	/**
	 * Replace the frontier queue with another.
	 * 
	 * This allows the Uniform Costs Search to be transformed into another
	 * search that orders the frontier queue differently.
	 * 
	 * @param frontier_queue
	 *            new frontier queue
	 */
	protected void frontierQueue(PriorityQueue<Node> frontier_queue) {
		this.frontier_queue = frontier_queue;
	}

	/**
	 * Get the number of nodes generated in this search.
	 *
	 * @return number of nodes generated
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.smcm.ai.search.Search#nodesGenerated()
	 */
	public int nodesGenerated() {
		return nodes_generated;
	}

	/**
	 * Perform a Uniform Cost Search.
	 * 
	 * Pseudocode for this method can be found in Russell and Norvig, Artificial
	 * Intelligence: A Modern Approach, Third Edition, p. 84. Note that the
	 * frontier is made up of both a priority queue and a has table.
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.smcm.ai.search.Search#search(edu.smcm.ai.search.Problem)
	 */
	public List<Action> search(Problem problem) {
		frontier_queue.clear();
		frontier_map.clear();
		Node child;
		
		Node node = new Node(null, null, problem.initialState(), 0);
		nodes_generated = 1;
		frontier_queue.add(node);
		frontier_map.put(node.state(), node);
		
		do {
			
			//System.out.println("cost: " + node.cost());
			
			if (frontier_queue.isEmpty())
				return null;
			
			node = frontier_queue.remove();
			nodes_generated++;
			frontier_map.remove(node);
			
			if (problem.isGoalState(node.state()))
				return node.solution();
			
			explored.add(node.state());
			
			//System.out.println(node.state().toString());
			//System.out.println(problem.actions(node.state()));
			
			for (Action e : problem.actions(node.state())){
				
				child = problem.childNode(node, e);
				
				if ((!frontier_queue.contains(child.state())) && (!explored.contains(child.state()))) {
					
					frontier_queue.add(child);
					frontier_map.put(child.state(), child);
					
				} else if ((frontier_queue.contains(child.state()) && frontier_map.get(child).cost() > child.cost())) {
					
					frontier_map.put(child.state(), child);
					
				}
				
			}
			
		} while(true);
		
	}
}
