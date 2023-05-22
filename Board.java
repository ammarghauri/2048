package game;
import java.util.ArrayList;

/**
 * main class for game commands
 * @author Kal Pandit
 * @author Ammar Ghauri
 **/

public class Board {
    private int[][] gameBoard;               // the game board array
    private ArrayList<BoardSpot> openSpaces; // the ArrayList of open spots: board cells without numbers.

    /**
    initializes a 4x4 game board.
     **/
    public Board() {
        gameBoard = new int[4][4];
        openSpaces = new ArrayList<>();
    }

    /**
     * One-argument Constructor: initializes a game board based on a given array.
     * @param board the board array with values to be passed through
     **/
    public Board ( int[][] board ) {
        gameBoard = new int[board.length][board[0].length];
        for ( int r = 0; r < gameBoard.length; r++ ) {
            for ( int c = 0; c < gameBoard[r].length; c++ ) {
                gameBoard[r][c] = board[r][c];
            }
        }
        openSpaces = new ArrayList<>();
    }

    /**
     * 1. Initializes the instance variable openSpaces (open board spaces) with an empty array.
     * 2. Adds open spots to openSpaces (the ArrayList of open BoardSpots).
     **/
    public void updateOpenSpaces() {
        openSpaces = new ArrayList<BoardSpot>();
        for (int t = 0; t < gameBoard.length; t++) {
            for (int j = 0; j < gameBoard.length; j++) {
                if (gameBoard[t][j] == 0) {
                    BoardSpot space = new BoardSpot(t,j);
                    openSpaces.add(space);
                }
            }
        }
    }

    /**
     * Adds a random tile to an open spot with a 90% chance of a 2 value and a 10% chance of a 4 value.
     * 1. Select a tile t by picking a random open space from openSpaces
     * 2. Pick a value v by picking a double from 0 to 1 (not inclusive of 1); < .1 means the tile is a 4, otherwise 2
     * 3. Update the tile t on gameBoard with the value v
    
     **/
    public void addRandomTile() {
        int ran = StdRandom.uniform(0, openSpaces.size());
        double i = StdRandom.uniform(0.0,1.0);
        BoardSpot s = openSpaces.get(ran);
        if (i < 0.1){
            gameBoard[s.getRow()][s.getCol()] = 4;
        } else {
            gameBoard[s.getRow()][s.getCol()] = 2;
        }

    }
    /**
     * Swipes the entire board left, shifting all nonzero tiles as far left as possible.
     * Maintains the same number and order of tiles. 
     * After swiping left, no zero tiles have to be in between nonzero tiles. 
     **/
    public void swipeLeft() {
        for (int t = 0; t < 4; t++) {
            int j = 0;
            for (int i = 0; i < 4; i++) {
                if (gameBoard[t][i] != 0) {
                    int temp = gameBoard[t][i];
                    gameBoard[t][j++] = temp;
                }
            }
            for (int i = j; i < 4; i++) {
                gameBoard[t][i] = 0;
            }
        }
    }

    /**
     * Find and merge all identical left pairs in the board.
     * The leftmost value takes on double its own value, and the rightmost empties and becomes 0.
     **/
    public void mergeLeft() {
            for (int i = 0; i < gameBoard.length; i++) {
                for (int t = 0; t < gameBoard[i].length - 1; t++) {
                    if (gameBoard[i][t] == gameBoard[i][t+1]){
                        gameBoard[i][t] += gameBoard[i][t+1];
                        gameBoard[i][t+1] = 0;
                    }
            }
        }
    }
    


    /**
     * Rotates 90 degrees clockwise by taking the transpose of the board and then reversing rows. 
     **/
    public void rotateBoard() {
        transpose();
        flipRows();
    }

    /**
     * Updates the instance variable gameBoard to be its transpose. 
     * Transposing flips the board along its main diagonal (top left to bottom right).
     **/
    public void transpose() {
        for (int r = 0; r < gameBoard.length; r++) {
            for (int c = r + 1; c < gameBoard[r].length; c++) {
                int temp = gameBoard[r][c];
                gameBoard[r][c] = gameBoard[c][r];
                gameBoard[c][r] = temp;
               
            }
        }
      
    }

