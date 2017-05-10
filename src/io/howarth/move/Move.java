package io.howarth.move;

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
	private short weight;
	
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
	public Move(Piece obj, byte x, byte y, byte i, byte j, TakePiece b, boolean gW, short weight) {
		piece = obj;
		x1 = x;
		y1 = y;
		x2 = i;
		y2 = j;
		truth = b;
		gameWinning = gW;
		this.weight = weight;
	}

	// Getters
	public Piece getPiece(){ return piece;}
	public byte getX(){return x1;}
	public byte getY(){return y1;}
	public byte getI(){return x2;}
	public byte getJ(){return y2;}
	public short getWeight(){return weight;}
	public TakePiece getTruth(){return truth;}
	public boolean getGameOver(){return gameWinning;}
	
	//Setters
	public void setWeight(short d){this.weight = d;}
	

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
					&&this.getJ() == c.getJ()
					&&this.getPiece().equals(c.getPiece())
					&&this.getTruth()==c.getTruth());
		}
		return false;
	}

	@Override public String toString(){
		return "Move ("+x1+", "+y1+"), ("+x2+", "+y2+")";
	}

}