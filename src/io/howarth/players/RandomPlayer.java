package io.howarth.players;

import io.howarth.Board;
import io.howarth.Move;
import io.howarth.Piece;
import io.howarth.Pieces;
import io.howarth.Player;

import java.util.ArrayList;
/**
 * RandomPlayer.java 
 *
 * Concrete Class to represent a random computer generated chess player. 
 *
 * @version 1.0 1st May 2015
 *
 * @author Harry Howarth 
 */
public class RandomPlayer extends Player {

	private String name;
	private Pieces pieces;
	/**
	 * This constructs a RandomPlayer object. 
	 * @param n The name of the player
	 * @param p The player's set of Pieces [object]
	 * @param b The board object of the game.
	 * @param o The Player's opponent.
	 */
	public RandomPlayer(String n, Pieces p, Board b, Player o) {
		super(n, p, b, o);
		name = n;
		pieces = p;
	}

	//access method.
	public String getName(){return name;}
	/**
	 * Boolean method that checks if the player can make a move implements abstract method from Player class.
	 * @return true -- if the player still has a king. 
	 */
	public boolean makeMove() {
		boolean truth =false;
		for(int i =0;i<pieces.getData().size();i++){
			if (pieces.getData().get(i).toString().equals("k")||pieces.getData().get(i).toString().equals("K")){
				truth=true;
			}
		}
		return truth;
	}
	/**
	 * Boolean method -- Allows the RandomPlayer to move by choosing a random move from the availableMoves()
	 * @return true when a move has been performed.
	 */
	public boolean doMove(){
		Board board = this.getBoard();
		ArrayList<Move> fullList = new ArrayList<Move>();

		Pieces pieces = this.getPieces();
		ArrayList<Piece> pieceList = pieces.getData();

		//use of the for each enhanced for loop.
		for (Piece p: pieceList){
			ArrayList<Move> instance = p.availableMoves();
			if (instance !=null){
				for (Move m: instance){
					fullList.add(m);
				}
			}
		}
		
		int randomMove = (int)(Math.random()*fullList.size());
		Move moveToConvert =fullList.get(randomMove);
		//convert the move objects parameters to basic types.
		int x = moveToConvert.getX();
		int y = moveToConvert.getY();
		int i = moveToConvert.getI();
		int j = moveToConvert.getJ();
		boolean b = moveToConvert.getTruth();
		Piece piece1 = moveToConvert.getPiece();

		if (b) {//true if there is an enemy player to take.
			this.getOpponent().deletePiece(board.getPiece(i, j));
			board.remove(i,j);
		}
		board.setPosition(i, j, piece1);
		piece1.setPosition(i, j);
		board.remove(x,y);
		//debug line
		//System.out.println(this.getName()+" moved from ("+x+","+y+") to ("+i+","+j+")");
		return true;
	}
}
