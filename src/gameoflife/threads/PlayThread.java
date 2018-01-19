package gameoflife.threads;



import java.util.concurrent.BlockingQueue;

import gameoflife.controllers.GOLMap;
import gameoflife.controllers.Gol;
import gameoflife.graphics.BoardDrawer;
import javafx.scene.canvas.Canvas;

public class PlayThread implements Runnable {

	private final BlockingQueue<String> messageQueue;
	Gol gol;
	
	private volatile boolean running = false;
	
	public void terminate() {
		running = false;
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public PlayThread(Gol gol,BlockingQueue<String> messageQueue) {
		this.gol = gol;
		this.messageQueue = messageQueue;
	}
	
	@Override
	public void run() {
		running = true;
		while(running) {
			// Calculating the next generation
			gol.nextGeneration();
			
			//boardDrawer.drawBoard2D(gm, c);
			try {
				messageQueue.put("UPDATE");
				//Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
