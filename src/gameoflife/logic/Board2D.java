package gameoflife.logic;

/**
 * Interface that represents a two dimensional board with boolean-valued cells.
 */
public interface Board2D {

    /**
     * Creates an empty game board with a given width and height.
     * @param width The width of the board
     * @param hegiht The height of the board
     */
    void createEmptyBoard(int width, int height);

    /**
     * Returns a copy of the board.
     * @return Deep copy of the board.
     */
    Board2D copy();

    /**
     * Gets the state of a given cell.
     * @param x The x coordinate of the cell.
     * @param y The y coordinate of the cell.
     * @return The state of cell (x,y).
     */
    boolean getCellState(int x, int y);

    /**
     * Sets the state of a given cell.
     * @param x The x coordinate of the cell.
     * @param y The y coordinate of the cell.
     * @param state The state of the cell.
     */
    void setCellState(int x, int y, boolean state);

    /**
     * Gets the current width of the board.
     * @return The width of the board.
     */
    int getWidth();

    /**
     * Gets the height of the board.
     * @return The height of the board.
     */
    int getHeight();

}
