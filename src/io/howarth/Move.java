package io.howarth;

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

public class Move {

	//instance fields
	private Piece piece;
	private byte x1;// starting x coordinate
	private byte y1;// starting y coordinate
	private byte x2;// end x coordinate
	private byte y2;// end y coordinate
	private TakePiece truth;// are you taking a piece
	private boolean gameWinning;
	private int weight;
	
	//FIXME change this to include the piece to take!! it will be something in von nuemann's neighbour 

	/**
	 * Move constructor, creates an object that stores information about the moves it is possible to make.
	 * @param obj the Piece object that will be effected.
	 * @param x the current x coordinate
	 * @param y the current y coordinate
	 * @param i the desired x coordinate
	 * @param j the desired y coordinate
	 * @param b true if a piece is being taken.
	 * @param gW true if this move will end the game
	 */
	public Move(Piece obj, byte x, byte y, byte i, byte j, TakePiece b, boolean gW, int weight) {
		piece = obj;
		x1 = x;
		y1 = y;
		x2 = i;
		y2 = j;
		truth = b;
		gameWinning = gW;
		this.weight = weight;
	}

	// access methods for usage in the override equals method of move.
	public Piece getPiece(){ return piece;}
	public byte getX(){return x1;}
	public byte getY(){return y1;}
	public byte getI(){return x2;}
	public byte getJ(){return y2;}
	public void setWeight(int d){this.weight = d;}
	public int getWeight(){return weight;}
	public TakePiece getTruth(){return truth;}
	public boolean getGameOver(){return gameWinning;}

	/**
	 * Boolean equals method that overrides the superclass equals method
	 * @param takes an object as its parameters and compares it to a move object.
	 * @return true is the two objects parameters are equals.
	 */
	@Override public boolean equals(Object obj) {
		boolean test = false;

		if (obj instanceof Move) {
			Move c = (Move) obj;
			if (this.getX() == c.getX()
					&&this.getY() == c.getY()
					&&this.getI() == c.getI()
					&&this.getJ() == c.getJ()
					&&this.getPiece().equals(c.getPiece())
					&&this.getTruth()==c.getTruth()) { 
				test =true;
			}
		}
		return test;
	}

	@Override public String toString(){
		return "Move ("+x1+", "+y1+"), ("+x2+", "+y2+")";
	}

}