package gameoflife.logic.readers;

import java.io.IOException;
import java.io.Reader;

import gameoflife.logic.Board2D;

/**
 * Supports the RLE Game of Life file format.
 */
public class GameOfLifeReaderRLE extends GameOfLifeReader {
    private final int INVALID = -1;
    private final char DEAD_CELL = 'b';
    private final char LIVING_CELL = 'o';
    private final char END_OF_ROW = '$';
    private final char END_OF_DATA = '!';
	
	@Override
	public void readGameBoard(Reader reader, Board2D board) {
        int character;
        
        try {
			while ((character = reader.read()) != INVALID)
			{
			    if (character == '#')
			    	readMetadata(reader);
			    else if (character == 'x') {
			        boolean ok = createBoard(reader, board);
			        if(!ok) {
			        	System.err.println("Could not instansiate board from data.");
			        	return;
			        }
			        break;
			    }
			}
			readBoard(reader, board);
		} catch (Exception e) {
			System.err.println("Could not read pattern from stream.");
			e.printStackTrace();
		}
	}
	
    private void readMetadata(Reader reader) throws IOException
    {
    	// loop through meta data.
    	int character;
        while ((character = reader.read()) != INVALID && character != '\n');
    }
	
    private boolean createBoard(Reader reader, Board2D board) throws IOException {
        int character;
        int number = 0;
        int height = INVALID;
        int width = INVALID;
        while ((character = reader.read()) != INVALID && character != '\n')
        {
            if (height == INVALID)
            {
                if (Character.isDigit((char) character))
                {
                    number = number * 10 + (character - '0');
                }
                else if (number != 0)
                {
                    if (width == INVALID)
                        width = number;
                    else
                        height = number;

                    number = 0;
                }
            }
        }

        if(width == INVALID || height == INVALID)
        	return false;

        board.createEmptyBoard(width, height);
        return true;
    }

    private void readBoard(Reader reader, Board2D board) throws Exception {
        int row = 0;
        int index = 0;
        int number = 0;
        int character;

        while ((character = reader.read()) != INVALID && character != END_OF_DATA)
        {
            if (Character.isDigit((char) character))
            {
                number = number * 10 + (character - '0');
            }
            else if (character == LIVING_CELL)
            {
                number = Math.max(1, number);
                if(index + number > board.getWidth())
                    throw new Exception();
                for (int x = index; x < (index + number); x++)
                	board.setCellState(x, row, true);
                index += number;
                number = 0;
            }
            else if(character == DEAD_CELL)
            {
                index += Math.max(1, number);
                number = 0;
            }
            else if (character == END_OF_ROW)
            {
                row += Math.max(1, number);
                index = number = 0;
            }
        }
    }


}
