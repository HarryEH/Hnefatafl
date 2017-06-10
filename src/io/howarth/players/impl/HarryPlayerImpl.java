package io.howarth.players.impl;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import io.howarth.Board;
import io.howarth.Hnefatafl;
import io.howarth.TextHandler;
import io.howarth.analysis.Analysis;
import io.howarth.analysis.AnalysisBoard;
import io.howarth.analysis.MoveChecking;
import io.howarth.move.Move;
import io.howarth.move.PieceCoordinates;
import io.howarth.move.TakePiece;
import io.howarth.pieces.Piece;
import io.howarth.pieces.Pieces;
import io.howarth.players.Player;

/**
 * HarryPlayImpl.java 
 *
 * Concrete Class to be an AI player, makes Move object and
 * does the move.
 *
 * @version 1.0 25/5/17
 *
 * @author Harry Howarth 
 */
public class HarryPlayerImpl extends Player{
	
	private byte col;
	
	public HarryPlayerImpl(String n, Pieces p, Board b, Player o) {
		super(n, p, b, o);
		this.col = (byte)getPieces().getColour();
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
			
			Move moveToConvert = new Move(null,(byte)0,(byte)0,(byte)0,(byte)0);
			moveToConvert.setWeight(Short.MIN_VALUE);
			
			ArrayList<Move> weightedMoves = runSimulationsWhite(fullList);
			
			
			if(!weightedMoves.isEmpty()) {
				List<Move> bestFive = new ArrayList<Move>();
				
				System.out.println("pre sort: ");
				for(Move q : weightedMoves) {
					System.out.print(q.getWeight()+", ");
				}
				System.out.println("");
				
				Collections.sort(weightedMoves);// sort the moves
				
				System.out.println("post sort: ");
				for(Move q : weightedMoves) {
					System.out.print(q.getWeight()+", ");
				}
				System.out.println("");
				
				if(weightedMoves.size() >= 5){
					bestFive = weightedMoves.subList(0, 5);
				} else {
					bestFive = weightedMoves.subList(0, weightedMoves.size());
				}
				System.out.println("best five: ");
				for(Move q : bestFive) {
					System.out.print(q.getWeight()+", ");
				}
				System.out.println("");
				
				
				int SIMULATIONS = bestFive.size();
				
				ArrayList<Move> simulationMoves = new ArrayList<>();
				
				ExecutorService service = Executors.newFixedThreadPool(SIMULATIONS);
				List<MoveChecking> futureLis = new ArrayList<MoveChecking>();
				for(int i=0; i<SIMULATIONS ;i++){
					futureLis.add(new MoveChecking(bestFive, col, getBoard()));
				}
				
				try{
					List<Future<ArrayList<Move>>> futures = service.invokeAll(futureLis);
					
					for(Future<ArrayList<Move>> future :  futures){
						try{
							simulationMoves.addAll(future.get());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} catch (Exception e){
					
				}
				service.shutdown();
			
				ArrayList<Move> totalMoves = new ArrayList<>();
				
				System.out.println("Moves returned: "+ simulationMoves.size());
				
				for(Move m1 : simulationMoves){
					ArrayList<Move> matches = new ArrayList<>();
					if(!totalMoves.contains(m1)){
						for(Move m2 : simulationMoves){
							if(m1.equals(m2)){
								matches.add(m2);
							}
						}
						totalMoves.add(minMove(matches));
					}
				}
				
				System.out.println("Unique Moves: "+ totalMoves.size());
						
				if(totalMoves != null && totalMoves != null) {
					moveToConvert = totalMoves.get(0);
	
					for(Move m : totalMoves){
						if(m.getWeight() >= moveToConvert.getWeight()){
							moveToConvert = m;
						}
					}
					
					ArrayList<Move> maxMoves = new ArrayList<Move>();
					
					for(Move m : totalMoves){
						if(m.getWeight() >= moveToConvert.getWeight()){
							maxMoves.add(m);
						}
					}
					
					if(!maxMoves.isEmpty()){
						int randomMove = (int)(Math.random()*maxMoves.size());
						if(randomMove == maxMoves.size()){
							randomMove -= 1;
						}
						moveToConvert = maxMoves.get(randomMove);
					}
					
				
					
				}
				
			
			} else {
				int randomMove = (int)(Math.random()*fullList.size());
				
				moveToConvert =fullList.get(randomMove);
			}
			
			if(moveToConvert != null) {
				
				//convert the move objects parameters to basic types.
				byte x = moveToConvert.getX();
				byte y = moveToConvert.getY();
				byte i = moveToConvert.getI();
				byte j = moveToConvert.getJ();
				
				TakePiece tP = Analysis.analyseBoard(moveToConvert, board);
				
				Piece piece1 = moveToConvert.getPiece();
		
				if (tP.getTake()) {//true if there is an enemy player to take.
					for(PieceCoordinates p : tP.getPiece()){
						this.getOpponent().deletePiece(board.getPiece(p.getX(), p.getY()));
						board.remove(p.getX(),p.getY());
					}
				}
				board.setPosition(i, j, piece1);
				piece1.setPosition(i, j);
				board.remove(x,y);
				
				if(Hnefatafl.emitMove) {
					emitUdpMove(moveToConvert);
				} 
				
				return true;
			}		
		} else {
			return false;
		}
		return false;
	}
	
	private ArrayList<Move> runSimulationsWhite(ArrayList<Move> mvs) {
		
		ArrayList<Move> rtnMvs = new ArrayList<>();
		
		long a = System.nanoTime()/1000000 ;
		
		byte oppoColour =-1;
		
		if (col == Player.BLACK){
			oppoColour = Player.WHITE;
		} else {
			oppoColour = Player.BLACK;
		}
		
		for(Move m : mvs ) {
			
			try{
				/**
				 * 1
				 */
				AnalysisBoard orig = AnalysisBoard.convB(getBoard());
				
				TakePiece tP_1 = Analysis.analyseBoard(m, getBoard());
				
				short before_k = Analysis.kingToCorner(orig.getData());
				if(before_k == 0){
					before_k = 8;
				}
				
				if(tP_1.getTake()) {
					for(PieceCoordinates p1 : tP_1.getPiece()) {
						orig.remove(p1.getX(), p1.getY()); // do the first move
						m.setWeight((short) (m.getWeight() + TAKE_PIECE));
					}
				}
				
				// do the first move
				orig.remove(m.getX(),m.getY());
				orig.setPosition(m.getI(), m.getJ(), m.getPiece().getChar());
				
				short after_k = Analysis.kingToCorner(orig.getData());
				if(after_k == 0){
					after_k = 8;
				}
				short diff_k = (short) ((before_k - after_k)*2);
				
				System.out.println("KING DIFF: "+diff_k);
				
				m.setWeight((short)(m.getWeight()+diff_k));
				System.out.println("move before: "+m.getWeight());
				
				if(tP_1.getGameOver()) {
					m.setWeight(Short.MAX_VALUE);
					rtnMvs.add(m);
					return rtnMvs;
				}
				
				ArrayList<Move> mvs2 = Analysis.moves(orig, oppoColour, false);
				
				try{
					/**
					 * 2
					 */
					Move test = new Move(null, (byte)0,(byte)0,(byte)0,(byte)0);
					
					for(Move mv2 : mvs2) {
						TakePiece tP_2 = Analysis.analyseBoard(mv2, AnalysisBoard.convAB(orig));
						
						AnalysisBoard orig_copy = new AnalysisBoard(arrayCopy(orig.getData()));
						short before_c = Analysis.cornerCheck(orig_copy.getData(),(short)2);
						orig_copy.remove(mv2.getX(),mv2.getY());
						orig_copy.setPosition(mv2.getI(), mv2.getJ(), mv2.getPiece().getChar());
						
						if(tP_2.getTake()){
							for(PieceCoordinates p : tP_2.getPiece()){
								orig_copy.remove(p.getX(), p.getY());
								mv2.setWeight((short) (mv2.getWeight()+(TAKE_PIECE-1)));
							}
						}
						
						short after_c = Analysis.cornerCheck(orig_copy.getData(),(short)2);
						
						short diff_c= (short)(after_c - before_c);
						
						mv2.setWeight((short)(mv2.getWeight()+diff_c));
						
						if(tP_2.getGameOver()){
							mv2.setWeight(Short.MAX_VALUE);
						} 
						
						if(mv2.getWeight() >= test.getWeight()){
							test = mv2;
						}
					}
					
					m.setWeight((short)(m.getWeight() - test.getWeight()));
					System.out.println("move after: "+m.getWeight());
					
				} catch (NullPointerException e2) {
					System.out.println("NullPointer - 2 ahead");
					// do nothing its already skipped the block
				}
			} catch (NullPointerException e1) {
				System.out.println("NullPointer - 1 ahead");
				// do nothing its already skipped the block
			}
		
			rtnMvs.add(m);
		}
		
		long a1 = System.nanoTime()/1000000;
	
		System.out.println("Time taken: "+ (a1-a) +"ms");
		return rtnMvs;
	}
	
	private Move minMove(ArrayList<Move> mvs){
		if(mvs == null) return null;
		
		if(mvs.isEmpty()) return null;
		
		if(mvs.size()==1) return mvs.get(0);
		
		Move rtn = mvs.get(0);
		for(Move m : mvs){
			
			if(m.getWeight() == Short.MAX_VALUE) return m;
			
			if(m.getWeight() < rtn.getWeight()){
				rtn = m;
			}
		}
		
		return rtn;
	}
}
