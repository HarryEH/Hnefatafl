package io.howarth;
import java.util.ArrayList;


public class TakePiece {

	private ArrayList<Piece> piece;
	private boolean take;
	
	public TakePiece(ArrayList<Piece> p, boolean b){
		this.piece = p;
		this.take = b;
	}
	
	public void setPiece(ArrayList<Piece> p){
		this.piece = p;
	}
	
	public ArrayList<Piece> getPiece(){
		return this.piece;
	}
	
	public void setTake(boolean b){
		this.take = b;
	}
	
	public boolean getTake(){
		return this.take;
	}
}
