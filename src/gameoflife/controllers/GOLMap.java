package gameoflife.controllers;

import java.util.Random;

import gameoflife.logic.Board2D;

public class GOLMap implements Board2D {

    boolean[][] map;

    int generation = 0;

    public GOLMap(int width, int height) {
        createEmptyBoard(width, height);
    }

    /// This is used to clone it manually to avoid work
    public GOLMap(GOLMap clone) {
        // Make sure to keep up2date as new values are needed
        this.map = clone.map.clone();
        this.generation = clone.generation;
    }

    @Override
    public void createEmptyBoard(int width, int height) {
        initialiseMap(width, height);
    }

    @Override
    public Board2D copy() {
        return new GOLMap(this);
    }

    @Override
    public boolean getCellState(int x, int y) {
        return map[x][y];
    }

    @Override
    public void setCellState(int x, int y, boolean state) {
        map[x][y] = state;
    }

    @Override
    public int getWidth() {
        return map.length;
    }

    @Override
    public int getHeight() {
        // 2D array has fixed width, so first position in x axis is used
        return map[0].length;
    }

    @SuppressWarnings("unused")
    private void initialiseMap(int width, int height) {
        // create 2d map based on dimensions
        map = new boolean[width][height];

        // Set default values
        for(boolean[] outer : map) {
            for(boolean point : outer) {
                point = false;
            }
        }
    }

    /**
     * This will give you a cloned 2d boolean array
     * Tried the map.clone() but it apparently did not break reference
     * TODO: Check into CLONE behavior
     * @param map This is the initial map to be cloned
     * @return This is an exact copy of map, but without reference
     */
    static boolean[][] cloneMap(boolean[][] map){
        // Going oldschool manual
        boolean[][] tmp = new boolean[map.length][map[0].length];

        // iterating to create map
        for(int i = 0; i < map.length; i++) {
            for(int j = 0; j < map[i].length; j++) {
                tmp[i][j] = map[i][j];
            }
        }
        // retrurning tmp
        return tmp;
    }

    /**
     * This function randomizeses the map, mostly used for debugging
     */
    public void randomizeMap() {
        Random r = new Random();
        for(int i = 0; i < map.length; i++) {
            for(int j = 0; j < map[i].length; j++) {
                setCellState(i, j, r.nextFloat() >= 0.5f); // Sets random values
            }
        }
    }
}
