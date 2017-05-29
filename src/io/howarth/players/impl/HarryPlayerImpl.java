package io.howarth.players.impl;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

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
	
	private final byte      START_mc      = 0;
	private final short     WIN_mc        = 300;
	private final byte      TAKE_PIECE_mc = 10;
	private final byte      LOSE_PIECE_mc = 10;
	private byte            col;
	
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
			
			System.out.println("Total Moves: "+fullList.size());
			
			Move moveToConvert = null;
			
			
			ArrayList<Move> weightedMoves = runSimulationsWhite(fullList);
			
			
			System.out.println(weightedMoves.size()+" weighted Moves");
			
			ArrayList<Move> totalMoves = new ArrayList<>();
			for(Move m1 : weightedMoves){
				ArrayList<Move> matches = new ArrayList<>();
				if(!totalMoves.contains(m1)){
					for(Move m2 : weightedMoves){
						if(m1.equals(m2)){
							matches.add(m2);
						}
					}
					totalMoves.add(minMove(matches));
				}
			}
			
			System.out.println(totalMoves.size()+" unique minimum Moves");
						
			if(totalMoves != null && totalMoves != null) {
				moveToConvert = totalMoves.get(0);
				
				System.out.println("initial weight: "+moveToConvert.getWeight());
				
				for(Move m : totalMoves){
					if(m.getWeight() >= moveToConvert.getWeight()){
						moveToConvert = m;
					}
				}
				
				System.out.println("comparison weight: "+moveToConvert.getWeight());
				
				ArrayList<Move> maxMoves = new ArrayList<Move>();
				
				for(Move m : totalMoves){
					if(m.getWeight() >= moveToConvert.getWeight()){
						maxMoves.add(m);
					}
				}
				
				if(!maxMoves.isEmpty()){
					System.out.println("Number to choose from: "+maxMoves.size());
					int randomMove = (int)(Math.random()*maxMoves.size());
					if(randomMove == maxMoves.size()){
						randomMove -= 1;
					}
					moveToConvert = maxMoves.get(randomMove);
				}
				
				
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
					try {
						
						DatagramSocket clientSocket = new DatagramSocket();
						InetAddress IPAddress = InetAddress.getByName(Hnefatafl.ip);
						
						byte[] sendData;
						
						String move = TextHandler.convertNumToLetter(y)+""+(10-x)+"-"+TextHandler.convertNumToLetter(j)+""+(10-i)+"<EOF>";
						sendData = move.getBytes();
						
						int PORT = 11000;
						
						if(getPieces().getColour() == WHITE){
							PORT = 12000;
						}
						
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
		} else {
			return false;
		}
	}
	
	private ArrayList<Move> runSimulationsWhite(ArrayList<Move> mvs) {
		
		
		ArrayList<Move> rtnMvs = new ArrayList<>();
		long startTime = 0;
		
		for(short i=0; i<10000 ; i++){
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
					AnalysisBoard orig = AnalysisBoard.convB(Hnefatafl.b);
					
					m.setWeight((short)0);
					
					orig.remove(m.getX(), m.getY());
					orig.setPosition(m.getI(), m.getJ(), m.getPiece().getChar());
					
					TakePiece tP_1 = Analysis.analyseBoard(m, Hnefatafl.b);
					
					if(tP_1.getTake()) {
						for(PieceCoordinates p1 : tP_1.getPiece()) {
							orig.remove(p1.getX(), p1.getY());
							m.setWeight((short)(m.getWeight()+TAKE_PIECE_mc));
						}
					}
					
					if(tP_1.getGameOver()) {
						m.setWeight(Short.MAX_VALUE);
						rtnMvs.add(m);
						return rtnMvs;
					}
					
					if(Hnefatafl.moveNum > 43) {
						short kingToCorner = Analysis.kingToCorner(orig.getData());
						
						
						if(kingToCorner == 0 ) {
							kingToCorner = 15;
						}
						
						m.setWeight( (short) (m.getWeight() + ((15-kingToCorner))) );
					}
					
					
					ArrayList<GameStatus> gs = Analysis.gameStatus(orig, oppoColour, false);
					
					GameStatus mostLikely1 = new GameStatus(null,null);
					
					mostLikely1.setWeight((short)-1);
					
					try{
						/**
						 * 2
						 */
						boolean cont = true;
						cont_loop:
						for(GameStatus g : gs){
							TakePiece tP_2 = Analysis.analyseBoard(g.getMove(), AnalysisBoard.convAB(g.getBoard()));
							cont = tP_2.getGameOver();
							if(cont){
								m.setWeight(Short.MIN_VALUE);
								System.out.println("You're going to fucking lose");
								break cont_loop;
							}
						}
						
						if(!cont) {
							int randomIndex = (int) (Math.random()*gs.size());
							if(randomIndex == gs.size()){
								randomIndex -= 1;
							}
							mostLikely1 = gs.get(randomIndex);
							
							TakePiece tP_2 = Analysis.analyseBoard(mostLikely1.getMove(), AnalysisBoard.convAB(mostLikely1.getBoard()));
							mostLikely1.setTakePiece(tP_2);
							short weight = START_mc;
							
							if(tP_2.getGameOver()){
								weight += WIN_mc;
							}
							
							mostLikely1.getBoard().remove(mostLikely1.getMove().getX(), mostLikely1.getMove().getY());
							mostLikely1.getBoard().setPosition(mostLikely1.getMove().getI(), mostLikely1.getMove().getJ(), mostLikely1.getMove().getPiece().getChar());
							
							if(tP_2.getTake()){
								for(PieceCoordinates p : tP_2.getPiece()){
									weight+=LOSE_PIECE_mc;
									mostLikely1.getBoard().remove(p.getX(),p.getY());
								}
							}
							
							m.setWeight((short)(m.getWeight()- weight));
							
							ArrayList<GameStatus> gs1 = Analysis.gameStatus(mostLikely1.getBoard(), col, false);
							
							GameStatus mostLikely2 = new GameStatus(null,null);
							
							mostLikely2.setWeight((short)-1);
							
							try{
								/**
								 * 3
								 */
								
								for(GameStatus g : gs1 ){
									
									weight = START_mc;
									
									TakePiece tP_3 = Analysis.analyseBoard(g.getMove(), AnalysisBoard.convAB(g.getBoard()));
									g.setTakePiece(tP_3);
									if(tP_3.getGameOver()){
										weight += WIN_mc;
									}
									
									g.getBoard().remove(g.getMove().getX(), g.getMove().getY());
									g.getBoard().setPosition(g.getMove().getI(), g.getMove().getJ(), g.getMove().getPiece().getChar());
									
									if(tP_3.getTake()) {
										for(PieceCoordinates p : tP_3.getPiece()){
											weight+=(TAKE_PIECE_mc);
											g.getBoard().remove(p.getX(),p.getY());
										}
									}
									
									
									g.setWeight(weight);
									
									if(mostLikely2.getWeight() < g.getWeight()){
										mostLikely2 = g;
									}	
								}
								
								m.setWeight((short)(m.getWeight() + (0.5*mostLikely2.getWeight())));
															
								ArrayList<GameStatus> gs2 = Analysis.gameStatus(mostLikely2.getBoard(), oppoColour, false);
					
								GameStatus mostLikely3 = new GameStatus(null,null);
								
								mostLikely3.setWeight((short)-1);
								
								try {
									/**
									 * 4
									 */
									
									boolean cont_2 = true;
									cont_loop_2:
									for(GameStatus g : gs2) {
										TakePiece tP_4 = Analysis.analyseBoard(g.getMove(), AnalysisBoard.convAB(g.getBoard()));
										cont_2 = tP_4.getGameOver();
										if(cont_2){
											m.setWeight(Short.MIN_VALUE);
											System.out.println("You're going to fucking lose 2");
											break cont_loop_2;
										}
									}
									
									if( !cont_2 ) {
										randomIndex = (int) (Math.random()*gs2.size());
										if(randomIndex == gs2.size()) {
											randomIndex -= 1;
										}
										mostLikely3 = gs2.get(randomIndex);
										
										TakePiece tP_4 = Analysis.analyseBoard(mostLikely3.getMove(), AnalysisBoard.convAB(mostLikely3.getBoard()));
										
										mostLikely3.setTakePiece(tP_4);
										
										weight = START_mc;
										
										if(tP_4.getGameOver()) {
											weight += WIN_mc;
										}
										
										
										if(tP_4.getTake()) {
											for(PieceCoordinates p : tP_4.getPiece()) {
												weight+=(LOSE_PIECE_mc);
											}
										}
										
										mostLikely3.setWeight(weight);	
										
										gs.clear();
										gs.add(mostLikely3);
										m.setWeight((short)(m.getWeight()-(0.5*mostLikely3.getWeight())));
									}
									
								} catch (NullPointerException e4) {
									System.out.println("NullPointer - 4 ahead");
									// do nothing its already skipped the block
								}

							} catch (NullPointerException e3) {
								System.out.println("NullPointer - 3 ahead");
								// do nothing its already skipped the block
								m.setWeight((short)(m.getWeight() - (0.5*WIN_mc)));
							}
						}
						
					} catch (NullPointerException e2) {
						System.out.println("NullPointer - 2 ahead");
						// do nothing its already skipped the block
						m.setWeight((short)(m.getWeight() + WIN_mc));
					}
				} catch (NullPointerException e1) {
					System.out.println("NullPointer - 1 ahead");
					// do nothing its already skipped the block
				}
			
				rtnMvs.add(m);
			}
			
			long a1 = System.nanoTime()/1000000;
			startTime += (a1-a);
			
			if(startTime >= 8200) {
				System.out.println("Simulations done: "+i);
				return rtnMvs;
			}
			
		}
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
	
