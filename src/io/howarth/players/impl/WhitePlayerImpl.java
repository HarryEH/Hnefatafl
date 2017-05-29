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
import io.howarth.move.Move;
import io.howarth.move.MoveWeight;
import io.howarth.move.PieceCoordinates;
import io.howarth.move.TakePiece;
import io.howarth.pieces.Piece;
import io.howarth.pieces.Pieces;
import io.howarth.players.Player;



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
		
		//try { Thread.sleep(500); } catch (InterruptedException e) {}
		
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
			char[][] data = AnalysisBoard.convB(Hnefatafl.b).getData();
			
			//Search code here
			ArrayList<ArrayList<MoveWeight>> moveWeights = new ArrayList<>(fullList.size());
			
			ArrayList<MoveWeight> topLevel = weightWhiteMoves(fullList, data);
			
			for(MoveWeight first : topLevel) {
				
				ArrayList<MoveWeight> depth = new ArrayList<>(5);
				
				// Create analysis board
				AnalysisBoard b = new AnalysisBoard(first.getData());
				
				/**FIRST*/
				// Add the first MoveWeight to the 
				depth.add(0, first);	
				
				// Do the Move
				b.remove(first.getMove().getX(), first.getMove().getY());
				
				b.setPosition(first.getMove().getI(), first.getMove().getJ(), first.getMove().getPiece().getChar());

				if(first.getTakePiece().getTake()) {
					for(PieceCoordinates p : first.getTakePiece().getPiece()) {
						b.remove(p.getX(),p.getY());
					}
				}
				
				if(!first.getTakePiece().getGameOver() ){
					
					/**SECOND*/
					// Get the MoveWeights & Get the best MoveWeight
					MoveWeight second = maxMoveWeight(weightBlackMoves(Analysis.moves(b, Player.BLACK, false), b.getData()));
					
					
					if( second != null ){
						
						// Add the bet MoveWeight to the ArrayList
						depth.add(1, second);
						
						if(!second.getTakePiece().getGameOver() ) {
							// Do the Move
							b.remove(second.getMove().getX(), second.getMove().getY());
							
							b.setPosition(second.getMove().getI(), second.getMove().getJ(), second.getMove().getPiece().getChar());

							if(second.getTakePiece().getTake()) {
								for(PieceCoordinates p : second.getTakePiece().getPiece()) {
									b.remove(p.getX(),p.getY());
								}
							}
							
							/**THIRD*/
							// Get the MoveWeights & Get the best MoveWeight
							MoveWeight third = maxMoveWeight(weightWhiteMoves(Analysis.moves(b, Player.WHITE, false), b.getData()));
							
							if( third != null ){
								
								// Add the bet MoveWeight to the ArrayList
								depth.add(2, third);
								
								if( !third.getTakePiece().getGameOver() ) {
									// Do the Move
									b.remove(third.getMove().getX(), third.getMove().getY());
									
									b.setPosition(third.getMove().getI(), third.getMove().getJ(), third.getMove().getPiece().getChar());

									if(third.getTakePiece().getTake()) {
										for(PieceCoordinates p : third.getTakePiece().getPiece()) {
											b.remove(p.getX(),p.getY());
										}
									}
									/**FOURTH*/
									// Get the MoveWeights & Get the best MoveWeight
									MoveWeight fourth = maxMoveWeight(weightBlackMoves(Analysis.moves(b, Player.BLACK, false), b.getData()));
									
									if( fourth != null ) {
										
										// Add the bet MoveWeight to the ArrayList
										depth.add(3, fourth);
										
										if( !fourth.getTakePiece().getGameOver() ) {
											// Do the Move
											b.remove(fourth.getMove().getX(), fourth.getMove().getY());
											
											b.setPosition(fourth.getMove().getI(), fourth.getMove().getJ(), fourth.getMove().getPiece().getChar());

											if( fourth.getTakePiece().getTake() ) {
												for(PieceCoordinates p : fourth.getTakePiece().getPiece()) {
													b.remove(p.getX(),p.getY());
												}
											}
										} else { // if fourth game over
											System.out.println("Fourth move wins");
										}
									}// if fourth null
								} else { // if third game over
									System.out.println("Third move wins");
								}
							}// if third null
						} else { // if second game over
							System.out.println("Second move wins");
						}
					}// if second null
				} else {// if first game over
					System.out.println("First move wins");
				}
				moveWeights.add(depth);
			}
			Move moveToConvert;
			
			if( moveWeights.isEmpty() ){
				
				int randomMove = (int)(Math.random()*fullList.size());
				
				// This will take a long time
				moveToConvert  = fullList.get(randomMove);
				
			} else {
				
				boolean max = false;
				
				ArrayList<MoveWeight> finalLis = moveWeights.get(0);
				
				short weight = moveWeight(finalLis);
				
				for(ArrayList<MoveWeight> mws : moveWeights) {
					if(mws!=null && !mws.isEmpty()) {
						short temp = moveWeight(mws);
						if(weight < temp) {
							finalLis = mws;
							weight = temp;
							max = true;
						}
					}
				}
				
				moveToConvert = finalLis.get(0).getMove();
				
				if(!max) {
					// Do something else with moveToConvert here
					System.out.println("No max weight!!");
				}
			}
				
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
				try {
					
					DatagramSocket clientSocket = new DatagramSocket();
					InetAddress IPAddress = InetAddress.getByName(Hnefatafl.ip);
					
					byte[] sendData;
					
					String move = TextHandler.convertNumToLetter(y)+""+(10-x)+"-"+TextHandler.convertNumToLetter(j)+""+(10-i)+"<EOF>";
					sendData = move.getBytes();
					
					final int PORT = 12000;
					
					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, PORT);
					clientSocket.send(sendPacket);
					
					try { Thread.sleep(150); } catch (InterruptedException e) { }
					
					System.out.println("Message omitted: " + move);
					
					
					
					clientSocket.close();
					
				} catch (SocketException e) {
					e.printStackTrace();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} 
			
			return true;
		
			
		} else {
			return false;
		}
	}
	
}
