package io.howarth.pieces.impl;
import io.howarth.Board;
import io.howarth.move.Move;
import io.howarth.move.PieceCoordinates;
import io.howarth.move.TakePiece;
import io.howarth.pieces.Piece;
import io.howarth.pieces.PieceCode;

import java.util.ArrayList;


/**
 * King.java 
 *
 * Concrete class to represent a King piece.
 *
 * @version 2.0 12/1/17
 *
 * @author Harry Howarth 
 */

public class KingImpl extends Piece{
	
	public KingImpl (byte ix, byte iy, byte c, Board b) {
		super(PieceCode.KING, ix, iy, c, b);
	}
	
	/**
	 * Method that gets all the available moves of either the black or white piece.Implements 
	 * abstract method from Piece class.
	 * @return ArrayList of Move objects.
	 */
	public ArrayList<Move> availableMoves() {
		if (getColour()==PieceCode.WHITE) {
			return whiteKing();
		} else {
			return null;
		}
	}
	
	private ArrayList<Move> whiteKing() {
		byte x = getX();
		byte y = getY();
		
		// Make 0 moves faster
		if( getBoard().occupiedOrBounds(x, (byte)(y+1)) && getBoard().occupiedOrBounds(x, (byte)(y-1)) 
				&& getBoard().occupiedOrBounds((byte)(x+1), y) && getBoard().occupiedOrBounds((byte)(x-1), y)){
			return null;
		}
		
		// otherwise create a new vector to store legal moves
		ArrayList<Move> v = new ArrayList<Move>();
		// set up m to refer to a Move object  
		Move m = null;

		//Moves down
		byte i = (byte) (getY()+1); 
		while(!getBoard().outOfRange(x, i)&&!getBoard().occupied(x, i)){
			
			//check if gamewinning
			boolean gW=false;
			if((x==10 && i==10) || (x==0 && i==10) ){
				gW = true;
			}
			
			m = new Move(this, x,y,x,i,analyseBoard(x,y,x,i),gW);
			v.add(m); 
			i++;
		}
		 
		//Moves up
		byte j = (byte)(getY()-1); 
		while(!getBoard().outOfRange(x, j)&&!getBoard().occupied(x, j)){
			
			//check if gamewinning
			boolean gW=false;
			if((x==0 && j==0) || (x==10 && j==0) ){
				gW = true;
			}
			
			m = new Move(this, x,y,x,j,analyseBoard(x,y,x,j),gW);
			v.add(m); 
			j--;
		}
			
		//Moves right up to being out of range or until it hits an opponent. 1st horizontal set
		byte k=(byte)(getX()+1); 
		while(!getBoard().outOfRange(k, y)&&!getBoard().occupied(k, y)){
			
			//check if gamewinning
			boolean gW=false;
			if((k==10 && y==0) || (k==10 && y==10) ){
				gW = true;
			}
			
			m = new Move(this, x,y,k,y,analyseBoard(x,y,k,y),gW);
			v.add(m); 
			k++;
		}
			 
		//Moves left up to being out of range or until it hits an opponent. 2nd horizontal set
		byte l = (byte)(getX()-1); 
		while(!getBoard().outOfRange(l, y)&&!getBoard().occupied(l, y)){
			
			//check if gamewinning
			boolean gW=false;
			if((l==0 && y==0) || (l==0 && y==10) ){
				gW = true;
			}
			
			m = new Move(this, x,y,l,y,analyseBoard(x,y,l,y),gW);
			v.add(m); 
			l--;
		}
		
		if (v.isEmpty()) return null;
		return v;
	}
	
	protected TakePiece analyseBoard(byte x, byte y, byte i, byte j) {
		Board b = getBoard();
		
		Piece take;
		Piece help;
		
		ArrayList<PieceCoordinates> takePiece = new ArrayList<>();
		TakePiece tp = new TakePiece(takePiece,false);
		
		final byte one  =  1;
		final byte two  =  2;
		final byte five =  5;
		
		if (i>0){
			take = b.getPiece((byte)(i-one),j);
			if (i >1){
				help = b.getPiece((byte)(i-two),j);
				if (take!=null) {
					
					if (take.getColour() != this.getColour() && (take.getChar() == 'P' || take.getChar() == 'p')){
						if (help!=null){
							if (help.getColour() == this.getColour()){
								tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
								tp.setTake(true);
							}
						} else if ( (i-2==0 && j == 0) || (i-2==0 && j == 10) || 
								((i-2==5 && j == 5) && (b.getPiece(five,five)==null || b.getPiece(five,five).getColour() == this.getColour() )) ) {
							tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
							tp.setTake(true);
						}
					}
				}
			}
		}
		
		
		if(i<10){
			take = b.getPiece((byte)(i+one),j);
			if(i<9){
				help = b.getPiece((byte)(i+two),j);
				if (take!=null) {
					if (take.getColour() != this.getColour() && (take.getChar() == 'P' || take.getChar() == 'p')){
						if (help!=null){
							if (help.getColour() == this.getColour()){
								tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
								tp.setTake(true);
							}
						} else if ( (i+2==10 && j == 0) || (i+2==10 && j == 10) || 
								((i+2==5 && j == 5) && (b.getPiece(five,five)==null || b.getPiece(five,five).getColour() == this.getColour() )) ) {
							tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
							tp.setTake(true);
						}
					}
				}
			}
			
		}
		
		
		if(j>0){
			take = b.getPiece(i,(byte)(j-one));
			if(j>1){
				help = b.getPiece(i,(byte)(j-two));
				if (take!=null) {
					if (take.getColour() != this.getColour() && (take.getChar() == 'P' || take.getChar() == 'p')){
						if (help!=null){
							if (help.getColour() == this.getColour()){
								tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
								tp.setTake(true);
							}
						} else if ( (i==10 && j-2 == 0) || (i==0 && j-2 == 0) || 
								((i==5 && j-2 == 5) && (b.getPiece(five,five)==null || b.getPiece(five,five).getColour() == this.getColour() )) ) {
							tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
							tp.setTake(true);
						}
					}
				}
			}
		}
		
		if(j<10){
			take = b.getPiece(i,(byte)(j+one));
			if(j<9){
				help = b.getPiece(i,(byte)(j+two));
				if (take!=null) {
					if (take.getColour() != this.getColour() && (take.getChar() == 'P' || take.getChar() == 'p')){
						if (help!=null){
							if (help.getColour() == this.getColour()){
								tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
								tp.setTake(true);
							}
						} else if ( (i==10 && j+2 == 10) || (i==0 && j+2 == 10) || 
								((i==5 && j+2 == 5) && (b.getPiece(five,five)==null || b.getPiece(five,five).getColour() == this.getColour() )) ) {
							tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
							tp.setTake(true);
						}
					}
				}
			}
		}
		
		return tp;
	}
	
	
	
}

