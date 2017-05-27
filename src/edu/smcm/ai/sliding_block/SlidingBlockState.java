package edu.smcm.ai.sliding_block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import edu.smcm.ai.search.Action;

/**
 * Sliding Block Puzzle State.
 * 
 * The position of the various tiles in an instance of the puzzle. The rows are
 * numbered from the top left corner. The blank should therefore end up at (0,
 * 0).
 */
public class SlidingBlockState extends edu.smcm.ai.search.State {

	/**
	 * Tiles of puzzle.
	 */
	private int[][] tiles;

	/**
	 * A random number generator for initial states.
	 */
	private static Random oracle;

	/**
	 * Static initialisation block.
	 * 
	 * Can be used as a way of initialising static variables. It is more
	 * flexible than initialising them where they are declared.
	 */
	static {
		oracle = new Random();
	}

	/**
	 * Coordinate of a tile in the puzzle.
	 * 
	 * This is used primarily to allow findBlank to return a single value.
	 *
	 */
	public static class Coordinate {
		private int row;
		private int column;

		public Coordinate(int row, int column) {
			this.row = row;
			this.column = column;
		}

		public int row() {
			return row;
		}

		public int column() {
			return column;
		}
	}

	/**
	 * Create an initial state for Sliding Block Puzzle.
	 * 
	 * The state created is the goal state. That is all tiles are in order with
	 * blank at the top left corner.
	 * 
	 * @param size
	 *            length of the sides
	 */
	public SlidingBlockState(int size) {
		
		tiles = new int[size][size];
		
		for (int row = 0; row < size; row++) {
			for (int column = 0; column < size; column ++) {
				tiles[row][column] = row * size + column;
			}
		}
	}

	/**
	 * Create an initial state for the Sliding Puzzle using random moves.
	 * 
	 * Apply a supplied number of random moves to the puzzle to create an
	 * initial starting point for the puzzle. NOTE: the puzzle is trivially
	 * solvable in no more than the number of moves supplied. It may take fewer
	 * moves because the random moves may contain moves that undo a previous
	 * move, for example.
	 * 
	 * @param size
	 *            size of puzzle to create
	 * @param moves
	 *            number of random moves to apply
	 */
	public SlidingBlockState(int size, int moves) {
		int thisAction;
		List<Action> actions;
		
		tiles = new int[size][size];
		
		for (int row = 0; row < size; row++) {
			for (int column = 0; column < size; column ++) {
				tiles[row][column] = row * size + column;
			}
		}
		
		for (int swaps = 0; swaps < moves; swaps++) {
			actions = actions();
			thisAction = oracle.nextInt(actions.size());
//			for (int row = 0; row < size; row++) {
//				for (int column = 0; column < size; column ++) {
//					System.out.print(tiles[row][column] + " ");
//				}
//				System.out.println();
//			}
//			System.out.println(actions.get(thisAction).toString());
			takeAction((SlidingBlockAction) actions.get(thisAction));		
		}
	}

	/**
	 * Generate the list of <I>legal</I> actions in this state.
	 * 
	 * Generates a list of all actions possible in any state, then removes those
	 * that are illegal in this state because the blank is on an edge (or worse
	 * in a corner). This method strictly belongs in the Problem subclass, but
	 * is used in generating random starting states.
	 * 
	 * @return list of <I>legal</I> actions
	 */
	public List<Action> actions() {
		Coordinate blank;		
		List<Action> actions = SlidingBlockAction.actions();
		blank = findBlank();
		
		if (blank.column == 0) {
			actions.remove(SlidingBlockAction.left);
		}
		if (blank.column == size() - 1) {
			actions.remove(SlidingBlockAction.right);
		}
		if (blank.row == 0) {
			actions.remove(SlidingBlockAction.up);
		}
		if (blank.row == size() - 1) {
			actions.remove(SlidingBlockAction.down);
		}
		return actions;
	}

	/**
	 * Copy the state for Sliding Block Puzzle.
	 * 
	 * The state created is an exact copy of the supplied state. This copy can
	 * be changed without changing the original.
	 * 
	 * @param original
	 *            original puzzle instance to be copied
	 */
	public SlidingBlockState(SlidingBlockState original) {
		this.tiles = new int[original.size()][original.size()];

		for (int row = 0; row < tiles.length; row++) {
			for (int column = 0; column < tiles[row].length; column++) {
				this.tiles[row][column] = original.tiles[row][column];
			}
		}
	}

	/**
	 * Obtain the length of the sides of the Sliding Block Puzzle.
	 * 
	 * @return length of sides of puzzle
	 */
	public int size() {
		return tiles.length;
	}

