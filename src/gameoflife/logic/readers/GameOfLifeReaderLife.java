package gameoflife.logic.readers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import gameoflife.logic.Board2D;

/**
 * Supports the file formats Life 1.06 and 1.05.
 */
public class GameOfLifeReaderLife extends GameOfLifeReader {

	@Override
	public void readGameBoard(Reader reader, Board2D board) {
        BufferedReader bufferedReader = new BufferedReader(reader);

        String line;
        String format = null;
        try {
			while ((line = bufferedReader.readLine()) != null)
			{
			    if (line.length() > 2)
			    {
			        char prefix = Character.toUpperCase(line.charAt(1));
			        String cleanLine = line.substring(2).trim();

			        if (prefix == 'D' || prefix == 'C' || prefix == 'O')
			        	; // meta data
			        else if (prefix == 'L')
			        {
			            format = cleanLine;
			            if (format.contains("1.06"))
			                parse106BoardData(bufferedReader, board);
			        }
			        else if (prefix == 'R')
			        {
			            // ignore rule
			        }
			        else if (prefix == 'P')
			        {
			            if(format == null) {
			                System.err.println("Could not read data. Wrong file format.");
			                return;
			            }

			            if (format.contains("1.05"))
			                parse105BoardData(bufferedReader, line, board);
			        }
			    }
			}
		} catch (IOException e) {
			System.err.println("Could not read data (io exception).");
		}
	}
	
    private void parse105BoardData(BufferedReader reader, String firstBlockStartLine, Board2D board) {
        List<Point> cellBlockStartPoints = new ArrayList<>();
        List<String> lines = new ArrayList<>();
        lines.add(firstBlockStartLine);

        // Add all proceeding lines to the line list
        try {
			for(String line; (line = reader.readLine()) != null;)
			    if(!line.isEmpty())
			        lines.add(line);
		} catch (IOException e) {
			System.err.println("IO exception when reading data stream.");
		}

        int maxX = 0, minX = 0, maxY = 0, minY = 0, rowCount = 1;
        Point blockStartPoint = new Point();
        for(String line : lines)
        {
            if(line.charAt(0) == '#')
            {
                // Get the block staring point and save it
                blockStartPoint = getPointFromStringNumbers(line.substring(2).trim().split(" "));
                if(blockStartPoint == null) return;
                cellBlockStartPoints.add(blockStartPoint);

                // Find the minimum x and y points
                minX = Math.min(minX, blockStartPoint.x);
                minY = Math.min(minY, blockStartPoint.y);

                // Reset the row count
                rowCount = 1;
            }
            else
            {
                // Find the maximum x and y points
                maxX = Math.max(maxX, blockStartPoint.x + line.length());
                maxY = Math.max(maxY, blockStartPoint.y + (rowCount++));
            }
        }

        // Calculate the width and height from the max and min points
        int width = maxX - minX;
        int height = maxY - minY;

        if(width < 1 || height < 1)
            System.err.println("Could not read data, invalid width or height.");

        // Create board data
        board.createEmptyBoard(width, height);

        int rowIndex = 0, cellBlockCount = 0, startX = 0, startY = 0;
        for(String line : lines)
        {
            if(line.charAt(0) == '#')
            {
                // Gets the start point and calculates the position in the array
                Point start = cellBlockStartPoints.get(cellBlockCount++);
                startX = (start.x < 0) ? Math.abs(start.x - minX) : Math.abs(minX) + start.x;
                startY = (start.y < 0) ? Math.abs(start.y - minY) : Math.abs(minY) + start.y;
                rowIndex = 0;
            }
            else
            {
                if(startX + line.length() > board.getWidth() || startY + rowIndex >= board.getHeight())
                    System.err.println("Outside board definition, invalid data.");

                for(int i = 0; i < line.length(); i++)
                    board.setCellState(startX + i, startY + rowIndex, line.charAt(i) == '*');

                rowIndex++;
            }
        }
    }

    private void parse106BoardData(BufferedReader reader, Board2D board) {
        List<Point> points = new ArrayList<>();

        // Reads all the lines and adds the numbers to the point list.
        try {
			for(String line; (line = reader.readLine()) != null;)
			    if(!line.isEmpty()) {
			    	Point readP = getPointFromStringNumbers(line.trim().split(" "));
			    	if(readP != null)
			    		points.add(readP);
			    	else return;
			    }
		} catch (IOException e) {
			System.err.println("IO exception when reading data stream.");
		}

        // Finds the max and min points
        int maxX = 0, minX = 0, maxY = 0, minY = 0;
        for(Point p : points)
        {
            minX = Math.min(minX, p.x);
            minY = Math.min(minY, p.y );
            maxX = Math.max(maxX, p.x + 1);
            maxY = Math.max(maxY, p.y + 1);
        }

        // Calculates the width and height from the max and min points
        int width = maxX - minX;
        int height = maxY - minY;

        if(width < 1 || height < 1)
        	System.err.println("Outside board definition, invalid data.");

        // Instantiates the array with the calculated sizes
        board.createEmptyBoard(width, height);

        // Sets the point in the array representing zero in the Life universe
        int zeroX = Math.abs(minX);
        int zeroY = Math.abs(minY);

        // Adds the point data to the array
        points.forEach(p -> board.setCellState(zeroX + p.x, zeroY + p.y, true));
    }

    private Point getPointFromStringNumbers(String[] numbers)
    {
        try
        {
            return new Point(Integer.parseInt(numbers[0]), Integer.parseInt(numbers[1]));
        }
        catch (Exception e)
        {
            System.err.println("Wrong or invalid format in data.");
            return null;
        }
    }

}

class Point
{
    public int x;
    public int y;

    public Point(){}

    public Point(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public Point add(int v) { x += v; y += v; return this; }
    public Point sub(int v) { x -= v; y -= v; return this; }
    public Point mul(double v) { x *= v; y *= v; return this; }
    public Point div(double v) { x /= v; y /= v; return this; }

    public Point add(Point v) { x += v.x; y += v.y; return this; }
    public Point sub(Point v) { x -= v.x; y -= v.y; return this; }
    public Point mul(Point v) { x *= v.x; y *= v.y; return this; }
    public Point div(Point v) { x /= v.x; y /= v.y; return this; }

    public static Point sub(Point a, int v) { return new Point(a.x - v, a.y - v); }
    public static Point add(Point a, int v) { return new Point(a.x + v, a.y + v); }
    public static Point div(Point a, int v) { return new Point(a.x / v, a.y / v); }
    public static Point mul(Point a, int v) { return new Point(a.x * v, a.y * v); }

    public static Point sub(Point a, Point b) { return new Point(a.x - b.x, a.y - b.y); }
    public static Point add(Point a, Point b) { return new Point(a.x + b.x, a.y + b.y); }
    public static Point div(Point a, Point b) { return new Point(a.x / b.x, a.y / b.y); }
    public static Point mul(Point a, Point b) { return new Point(a.x * b.x, a.y * b.y); }
}