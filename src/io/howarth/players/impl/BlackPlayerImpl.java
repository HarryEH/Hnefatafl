package io.howarth.players.impl;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import io.howarth.Board;
import io.howarth.Hnefatafl;
import io.howarth.TextHandler;
import io.howarth.analysis.Analysis;
import io.howarth.analysis.AnalysisBoard;
import io.howarth.analysis.GameStatus;
import io.howarth.move.Move;
import io.howarth.move.PieceCoordinates;
import io.howarth.move.TakePiece;
import io.howarth.pieces.Piece;
import io.howarth.pieces.Pieces;
import io.howarth.players.Player;



/***********************************************************
 * BlackPlayerImpl.java 
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
	
	@SuppressWarnings("unused")
	@Override
	public boolean doMove() {
		
		try { Thread.sleep(500); } catch (InterruptedException e) {} // This sleep should be removed
		
		Board board = this.getBoard();
		ArrayList<Move> fullList = new ArrayList<Move>();

		Pieces pieces = this.getPieces();
		ArrayList<Piece> pieceList = pieces.getData();

		for (Piece p: pieceList){
			ArrayList<Move> instance = p.availableMoves();
			if (instance != null){ // otherwise we don't want to do anything
				fullList.addAll(instance);
			}
		}
		
		if(!fullList.isEmpty()){ // We only care if there are moves for us to make
			
			final short TAKE = 5;
			final short LOSE = 101;
			
			long timer = System.nanoTime()/1000000;
			boolean time_f = false;
			
			full_loop:
			for (Move first : fullList) {
				if(System.nanoTime()/1000000 - timer > 8500){
					time_f = true;
					break full_loop;
				}				
				// Create analysis board
				AnalysisBoard b = AnalysisBoard.convB(getBoard());
				
				TakePiece pc = Analysis.analyseBoard(first, getBoard());
				
				short before_c = Analysis.cornerCheck(b.getData(),(short)10);
				
				// Do the Move
				b.remove(first.getX(), first.getY());
				
				b.setPosition(first.getI(), first.getJ(), first.getPiece().getChar());
				
				if(pc.getTake()) {
					for(PieceCoordinates p : pc.getPiece()) {
						b.remove(p.getX(),p.getY());
						first.setWeight((short)(first.getWeight()+TAKE));
					}
				}
				
				short after_c = Analysis.cornerCheck(b.getData(),(short)10);
				//short after_k = Analysis.kingToCorner(b.getData());
//				if(after_k == 0){
//					after_k = 8;
//				}
				short diff_c = (short) (after_c - before_c);
				//short diff_k = (short) (((8-after_k)*100));
				
				first.setWeight( (short) (first.getWeight()+diff_c) );
				
				if(!pc.getGameOver()) {
					if(System.nanoTime()/1000000 - timer > 8000){
						time_f = true;
						break full_loop;
					}
					/**SECOND*/
					// Get the MoveWeights & Get the best MoveWeight
					ArrayList<GameStatus> secondDepth = Analysis.gameStatus(b, Player.WHITE, false);
					
					if( secondDepth != null ){
						
						if( !secondDepth.isEmpty()) {
							GameStatus first_gs = secondDepth.get(0);
							//Weight the first move
							
							first_gs.setWeight((short)0);
							
							TakePiece pc_2 = Analysis.analyseBoard(first_gs.getMove(), AnalysisBoard.convAB(first_gs.getBoard()));
							
							if(pc_2.getGameOver()){
								first_gs.setWeight((short)15000);
							}
							
							if(pc_2.getTake()){
								for (PieceCoordinates pcs :pc_2.getPiece()) {
									first_gs.setWeight((short) (first_gs.getWeight()+LOSE));
								}
							}
							TakePiece todo = pc_2;
							// Go over every second move
							
							for(GameStatus gs : secondDepth.subList(1, secondDepth.size()-1)) {
								if(System.nanoTime()/1000000 - timer > 8000){
									time_f = true;
									break full_loop;
								}
								// Check shit in here
								pc_2 = Analysis.analyseBoard(gs.getMove(), AnalysisBoard.convAB(gs.getBoard()));
								gs.setWeight((short)0);
								
								if(pc_2.getGameOver()){
									gs.setWeight((short)15000);
								}
								
								if(pc_2.getTake()){
									for (PieceCoordinates pcs :pc_2.getPiece()) {
										gs.setWeight((short) (gs.getWeight()+LOSE));
									}
								}
								
								if(gs.getWeight() > first_gs.getWeight()){
									first_gs = gs;
									todo     = pc_2;
								}
							}
							
							//Take first_gs away from second
							first.setWeight( (short) (first.getWeight() - first_gs.getWeight()) );
							
							// Do the first_gs move
							// first_gs & todo
							if(!todo.getGameOver()) {
								
								first_gs.getBoard().remove(first_gs.getMove().getX(), first_gs.getMove().getY());
								first_gs.getBoard().setPosition(first_gs.getMove().getI(), first_gs.getMove().getJ(), first_gs.getMove().getPiece().getChar());
								
								
								if(todo.getTake()) {
									for(PieceCoordinates pcc : todo.getPiece()){
										first_gs.getBoard().remove(pcc.getX(), pcc.getY());
									}
								}
								
								if((System.nanoTime()/1000000)-timer < 8000) {
									time_f = false;
									// Generate all of the three deep moves
									ArrayList<GameStatus> thirdDepth = Analysis.gameStatus(first_gs.getBoard(), BLACK, false);
									if( thirdDepth != null ) {
										
										if( !thirdDepth.isEmpty()) {
											GameStatus second_gs = secondDepth.get(0);
											// Weight the first move
											
											second_gs.setWeight((short)0);
											
											TakePiece pc_3 = Analysis.analyseBoard(second_gs.getMove(), AnalysisBoard.convAB(second_gs.getBoard()));
											
											if(pc_3.getGameOver()){
												second_gs.setWeight((short)15000);
											}
											
											before_c = Analysis.cornerCheck(second_gs.getBoard().getData(),(short)10);
//											before_k = Analysis.kingToCorner(second_gs.getBoard().getData());
											
											
											second_gs.getBoard().remove(second_gs.getMove().getX(), second_gs.getMove().getY());
											second_gs.getBoard().setPosition(second_gs.getMove().getI(), second_gs.getMove().getJ(), second_gs.getMove().getPiece().getChar());
											
											if(pc_3.getTake()){
												for(PieceCoordinates pcs : pc_3.getPiece()) {
													second_gs.getBoard().remove(pcs.getX(), pcs.getY());
												}
											}
											
											after_c = Analysis.cornerCheck(second_gs.getBoard().getData(),(short)10);
//											after_k = Analysis.kingToCorner(second_gs.getBoard().getData());
											
											diff_c = (short) (after_c - before_c);
//											diff_k = (short) ((after_k - before_k)*100);
											
											second_gs.setWeight((short) (second_gs.getWeight() + diff_c) );
											
											TakePiece todo2 = pc_3;
											// Go over every second move
											for(GameStatus gs : thirdDepth.subList(1, thirdDepth.size()-1)){
												if(System.nanoTime()/1000000 - timer > 8000){
													time_f = true;
													break full_loop;
												}
												// Check shit in here
												pc_3 = Analysis.analyseBoard(gs.getMove(), AnalysisBoard.convAB(gs.getBoard()));
												gs.setWeight((short)0);
												
												before_c = Analysis.cornerCheck(gs.getBoard().getData(),(short)10);
//												before_k = Analysis.kingToCorner(gs.getBoard().getData());
//												if(before_k == 0){
//													before_k = 11;
//												}
												
												if(pc_3.getGameOver()){
													gs.setWeight((short)15000);
												}
												
												gs.getBoard().remove(gs.getMove().getX(), gs.getMove().getY());
												gs.getBoard().setPosition(gs.getMove().getI(), gs.getMove().getJ(), gs.getMove().getPiece().getChar());
												
												if(pc_3.getTake()){
													for(PieceCoordinates pcs : pc_3.getPiece()) {
														gs.getBoard().remove(pcs.getX(), pcs.getY());
													}
												}
												
												after_c = Analysis.cornerCheck(gs.getBoard().getData(),(short)10);
//												after_k = Analysis.kingToCorner(gs.getBoard().getData());
//												if(after_k == 0){
//													after_k = 11;
//												}
												diff_c = (short) (after_c - before_c);
//												diff_k = (short) ((after_k - before_k)*100);
												
												gs.setWeight((short) (gs.getWeight() + diff_c) );
												
												if(gs.getWeight() > second_gs.getWeight()){
													second_gs = gs;
													todo2     = pc_3;
												}
											}//for loop third
											// Add second weight on
											first.setWeight( (short) (first.getWeight() + 0.9*second_gs.getWeight()) );
										}// third depth empty
									}// thirdDepth null
								} else {
									time_f = true;
								}
								
							}// third game over
						} // end of check if secondDepth is empty						
					}// if secondDepth null
				} else {// if first game over
					first.setWeight(Short.MAX_VALUE); // Won the game
				}
				
				long timer2 = (System.nanoTime()/1000000) - timer;
//				System.out.println("Time taken so far: "+timer2);
				if(timer2 >= 8500 || time_f){
					break full_loop;
				}
			}
			
			Move moveToConvert = maxMove(fullList);
			
			//convert the move objects parameters to basic types.
			byte x = moveToConvert.getX();
			byte y = moveToConvert.getY();
			byte i = moveToConvert.getI();
			byte j = moveToConvert.getJ();
			
			TakePiece q = Analysis.analyseBoard(moveToConvert, getBoard());
			
			Piece piece1 = moveToConvert.getPiece();
				
			if (q.getTake()) {//true if there is an enemy player to take.
				for(PieceCoordinates p : q.getPiece()){
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
		} else {
			return false;
		}
	}
	
}
