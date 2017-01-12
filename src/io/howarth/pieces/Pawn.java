package io.howarth.pieces;
import io.howarth.Board;
import io.howarth.Move;
import io.howarth.Piece;

import java.util.ArrayList;


/**
 * Pawn.java 
 *
 * Concrete class to represent a pawn
 *
 * @version 0.1 12/1/17
 *
 * @author Harry Howarth
 */


public class Pawn extends Piece {

	public Pawn (int ix, int iy, int c, Board b) {
		super(PieceCode.PAWN, ix, iy, c, b);
	}

	/**
	 * Method that gets all the available moves of either the black or white piece.Implements 
	 * abstract method from Piece class.
	 * @return ArrayList of Move objects.
	 */
	public ArrayList<Move> availableMoves() {
		if (getColour()==PieceCode.WHITE) return whitePawn();
		else return blackPawn();
	}

	// method to return Vector of legal moves for a white pawn
	private ArrayList<Move> whitePawn() {
		int x = this.getX();
		int y = this.getY();

		// return null if the pawn is at the edge of the board, or if the
		// next move takes it out of range
		if (y==7) return null;
		if (getBoard().outOfRange(x,y+1)) return null;

		// otherwise create a new vector to store legal moves
		ArrayList<Move> v = new ArrayList<Move>();

		// set up m to refer to a Move object  
		Move m = null;

		// first legal move is to go from x,y to x,y+1 if x,y+1 is unoccupied  
		if (!getBoard().occupied(x,y+1)) {
			m = new Move(this, x,y,x,y+1,false);
			v.add(m);
		}

		// second legal move is to go from x,y to x+1,y+1 if x+1,y+1 is occupied 
		// by a black piece
		if (!getBoard().outOfRange(x+1, y+1)
				&& getBoard().occupied(x+1, y+1)
				&& (getBoard().getPiece(x+1, y+1).getColour()
						!=this.getColour())) {
			m = new Move(this, x,this.getY(),x+1,y+1,true);
			v.add(m);
		}

		// third legal move is to go from x,y to x-1,y+1 if x-1,y+1 is occupied 
		// by a black piece  
		if (!getBoard().outOfRange(x-1, y+1)
				&& getBoard().occupied(x-1, y+1)
				&& (getBoard().getPiece(x-1, y+1).getColour()
						!=this.getColour())) {
			m = new Move(this, x,y,x-1,y+1,true);
			v.add(m);
		}
		//fourth legal move is to go from x,y to x,y+2 if the piece hasn't moved yet. and both the spaces are unoccupied
		if( (this.getY() ==1) //this condition is only is true if the piece has not yet moved.
				&&!getBoard().occupied(x,y+1)
				&&!getBoard().occupied(x,y+2)
				) {
			m = new Move(this, x,y,x,y+2,false);
			v.add(m);
		} 



		if (v.isEmpty()) return null;
		return v;
	}

	// method to return Vector of legal moves for a white pawn
	private ArrayList<Move> blackPawn() {
		int x = getX();
		int y = getY();

		// return null if the pawn is at the edge of the board, or if the
		// next move takes it out of range
		if (y==0) return null;
		if (getBoard().outOfRange(x,y-1)) return null;

		ArrayList<Move> v = new ArrayList<Move>();

		// set up m to refer to a Move object        
		Move m = null;

		// first legal move is to go from x,y to x,y+1 if x,y+1 is unoccupied  
		if (!getBoard().occupied(x,y-1)) {
			m = new Move(this, x,y,x,y-1,false);
			v.add(m);
		}

		// second legal move is to go from x,y to x+1,y+1 if x+1,y+1 is occupied 
		// by a white piece      
		if (!getBoard().outOfRange(x+1, y-1)
				&& getBoard().occupied(x+1, y-1)
				&& (getBoard().getPiece(x+1, y-1).getColour()
						!=this.getColour())) {
			m = new Move(this, x,y,x+1,y-1,true);
			v.add(m);
		}

		// third legal move is to go from x,y to x-1,y+1 if x-1,y+1 is occupied 
		// by a white piece       
		if (!getBoard().outOfRange(x-1, y-1)
				&& getBoard().occupied(x-1, y-1)
				&& (getBoard().getPiece(x-1, y-1).getColour()
						!=this.getColour())) {
			m = new Move(this, x,y,x-1,y-1,true);
			v.add(m);
		}

		//fourth legal move. go 2 spaces off the start.
		if((getY() ==6)//this condition is only is true if the piece has not yet moved.
				&& !getBoard().occupied(x,y-1)
				&&!getBoard().occupied(x,y-2)
				) {
			m = new Move(this, x,y,x,y-2,false);
			v.add(m);
		} 
		if (v.isEmpty()) return null;
		return v;
	}

}
