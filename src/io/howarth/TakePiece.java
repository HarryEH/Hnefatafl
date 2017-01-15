package io.howarth;
import io.howarth.Piece;


public class TakePiece {

	private Piece piece;
	private boolean take;
	
	public TakePiece(Piece p, boolean b){
		this.piece = p;
		this.take = b;
	}
	
	public void setPiece(Piece p){
		this.piece = p;
	}
	
	public Piece getPiece(){
		return this.piece;
	}
	
	public void setTake(boolean b){
		this.take = b;
	}
	
	public boolean getTake(){
		return this.take;
	}
}
