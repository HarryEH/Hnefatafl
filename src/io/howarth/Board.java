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
		for (int i=0; i<BOARD_SIZE; i++)
			for (int j=0; j<BOARD_SIZE; j++) {
				data[i][j] = null;
			}
	}

	// method returns true if a particular location is occupied
	public boolean occupied(int i, int j) {
		return (data[i][j]!=null);
	}

	// method returns true if a particular location is off the board
	public boolean outOfRange(int i, int j) {
		return (i<0) || (i>=BOARD_SIZE)
				|| (j<0) || (j>=BOARD_SIZE);
	}

	// method to remove a piece from a particular location
	public void remove(int i, int j) {
		data[i][j] = null;
	}

	// method to place a piece at a particular location
	public void setPosition(int i, int j, Piece p) {
		data[i][j] = p;
	}

	// method to return the chess piece at a particular location
	public Piece getPiece(int x, int y) {
		return data[x][y];
	}
	
	public void setPieces(Piece[][] data){
		this.data = data;
	}

	// method to return the array of chess pieces on the board
	public Piece[][] getData() {
		return data;
	}
	
	public Board copy() {
		Board b1 = new Board();
		Piece[][] pcs = new Piece[11][11];
		for(int i=0; i<11; i++){
			for(int j=0; j<11; j++){
				if(getData()[i][j]!=null){
					pcs[i][j] = getData()[i][j].copy();
				} else {
					pcs[i][j] = null;
				}
				
			}
		}
		b1.setPieces(pcs);
		return b1;
	}

}
