package gameoflife.logic;

/**
 * Interface that represents the logic for Game of Life.
 */
public interface GameOfLife {
	
	/**
	 * Forward the game by one generation.
	 */
	void nextGeneration();
	
	/**
	 * Gets the game board.
	 * @return The current game board.
	 */
	Board2D getBoard();
	
	/**
	 * Sets the game board.
	 * @param board The game board.
	*/
	void setBoard(Board2D board);
	
}