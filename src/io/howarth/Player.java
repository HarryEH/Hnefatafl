package io.howarth;
import io.howarth.pieces.Piece;
import io.howarth.pieces.Pieces;

import java.util.*;


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

	public static final int BLACK = 0;
	public static final int WHITE = 1;

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
	
	public abstract boolean makeMove();
	//Added abstract method for classes that extend Player.
	public abstract boolean doMove();

	public void deletePiece(Piece p) {
		pieces.delete(p);
	}

	public String toString() {
		return name;
	}

}
