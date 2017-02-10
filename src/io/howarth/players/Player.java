package io.howarth.players;
import io.howarth.Board;
import io.howarth.Hnefatafl;
import io.howarth.pieces.Piece;
import io.howarth.pieces.Pieces;


/**
 * Player.java 
 *
 * Abstract class to represent a player
 *
 * @version 0.1 12/1/17
 *
 * @author Harry Howarth
 */

public abstract class Player {

	public static final byte BLACK = 0;
	public static final byte WHITE = 1;

	private String name;
	private Pieces pieces;
	private Board board;
	private Player opponent;

	public Player (String n, Pieces p, Board b, Player o) {
		name = n;
		pieces = p;
		board = b;
		opponent = o;
	}

	public Board getBoard() {
		return board;
	}

	public Player getOpponent() {
		return opponent;
	}

	public void setOpponent(Player p) {
		opponent = p;
	}

	public Pieces getPieces() {
		return pieces;
	}
	
	/***
	 * Checks to see if the player can make their move.
	 * @return boolean - true if this player can make a move
	 */
	public boolean makeMove(){
			
		byte zero = 0;
		byte ten  = 10;
		
		if (Hnefatafl.b.getPiece(zero,zero) != null || Hnefatafl.b.getPiece(ten,zero) != null ||
				Hnefatafl.b.getPiece(zero,ten) != null || Hnefatafl.b.getPiece(ten,ten) != null ){
			return false;
		}
		
		for(int i =0;i<pieces.getData().size();i++){
			if (pieces.getData().get(i).toString().equals("k") && pieces.getData().get(i).getColour() == Player.WHITE){
				return true;
			} 
		}
		
		for(int i =0;i<getOpponent().getPieces().getData().size();i++){
			if (getOpponent().getPieces().getData().get(i).toString().equals("k") && getOpponent().getPieces().getData().get(i).getColour() == Player.WHITE){
				return true;
			} 
		}
		
		return false;
		
	}
	//Added abstract method for classes that extend Player.
	public abstract boolean doMove();

	public void deletePiece(Piece p) {
		pieces.delete(p);
	}

	public String toString() {
		return name;
	}

}
