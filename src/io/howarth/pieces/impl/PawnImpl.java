package io.howarth.pieces.impl;
import java.util.ArrayList;

import io.howarth.Board;
import io.howarth.move.Move;
import io.howarth.pieces.Piece;
import io.howarth.pieces.PieceCode;


/**
 * Pawn.java 
 *
 * Concrete class to represent a pawn
 *
 * @version 0.1 12/1/17
 *
 * @author Harry Howarth
 */


public class PawnImpl extends Piece {

	// FIXME - can a pawn take a piece against the king square when the king square is occupied?
	
	public PawnImpl (byte ix, byte iy, byte c, Board b) {
		super(PieceCode.PAWN, ix, iy, c, b);
	}

	// 0,0 - 0,10 - 10,0 - 10,0
	
	/**
	 * Method that gets all the available moves of either the black or white piece.Implements 
	 * abstract method from Piece class.
	 * @return ArrayList of Move objects.
	 */
	public ArrayList<Move> availableMoves() {
		return movesPawn();
	}

	private ArrayList<Move> movesPawn() {
		byte x = getX();
		byte y = getY();
		
		Move m;
		
		ArrayList<Move> moveLis = new ArrayList<Move>();
		
		// Make 0 moves faster
		if( getBoard().occupiedOrBounds(x, (byte)(y+1)) && getBoard().occupiedOrBounds(x, (byte)(y-1)) 
				&& getBoard().occupiedOrBounds((byte)(x+1), y) && getBoard().occupiedOrBounds((byte)(x-1), y)){
			return moveLis;
		}
		
		boolean testUp    = true; boolean testDown  = true;
		boolean testRight = true; boolean testLeft  = true;
		
		for(byte loopCounter = 1; loopCounter < 11; loopCounter++) {
			
			if(testUp){ // Generate moves moving 'up' the board
				
				byte up = (byte)(y+loopCounter);
				
				if ( getBoard().occupiedOrBounds(x, up) ||
						((x==0 && up==0) || (x==0 && up==10) || (x==10 && up==0) || (x==10 && up==10) ) ) {
					testUp = false; // Won't come back into this logic block again
				} else if ((x==5 && up==5)) {
					// do nothing
				} else {
					m = new Move(this, x, y, x, up);
					moveLis.add(m); 		
				}
			}
			
			if(testDown){ // Generate moves moving 'down' the board
				
				byte down = (byte)(y-loopCounter);
				
				if ( getBoard().occupiedOrBounds(x, down) || ((x==0 && down==0) || (x==0 && down==10) || (x==10 && down==0) || (x==10 && down==10) ) ) {
					testDown = false; // Won't come back into this logic block again
				} else if ((x==5 && down==5)) {
					// do nothing
				}  else {
					m = new Move(this, x, y, x, down);
					moveLis.add(m); 
				}
			} 
			
			if(testLeft){ // Generate moves moving 'left' across the board
				
				byte left = (byte)(x-loopCounter);
				
				if ( getBoard().occupiedOrBounds(left, y) ||
						((left==0 && y==0) || (left==0 && y==10) || (left==10 && y==0) || (left==10 && y==10) )) {
					testLeft = false; // Won't come back into this logic block again
				} else if ((left==5 && y==5)) {
					// do nothing
				}  else {
					
					m = new Move(this, x, y, left, y);
					moveLis.add(m); 
				}
			}
			
			if(testRight){ // Generate moves moving 'left' across the board
				
				byte right = (byte)(x+loopCounter);
				
				if ( getBoard().occupiedOrBounds(right, y) ||
						((right==0 && y==0) || (right==0 && y==10) || (right==10 && y==0) || (right==10 && y==10)) ) {
					testRight = false; // Won't come back into this logic block again
				} else if ((right==5 && y==5)) {
					// do nothing
				}  else {
					
					m = new Move(this, x, y, right, y);
					moveLis.add(m); 
				}
			}
			
			if(! (testRight || testLeft || testUp || testDown) ){
				return moveLis;
			}	
		}
		return moveLis;
	}

	

}
