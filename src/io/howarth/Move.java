package io.howarth;
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
	private int x1;// starting x coordinate
	private int y1;// starting y coordinate
	private int x2;// end x coordinate
	private int y2;// end y coordinate
	private TakePiece truth;// are you taking a piece
	private boolean gameWinning;
	
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
	public Move(Piece obj, int x, int y, int i, int j, TakePiece b, boolean gW) {
		piece = obj;
		x1 = x;
		y1 = y;
		x2 = i;
		y2 = j;
		truth = b;
		gameWinning = gW;
	}

	// access methods for usage in the override equals method of move.
	public Piece getPiece(){ return piece;}
	public int getX(){return x1;}
	public int getY(){return y1;}
	public int getI(){return x2;}
	public int getJ(){return y2;}
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


}