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
			// Left in if you want to be more efficient.
//			if( m.getGameOver()){
//				return m;
//			}
			
			AnalysisBoard orig = AnalysisBoard.convB(Hnefatafl.b);
			
			
			ArrayList<GameStatus> moves = getMoves(orig, m, Player.BLACK);
			
			Analysis.cornerAccess(orig.getData());
			
			m.setFutureMoves(moves);
			
			for(GameStatus mW_1 : m.getFutureMoves()) {
				
				// Do the move
				Analysis.cornerAccess(mW_1.getBoard().getData());
				
				ArrayList<GameStatus> movesW_1 = getMoves(mW_1.getBoard(), m, Player.WHITE);
				
				mW_1.getMove().setFutureMoves(movesW_1);
				
				for(GameStatus mW_2 : m.getFutureMoves()){
					Analysis.cornerAccess(mW_2.getBoard().getData());
				}
				
			}
			
			moveWeight.add(new MoveWeight(m, (short)0));
			
		}
		

//		int sum = 0;
//		for(MoveWeight mw : moveWeight){
//			sum++;
//			if(mw.getMove().getFutureMoves() != null){
//				for(GameStatus m_q : mw.getMove().getFutureMoves()) {
//					sum++;
//					if(m_q.getMove().getFutureMoves() != null){
//						sum += m_q.getMove().getFutureMoves().size();
//					}
//				}
//				
//			}
//		}
//		
//		System.out.println(sum+ " LOLOL");
		
		// Add in code to return a random move
		int randomMove = (int)(Math.random()*mvs.size());
		
		return mvs.get(randomMove);
		
//		byte zero = 0;
//		
//		Move returnM = new Move(null,zero,zero,zero,zero,null, false);
//		
//		byte oppoColour = Player.BLACK; // Opposition's Piece colour
//		
//		System.out.println("Number of moves to analyse: "+mvs.size());
//		
//		for(Move m : mvs ){
//			try{
//				// If you can win the game, do it.
//				if(m.getGameOver()){
//					return m;
//				}
//				
//				AnalysisBoard orig = AnalysisBoard.convB(Hnefatafl.b);
//				
//				orig.remove(m.getX(), m.getY());
//				
//				if(m.getTruth().getTake()){
//					orig.remove(m.getI(), m.getJ());
//					for(PieceCoordinates p : m.getTruth().getPiece()){
////						System.out.println("Added take: "+TAKE_PIECE);
////						m.setWeight((short) (m.getWeight()+TAKE_PIECE));
//					} 
//				}
//				orig.setPosition(m.getI(), m.getJ(), m.getPiece().getChar());
//				
//				byte whiteToCorner = Analysis.kingToCorner(orig.getData());
//				
//				if(thisColour == Player.BLACK){
////					System.out.println("Added corner bl: "+(whiteToCorner*3));
//					if(whiteToCorner == 0){
////						m.setWeight((short) (m.getWeight()+(TAKE_AWAY*3)));
//					} else {
////						m.setWeight((short) (m.getWeight()+(whiteToCorner*3)));
//					}
//				} else {
//					if(whiteToCorner != 0){
////						m.setWeight((short) (m.getWeight()+Math.abs(whiteToCorner - TAKE_AWAY)*3));
//					}
//				}
//				
//				
//				// TODO go to the next players move and analyze that next
////				if(m.getWeight() > returnM.getWeight()){
////					returnM = m;
////				}
//				
//			} catch (Exception uhoh){
//				uhoh.printStackTrace();
//			}
//		}
//
//		if(returnM.getPiece()==null){
//			return null;
//		}
		
//		if(returnM.getWeight() == 0){
//			int randomMove = (int)(Math.random()*mvs.size());
			
//			return mvs.get(randomMove);
//		}
		
//		return returnM;
	}
	
	private ArrayList<GameStatus> getMoves(AnalysisBoard board, Move m, byte col){
		board.remove(m.getX(), m.getY());
		
		if(m.getTruth().getTake()){
			board.remove(m.getI(), m.getJ());
		}
		
		board.setPosition(m.getI(), m.getJ(), m.getPiece().getChar());
		
		return Analysis.moves(board, col, false);
	}
}
