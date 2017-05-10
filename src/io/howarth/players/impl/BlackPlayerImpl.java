package io.howarth.players.impl;

import io.howarth.Board;
import io.howarth.move.Move;
import io.howarth.move.PieceCoordinates;
import io.howarth.pieces.Piece;
import io.howarth.pieces.Pieces;
import io.howarth.players.Player;

import java.util.ArrayList;



/***********************************************************
 * WhitePlayerImpl.java 
 *
 * Concrete Class to be an AI player, makes Move object and
 * does the move.
 *
 * @version 1.0 10/2/17
 *
 * @author Harry Howarth 
 **********************************************************/
public class BlackPlayerImpl extends Player {
	
	public BlackPlayerImpl(String n, Pieces p, Board b, Player o) {
		super(n, p, b, o);
	}
	
	@Override
	public boolean doMove() {
		System.out.println("white player");
	
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
			boolean b = moveToConvert.getTruth().getTake();
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
