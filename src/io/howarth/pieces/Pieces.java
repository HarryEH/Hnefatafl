package io.howarth.pieces;
import io.howarth.Board;
import io.howarth.pieces.impl.KingImpl;
import io.howarth.pieces.impl.PawnImpl;
import io.howarth.players.Player;

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

	public Pieces (Board b, byte c) {
		//int j;
		Piece p;
		board = b;
		colour = c;
		data = new ArrayList<Piece>();
		
		byte zero  = 0; byte one  = 1; byte three = 3;
		byte four = 4; byte five = 5; byte six = 6; byte seven = 7; 
		byte nine = 9; byte ten = 10;
		
		if (c==Player.WHITE) {
			
			p = new PawnImpl(five,three,c,board); 
			data.add(p);
			b.setPosition(five,three,p);
			
			p = new PawnImpl(four,four,c,board); 
			data.add(p);
			b.setPosition(four,four,p);
			
			p = new PawnImpl(five,four,c,board); 
			data.add(p);
			b.setPosition(five,four,p);
			
			p = new PawnImpl(six,four,c,board); 
			data.add(p);
			b.setPosition(six,four,p);
			
			p = new PawnImpl(three,five,c,board); 
			data.add(p);
			b.setPosition(three,five,p);
			
			p = new PawnImpl(four,five,c,board); 
			data.add(p);
			b.setPosition(four,five,p);
			
			p = new PawnImpl(six,five,c,board); 
			data.add(p);
			b.setPosition(six,five,p);
			
			p = new PawnImpl(seven,five,c,board); 
			data.add(p);
			b.setPosition(seven,five,p);
			
			p = new PawnImpl(four,six,c,board); 
			data.add(p);
			b.setPosition(four,six,p);
			
			p = new PawnImpl(five,six,c,board); 
			data.add(p);
			b.setPosition(five,six,p);
			
			p = new PawnImpl(six,six,c,board); 
			data.add(p);
			b.setPosition(six,six,p);
			
			p = new PawnImpl(five,seven,c,board); 
			data.add(p);
			b.setPosition(five,seven,p);
			
		} else {
			p = new PawnImpl(zero,three,c,board); 
			data.add(p);
			b.setPosition(zero,three,p);
			
			p = new PawnImpl(zero,four,c,board); 
			data.add(p);
			b.setPosition(zero,four,p);
			
			p = new PawnImpl(zero,five,c,board); 
			data.add(p);
			b.setPosition(zero,five,p);
			
			p = new PawnImpl(zero,six,c,board); 
			data.add(p);
			b.setPosition(zero,six,p);
			
			p = new PawnImpl(zero,seven,c,board); 
			data.add(p);
			b.setPosition(zero,seven,p);
			
			p = new PawnImpl(one,five,c,board); 
			data.add(p);
			b.setPosition(one,five,p);
			
			p = new PawnImpl(three,zero,c,board); 
			data.add(p);
			b.setPosition(three,zero,p);
			
			p = new PawnImpl(four,zero,c,board); 
			data.add(p);
			b.setPosition(four,zero,p);
			
			p = new PawnImpl(five,zero,c,board); 
			data.add(p);
			b.setPosition(five,zero,p);
			
			p = new PawnImpl(six,zero,c,board); 
			data.add(p);
			b.setPosition(six,zero,p);
			
			p = new PawnImpl(seven,zero,c,board); 
			data.add(p);
			b.setPosition(seven,zero,p);
			
			p = new PawnImpl(five,one,c,board); 
			data.add(p);
			b.setPosition(five,one,p);
			
			p = new PawnImpl(ten,three,c,board); 
			data.add(p);
			b.setPosition(ten,three,p);
			
			p = new PawnImpl(ten,four,c,board); 
			data.add(p);
			b.setPosition(ten,four,p);
			
			p = new PawnImpl(ten,five,c,board); 
			data.add(p);
			b.setPosition(ten,five,p);
			
			p = new PawnImpl(ten,six,c,board); 
			data.add(p);
			b.setPosition(ten,six,p);
			
			p = new PawnImpl(ten,seven,c,board); 
			data.add(p);
			b.setPosition(ten,seven,p);
			
			p = new PawnImpl(nine,five,c,board); 
			data.add(p);
			b.setPosition(nine,five,p);
			
			p = new PawnImpl(three,ten,c,board); 
			data.add(p);
			b.setPosition(three,ten,p);
			
			p = new PawnImpl(four,ten,c,board); 
			data.add(p);
			b.setPosition(four,ten,p);
			
			p = new PawnImpl(five,ten,c,board); 
			data.add(p);
			b.setPosition(five,ten,p);
			
			p = new PawnImpl(six,ten,c,board); 
			data.add(p);
			b.setPosition(six,ten,p);
			
			p = new PawnImpl(seven,ten,c,board); 
			data.add(p);
			b.setPosition(seven,ten,p);
			
			p = new PawnImpl(five,nine,c,board); 
			data.add(p);
			b.setPosition(five,nine,p);
		}
		

		// and finally 1 King
		if (c==Player.WHITE) { 
			p = new KingImpl(five,five,c,board);
			data.add(p);
			b.setPosition(five,five, p);
		}

		
	}

	public int getNumPieces() {
		return data.size();
	}

	public Piece getPiece(byte i) {
		return (Piece)data.get(i);
	}

	public void delete(Piece p) {
		boolean removed = data.remove(p);
		if (!removed) System.out.println("error");
	}
	

	public String toString () {
		String s = "";
		for (byte i=0; i<data.size(); i++) {
			s = s+(Piece)data.get(i);
		}
		return s;
	}
	//Added this method to return the integer value of the colour of the piece.
	public int getColour(){return colour;}
	//Added this method to return the arraylist of Piece objects.
	public ArrayList<Piece> getData(){return data;}

}