    /**
     * Updates the instance variable gameBoard to reverse its rows.
     * 
     * Reverses all rows. Columns 1, 2, 3, and 4 become 4, 3, 2, and 1.
     * 
     **/
    public void flipRows() {
        for (int i = 0; i < gameBoard.length; i++){
            for (int t = 0; t < gameBoard[i].length/2; t++){
                
                int temp = gameBoard[i][t];
                gameBoard[i][t] = gameBoard[i][gameBoard[i].length - t - 1];
                gameBoard[i][gameBoard[i].length - t - 1] = temp;
            }
        }
    }

    /**
     * Calls previous methods to make right, left, up and down moves.
     **/
    public void makeMove(char letter) {
        
        if (letter == 'L'){
            updateOpenSpaces();
            swipeLeft();
            mergeLeft();
            swipeLeft();
        }
        if(letter == 'R'){
            updateOpenSpaces();
            rotateBoard();
            rotateBoard();
            swipeLeft();
            mergeLeft();
            swipeLeft();
            rotateBoard();
            rotateBoard();
        }
        if(letter == 'U'){
            updateOpenSpaces();
            rotateBoard();
            rotateBoard();
            rotateBoard();
            swipeLeft();
            mergeLeft();
            swipeLeft();
            rotateBoard();

        }
        if(letter == 'D'){
            updateOpenSpaces();
            rotateBoard();
            swipeLeft();
            mergeLeft();
            swipeLeft();
            rotateBoard();
            rotateBoard();
            rotateBoard();
        } 
    }
    /**
     * Returns true when the game is lost and no empty spaces are available. Ignored
     * when testing methods in isolation.
     * 
     * @return the status of the game -- lost or not lost
     **/
    public boolean isGameLost() {
        return openSpaces.size() == 0;
    }

    /**
     * Shows a final score when the game is lost. 
     **/
    public int showScore() {
        int score = 0;
        for ( int r = 0; r < gameBoard.length; r++ ) {
            for ( int c = 0; c < gameBoard[r].length; c++ ) {
                score += gameBoard[r][c];
            }
        }
        return score;
    }

    /**
     * Prints the board as integer values in the text window.
     **/
    public void print() {
        for ( int r = 0; r < gameBoard.length; r++ ) {
            for ( int c = 0; c < gameBoard[r].length; c++ ) {
                String g = Integer.toString(gameBoard[r][c]);
                StdOut.print((g.equals("0")) ? "-" : g);
                for ( int o = 0; o < (5 - g.length()); o++ ) {
                    StdOut.print(" ");
                }
            }
            StdOut.println();
        }
    }
    /**
     * Prints the board as integer values in the text window, with open spaces denoted by "**"". Used by TextDriver.
     **/
    public void printOpenSpaces() {
        for ( int r = 0; r < gameBoard.length; r++ ) {
            for ( int c = 0; c < gameBoard[r].length; c++ ) {
                String g = Integer.toString(gameBoard[r][c]);
                for ( BoardSpot bs : getOpenSpaces() ) {
                    if (r == bs.getRow() && c == bs.getCol()) {
                        g = "**";
                    }
                }
                StdOut.print((g.equals("0")) ? "-" : g);
                for ( int o = 0; o < (5 - g.length()); o++ ) {
                    StdOut.print(" ");
                }
            }
            StdOut.println();
        }
    }

    /**
     * Seed Constructor: Allows students to set seeds to debug random tile cases.
     * 
     * @param seed the long seed value
     **/
    public Board(long seed) {
        StdRandom.setSeed(seed);
        gameBoard = new int[4][4];
    }

    /**
     * Gets the open board spaces.
     * 
     * @return the ArrayList of BoardSpots containing open spaces
     **/
    public ArrayList<BoardSpot> getOpenSpaces() {
        return openSpaces;
    }

    /**
     * Gets the board 2D array values.
     * 
     * @return the 2D array game board
     **/
    public int[][] getBoard() {
        return gameBoard;
    }
}
