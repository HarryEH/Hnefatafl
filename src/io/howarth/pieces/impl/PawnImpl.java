package io.howarth.pieces.impl;
import io.howarth.Board;
import io.howarth.move.Move;
import io.howarth.move.PieceCoordinates;
import io.howarth.move.TakePiece;
import io.howarth.pieces.Piece;
import io.howarth.pieces.PieceCode;

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
		
		// Make 0 moves faster
		if( getBoard().occupiedOrBounds(x, (byte)(y+1)) && getBoard().occupiedOrBounds(x, (byte)(y-1)) 
				&& getBoard().occupiedOrBounds((byte)(x+1), y) && getBoard().occupiedOrBounds((byte)(x-1), y)){
			return null;
		}
		
		Move m;
		
		ArrayList<Move> moveLis = new ArrayList<Move>();
		
		boolean testUp    = true; boolean testDown  = true;
		boolean testRight = true; boolean testLeft  = true;
		
		testLoop:
		for(byte loopCounter = 1; loopCounter < 11; loopCounter++) {
			
			if(testUp){ // Generate moves moving 'up' the board
				
				byte up = (byte)(y+loopCounter);
				
				if ( getBoard().occupiedOrBounds(x, up) ||
						((x==0 && up==0) || (x==0 && up==10) || (x==10 && up==0) || (x==10 && up==10) ) ) {
					testUp = false; // Won't come back into this logic block again
				} else {
					
					TakePiece p = analyseBoard(x, y, x, up);
					boolean gW = false;
					if(p != null) {
						if (p.getPiece() !=null && !p.getPiece().isEmpty()) {
							for(PieceCoordinates p1 : p.getPiece()){
								if (getBoard().getPiece(p1.getX(), p1.getY()).getChar() == 'k') {
									gW = true;
								}
							}
						}
					}
					m = new Move(this, x, y, x, up, p, gW);
					moveLis.add(m); 		
				}
			}
			
			if(testDown){ // Generate moves moving 'down' the board
				
				byte down = (byte)(y-loopCounter);
				
				if ( getBoard().occupiedOrBounds(x, down) || ((x==0 && down==0) || (x==0 && down==10) || (x==10 && down==0) || (x==10 && down==10) ) ) {
					testDown = false; // Won't come back into this logic block again
				} else {
					
					TakePiece p = analyseBoard(x, y, x, down);
					boolean gW = false;
					if(p != null) {
						if (p.getPiece() !=null && !p.getPiece().isEmpty()) {
							for(PieceCoordinates p1 : p.getPiece()){
								if (getBoard().getPiece(p1.getX(), p1.getY()).getChar() == 'k') {
									gW = true;
								}
							}
						}
					}
					m = new Move(this, x, y, x, down, p, gW);
					moveLis.add(m); 
				}
			} 
			
			if(testLeft){ // Generate moves moving 'left' across the board
				
				byte left = (byte)(x-loopCounter);
				
				if ( getBoard().occupiedOrBounds(left, y) ||
						((left==0 && y==0) || (left==0 && y==10) || (left==10 && y==0) || (left==10 && y==10) )) {
					testLeft = false; // Won't come back into this logic block again
				} else {
					
					TakePiece p = analyseBoard(x, y, left, y);
					boolean gW = false;
					if(p != null) {
						if (p.getPiece() !=null && !p.getPiece().isEmpty()) {
							for(PieceCoordinates p1 : p.getPiece()){
								if (getBoard().getPiece(p1.getX(), p1.getY()).getChar() == 'k') {
									gW = true;
								}
							}
						}
					}
					m = new Move(this, x, y, left, y, p, gW);
					moveLis.add(m); 
				}
			}
			
			if(testRight){ // Generate moves moving 'left' across the board
				
				byte right = (byte)(x+loopCounter);
				
				if ( getBoard().occupiedOrBounds(right, y) ||
						((right==0 && y==0) || (right==0 && y==10) || (right==10 && y==0) || (right==10 && y==10)) ) {
					testRight = false; // Won't come back into this logic block again
				} else {
					
					TakePiece p = analyseBoard(x, y, right, y);
					boolean gW = false;
					if(p != null) {
						if (p.getPiece() !=null && !p.getPiece().isEmpty()) {
							for(PieceCoordinates p1 : p.getPiece()){
								if (getBoard().getPiece(p1.getX(), p1.getY()).getChar() == 'k') {
									gW = true;
								}
							}
						}
					}
					m = new Move(this, x, y, right, y, p, gW);
					moveLis.add(m); 
				}
			}
			
			if(! (testRight || testLeft || testUp || testDown) ){
				break testLoop;
			}	
		}
		
		if (moveLis.isEmpty()) return null;
		return moveLis;
	
	}
	
	protected TakePiece analyseBoard(byte x, byte y, byte i, byte j){
		Board b = getBoard();
//		b.remove(x, y);
//		b.setPosition(i, j, b.getPiece(x, y));
		
		
		if(!b.occupiedOrBounds((byte)(i-1),j) && !b.occupiedOrBounds((byte)(i+1),j) 
				&& !b.occupiedOrBounds(i,(byte)(j-1)) && !b.occupiedOrBounds(i,(byte)(j+1)) ) {
			return null;
		}
		
		Piece take;
		Piece help;
		Piece help1;
		Piece help2;
		
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
			if(i<9) {
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
		
		// Code that will be needed to take a king. Check from all the sides
		if (this.getColour() != PieceCode.WHITE){
			
			// From below
			if(j<9 && i<10 && i>0 && j>0){
				take = b.getPiece(i,(byte)(j+one));
				help = b.getPiece(i,(byte)(j+two));//above
				help1 = b.getPiece((byte)(i+one),(byte)(j+one));//left
				help2 = b.getPiece((byte)(i-one),(byte)(j+one));//right
				if (take!=null) {
					if (take.getColour() != this.getColour() && (take.getChar() == 'K' || take.getChar() == 'k')){
						
						if ( (help!=null || (i==5 && j+2 ==5)) 
								&& (help1 != null || (i+1==5 && j+1 ==5)) 
								&& (help2 != null || (i-1==5 && j+1 ==5))) {
							
							if (help == null) {
								if (help1.getColour() == this.getColour() 
									&& help2.getColour() == this.getColour()){
									tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
									tp.setTake(true);
								}
							} else if (help1 == null) {
								if (help.getColour() == this.getColour() 
										&& help2.getColour() == this.getColour()){
										tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
										tp.setTake(true);
									}
							} else if (help2 == null) {
								if (help1.getColour() == this.getColour() 
										&& help.getColour() == this.getColour()){
										tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
										tp.setTake(true);
									}
							} else if ( help1.getColour() == this.getColour() 
										&& help.getColour() == this.getColour()
										&& help2.getColour() == this.getColour()){
								tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
								tp.setTake(true);
							}
							
						}
					}
				}
			}
			
			// From above
			if(j>1 && i<10 && j<10 && i>0){
				take = b.getPiece(i,(byte)(j-one));
				help = b.getPiece(i,(byte)(j-two));
				help1 = b.getPiece((byte)(i-one),(byte)(j-one));
				help2 = b.getPiece((byte)(i+one),(byte)(j-one));
				if (take!=null) {
					if (take.getColour() != this.getColour() && (take.getChar() == 'K' || take.getChar() == 'k')){
						if ( (help!=null || (i==5 && j-2 ==5)) 
								&& (help1 != null || (i-1==5 && j-1 ==5)) 
								&& (help2 != null || (i+1==5 && j-1 ==5)) ) {
							if (help == null) {
								if (help1.getColour() == this.getColour() 
									&& help2.getColour() == this.getColour()){
									tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
									tp.setTake(true);
								}
							} else if (help1 == null) {
								if (help.getColour() == this.getColour() 
										&& help2.getColour() == this.getColour()){
										tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
										tp.setTake(true);
									}
							} else if (help2 == null) {
								if (help1.getColour() == this.getColour() 
										&& help.getColour() == this.getColour()){
										tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
										tp.setTake(true);
									}
							} else if ( help1.getColour() == this.getColour() 
									&& help.getColour() == this.getColour()
									&& help2.getColour() == this.getColour()){
								tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
								tp.setTake(true);
							}
						}
					}
				}
			}
			
			// From left
			if(i<9 && j<10 && j>0){
				take = b.getPiece((byte)(i+one),j);
				help = b.getPiece((byte)(i+two),j);
				help1 = b.getPiece((byte)(i+one),(byte)(j+one));
				help2 = b.getPiece((byte)(i+one),(byte)(j-one));
				if (take!=null) {
					if (take.getColour() != this.getColour() && (take.getChar() == 'K' || take.getChar() == 'k')){
						if ( (help!=null || (i+2==5 && j ==5)) 
								&& (help1 != null || (i+1==5 && j+1 ==5)) 
								&& (help2 != null || (i+1==5 && j-1 ==5)) ) {
							if (help == null) {
								if (help1.getColour() == this.getColour() 
									&& help2.getColour() == this.getColour()){
									tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
									tp.setTake(true);
								}
							} else if (help1 == null) {
								if (help.getColour() == this.getColour() 
										&& help2.getColour() == this.getColour()){
										tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
										tp.setTake(true);
									}
							} else if (help2 == null) {
								if (help1.getColour() == this.getColour() 
										&& help.getColour() == this.getColour()){
										tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
										tp.setTake(true);
									}
							} else if ( help1.getColour() == this.getColour() 
									&& help.getColour() == this.getColour()
									&& help2.getColour() == this.getColour()){
								tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
								tp.setTake(true);
							}
						}
					}
				}
			}
				
			// From right
			if(i>1 && j>0 && j<10){
				take = b.getPiece((byte)(i-one),j);
				help = b.getPiece((byte)(i-two),j);
				help1 = b.getPiece((byte)(i-one),(byte)(j-one));
				help2 = b.getPiece((byte)(i-one),(byte)(j+one));
				if (take!=null) {
					if (take.getColour() != this.getColour() && (take.getChar() == 'K' || take.getChar() == 'k')){
						if ( (help!=null ||( i-2==5 && j ==5)) 
								&& (help1 != null || (i-1==5 && j-1 ==5)) 
								&& (help2 != null || (i-1==5 && j+1 ==5)) ) {
							if (help == null) {
								if (help1.getColour() == this.getColour() 
									&& help2.getColour() == this.getColour()){
									tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
									tp.setTake(true);
								}
							} else if (help1 == null) {
								if (help.getColour() == this.getColour() 
										&& help2.getColour() == this.getColour()){
										tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
										tp.setTake(true);
									}
							} else if (help2 == null) {
								if (help1.getColour() == this.getColour() 
										&& help.getColour() == this.getColour()){
										tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
										tp.setTake(true);
								}
							} else if ( help1.getColour() == this.getColour() 
									&& help.getColour() == this.getColour()
									&& help2.getColour() == this.getColour()){
								tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
								tp.setTake(true);
							}
						}
					}
				}
			}
		}
//		
		return tp;
	}

	

}
