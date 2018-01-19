package gameoflife.controllers;

import gameoflife.logic.Board2D;
import gameoflife.logic.GameOfLife;

public class Gol implements GameOfLife {

    /**
     * This is the map reference used to calculate next value
     */
    private GOLMap gm;

    /**
     * This will keep track of what generation we are on
     */
    private int generation;

    public Gol(GOLMap gm) {
        this.gm = gm;
    }


    @Override
    public void nextGeneration() {
        // Clone map
        //boolean[][] tmp = map.clone(); // This did not break the reference

        boolean[][] tmp = GOLMap.cloneMap(gm.map);


        for(int i = 0; i < tmp.length; i++) {
            for(int j = 0; j < tmp[i].length; j++) {
                int live_neigboors = getLiveNeighboors(i, j); // Get live neigboors
                if(gm.map[i][j]) { // Cell is alive
                    if(!(live_neigboors == 3 || live_neigboors == 2)) { // If more than 3 or less than 2
                        tmp[i][j] = false; // Kill cell
                    }
                }else { // Cell is dead
                    if(live_neigboors == 3) { // if 3 live_neigboors, reproduce
                        tmp[i][j] = true; // Revive cell
                    }
                }
            }
        }
        // Calculations done
        generation++; // increment generation

        // Setting map to new map
        gm.map = tmp;
    }

    @Override
    public Board2D getBoard() {
        return gm;
    }

    @Override
    public void setBoard(Board2D board) {
        if(board instanceof GOLMap)
            gm = (GOLMap)board;
        else
            System.out.println("Don't play smart with me mister!");
    }

    /**
     * This returns the number of live neighboors
     * It does not wrap around and ignores edges
     * @param x coordinate
     * @param y coordinate
     * @return number of neighbooring square that's alive
     */
    private int getLiveNeighboors(int x, int y) {
        // Create an array of relative coordinates to check, mostly for simple visualization
        int[][] neigboors = new int[][] {
            {-1, -1},{-1, 0},{-1, 1},
            { 0, -1}        ,{ 0, 1},
            { 1, -1},{ 1, 0},{ 1, 1},
        };

        int live_neigboors = 0;

        for(int i = 0; i < neigboors.length; i++) {
            // Coordinates to check
            int x_tmp = x + neigboors[i][0];
            int y_tmp = y + neigboors[i][1];
            // Checking bounds
            if(isWithinMap(x_tmp, y_tmp)) {
                if(gm.getCellState(x_tmp, y_tmp)) { // If cell is live
                    live_neigboors++; // Increment live_neigboors count
                }
            }
        }

        // Returning live_neigboors
        return live_neigboors;
    }

    /**
     * This will tell you wether the given coordinates are withing the bounds of the map or not
     * @param x coordinate
     * @param y coordinate
     * @return TRUE if this is valid map coordinate FALSE otherwise
     */
    private boolean isWithinMap(int x, int y) {
        //xy must be < max and > min
        return x < gm.map.length && x > 0 && y < gm.map[0].length && y > 0;
    }


}
