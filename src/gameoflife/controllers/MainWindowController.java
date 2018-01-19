package gameoflife.controllers;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import gameoflife.graphics.BoardDrawer;
import gameoflife.logic.readers.GameOfLifeReaderRLE;
import gameoflife.threads.PlayThread;
import javafx.animation.AnimationTimer;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainWindowController  implements Initializable, EventHandler<KeyEvent>{

	@FXML
	private Canvas canvas;
	
	@FXML
	private MenuItem openFile;
	
	@FXML
	private MenuItem SaveFile;
	
	// Added to make communicating between threads synchronized
	final BlockingQueue<String> messageQueue = new ArrayBlockingQueue<>(1);
	
	BoardDrawer boardDrawer;
	Gol gol;
	GOLMap gm;
	
	// This is the calculation thread
	PlayThread playThread;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		// set canvas draw
		//canvas = new Canvas(500, 400);
		boardDrawer = new BoardDrawer();
		gm = new GOLMap(500, 500);
		gm.randomizeMap();
		gol = new Gol(gm);
		draw();
		
		// Setting up playThread
		playThread = new PlayThread(gol, messageQueue);
		
		// Setting openFile reader
		System.out.println(openFile);
		openFile.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				GameOfLifeReaderRLE rle = new GameOfLifeReaderRLE();
			    FileChooser chooser = new FileChooser();
			    chooser.setTitle("Open File");
			    File f = chooser.showOpenDialog(openFile.getParentPopup().getScene().getWindow());
			    rle.readGameBoardFromFile(f, gm);
			}
			
		});
		
	}
	
	private void draw() {
		boardDrawer.drawBoard2D(gm, canvas);
	}
	
	private void playGOL() {
		Thread t = new Thread(playThread);
		t.setDaemon(true);
		t.start();
		startTimer();
	}
	
	private void startTimer() {
	     final LongProperty lastUpdate = new SimpleLongProperty();

	      final long minUpdateInterval = 10 ; // nanoseconds. Set to higher number to slow output.

	        AnimationTimer timer = new AnimationTimer() {
	            @Override
	            public void handle(long now) {
	                if (now - lastUpdate.get() > minUpdateInterval) {
	                    final String message = messageQueue.poll();
	                    if (message != null) { // If message is present, update canvas
	                        draw();
	                    }
	                    lastUpdate.set(now);
	                }
	            }

	        };

	        timer.start();
	}

	@Override
	public void handle(KeyEvent event) {
		
		switch(event.getCode()) {
		case A:
			boardDrawer.setX_offset(boardDrawer.getX_offset() + 10.0);
			break;
		case D:
			boardDrawer.setX_offset(boardDrawer.getX_offset() - 10.0);
			break;
		case W:
			boardDrawer.setY_offset(boardDrawer.getY_offset() + 10.0);
			break;
		case S:
			boardDrawer.setY_offset(boardDrawer.getY_offset() - 10.0);
			break;
		case E:
			boardDrawer.setZ_offset(boardDrawer.getZ_offset() + 1.0);
			break;
		case Q:
			boardDrawer.setZ_offset(boardDrawer.getZ_offset() - 1.0);
			break;
		}
		draw();
		if(event.getCode() == KeyCode.SPACE) {
			if(playThread.isRunning()) {
				playThread.terminate();
			}else {
				playGOL();
			}
		}
		
	}

	public void setCanvasWidth(double width) {
		canvas.setWidth(width);
		draw();
	}
	
	public void setCanvasHeight(double height) {
		canvas.setHeight(height);
		draw();
	}

}