//	private Move endGame(ArrayList<Move> mvs) {
//		
//		byte oppoColour =-1;
//		
//		if (col == Player.BLACK){
//			oppoColour = Player.WHITE;
//		} else {
//			oppoColour = Player.BLACK;
//		}
//		
//		for(Move m : mvs ) {
//			// Weight the moves in here
//			m.setWeight((short)0);
//			TakePiece p = Analysis.analyseBoard(m, getBoard());
//			
//			if(p.getGameOver()){
//				return m;
//			}
//			
//			AnalysisBoard bd = AnalysisBoard.convB(getBoard());
//			
//			System.out.println("Before");
//			
//			for(char[] pcs : bd.getData()){
//				System.out.println(Arrays.toString(pcs));
//			}
//			
//			short kingToCorner = Analysis.kingToCorner(bd.getData());
//			
//			System.out.println("After");
//			
//			bd.remove(m.getX(), m.getY());
//			bd.setPosition(m.getI(), m.getJ(), m.getPiece().getChar());
//			
//			if(p.getTake()){
//				for(PieceCoordinates pC : p.getPiece()){
//					bd.remove(pC.getX(), pC.getY());
//					m.setWeight((short)(m.getWeight()+ TAKE_PIECE_mc));
//				}
//			}
//			
//			kingToCorner -= Analysis.kingToCorner(bd.getData());
//			
//			m.setWeight((short)(m.getWeight()+ kingToCorner*10));
//			
//			System.out.println("King To Corner difference: "+kingToCorner);
//			
//			for(char[] pcs : bd.getData()){
//				System.out.println(Arrays.toString(pcs));
//			}
//			
//			ArrayList<GameStatus> gs = Analysis.gameStatus(bd, oppoColour, false);
//			
//			for(GameStatus g : gs) {
//				
//				Board b = AnalysisBoard.convAB(g.getBoard());
//				
//				TakePiece p1 = Analysis.analyseBoard(g.getMove(), b);
//				
//				if(p1.getGameOver()){
//					m.setWeight(Short.MIN_VALUE);
//				}	
//			}
//			
//		}
//		
//		return maxMove(mvs);
//	}
}
