package io.howarth.players;

import io.howarth.Board;
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
					//if it can take a piece add to trueList
					if (m.getTruth()==true){
						trueList.add(m);
					}
				}
			}
		}
		Move moveToConvert =null;
		//if the new ArrayList isn't null do
		//then compare each of these moves for the 
		//piece which it takes, then the one with 
		// the highest piece value will be taken.
		boolean truthTest =false;
		if (!trueList.isEmpty()){
			Move highestPiece=null;
			int num = PieceCode.charToInt(this.getBoard().getPiece(trueList.get(0).getI(),trueList.get(0).getJ()).getChar());
			for (Move m: trueList){
				if (num <= (PieceCode.charToInt(this.getBoard().getPiece(m.getI(),m.getJ()).getChar()))){
					num = (PieceCode.charToInt(this.getBoard().getPiece(m.getI(),m.getJ()).getChar()));
					highestPiece = m;
				}
			}
			//convert the move objects parameters to basic types.
				int x = highestPiece.getX();
				int y = highestPiece.getY();
				int i = highestPiece.getI();
				int j = highestPiece.getJ();

				Piece piece1 = highestPiece.getPiece();

				this.getOpponent().deletePiece(board.getPiece(i, j));
				board.remove(i,j);
				board.setPosition(i, j, piece1);
				piece1.setPosition(i, j);
				board.remove(x,y);
				
				//debug line
				//System.out.println(this.getName()+" moved from ("+x+","+y+") to ("+i+","+j+") true");	
				
				truthTest =true;				
		} 
		else {
			int randomMove = (int)(Math.random()*fullList.size());
			moveToConvert =fullList.get(randomMove);
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
			//System.out.println(this.getName()+" moved from ("+x+","+y+") to ("+i+","+j+")"+b);
			truthTest = true;
		}
		
	return truthTest;
	}
}
