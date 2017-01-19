package io.howarth.players;

import java.util.ArrayList;

import io.howarth.Board;
import io.howarth.Hnefatafl;
import io.howarth.Move;
import io.howarth.Piece;
import io.howarth.Pieces;
import io.howarth.Player;
import io.howarth.pieces.PieceCode;



/**
 * AggressivePlayer.java 
 *
 * Concrete Class to be an AI player, makes Move object and
 * does the move.
 *
 * @version 1.0 19/1/17
 *
 * @author Harry Howarth 
 */
public class MonteCarloPlayer extends Player {

	private String name;
	private Pieces pieces;
	
	public MonteCarloPlayer(String n, Pieces p, Board b, Player o) {
		super(n, p, b, o);
		this.name = n;
		this.pieces = p;
	}

	@Override
	public boolean makeMove() {
		if (Hnefatafl.b.getPiece(0,0) != null || Hnefatafl.b.getPiece(10,0) != null ||
				Hnefatafl.b.getPiece(0,10) != null || Hnefatafl.b.getPiece(10,10) != null ){
			return false;
		}
		
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

	// This is random atm
	@Override
	public boolean doMove() {
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
			//debug line
			//System.out.println(this.getName()+" moved from ("+x+","+y+") to ("+i+","+j+")");
			return true;
		 } else {
			 return true;
		 }
	}

}
