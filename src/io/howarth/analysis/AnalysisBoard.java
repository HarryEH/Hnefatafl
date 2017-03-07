package io.howarth.analysis;

import io.howarth.Board;
import io.howarth.pieces.Piece;
import io.howarth.pieces.PieceCode;

public class AnalysisBoard {
	
	private final static byte BOARD_SIZE=11;
	private char[][] data;

	public AnalysisBoard () {
		data = new char[BOARD_SIZE][BOARD_SIZE];
		for (byte i=0; i<BOARD_SIZE; i++)
			for (byte j=0; j<BOARD_SIZE; j++) {
				data[i][j] = 'x';
			}
	}
	
	public AnalysisBoard (char[][] board) {
		data = board;
	}

	// method returns true if a particular location is occupied
	public boolean occupied(byte i, byte j) {
		return (data[i][j]!='x');
	}

	// method returns true if a particular location is off the board
	public boolean outOfRange(byte i, byte j) {
		return (i<0) || (i>=BOARD_SIZE)
				|| (j<0) || (j>=BOARD_SIZE);
	}

	// method to remove a piece from a particular location
	public void remove(byte i, byte j) {
		data[i][j] = 'x';
	}

	// method to place a piece at a particular location
	public void setPosition(byte i, byte j, char p) {
		data[i][j] = p;
	}

	// method to return the chess piece at a particular location
	public char getPiece(byte x, byte y) {
		return data[x][y];
	}
	
	public void setPieces(char[][] data){
		this.data = data;
	}

	// method to return the array of chess pieces on the board
	public char[][] getData() {
		return data;
	}
	
	public static AnalysisBoard convB(Board b){
		AnalysisBoard bd = new AnalysisBoard();
		
		char[][] pcs = new char[BOARD_SIZE][BOARD_SIZE];
		Piece[][] data = b.getData();
		for (int i=0; i<BOARD_SIZE; i++){
			for (int j=0; j<BOARD_SIZE; j++) {
				if(data[i][j]!=null){
					pcs[i][j] = data[i][j].getChar();
				} else {
					pcs[i][j] = 'x';
				}
			}
		}
		
		bd.setPieces(pcs);
		
		return bd;
	}
	
	public static Board convAB(AnalysisBoard b){
		Board bd = new Board();
		
		char[][] pcs = b.getData();
		Piece[][] data = new Piece[BOARD_SIZE][BOARD_SIZE];
		for (byte i=0; i<BOARD_SIZE; i++){
			for (byte j=0; j<BOARD_SIZE; j++) {
				if(pcs[i][j]!='x'){
					data[i][j] = PieceCode.charToPiece(pcs[i][j],i ,j , bd);
				} else {
					data[i][j] = null;
				}
			}
		}
		
		bd.setPieces(data);
		
		return bd;
	}
	
}
