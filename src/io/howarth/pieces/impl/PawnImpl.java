package io.howarth.pieces.impl;
import io.howarth.Board;
import io.howarth.move.Move;
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
		// otherwise create a new vector to store legal moves
		ArrayList<Move> v = new ArrayList<Move>();
		// set up m to refer to a Move object  
		Move m = null;
		
		// Moves down
		byte i= (byte) (getY()+1); 
		loop:
		while(!getBoard().outOfRange(x, i)&&!getBoard().occupied(x, i)){
			if (!(i == 10 &&x==10) && !(i == 10 &&x==0) && !(x==5&&i==5)){
				TakePiece p = analyseBoard(x,y,x,i);
				boolean gW = false;
				if (p.getPiece() !=null && !p.getPiece().isEmpty()) {
					for(Piece p1 : p.getPiece()){
						if (p1.getChar() == 'k') {
							gW = true;
						}
					}
				}
				m = new Move(this, x,y,x,i,p,gW,0);
				v.add(m);
			} else {
				if(!(x==5&&i==5)){
					break loop;
				}
			}
			i++;
		}
		 
		//Moves up
		byte j=(byte) (getY()-1); 
		loop:
		while(!getBoard().outOfRange(x, j)&&!getBoard().occupied(x, j)){
			if (!(j == 0 &&x==10) && !(j == 0 &&x==0)&& !(x==5&&j==5)){
				TakePiece p = analyseBoard(x,y,x,j);
				boolean gW = false;
				if (p.getPiece() !=null && !p.getPiece().isEmpty()) {
					for(Piece p1 : p.getPiece()){
						if (p1.getChar() == 'k') {
							gW = true;
						}
					}
				}
				
				m = new Move(this, x,y,x,j,p,gW,0);
				v.add(m);
			} else {
				if(!(x==5&&j==5)){
					break loop;
				}
			}
			j--;
		}
		
			
		//Moves right
		byte k=(byte) (getX()+1);
		loop:
		while(!getBoard().outOfRange(k, y)&&!getBoard().occupied(k, y)){
			if (!(y == 10 &&k==10) && !(y == 0 &&k==10) && !(k==5&&y==5)){
				TakePiece p = analyseBoard(x,y,k,y);
				boolean gW = false;
				if (p.getPiece() !=null && !p.getPiece().isEmpty()) {
					for(Piece p1 : p.getPiece()){
						if (p1.getChar() == 'k') {
							gW = true;
						}
					}
				}
				m = new Move(this, x,y,k,y,p,gW,0);
				v.add(m); 
			} else {
				if(!(k==5&&y==5)){
					break loop;
				}
			}
			k++;
		}
		 
		//Moves left 
		byte l = (byte)(getX()-1); 
		loop:
		while(!getBoard().outOfRange(l, y)&&!getBoard().occupied(l, y)){
			if (!(y == 10 && l == 0) && !(y == 0 && l == 0) && !(l == 5&&y == 5)){
				TakePiece p = analyseBoard(x,y,l,y);
				boolean gW = false;
				if (p.getPiece() !=null && !p.getPiece().isEmpty()) {
					for(Piece p1 : p.getPiece()){
						if (p1.getChar() == 'k') {
							gW = true;
						}
					}
				}
				m = new Move(this, x,y,l,y,p,gW,0);
				v.add(m); 
			} else {
				if(!(l == 5&&y == 5)){
					break loop;
				}
			}
			l--;
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
		
		ArrayList<Piece> takePiece = new ArrayList<>();
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
								tp.getPiece().add(take);
								tp.setTake(true);
							}
						} else if ( (i-2==0 && j == 0) || (i-2==0 && j == 10) || 
								((i-2==5 && j == 5) && (b.getPiece(five,five)==null || b.getPiece(five,five).getColour() == this.getColour() )) ) {
							tp.getPiece().add(take);
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
								tp.getPiece().add(take);
								tp.setTake(true);
							}
						} else if ( (i+2==10 && j == 0) || (i+2==10 && j == 10) || 
								((i+2==5 && j == 5) && (b.getPiece(five,five)==null || b.getPiece(five,five).getColour() == this.getColour() )) ) {
							tp.getPiece().add(take);
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
								tp.getPiece().add(take);
								tp.setTake(true);
							}
						} else if ( (i==10 && j-2 == 0) || (i==0 && j-2 == 0) || 
								((i==5 && j-2 == 5) && (b.getPiece(five,five)==null || b.getPiece(five,five).getColour() == this.getColour() )) ) {
							tp.getPiece().add(take);
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
								tp.getPiece().add(take);
								tp.setTake(true);
							}
						} else if ( (i==10 && j+2 == 10) || (i==0 && j+2 == 10) || 
								((i==5 && j+2 == 5) && (b.getPiece(five,five)==null || b.getPiece(five,five).getColour() == this.getColour() )) ) {
							tp.getPiece().add(take);
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
									tp.getPiece().add(take);
									tp.setTake(true);
								}
							} else if (help1 == null) {
								if (help.getColour() == this.getColour() 
										&& help2.getColour() == this.getColour()){
										tp.getPiece().add(take);
										tp.setTake(true);
									}
							} else if (help2 == null) {
								if (help1.getColour() == this.getColour() 
										&& help.getColour() == this.getColour()){
										tp.getPiece().add(take);
										tp.setTake(true);
									}
							} else if ( help1.getColour() == this.getColour() 
										&& help.getColour() == this.getColour()
										&& help2.getColour() == this.getColour()){
								tp.getPiece().add(take);
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
									tp.getPiece().add(take);
									tp.setTake(true);
								}
							} else if (help1 == null) {
								if (help.getColour() == this.getColour() 
										&& help2.getColour() == this.getColour()){
										tp.getPiece().add(take);
										tp.setTake(true);
									}
							} else if (help2 == null) {
								if (help1.getColour() == this.getColour() 
										&& help.getColour() == this.getColour()){
										tp.getPiece().add(take);
										tp.setTake(true);
									}
							} else if ( help1.getColour() == this.getColour() 
									&& help.getColour() == this.getColour()
									&& help2.getColour() == this.getColour()){
								tp.getPiece().add(take);
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
									tp.getPiece().add(take);
									tp.setTake(true);
								}
							} else if (help1 == null) {
								if (help.getColour() == this.getColour() 
										&& help2.getColour() == this.getColour()){
										tp.getPiece().add(take);
										tp.setTake(true);
									}
							} else if (help2 == null) {
								if (help1.getColour() == this.getColour() 
										&& help.getColour() == this.getColour()){
										tp.getPiece().add(take);
										tp.setTake(true);
									}
							} else if ( help1.getColour() == this.getColour() 
									&& help.getColour() == this.getColour()
									&& help2.getColour() == this.getColour()){
								tp.getPiece().add(take);
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
									tp.getPiece().add(take);
									tp.setTake(true);
								}
							} else if (help1 == null) {
								if (help.getColour() == this.getColour() 
										&& help2.getColour() == this.getColour()){
										tp.getPiece().add(take);
										tp.setTake(true);
									}
							} else if (help2 == null) {
								if (help1.getColour() == this.getColour() 
										&& help.getColour() == this.getColour()){
										tp.getPiece().add(take);
										tp.setTake(true);
								}
							} else if ( help1.getColour() == this.getColour() 
									&& help.getColour() == this.getColour()
									&& help2.getColour() == this.getColour()){
								tp.getPiece().add(take);
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
