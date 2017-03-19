package io.howarth.move;
import io.howarth.pieces.Piece;

import java.util.ArrayList;


public class TakePiece {

	private ArrayList<PieceCoordinates> piece;
	private boolean take;
	
	public TakePiece(ArrayList<PieceCoordinates> p, boolean b){
		this.piece = p;
		this.take = b;
	}
	
	public void setPiece(ArrayList<PieceCoordinates> p){
		this.piece = p;
	}
	
	public ArrayList<PieceCoordinates> getPiece(){
		return this.piece;
	}
	
	public void setTake(boolean b){
		this.take = b;
	}
	
	public boolean getTake(){
		return this.take;
	}
}
