package gameoflife.logic.readers;
import gameoflife.logic.Board2D;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Abstract class that represents file and stream reading for Game of Life.
 * Note: the exception handling in this class is not "correct" according to standard
 * practice. Exceptions should be cast upwards to the caller so that the caller can deal
 * with the given error appropriately. Since exception handling has not been introduced yet,
 * the exception handling is performed in this class.
 */
public abstract class GameOfLifeReader {
	
	/**
	 * Reads a game board from a character stream.
	 * @param r The character stream.
	 * @return The read game board or null if read failed.
	 */
	public abstract void readGameBoard(Reader r, Board2D board);
	
	/**
	 * Reads a game board from a file on secondary storage.
	 * @param file The file
	 * @return The game board or null if read failed.
	 */
	public void readGameBoardFromFile(File file, Board2D board) {
		try {
			readGameBoard(new FileReader(file), board);
		} catch (FileNotFoundException e) {
			System.err.println("Could not open the provided file.");
		}
	}
	
	/**
	 * Reads a game board from URL.
	 * @param url The URL
	 * @return The game board or null if read failed.
	 */
	public void readGameBoardFromURL(String url, Board2D board) {
		URL destination;
		try {
			destination = new URL(url);
		} catch (MalformedURLException e) {
			System.err.println("Malformed URL. Please provide a valid URL.");
			return;
		}
		URLConnection conn;
		try {
			conn = destination.openConnection();
		} catch (IOException e) {
			System.err.println("Could not establish connection. Check your Internet connection.");
			return;
		}
		try {
			readGameBoard(new InputStreamReader(conn.getInputStream()), board);
		} catch (IOException e) {
			System.err.println("Connection broken. Is your Internet connection broken?");
		}
	}
	
}