package io.howarth.pieces;
import io.howarth.Board;
import io.howarth.Player;

import java.util.ArrayList;
 

/**
 * Pieces.java 
 *
 * Class to keep and manage a collection of pieces, which are stored in an ArrayList
 * Also used to place the pieces on the board at the start of a game
 *
 * @version 0.1 12/1/17
 *
 * @author Harry Howarth
 */

public class Pieces {

//	private static final int MAX_PIECES = 16;

	/* White pieces will be placed on the top two rows, 
     black on the the last two rows.
     Can switch display depending on what colour
     the player chooses
	 */

	private ArrayList<Piece> data;
	//private int numData;
	private Board board;
	private int colour;

	public Pieces (Board b, int c) {
		//int j;
		Piece p;
		board = b;
		colour = c;
		data = new ArrayList<Piece>();

		// add 8 pawns
		if (c==Player.WHITE) {
			
			p = new Pawn(5,3,c,board); 
			data.add(p);
			b.setPosition(5,3,p);
			
			p = new Pawn(4,4,c,board); 
			data.add(p);
			b.setPosition(4,4,p);
			
			p = new Pawn(5,4,c,board); 
			data.add(p);
			b.setPosition(5,4,p);
			
			p = new Pawn(6,4,c,board); 
			data.add(p);
			b.setPosition(6,4,p);
			
			p = new Pawn(3,5,c,board); 
			data.add(p);
			b.setPosition(3,5,p);
			
			p = new Pawn(4,5,c,board); 
			data.add(p);
			b.setPosition(4,5,p);
			
			p = new Pawn(6,5,c,board); 
			data.add(p);
			b.setPosition(6,5,p);
			
			p = new Pawn(7,5,c,board); 
			data.add(p);
			b.setPosition(7,5,p);
			
			p = new Pawn(4,6,c,board); 
			data.add(p);
			b.setPosition(4,6,p);
			
			p = new Pawn(5,6,c,board); 
			data.add(p);
			b.setPosition(5,6,p);
			
			p = new Pawn(6,6,c,board); 
			data.add(p);
			b.setPosition(6,6,p);
			
			p = new Pawn(5,7,c,board); 
			data.add(p);
			b.setPosition(5,7,p);
			
		} else {
			p = new Pawn(0,3,c,board); 
			data.add(p);
			b.setPosition(0,3,p);
			
			p = new Pawn(0,4,c,board); 
			data.add(p);
			b.setPosition(0,4,p);
			
			p = new Pawn(0,5,c,board); 
			data.add(p);
			b.setPosition(0,5,p);
			
			p = new Pawn(0,6,c,board); 
			data.add(p);
			b.setPosition(0,6,p);
			
			p = new Pawn(0,7,c,board); 
			data.add(p);
			b.setPosition(0,7,p);
			
			p = new Pawn(1,5,c,board); 
			data.add(p);
			b.setPosition(1,5,p);
			
			p = new Pawn(3,0,c,board); 
			data.add(p);
			b.setPosition(3,0,p);
			
			p = new Pawn(4,0,c,board); 
			data.add(p);
			b.setPosition(4,0,p);
			
			p = new Pawn(5,0,c,board); 
			data.add(p);
			b.setPosition(5,0,p);
			
			p = new Pawn(6,0,c,board); 
			data.add(p);
			b.setPosition(6,0,p);
			
			p = new Pawn(7,0,c,board); 
			data.add(p);
			b.setPosition(7,0,p);
			
			p = new Pawn(5,1,c,board); 
			data.add(p);
			b.setPosition(5,1,p);
			
			p = new Pawn(10,3,c,board); 
			data.add(p);
			b.setPosition(10,3,p);
			
			p = new Pawn(10,4,c,board); 
			data.add(p);
			b.setPosition(10,4,p);
			
			p = new Pawn(10,5,c,board); 
			data.add(p);
			b.setPosition(10,5,p);
			
			p = new Pawn(10,6,c,board); 
			data.add(p);
			b.setPosition(10,6,p);
			
			p = new Pawn(10,7,c,board); 
			data.add(p);
			b.setPosition(10,7,p);
			
			p = new Pawn(9,5,c,board); 
			data.add(p);
			b.setPosition(9,5,p);
			
			p = new Pawn(3,10,c,board); 
			data.add(p);
			b.setPosition(3,10,p);
			
			p = new Pawn(4,10,c,board); 
			data.add(p);
			b.setPosition(4,10,p);
			
			p = new Pawn(5,10,c,board); 
			data.add(p);
			b.setPosition(5,10,p);
			
			p = new Pawn(6,10,c,board); 
			data.add(p);
			b.setPosition(6,10,p);
			
			p = new Pawn(7,10,c,board); 
			data.add(p);
			b.setPosition(7,10,p);
			
			p = new Pawn(5,9,c,board); 
			data.add(p);
			b.setPosition(5,9,p);
		}
		

		// and finally 1 King
		if (c==Player.WHITE) { 
			p = new King(5,5,c,board);
			data.add(p);
			b.setPosition(5,5, p);
		}

		
	}

	public int getNumPieces() {
		return data.size();
	}

	public Piece getPiece(int i) {
		return (Piece)data.get(i);
	}

	public void delete(Piece p) {
		boolean removed = data.remove(p);
		if (!removed) System.out.println("error");
	}
	

	public String toString () {
		String s = "";
		for (int i=0; i<data.size(); i++) {
			s = s+(Piece)data.get(i);
		}
		return s;
	}
	//Added this method to return the integer value of the colour of the piece.
	public int getColour(){return colour;}
	//Added this method to return the arraylist of Piece objects.
	public ArrayList<Piece> getData(){return data;}

}
