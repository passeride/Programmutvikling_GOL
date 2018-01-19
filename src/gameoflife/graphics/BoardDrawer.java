package gameoflife.graphics;

import gameoflife.controllers.GOLMap;
import gameoflife.logic.Board2D;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class BoardDrawer {

	private double square_size = 1.0;
	private double x_offset = 1.0, y_offset = 1.0, z_offset = 10.0;

	
	public double getSquare_size() {
		return square_size;
	}


	public void setSquare_size(double square_size) {
		this.square_size = square_size;
	}


	public double getX_offset() {
		return x_offset;
	}


	public void setX_offset(double x_offset) {
		this.x_offset = x_offset;
	}


	public double getY_offset() {
		return y_offset;
	}


	public void setY_offset(double y_offset) {
		this.y_offset = y_offset;
	}


	public double getZ_offset() {
		return z_offset;
	}


	public void setZ_offset(double z_offset) {
		if(z_offset > 0.0) // ZERO IS NOT VALID VALUE
			this.z_offset = z_offset;
	}


	public void drawBoard2D(Board2D map, Canvas c) {
		// Get the graphicscontext for drawing
		GraphicsContext gc = c.getGraphicsContext2D();
		
		// Clear old
		gc.clearRect(0, 0, c.getWidth(), c.getHeight());
		
		// Fill background, might not be needed
		gc.fillRect(0, 0, c.getWidth(), c.getHeight());
		
		// Get dimensions
		int width = map.getWidth();
		int height = map.getHeight();
		
		// Go over rects
		for(int i = 0; i < width; i ++) {
			for(int j = 0; j < height; j++) {
				// Get state of cell
				boolean state = map.getCellState(i, j);
				
				// Set color to state
				gc.setFill(state ? Color.WHITE : Color.BLACK);
				
				// Draw rects
				gc.fillRect( x_offset + i * square_size * z_offset,  y_offset + j * square_size * z_offset, square_size * z_offset, square_size * z_offset);
				
			}
		}
		
	}
	
}
