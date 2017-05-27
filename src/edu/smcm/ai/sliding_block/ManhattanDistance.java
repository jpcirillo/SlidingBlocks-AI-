package edu.smcm.ai.sliding_block;

import edu.smcm.ai.search.Heuristic;
import edu.smcm.ai.search.State;

/**
 * A heuristic that computes the sum of the Manhattan distances between the
 * tiles and their correct positions. See Russell and Norvig, Artificial
 * Intelligence: A Modern Approach, Third Edition, p.103.
 */
public class ManhattanDistance extends Heuristic {

	/**
	 * Sum of Manhattan distances of tiles.
	 * 
	 * The sum for each tile of the difference in the row and column between
	 * actual and goal positions.
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.smcm.ai.search.Heuristic#cost(edu.smcm.ai.search.State)
	 */
	@Override
	public double cost(State state) {
		
		SlidingBlockState actual = (SlidingBlockState) state;
		
		double dist = 0;
		
		for (int row = 0; row < actual.size(); row++) {
			for (int column = 0; column < actual.size(); column++) {
				dist += Math.abs((actual.tileAt(row, column) / 3) - row) + Math.abs((actual.tileAt(row, column) % 3) - column);
			}
		}
		
		return dist;
	}

}
