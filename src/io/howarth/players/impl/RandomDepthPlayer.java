package io.howarth.players.impl;

import io.howarth.Board;
import io.howarth.Hnefatafl;
import io.howarth.Move;
import io.howarth.analysis.Analysis;
import io.howarth.analysis.AnalysisBoard;
import io.howarth.analysis.GameStatus;
import io.howarth.pieces.Piece;
import io.howarth.pieces.PieceCode;
import io.howarth.pieces.Pieces;
import io.howarth.players.Player;

import java.util.ArrayList;



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
public class RandomDepthPlayer extends Player {

	private String name;
	private Pieces pieces;
	
	public RandomDepthPlayer(String n, Pieces p, Board b, Player o) {
		super(n, p, b, o);
		this.name = n;
		this.pieces = p;
	}

	@Override
	public boolean makeMove() {
		
		byte zero = 0;
		byte ten  = 10;
		
		if (Hnefatafl.b.getPiece(zero,zero) != null || Hnefatafl.b.getPiece(ten,zero) != null ||
				Hnefatafl.b.getPiece(zero,ten) != null || Hnefatafl.b.getPiece(ten,ten) != null ){
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
			
			final short SIMULATIONS = 10000;
			// This will take a long time
			ArrayList<Move> simulationMoves = new ArrayList<>();
			byte currentColour = fullList.get(0).getPiece().getColour();
			long overallTime = 0;
			loop:
			for(int i=0; i<SIMULATIONS ;i++){
				if(overallTime < 9500){
					long a = System.nanoTime();
					if(currentColour==Player.BLACK){
						simulationMoves.add(weightMoves(fullList, Player.BLACK));
					} else {
						simulationMoves.add(weightMoves(fullList, Player.WHITE));
					}
					long a1 = System.nanoTime();
					overallTime += (a1 - a)/1000000.0;
				} else {
					System.out.println("Breaking the loop, more than 10seconds");
					break loop;
				}
			}
			
			
			Move moveToConvert;
			if(simulationMoves != null && !simulationMoves.isEmpty()){
				System.out.println("Moves checked :"+ simulationMoves.size());
				moveToConvert = simulationMoves.get(0);
				for(Move m : simulationMoves){
					if(m.getWeight() > moveToConvert.getWeight()){
						moveToConvert = m;
					}
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
		} else {
			return false;
		}
	}
	
	//Move weights
	private final byte START        = 1;
	private final short WIN         = 30000;
	private final byte TAKE_PIECE   = 120;
	private final byte LOSE_PIECE   = 75;
	

	private Move weightMoves(ArrayList<Move> mvs, int thisColour){
		// First analysis board
		// Get next set, find most probable move continue unless its game winning
		// Get all your own set, take weights 
		// Get next set, find most probable move continue unless its game winning
		// Get all your own next set, take weights
		// store move with its weight
		// replace the last one with this one if the weight is higher
		
		byte zero = 0;
		Move returnM = new Move(null,zero,zero,zero,zero,null, false, -10000000);
		
		int oppoColour =-1;
		
		if (thisColour == Player.BLACK){
			oppoColour = Player.WHITE;
		} else {
			oppoColour = Player.BLACK;
		}
		
		// FIXME, never finds the 5 deep winning possibility. Is this because it is always blocked?
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
				
				if(m.getGameOver()){
					return m;
				}
				
				orig.setPosition(m.getI(), m.getJ(), m.getPiece().getChar());
				
				ArrayList<GameStatus> gs = Analysis.moves(orig, oppoColour);
				
				GameStatus mostLikely1 = new GameStatus(null,null);
				
				mostLikely1.setWeight(-1);
				
				try{
					/**
					 * 2
					 */
					
					int randomIndex = (int) Math.random()*gs.size();
					if(randomIndex == gs.size()){
						randomIndex -= 1;
					}
					mostLikely1 = gs.get(randomIndex);
					
					int weight = START;
					if(mostLikely1.getMove().getGameOver()){
						weight += WIN;
					}
					
					if(mostLikely1.getMove().getTruth().getTake()){
						for(Piece p : mostLikely1.getMove().getTruth().getPiece()){
							weight+=LOSE_PIECE;
						}
					}
					
					mostLikely1.setWeight(weight);
					
					
					gs.clear();
					gs.add(mostLikely1);
					m.setWeight(m.getWeight()- mostLikely1.getWeight());
					
					
					ArrayList<Board> boards = Analysis.doMoves(gs);
					ArrayList<GameStatus> gs1 = new ArrayList<>();
					
					for(Board b : boards){
						AnalysisBoard b1 = AnalysisBoard.convB(b);
						
						gs1.addAll(Analysis.moves(b1, thisColour));
					}
					
					GameStatus mostLikely2 = new GameStatus(null,null);
					
					mostLikely2.setWeight(-1);
					
					try{
						/**
						 * 3
						 */
						
						for(GameStatus g : gs1 ){
							
							weight = START;
							if(g.getMove().getGameOver()){
								weight += WIN;
							}
							
							if(g.getMove().getTruth().getTake()){
								for(Piece p : g.getMove().getTruth().getPiece()){
									weight+=TAKE_PIECE;
								}
							}
							
							g.setWeight(weight);
							
							if(mostLikely2.getWeight() < g.getWeight()){
								mostLikely2 = g;
							}	
						}
						
						gs.clear();
						gs.add(mostLikely2);
						m.setWeight(m.getWeight()+mostLikely2.getWeight());
						
						ArrayList<Board> boards1 = Analysis.doMoves(gs);
						ArrayList<GameStatus> gs2 = new ArrayList<>();
						
						for(Board b : boards1){
							AnalysisBoard b1 = AnalysisBoard.convB(b);
							
							gs2.addAll(Analysis.moves(b1, oppoColour));
						}
						
						GameStatus mostLikely3 = new GameStatus(null,null);
						
						mostLikely3.setWeight(-1);
						
						try {
							/**
							 * 4
							 */
							randomIndex = (int) Math.random()*gs.size();
							if(randomIndex == gs.size()){
								randomIndex -= 1;
							}
							mostLikely3 = gs2.get(randomIndex);
							
							weight = START;
							if(mostLikely3.getMove().getGameOver()){
								weight += WIN;
							}
							
							if(mostLikely3.getMove().getTruth().getTake()){
								for(Piece p : mostLikely3.getMove().getTruth().getPiece()){
									weight+=LOSE_PIECE;
								}
							}
							
							mostLikely3.setWeight(weight);	
							
							
							gs.clear();
							gs.add(mostLikely3);
							m.setWeight(m.getWeight()-mostLikely3.getWeight());
							
							boards1 = Analysis.doMoves(gs);
							gs2 = new ArrayList<>();
							
							for(Board b : boards1){
								AnalysisBoard b1 = AnalysisBoard.convB(b);
								
								gs2.addAll(Analysis.moves(b1, thisColour));
							}
							
							mostLikely3 = new GameStatus(null,null);
							
							mostLikely3.setWeight(-1);
							
							try {
								/**
								 * 5
								 */
								
								for(GameStatus g : gs2 ){
									
									weight = START;
									if(g.getMove().getGameOver()){
										weight = WIN;
										System.out.println("Win 5 moves in future");
									}
									
									if(g.getMove().getTruth().getTake()){
										for(Piece p : g.getMove().getTruth().getPiece()){
											weight+=TAKE_PIECE;
										}
									}
									
									g.setWeight(weight);
									
									if(mostLikely3.getWeight() < g.getWeight()){
										mostLikely3 = g;
									}	
								}
								
								gs.clear();
								gs.add(mostLikely3);
								m.setWeight(m.getWeight()+mostLikely3.getWeight());
								
								boards1 = Analysis.doMoves(gs);
								gs2 = new ArrayList<>();
								
								for(Board b : boards1){
									AnalysisBoard b1 = AnalysisBoard.convB(b);
									
									gs2.addAll(Analysis.moves(b1, oppoColour));
								}
								
								mostLikely3 = new GameStatus(null,null);
								
								mostLikely3.setWeight(-1);
								
//								try{
//									/**
//									 * 6
//									 */
//									for(GameStatus g : gs2 ){
//										
//										double weight = START;
//										if(g.getMove().getGameOver()){
//											System.out.println("Lose - 6 ahead");
//											weight = WIN;
//										}
//										
//										if(g.getMove().getTruth().getTake()){
//											for(Piece p : g.getMove().getTruth().getPiece()){
//												weight+=TAKE_PIECE;
//											}
//										}
//										
//										g.setWeight(weight);
//										
//										if(mostLikely3.getWeight() < g.getWeight()){
//											mostLikely3 = g;
//										}	
//									}
//									
//									gs.clear();
//									gs.add(mostLikely3);
//									m.setWeight(m.getWeight()-mostLikely3.getWeight());
//									
//									boards1 = Analysis.doMoves(gs);
//									gs2 = new ArrayList<>();
//									
//									for(Board b : boards1){
//										AnalysisBoard b1 = AnalysisBoard.convB(b);
//										
//										gs2.addAll(Analysis.moves(b1, Player.BLACK));
//									}
//									
//									mostLikely3 = new GameStatus(null,null);
//									
//									mostLikely3.setWeight(-1);
//									
//									try{
//										/**
//										 * 7
//										 */
//										for(GameStatus g : gs2 ){
//											
//											double weight = START;
//											if(g.getMove().getGameOver()){
//												weight = WIN;
//												System.out.println("Win - 7 ahead");
//											}
//											
//											if(g.getMove().getTruth().getTake()){
//												for(Piece p : g.getMove().getTruth().getPiece()){
//													weight+=TAKE_PIECE;
//												}
//											}
//											
//											g.setWeight(weight);
//											
//											if(mostLikely3.getWeight() < g.getWeight()){
//												mostLikely3 = g;
//											}	
//										}
//										
//										gs.clear();
//										gs.add(mostLikely3);
//										m.setWeight(m.getWeight()+mostLikely3.getWeight());
//										
//										boards1 = Analysis.doMoves(gs);
//										gs2 = new ArrayList<>();
//										
//										for(Board b : boards1){
//											AnalysisBoard b1 = AnalysisBoard.convB(b);
//											
//											gs2.addAll(Analysis.moves(b1, Player.WHITE));
//										}
//										
//										mostLikely3 = new GameStatus(null,null);
//										
//										mostLikely3.setWeight(-1);
//										
//									} catch (NullPointerException e7){
//										System.out.println("NullPointer - 7 ahead");
//										//do nothing its already skipped the block
//									}
//								} catch (NullPointerException e6){
//									System.out.println("NullPointer - 6 ahead");
//									//do nothing its already skipped the block
//								}
							} catch (NullPointerException e5){
								System.out.println("NullPointer - 5 ahead");
								//do nothing its already skipped the block
							}
						} catch (NullPointerException e4){
							System.out.println("NullPointer - 4 ahead");
							//do nothing its already skipped the block
						}
					} catch (NullPointerException e3) {
						System.out.println("NullPointer - 3 ahead");
						//do nothing its already skipped the block
					}
				} catch (NullPointerException e2){
					System.out.println("NullPointer - 2 ahead");
					//do nothing its already skipped the block
				}
			} catch (NullPointerException e1){
				System.out.println("NullPointer - 1 ahead");
				//do nothing its already skipped the block
			}
		
			if(m.getWeight() > returnM.getWeight()){
				returnM = m;
			}
		}

		if(returnM.getPiece()==null){
			return null;
		}
		
		return returnM;
	}
}