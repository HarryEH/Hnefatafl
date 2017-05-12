package io.howarth.players.impl;

import io.howarth.Board;
import io.howarth.Hnefatafl;
import io.howarth.analysis.Analysis;
import io.howarth.analysis.AnalysisBoard;
import io.howarth.analysis.GameStatus;
import io.howarth.move.Move;
import io.howarth.move.MoveWeight;
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
public class WhitePlayerImpl extends Player {
	
	public WhitePlayerImpl(String n, Pieces p, Board b, Player o) {
		super(n, p, b, o);
	}
	
	@Override
	public boolean doMove() {
		
		Board board = this.getBoard();
		ArrayList<Move> fullList = new ArrayList<Move>();

		Pieces pieces = this.getPieces();
		ArrayList<Piece> pieceList = pieces.getData();

		for (Piece p: pieceList){
			//long a = System.nanoTime();
			ArrayList<Move> instance = p.availableMoves();
			//long b = System.nanoTime();
			if (instance != null){
				//System.out.println("Time taken to find "+ p.availableMoves().size()+" available moves: "+ (b-a)/1000000.0 + " ms");
				fullList.addAll(instance);
			} else {
				//System.out.println("Time taken to find 0 available moves: "+ (b-a)/1000000.0 + " ms");
			}
		}
		
		if(!fullList.isEmpty()){
			// This will take a long time
			Move moveToConvert  = weightMoves(fullList, Player.WHITE);
				
			//convert the move objects parameters to basic types.
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
	

	private Move weightMoves(ArrayList<Move> mvs, byte thisColour){
		
		// Don't bother doing anything else if you can win 
		for(Move m : mvs) {
			if( m.getGameOver()){
				return m;
			}
		}
		
		// Convert to List of MoveWeight objects
		ArrayList<MoveWeight> moveWeight = new ArrayList<>(mvs.size());
		
		for(Move m : mvs) {
			
			AnalysisBoard orig = AnalysisBoard.convB(Hnefatafl.b);
			
			ArrayList<GameStatus> moves = getFutureMoves(orig, m, Player.BLACK);
			
			
			m.setFutureMoves(moves);
			
			for(GameStatus mW_1 : m.getFutureMoves()) {
				
				
				ArrayList<GameStatus> movesW_1 = getFutureMoves(mW_1.getBoard(), m, Player.WHITE);
				
				mW_1.getMove().setFutureMoves(movesW_1);
				
				for(GameStatus mW_2 : movesW_1) {
					// Need to decide what to do with the 3rd depth moves here
				}
			}
			
			moveWeight.add( new MoveWeight(m, (short)0) );
			
		}
		
//		int levelOne   = moveWeight.size();
//		int levelTwo   = 0;
//		int levelThree = 0;
//		
//		for(MoveWeight mw : moveWeight) {
//			if(mw.getMove().getFutureMoves() != null) {
//				levelTwo += mw.getMove().getFutureMoves().size();
//				for(GameStatus m_q : mw.getMove().getFutureMoves()) {
//					if(m_q.getMove().getFutureMoves() != null) {
//						levelThree += m_q.getMove().getFutureMoves().size();
//					}
//				}
//				
//			}
//		}
		
//		System.out.println("Number of moves calculated at level 1: " + levelOne);
//		System.out.println("Number of moves calculated at level 2: " + levelTwo);
//		System.out.println("Branching factor 1-2: " + (levelTwo/(double)levelOne) );
//		System.out.println("Number of moves calculated at level 3: " + levelThree);
//		System.out.println("Branching factor 2-3: " + (levelThree/(double)levelTwo) );
//		System.out.println("Total number of moves calculated: "+ (levelOne + levelTwo +levelThree));
		
		int randomMove = (int)(Math.random()*mvs.size());
		
		return mvs.get(randomMove);
		
	}
	
	private ArrayList<GameStatus> getFutureMoves(AnalysisBoard board, Move m, byte col){
		board.remove(m.getX(), m.getY());
		
		if(m.getTruth() != null && m.getTruth().getTake()){
			board.remove(m.getI(), m.getJ());
		}
		
		board.setPosition(m.getI(), m.getJ(), m.getPiece().getChar());
		
		return Analysis.moves(board, col, false);
	}
	
}
