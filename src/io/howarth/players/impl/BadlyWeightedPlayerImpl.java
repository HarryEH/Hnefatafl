package io.howarth.players.impl;

import io.howarth.Board;
import io.howarth.Hnefatafl;
import io.howarth.Move;
import io.howarth.analysis.Analysis;
import io.howarth.analysis.AnalysisBoard;
import io.howarth.analysis.GameStatus;
import io.howarth.pieces.Piece;
import io.howarth.pieces.Pieces;
import io.howarth.players.Player;

import java.util.ArrayList;



/**
 * BadlyWeightedPlayer.java 
 *
 * Concrete Class to be an AI player, makes Move object and
 * does the move.
 *
 * @version 1.0 10/2/17
 *
 * @author Harry Howarth 
 */
public class BadlyWeightedPlayerImpl extends Player {
	
	public BadlyWeightedPlayerImpl(String n, Pieces p, Board b, Player o) {
		super(n, p, b, o);
	}
	// This is random obviously
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
		
		if(!fullList.isEmpty()){

			// This will take a long time
			Move moveToConvert;
			if(fullList.get(0).getPiece().getColourChar()==Player.BLACK){
				moveToConvert = weightMoves(fullList, Player.BLACK);
			} else {
				moveToConvert = weightMoves(fullList, Player.WHITE);
			}
				
			
			//convert the move objects parameters to basic types.
			byte x = moveToConvert.getX();
			byte y = moveToConvert.getY();
			byte i = moveToConvert.getI();
			byte j = moveToConvert.getJ();
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
			
			return true;
		} else {
			return false;
		}
	}
	

	@SuppressWarnings("unused")
	private Move weightMoves(ArrayList<Move> mvs, byte thisColour){
		// First analysis board
		// Get next set, find most probable move continue unless its game winning
		// Get all your own set, take weights 
		// Get next set, find most probable move continue unless its game winning
		// Get all your own next set, take weights
		// store move with its weight
		// replace the last one with this one if the weight is higher
		
		byte zero = 0;
		Move returnM = new Move(null,zero,zero,zero,zero,null, false, -10000000);
		
		byte oppoColour =-1;
		
		if (thisColour == Player.BLACK){
			oppoColour = Player.WHITE;
		} else {
			oppoColour = Player.BLACK;
		}
		
		for(Move m : mvs ){
			try{
				/**
				 * 1
				 */
				AnalysisBoard orig = AnalysisBoard.convB(Hnefatafl.b);
				
				orig.remove(m.getX(), m.getY());
				
				if(m.getTruth().getTake()){
					orig.remove(m.getI(), m.getJ());
					m.setWeight(m.getWeight()+TAKE_PIECE);
				}
				
				byte whiteToCorner = Analysis.kingToCorner_James(orig.getData());
				
				if(thisColour == Player.BLACK){
					m.setWeight(m.getWeight()+(TAKE_AWAY - whiteToCorner)*3);
				} else {
					m.setWeight(m.getWeight()+Math.abs(whiteToCorner - TAKE_AWAY)*3);
				}
				
				
				if(m.getGameOver()){
					return m;
				}
				
				if(m.getWeight() > returnM.getWeight()){
					returnM = m;
				}
				
			} catch (Exception uhoh){
				uhoh.printStackTrace();
			}
		}

		if(returnM.getPiece()==null){
			return null;
		}
		
		return returnM;
	}
}
