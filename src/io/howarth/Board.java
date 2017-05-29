package io.howarth;

import io.howarth.pieces.Piece;



/**
 * Board.java 
 *
 * Class to represent a board
 *
 * @version 0.1 12/1/17
 *
 * @author Harry Howarth
 */


public class Board {

	private static final int BOARD_SIZE=11;
	//private static final int EMPTY=0;

	// each board has an array of Piece objects, which stores the chess pieces at each location
	// on the board
	private Piece[][] data;

	public Board () {
		data = new Piece[BOARD_SIZE][BOARD_SIZE];
		for (byte i=0; i<BOARD_SIZE; i++)
			for (byte j=0; j<BOARD_SIZE; j++) {
				data[i][j] = null;
			}
	}

	// method returns true if a particular location is occupied
	public boolean occupiedOrBounds(byte i, byte j) {
		return outOfRange(i,j) || (data[i][j]!=null);
	} // occupied or out of bounds, for while loops
	
	public boolean occupied(byte i, byte j) {
		return (data[i][j]!=null);
	}

	// method returns true if a particular location is off the board
	public boolean outOfRange(byte i, byte j) {
		return (i<0) || (i>=BOARD_SIZE)
				|| (j<0) || (j>=BOARD_SIZE);
	}

	// method to remove a piece from a particular location
	public void remove(byte i, byte j) {
		data[i][j] = null;
	}

	// method to place a piece at a particular location
	public void setPosition(byte i, byte j, Piece p) {
		data[i][j] = p;
	}

	// method to return the chess piece at a particular location
	public Piece getPiece(byte x, byte y) {
		return data[x][y];
	}
	
	public void setPieces(Piece[][] data){
		this.data = data;
	}

	// method to return the array of chess pieces on the board
	public Piece[][] getData() {
		return data;
	}

}
