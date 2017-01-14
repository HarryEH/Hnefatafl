package io.howarth.players;
import io.howarth.Board;
import io.howarth.Pieces;
import io.howarth.Player;
import io.howarth.pieces.PieceCode;
/**
 * HumanPlayer.java 
 *
 *Class that extends player. Completes the moves and takes the user's input.
 *
 * @version 1.5 1st May 2015
 *
 * @author Harry Howarth 
 */
public class HumanPlayer extends Player {

	//private TextHandler input = new TextHandler();
	private String name;
	private Pieces pieces;
	private Board board;
	/**
	 * This constructs a HumanPlayer object. 
	 * @param n The name of the player
	 * @param p The player's set of Pieces [object]
	 * @param b The board object of the game.
	 * @param o The Player's opponent.
	 */
	public HumanPlayer(String n, Pieces p, Board b, Player o) {
		super(n, p, b, o);
		name = n;
		pieces = p;
		board = b;
	}

	//access methods
	public Pieces getPieces(){return pieces;}
	public String getName(){return name;}
	public Board getBoard(){return board;}
	
	/**
	 * Boolean method that checks if the player can make a move implements abstract method from Player class.
	 * @return true -- if the player still has a king. 
	 */
	
	//checks to see if king is still in Pieces arraylist, if so they can make their move.
	public boolean makeMove(){
		boolean truth =false;
		
		// true is white false is black
		int myColour = pieces.getColour();
		if (myColour == PieceCode.WHITE){
			for(int i =0;i<pieces.getData().size();i++){
				if (pieces.getData().get(i).toString().equals("k")){
					System.out.println("bitch1");
					truth=true;
				}
			}
		} else {
			// if opponent is a white then check if it has its king.
			for(int i =0;i<getOpponent().getPieces().getData().size();i++){
				if (getOpponent().getPieces().getData().get(i).toString().equals("k")){
					System.out.println("bitch2");
					truth=true;
				}
			}
		}
		
		
		return truth;
	}
	
	/**
	 * Boolean method -- Allows the HumanPLayer to make a move by taking their input.
     * code removed because now taken from the board directly.
	 * @return true when a move has been performed.
	 */
	public boolean doMove(){
		return false;
	}

}
