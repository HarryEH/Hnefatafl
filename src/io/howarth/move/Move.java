package io.howarth.move;

import java.util.Comparator;

import io.howarth.analysis.GameStatus;
import io.howarth.pieces.Piece;

/**
 * Move.java 
 *
 * Class that constructs a Move object, has an equals method to compare the parameters.
 *
 * @version 1.0 12/1/17
 *
 * @author Harry Howarth 
 */
public class Move implements Comparator<Move>, Comparable<Move>{

	//instance fields
	private Piece piece;
	private byte x1;// starting x coordinate
	private byte y1;// starting y coordinate
	private byte x2;// end x coordinate
	private byte y2;// end y coordinate
	
	private short weight = 0;
	
	GameStatus future = null;

	/**
	 * Move constructor, creates an object that stores information about the moves it is possible to make.
	 * @param obj the Piece object that will be effected.
	 * @param x the current x coordinate
	 * @param y the current y coordinate
	 * @param i the desired x coordinate
	 * @param j the desired y coordinate
	 */
	public Move(Piece obj, byte x, byte y, byte i, byte j) {
		piece = obj;
		x1 = x;
		y1 = y;
		x2 = i;
		y2 = j;
	}

	// Getters
	public Piece getPiece(){ return piece;}
	public byte getX(){ return x1; }
	public byte getY(){ return y1; }
	public byte getI(){ return x2; }
	public byte getJ(){ return y2; }
	public short getWeight(){return this.weight;}
	
	public void setWeight(short weight){
		this.weight = weight;
	}
	
	// Getters
	public GameStatus getFutureMove(){ return this.future; }
	// Setters
	public void setFutureMove(GameStatus m){ this.future = m; }

	/**
	 * Boolean equals method that overrides the superclass equals method
	 * @param takes an object as its parameters and compares it to a move object.
	 * @return true is the two objects parameters are equals.
	 */
	@Override public boolean equals(Object obj) {
		if (obj instanceof Move) {
			Move c = (Move) obj;
			return (this.getX() == c.getX()
					&&this.getY() == c.getY()
					&&this.getI() == c.getI()
					&&this.getJ() == c.getJ());
		}
		return false;
	}

	@Override public String toString(){
		return "Move ("+x1+", "+y1+"), ("+x2+", "+y2+")";
	}

	@Override
	public int compare(Move o1, Move o2) {
		if(o1.getWeight() > o2.getWeight()){
			return 1;
		} 
		if(o1.getWeight() < o2.getWeight()){
			return -1;
		}
		
		return 0;
		
	}

	@Override
	public int compareTo(Move o) {
		if(this.getWeight() > o.getWeight()){
			return -1;
		} 
		if(this.getWeight() < o.getWeight()){
			return 1;
		}
		
		return 0;
	}

}