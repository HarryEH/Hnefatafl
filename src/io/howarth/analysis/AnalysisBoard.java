package io.howarth.analysis;

public class AnalysisBoard {
	
	private final int BOARD_SIZE=11;
	private char[][] data;

	public AnalysisBoard () {
		data = new char[BOARD_SIZE][BOARD_SIZE];
		for (int i=0; i<BOARD_SIZE; i++)
			for (int j=0; j<BOARD_SIZE; j++) {
				data[i][j] = 'x';
			}
	}

	// method returns true if a particular location is occupied
	public boolean occupied(int i, int j) {
		return (data[i][j]!='x');
	}

	// method returns true if a particular location is off the board
	public boolean outOfRange(int i, int j) {
		return (i<0) || (i>=BOARD_SIZE)
				|| (j<0) || (j>=BOARD_SIZE);
	}

	// method to remove a piece from a particular location
	public void remove(int i, int j) {
		data[i][j] = 'x';
	}

	// method to place a piece at a particular location
	public void setPosition(int i, int j, char p) {
		data[i][j] = p;
	}

	// method to return the chess piece at a particular location
	public char getPiece(int x, int y) {
		return data[x][y];
	}
	
	public void setPieces(char[][] data){
		this.data = data;
	}

	// method to return the array of chess pieces on the board
	public char[][] getData() {
		return data;
	}
}
