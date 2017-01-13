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

	// 0,0 - 0,10 - 10,0 - 10,0
	
	/**
	 * Method that gets all the available moves of either the black or white piece.Implements 
	 * abstract method from Piece class.
	 * @return ArrayList of Move objects.
	 */
	public ArrayList<Move> availableMoves() {
		if (getColour()==PieceCode.WHITE) return whitePawn();
		else return blackPawn();
	}

	private ArrayList<Move> whitePawn() {
		int x = getX();
		int y = getY();
		// otherwise create a new vector to store legal moves
		ArrayList<Move> v = new ArrayList<Move>();
		// set up m to refer to a Move object  
		Move m = null;
		
		//Moves up up to being out of range or until it hits an opponent. 1st vertical set
		int i=getY()+1; 
		while(!getBoard().outOfRange(x, i)&&!getBoard().occupied(x, i)){
			if (i != 10){
				m = new Move(this, x,y,x,i,false);
				v.add(m);
			}
			i++;
		}
		 
		//Moves down up to being out of range or until it hits an opponent. 2st vertical set
		int j=getY()-1; 
		while(!getBoard().outOfRange(x, j)&&!getBoard().occupied(x, j)){
			if (i != 0){
				m = new Move(this, x,y,x,j,false);
				v.add(m);
			} 
			j--;
		}
			
		//Moves right up to being out of range or until it hits an opponent. 1st horizontal set
		int k=getX()+1; 
		while(!getBoard().outOfRange(k, y)&&!getBoard().occupied(k, y)){
			if (k != 10){
				m = new Move(this, x,y,k,y,false);
				v.add(m); 
			}
			k++;
		}
		 
		//Moves left up to being out of range or until it hits an opponent. 2nd horizontal set
		int l=getX()-1; 
		while(!getBoard().outOfRange(l, y)&&!getBoard().occupied(l, y)){
			if (l != 0){
				m = new Move(this, x,y,l,y,false);
				v.add(m); 
			}
			l--;
		}
		
		if (v.isEmpty()) return null;
		return v;
	}
	//Black Rook code, pretty much identical to above as can move both backwards/forwards/left/right
	private ArrayList<Move> blackPawn() {
		int x = getX();
		int y = getY();
		// otherwise create a new vector to store legal moves
		ArrayList<Move> v = new ArrayList<Move>();
		// set up m to refer to a Move object  
		Move m = null;
		
		//Moves up up to being out of range or until it hits an opponent. 1st vertical set
		int i=getY()+1; 
		while(!getBoard().outOfRange(x, i)&&!getBoard().occupied(x, i)){
			if (i != 10){
				m = new Move(this, x,y,x,i,false);
				v.add(m);
			}
			i++;
		}
		 
		// Moves down up to being out of range or until it hits an opponent. 2st vertical set
		int j=getY()-1; 
		while(!getBoard().outOfRange(x, j)&&!getBoard().occupied(x, j)){
			if (i != 0){
				m = new Move(this, x,y,x,j,false);
				v.add(m);
			} 
			j--;
		}
			
		// Moves right up to being out of range or until it hits an opponent. 1st horizontal set
		int k=getX()+1; 
		while(!getBoard().outOfRange(k, y)&&!getBoard().occupied(k, y)){
			if (k != 10){
				m = new Move(this, x,y,k,y,false);
				v.add(m); 
			}
			k++;
		}
		 
		// Moves left up to being out of range or until it hits an opponent. 2nd horizontal set
		int l=getX()-1; 
		while(!getBoard().outOfRange(l, y)&&!getBoard().occupied(l, y)){
			if (l != 0){
				m = new Move(this, x,y,l,y,false);
				v.add(m); 
			}
			l--;
		}

		if (v.isEmpty()) return null;
		return v;
	}
	
	protected boolean analyseBoard(int x, int y, int i, int j){
		Board b = getBoard();
		b.remove(x, y);
		b.setPosition(i, j, b.getPiece(x, y));
		
		//FIXME all pieces in neighborhood & the pieces on the other side
		
		//i-1,j - i+1,j - i,j-1 - i,j+1
		
		//i-2,j - i+2,j - i,j-2 - i,j+2
		
		return true;
	}

}
