package io.howarth.players;

import io.howarth.Board;
import io.howarth.Hnefatafl;
import io.howarth.Move;
import io.howarth.Piece;
import io.howarth.Pieces;
import io.howarth.Player;
import io.howarth.pieces.PieceCode;

import java.util.ArrayList;
/**
 * AggressivePlayer.java 
 *
 * Concrete Class to represent an aggressive computer generated chess player, makes Move object and
 * does the move.
 *
 * @version 1.0 1st May 2015
 *
 * @author Harry Howarth 
 */
public class AggressivePlayer extends Player {

	private String name;
	private Pieces pieces;

	/**
	 * This constructs an AggresivePlayer which takes the highest value enemy piece 
	 * @param n The name of the player
	 * @param p The player's set of Pieces [object]
	 * @param b The board object of the game.
	 * @param o The Player's opponent.
	 */
	public AggressivePlayer(String n, Pieces p, Board b, Player o) {
		super(n, p, b, o);
		name = n;
		pieces = p;
	}

	//access method.
	public String getName(){return name;}

	/**
	 * Boolean method that checks if the player can make a move implements abstract method from Player class.
	 * @return true -- if the game is not over. 
	 */
	public boolean makeMove(){
		
		if (Hnefatafl.b.getPiece(0,0) != null || Hnefatafl.b.getPiece(10,0) != null ||
				Hnefatafl.b.getPiece(0,10) != null || Hnefatafl.b.getPiece(10,10) != null ){
			return false;
		}
		
		//FIXME doesn't take into account when white has no pawns & king is trapped against the side
		
		// true is white false is black
		int myColour = pieces.getColour();
		
		if (myColour == PieceCode.WHITE){
			for(int i =0;i<pieces.getData().size();i++){
				if (pieces.getData().get(i).toString().equals("k")){
					return true;
				}
			}
		} else {
			// if opponent is a white then check if it has its king.
			for(int i =0;i<getOpponent().getPieces().getData().size();i++){
				if (getOpponent().getPieces().getData().get(i).toString().equals("k")){
					return true;
				}
			}
		}

		
		return false;
	}

	/**
	 * Boolean method -- Aggressive player makes his move, this 
	 * method also actually performs the move (ie takes the highest value piece of the 
	 * enemy, and if that is not possible does a random move).
	 * @return true when a move has been performed.
	 */
	public boolean doMove(){
		
		Board board = this.getBoard();
		ArrayList<Move> fullList = new ArrayList<Move>();
		ArrayList<Move> trueList = new ArrayList<Move>();
		Pieces pieces = this.getPieces();
		ArrayList<Piece> pieceList = pieces.getData();

		//use of the for each enhanced for loop.
		for (Piece p: pieceList){
			ArrayList<Move> instance = p.availableMoves();
			if (instance !=null){
				for (Move m: instance){
					fullList.add(m);
					//if it can take a piece add to trueList or if it can win the game otherwise
					if (m.getTruth().getTake() || m.getGameOver()){
						trueList.add(m);
					}
				}
			}
		}
		if (fullList!=null && !fullList.isEmpty()) {
			Move moveToConvert =null;
			//if the new ArrayList isn't null do
			//then compare each of these moves for the 
			//piece which it takes, then the one with 
			// the highest piece value will be taken.
			if (!trueList.isEmpty()){
				
				Move highestPiece=trueList.get(0);
				int numPieces = highestPiece.getTruth().getPiece().size();
				loop:
				for (Move m: trueList){
					if (m.getGameOver()){
						highestPiece = m;
						break loop;
					} else if (m.getTruth().getPiece().size() >= numPieces){
						highestPiece = m;
						numPieces = m.getTruth().getPiece().size();
					}
				}
				//convert the move objects parameters to basic types.
				if (highestPiece.getTruth().getTake()) {
					int x = highestPiece.getX();
					int y = highestPiece.getY();
					int i = highestPiece.getI();
					int j = highestPiece.getJ();

					Piece piece1 = highestPiece.getPiece();
					System.out.println(piece1.getChar());
					for(Piece p : highestPiece.getTruth().getPiece()){
						this.getOpponent().deletePiece(p);
						board.remove(p.getX(),p.getY());
					}
					
					board.setPosition(i, j, piece1);
					piece1.setPosition(i, j);
					board.remove(x,y);
					System.out.println(highestPiece.toString());
					return true;	
				} else {
					
					int x = highestPiece.getX();
					int y = highestPiece.getY();
					int i = highestPiece.getI();
					int j = highestPiece.getJ();
					
					Piece piece1 = highestPiece.getPiece();
					
					board.setPosition(i, j, piece1);
					piece1.setPosition(i, j);
					board.remove(x,y);
					System.out.println(highestPiece.toString());
					return true;
				}
								
			} 
			else {
				int randomMove = (int)(Math.random()*fullList.size());
				moveToConvert =fullList.get(randomMove);
				//convert the move objects parameters to basic types.
				int x = moveToConvert.getX();
				int y = moveToConvert.getY();
				int i = moveToConvert.getI();
				int j = moveToConvert.getJ();
				boolean b = moveToConvert.getTruth().getTake();
				Piece piece1 = moveToConvert.getPiece();

				
				if (b) {//true if there is an enemy player to take.
					for(Piece p : moveToConvert.getTruth().getPiece()){
						this.getOpponent().deletePiece(p);
						board.remove(p.getX(),p.getY());
					}
				}
				board.setPosition(i, j, piece1);
				piece1.setPosition(i, j);
				board.remove(x,y);
				System.out.println(moveToConvert.toString());
				return true;
			}
		} else {
			return true;
		}
		
	}
}
