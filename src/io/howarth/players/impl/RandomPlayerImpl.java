package io.howarth.players.impl;

import io.howarth.Board;
import io.howarth.move.Move;
import io.howarth.move.PieceCoordinates;
import io.howarth.pieces.Piece;
import io.howarth.pieces.Pieces;
import io.howarth.players.Player;

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
public class RandomPlayerImpl extends Player {
	
	/**
	 * This constructs a RandomPlayer object. 
	 * @param n The name of the player
	 * @param p The player's set of Pieces [object]
	 * @param b The board object of the game.
	 * @param o The Player's opponent.
	 */
	public RandomPlayerImpl(String n, Pieces p, Board b, Player o) {
		super(n, p, b, o);
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
		if (fullList != null && !fullList.isEmpty()){
			int randomMove = (int)(Math.random()*fullList.size());
			
			Move moveToConvert =fullList.get(randomMove);
			
			// Convert the move objects parameters to basic types.
			byte x = moveToConvert.getX();
			byte y = moveToConvert.getY();
			byte i = moveToConvert.getI();
			byte j = moveToConvert.getJ();
			boolean b = false;
			
			if(moveToConvert.getTruth() != null){
				b = moveToConvert.getTruth().getTake();
			}
			
			Piece piece1 = moveToConvert.getPiece();
				
			if (b) {//true if there is an enemy player to take.
				for(PieceCoordinates p : moveToConvert.getTruth().getPiece()){
					this.getOpponent().deletePiece(board.getPiece(p.getX(), p.getY()));
					board.remove(p.getX(),p.getY());
				}
			}
			
			board.setPosition(i, j, piece1);
			piece1.setPosition(i, j);
			board.remove(x,y);
			
			return true;
		 } else {
			 return false;
		 }
	}
}
