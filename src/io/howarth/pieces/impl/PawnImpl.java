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
		byte currentX = getX();
		byte currentY = getY();
		
		// Make 0 moves faster
		if( getBoard().occupied(currentX, (byte)(currentY+1)) && getBoard().occupied(currentX, (byte)(currentY-1)) 
				&& getBoard().occupied((byte)(currentX+1), currentY) && getBoard().occupied((byte)(currentX-1), currentY)){
			return null;
		}
		
		// otherwise create a new vector to store legal moves
		ArrayList<Move> v = new ArrayList<Move>();
		// set up m to refer to a Move object  
		Move m = null;
		
		// Moves down
		byte down = (byte) (getY() + 1); 
		loop:
		while ( !getBoard().occupied(currentX, down) ) {
			if (!(down == 10 &&currentX==10) && !(down == 10 && currentX == 0) && !(currentX == 5 && down == 5)){
				TakePiece p = analyseBoard(currentX, currentY, currentX, down);
				boolean gW = false;
				if (p.getPiece() != null && !p.getPiece().isEmpty()) {
					for(PieceCoordinates p1 : p.getPiece()){
						if (getBoard().getPiece(p1.getX(), p1.getY()).getChar() == 'k') {
							gW = true;
						}
					}
				}
				m = new Move(this, currentX, currentY, currentX, down, p, gW);
				v.add(m);
			} else {
				if( !(currentX == 5 && down == 5) ){
					break loop;
				}
			}
			down++;
		}
		 
		//Moves up
		byte up = (byte) (getY() - 1); 
		loop:
		while ( !getBoard().occupied(currentX, up) ) {
			if ( !(up == 0 && currentX == 10) && !(up == 0 && currentX == 0) && !(currentX == 5 && up == 5) ){
				TakePiece p = analyseBoard(currentX, currentY, currentX, up);
				boolean gW = false;
				if ( (p.getPiece() != null) && !p.getPiece().isEmpty() ) {
					for(PieceCoordinates p1 : p.getPiece()){
						if ( getBoard().getPiece(p1.getX(), p1.getY()).getChar() == 'k' ) {
							gW = true;
						}
					}
				}
				
				m = new Move(this, currentX, currentY, currentX, up, p, gW);
				v.add(m);
			} else {
				if(!(currentX==5&&up==5)){
					break loop;
				}
			}
			up--;
		}
		
			
		//Moves right
		byte right = (byte) (getX() + 1);
		loop:
		while ( !getBoard().occupied(right, currentY) ) {
			if (!(currentY == 10 && right == 10) && !(currentY == 0 &&right == 10) && !(right == 5 && currentY == 5)){
				TakePiece p = analyseBoard(currentX,currentY,right,currentY);
				boolean gW = false;
				if (p.getPiece() !=null && !p.getPiece().isEmpty()) {
					for(PieceCoordinates p1 : p.getPiece()){
						if (getBoard().getPiece(p1.getX(), p1.getY()).getChar() == 'k') {
							gW = true;
						}
					}
				}
				m = new Move(this, currentX,currentY,right,currentY,p,gW);
				v.add(m); 
			} else {
				if(!(right==5&&currentY==5)){
					break loop;
				}
			}
			right++;
		}
		 
		//Moves left 
		byte left = (byte)(getX()-1); 
		loop:
		while (!getBoard().occupied(left, currentY)) {
			if (!(currentY == 10 && left == 0) && !(currentY == 0 && left == 0) && !(left == 5&&currentY == 5)){
				TakePiece p = analyseBoard(currentX,currentY,left,currentY);
				boolean gW = false;
				if (p.getPiece() !=null && !p.getPiece().isEmpty()) {
					for(PieceCoordinates p1 : p.getPiece()){
						if (getBoard().getPiece(p1.getX(), p1.getY()).getChar() == 'k') {
							gW = true;
						}
					}
				}
				m = new Move(this, currentX,currentY,left,currentY,p,gW);
				v.add(m); 
			} else {
				if(!(left == 5&&currentY == 5)){
					break loop;
				}
			}
			left--;
		}
		
		if (v.isEmpty()) return null;
		return v;
	}
	
	protected TakePiece analyseBoard(byte x, byte y, byte i, byte j){
		Board b = getBoard();
//		b.remove(x, y);
//		b.setPosition(i, j, b.getPiece(x, y));
		
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