	/**
	 * Accessor for tile value.
	 * 
	 * This particular accessor is useful for iterating through all of the tiles
	 * on the grid. NOTE: the top left corner is (0, 0).
	 * 
	 * @param row
	 *            the row being accessed
	 * @param column
	 *            the column being accessed
	 * @return the value on the tile at that position
	 */
	public int tileAt(int row, int column) {
		return tiles[row][column];
	}

	/**
	 * Accessor for a tile value.
	 * 
	 * This accessor is here for completeness sake. It is not expected that you
	 * will use it since you must create a Coordinate object to do so.
	 * 
	 * @param coordinate
	 *            the coordinates of the tile being accessed
	 * @return the value of the tile at the supplied coordinates
	 */
	public int tileAt(Coordinate coordinate) {
		return tiles[coordinate.row()][coordinate.column()];
	}

	/**
	 * Find the blank tile on the board.
	 * 
	 * This method returns a Coordinate object so we don't have to search the
	 * board twice -- once for the row and another time for the column.
	 * 
	 * @return the Coordinate of the blank tile
	 */
	public Coordinate findBlank() {
		for (int row = 0; row < tiles.length; row++) {
			for (int column = 0; column < tiles[row].length; column++) {
				if (tiles[row][column] == 0) {
					return new Coordinate(row, column);
				}
			}
		}

		throw new ImplementationException("No blank");
	}

	/**
	 * Mutator method that applies a SlidingBlockAction to the current tiles.
	 * 
	 * NOTE: This method should be used with care since it mutates the current
	 * state's tiles. It should only be used when generating an initial board
	 * and when updating a state that has been copied to make the next state (in
	 * the result method).
	 * 
	 * @param action
	 *            the action to be applied to the tile
	 */
	private void takeAction(SlidingBlockAction action) {
		
		int temp;
		Coordinate blank = findBlank();
		
		switch (action.value()) {
		case Right:
			temp = tileAt(blank.row, blank.column+1);
			tiles[blank.row][blank.column] = temp;
			tiles[blank.row][blank.column+1] = 0;
			break;
		case Left:
			temp = tileAt(blank.row, blank.column-1);
			tiles[blank.row][blank.column] = temp;
			tiles[blank.row][blank.column-1] = 0;
			break;
		case Up:
			temp = tileAt(blank.row-1, blank.column);
			tiles[blank.row][blank.column] = temp;
			tiles[blank.row-1][blank.column] = 0;
			break;
		case Down:
			temp = tileAt(blank.row+1, blank.column);
			tiles[blank.row][blank.column] = temp;
			tiles[blank.row+1][blank.column] = 0;
			break;
		}
	}

	/**
	 * Derive a new state from this state by applying an action.
	 * 
	 * This method should really be part of the SlidingBlockProblem class, but
	 * that means exposing a lot about the SlidingBlockState. This approach
	 * leads to more modularity, though re-implementation would be necessary if
	 * the SlidingBlockProblem's environment description were to change
	 * significantly.
	 * 
	 * @param action
	 *            the action to be taken
	 * @return a new state with the action taken
	 */
	public SlidingBlockState result(SlidingBlockAction action) {
		SlidingBlockState result;

		result = new SlidingBlockState(this);

		result.takeAction(action);

		return result;
	}

	/**
	 * Equality method.
	 * 
	 * In order to store SlidingBlockStates in a Set&lt;&gt; we need to supply
	 * an appropriate equals() method, otherwise equality will be reference
	 * equality, which is not what we intend. The method simply ensures the
	 * tiles are in the same place in both objects.
	 * 
	 * @param that
	 *            the other state being compared
	 * @return true if the tiles are in the same place
	 */
	public boolean equals(SlidingBlockState that) {
		boolean result;

		result = true;
		for (int row = 0; row < tiles.length; row++) {
			for (int column = 0; column < tiles[0].length; column++) {
				result = result && (tiles[row][column] == that.tiles[row][column]);
			}
		}

		return result;
	}

	/**
	 * Hash code.
	 * 
	 * In order to use the SlidingBlockState in a hash table (such as
	 * HashSet&lt;&gt;) or a Map (such as HashMap&lt;&gt;) we need to provide a
	 * has function. The hash should be the same for two objects that are the
	 * same, but as different as possible for other objects. We simply use the
	 * tiles to create a number using a radix of the board size.
	 * 
	 * @return the hash value
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return Arrays.deepHashCode(tiles);
	}

	/**
	 * A toString method.
	 * 
	 * Creates a String with numbers in the appropriate places for tiles. NOTE:
	 * This only works for the 8-puzzle currently.
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String result;

		result = "";
		for (int row = 0; row < tiles.length; row++) {
			for (int column = 0; column < tiles[0].length; column++) {
				result = result + tiles[row][column];
			}
			result = result + '\n';
		}

		return result;
	}
}
