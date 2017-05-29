package io.howarth.move;
import java.util.ArrayList;


public class TakePiece {

	private ArrayList<PieceCoordinates> piece;
	private boolean take;
	private boolean gameOver;
	
	public TakePiece(ArrayList<PieceCoordinates> p, boolean b, boolean gameOver){
		this.piece = p;
		this.take = b;
		this.gameOver = gameOver;
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
	
	public void setGameOver(boolean b){
		this.take = b;
	}
	
	public boolean getTake(){
		return this.take;
	}
	
	public boolean getGameOver(){
		return this.gameOver;
	}
}
