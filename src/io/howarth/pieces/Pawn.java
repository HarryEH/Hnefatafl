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
			if (i != 10  && !(x==5&&i==5)){
				m = new Move(this, x,y,x,i,analyseBoard(x,y,x,i));
				v.add(m);
			}
			i++;
		}
		 
		//Moves down up to being out of range or until it hits an opponent. 2st vertical set
		int j=getY()-1; 
		while(!getBoard().outOfRange(x, j)&&!getBoard().occupied(x, j)){
			if (j != 0  && !(x==5&&j==5)){
				m = new Move(this, x,y,x,j,analyseBoard(x,y,x,j));
				v.add(m);
			} 
			j--;
		}
			
		//Moves right up to being out of range or until it hits an opponent. 1st horizontal set
		int k=getX()+1; 
		while(!getBoard().outOfRange(k, y)&&!getBoard().occupied(k, y)){
			if (k != 10 && !(k==5&&y==5)){
				m = new Move(this, x,y,k,y,analyseBoard(x,y,k,y));
				v.add(m); 
			}
			k++;
		}
		 
		//Moves left up to being out of range or until it hits an opponent. 2nd horizontal set
		int l=getX()-1; 
		while(!getBoard().outOfRange(l, y)&&!getBoard().occupied(l, y)){
			if (l != 0 && !(l==5&&y==5)){
				m = new Move(this, x,y,l,y,analyseBoard(x,y,l,y));
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
			if (i != 10  && !(x==5&&i==5)){
				m = new Move(this, x,y,x,i,analyseBoard(x,y,x,i));
				v.add(m);
			}
			i++;
		}
		 
		// Moves down up to being out of range or until it hits an opponent. 2st vertical set
		int j=getY()-1; 
		while(!getBoard().outOfRange(x, j)&&!getBoard().occupied(x, j)){
			if (j != 0  && !(x==5&&j==5)){
				m = new Move(this, x,y,x,j,analyseBoard(x,y,x,j));
				v.add(m);
			} 
			j--;
		}
			
		// Moves right up to being out of range or until it hits an opponent. 1st horizontal set
		int k=getX()+1; 
		while(!getBoard().outOfRange(k, y)&&!getBoard().occupied(k, y)){
			if (k != 10  && !(k==5&&y==5)){
				m = new Move(this, x,y,k,y,analyseBoard(x,y,k,y));
				v.add(m); 
			}
			k++;
		}
		 
		// Moves left up to being out of range or until it hits an opponent. 2nd horizontal set
		int l=getX()-1; 
		while(!getBoard().outOfRange(l, y)&&!getBoard().occupied(l, y)){
			if (l != 0 && !(l==5&&y==5)){
				m = new Move(this, x,y,l,y,analyseBoard(x,y,l,y));
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
		
		Piece take;
		Piece help;
		
		if (i>0){
			take = b.getPiece(i-1,j);
			if (i >1){
				help = b.getPiece(i-2,j);
				if (take!=null) {
					if (take.getColour() != this.getColour() && (take.getChar() == 'P' || take.getChar() == 'p')){
						if (help!=null){
							if (help.getColour() == this.getColour()){
								return true;
							}
						} else if ( (i-2==0 && j == 0) || (i-2==0 && j == 10) || 
								((i-2==5 && j == 5) && (b.getPiece(5,5)==null || b.getPiece(5,5).getColour() == this.getColour() )) ) {
							return true;
						}
					}
				}
			}
		}
		
		
		if(i<10){
			take = b.getPiece(i+1,j);
			if(i<9){
				help = b.getPiece(i+2,j);
				if (take!=null) {
					if (take.getColour() != this.getColour() && (take.getChar() == 'P' || take.getChar() == 'p')){
						if (help!=null){
							if (help.getColour() == this.getColour()){
								return true;
							}
						} else if ( (i+2==10 && j == 0) || (i+2==10 && j == 10) || 
								((i+2==5 && j == 5) && (b.getPiece(5,5)==null || b.getPiece(5,5).getColour() == this.getColour() )) ) {
							return true;
						}
					}
				}
			}
			
		}
		
		
		if(j>0){
			take = b.getPiece(i,j-1);
			if(j>1){
				help = b.getPiece(i,j-2);
				if (take!=null) {
					if (take.getColour() != this.getColour() && (take.getChar() == 'P' || take.getChar() == 'p')){
						if (help!=null){
							if (help.getColour() == this.getColour()){
								return true;
							}
						} else if ( (i==10 && j-2 == 0) || (i==0 && j-2 == 0) || 
								((i==5 && j-2 == 5) && (b.getPiece(5,5)==null || b.getPiece(5,5).getColour() == this.getColour() )) ) {
							return true;
						}
					}
				}
			}
		}
		
		if(j<10){
			take = b.getPiece(i,j+1);
			if(j<9){
				help = b.getPiece(i,j+2);
				if (take!=null) {
					if (take.getColour() != this.getColour() && (take.getChar() == 'P' || take.getChar() == 'p')){
						if (help!=null){
							if (help.getColour() == this.getColour()){
								return true;
							}
						} else if ( (i==10 && j+2 == 10) || (i==0 && j+2 == 10) || 
								((i==5 && j+2 == 5) && (b.getPiece(5,5)==null || b.getPiece(5,5).getColour() == this.getColour() )) ) {
							return true;
						}
					}
				}
			}
		}
		
		// Code that will be needed to take a king. Check from all the sides
//		take = b.getPiece(i,j+1);
//		help = b.getPiece(i,j+2);
//		Piece help1 = b.getPiece(i,j+2);
//		Piece help2 = b.getPiece(i,j+2);
//		if (take!=null) {
//			if (take.getColour() != this.getColour() && (take.getChar() == 'K' || take.getChar() == 'k')){
//				if (help!=null && help1 != null && help2 != null){
//					if (help.getColour() == this.getColour() 
//							&& help1.getColour() == this.getColour() 
//							&& help2.getColour() == this.getColour()){
//						return true;
//					}
//				}
//			}
//		}
//		
//		 
//		take = b.getPiece(i,j+1);
//		help = b.getPiece(i,j+2);
//		Piece help1 = b.getPiece(i,j+2);
//		Piece help2 = b.getPiece(i,j+2);
//		if (take!=null) {
//			if (take.getColour() != this.getColour() && (take.getChar() == 'K' || take.getChar() == 'k')){
//				if (help!=null && help1 != null && help2 != null){
//					if (help.getColour() == this.getColour() 
//							&& help1.getColour() == this.getColour() 
//							&& help2.getColour() == this.getColour()){
//						return true;
//					}
//				}
//			}
//		}
//		
//		take = b.getPiece(i,j+1);
//		help = b.getPiece(i,j+2);
//		Piece help1 = b.getPiece(i,j+2);
//		Piece help2 = b.getPiece(i,j+2);
//		if (take!=null) {
//			if (take.getColour() != this.getColour() && (take.getChar() == 'K' || take.getChar() == 'k')){
//				if (help!=null && help1 != null && help2 != null){
//					if (help.getColour() == this.getColour() 
//							&& help1.getColour() == this.getColour() 
//							&& help2.getColour() == this.getColour()){
//						return true;
//					}
//				}
//			}
//		}
//		
//		take = b.getPiece(i,j+1);
//		help = b.getPiece(i,j+2);
//		Piece help1 = b.getPiece(i,j+2);
//		Piece help2 = b.getPiece(i,j+2);
//		if (take!=null) {
//			if (take.getColour() != this.getColour() && (take.getChar() == 'K' || take.getChar() == 'k')){
//				if (help!=null && help1 != null && help2 != null){
//					if (help.getColour() == this.getColour() 
//							&& help1.getColour() == this.getColour() 
//							&& help2.getColour() == this.getColour()){
//						return true;
//					}
//				}
//			}
//		}
		
		return false;
	}

}
