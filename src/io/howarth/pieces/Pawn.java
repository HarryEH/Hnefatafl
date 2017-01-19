package io.howarth.pieces;
import io.howarth.Board;
import io.howarth.Move;
import io.howarth.Piece;
import io.howarth.TakePiece;

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
		return movesPawn();
	}

	private ArrayList<Move> movesPawn() {
		int x = getX();
		int y = getY();
		// otherwise create a new vector to store legal moves
		ArrayList<Move> v = new ArrayList<Move>();
		// set up m to refer to a Move object  
		Move m = null;
		
		// Moves down
		int i=getY()+1; 
		while(!getBoard().outOfRange(x, i)&&!getBoard().occupied(x, i)){
			if (!(i == 10 &&x==10) && !(i == 10 &&x==0)  && !(x==5&&i==5)){
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
			} 
			i++;
		}
		 
		//Moves up
		int j=getY()-1; 
		while(!getBoard().outOfRange(x, j)&&!getBoard().occupied(x, j)){
			if (!(j == 0 &&x==10) && !(j == 0 &&x==0) && !(x==5&&j==5)){
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
			} 
			j--;
		}
		
			
		//Moves right
		int k=getX()+1; 
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
			}
			k++;
		}
		 
		//Moves left 
		int l=getX()-1; 
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
			}
			l--;
		}
		
		if (v.isEmpty()) return null;
		return v;
	}
	
	protected TakePiece analyseBoard(int x, int y, int i, int j){
		Board b = getBoard();
//		b.remove(x, y);
//		b.setPosition(i, j, b.getPiece(x, y));
		
		Piece take;
		Piece help;
		Piece help1;
		Piece help2;
		
		ArrayList<Piece> takePiece = new ArrayList<>();
		TakePiece tp = new TakePiece(takePiece,false);
		
		if (i>0){
			take = b.getPiece(i-1,j);
			if (i >1){
				help = b.getPiece(i-2,j);
				if (take!=null) {
					if (take.getColour() != this.getColour() && (take.getChar() == 'P' || take.getChar() == 'p')){
						if (help!=null){
							if (help.getColour() == this.getColour()){
								tp.getPiece().add(take);
								tp.setTake(true);
							}
						} else if ( (i-2==0 && j == 0) || (i-2==0 && j == 10) || 
								((i-2==5 && j == 5) && (b.getPiece(5,5)==null || b.getPiece(5,5).getColour() == this.getColour() )) ) {
							tp.getPiece().add(take);
							tp.setTake(true);
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
								tp.getPiece().add(take);
								tp.setTake(true);
							}
						} else if ( (i+2==10 && j == 0) || (i+2==10 && j == 10) || 
								((i+2==5 && j == 5) && (b.getPiece(5,5)==null || b.getPiece(5,5).getColour() == this.getColour() )) ) {
							tp.getPiece().add(take);
							tp.setTake(true);
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
								tp.getPiece().add(take);
								tp.setTake(true);
							}
						} else if ( (i==10 && j-2 == 0) || (i==0 && j-2 == 0) || 
								((i==5 && j-2 == 5) && (b.getPiece(5,5)==null || b.getPiece(5,5).getColour() == this.getColour() )) ) {
							tp.getPiece().add(take);
							tp.setTake(true);
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
								tp.getPiece().add(take);
								tp.setTake(true);
							}
						} else if ( (i==10 && j+2 == 10) || (i==0 && j+2 == 10) || 
								((i==5 && j+2 == 5) && (b.getPiece(5,5)==null || b.getPiece(5,5).getColour() == this.getColour() )) ) {
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
				take = b.getPiece(i,j+1);
				help = b.getPiece(i,j+2);//above
				help1 = b.getPiece(i+1,j+1);//left
				help2 = b.getPiece(i-1,j+1);//right
				if (take!=null) {
					if (take.getColour() != this.getColour() && (take.getChar() == 'K' || take.getChar() == 'k')){
						if (help!=null && help1 != null && help2 != null){
							if (help.getColour() == this.getColour() 
									&& help1.getColour() == this.getColour() 
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
				take = b.getPiece(i,j-1);
				help = b.getPiece(i,j-2);
				help1 = b.getPiece(i-1,j-1);
				help2 = b.getPiece(i+1,j-1);
				if (take!=null) {
					if (take.getColour() != this.getColour() && (take.getChar() == 'K' || take.getChar() == 'k')){
						if (help!=null && help1 != null && help2 != null){
							if (help.getColour() == this.getColour() 
									&& help1.getColour() == this.getColour() 
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
				take = b.getPiece(i+1,j);
				help = b.getPiece(i+2,j);
				help1 = b.getPiece(i+1,j+1);
				help2 = b.getPiece(i+1,j-1);
				if (take!=null) {
					if (take.getColour() != this.getColour() && (take.getChar() == 'K' || take.getChar() == 'k')){
						if (help!=null && help1 != null && help2 != null){
							if (help.getColour() == this.getColour() 
									&& help1.getColour() == this.getColour() 
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
				take = b.getPiece(i-1,j);
				help = b.getPiece(i-2,j);
				help1 = b.getPiece(i-1,j-1);
				help2 = b.getPiece(i-1,j+1);
				if (take!=null) {
					if (take.getColour() != this.getColour() && (take.getChar() == 'K' || take.getChar() == 'k')){
						if (help!=null && help1 != null && help2 != null){
							if (help.getColour() == this.getColour() 
									&& help1.getColour() == this.getColour() 
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
