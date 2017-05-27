package edu.smcm.ai.sliding_block;

import edu.smcm.ai.search.Heuristic;
import edu.smcm.ai.search.State;

/**
 * A heuristic that simply counts the number of tiles that are not in their
 * correct place in the puzzle. See page Russell and Norvig, Artificial
 * Intelligence: A Modern Approach, Third Edition, p. 103.
 *
 */
public class OutOfPlace extends Heuristic {

	/**
	 * Number of tiles out of place.
	 * 
	 * A count of the total number of tiles that are out of place.
	 */
	/* (non-Javadoc)
	 * @see edu.smcm.ai.search.Heuristic#cost(edu.smcm.ai.search.State)
	 */
	@Override
	public double cost(State state) {
		
		SlidingBlockState actual = (SlidingBlockState) state;
		
		double OOP = 0;
		
		for (int row = 0; row < actual.size(); row++) {
			for (int column = 0; column < actual.size(); column++) {
				if (actual.tileAt(row, column) != actual.size() * row + column)
					OOP++;
			}
		}
		
		return OOP;
	}
}
