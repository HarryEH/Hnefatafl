package io.howarth.players.impl;

import io.howarth.Board;
import io.howarth.Hnefatafl;
import io.howarth.Move;
import io.howarth.analysis.Analysis;
import io.howarth.analysis.AnalysisBoard;
import io.howarth.analysis.GameStatus;
import io.howarth.analysis.MoveChecking;
import io.howarth.pieces.Piece;
import io.howarth.pieces.PieceCode;
import io.howarth.pieces.Pieces;
import io.howarth.players.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


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
			
			final short SIMULATIONS = 50;
			// This will take a long time
			ArrayList<Move> simulationMoves = new ArrayList<>();
			byte currentColour = fullList.get(0).getPiece().getColour();
			int movesChecked = 0;
			
			ExecutorService service = Executors.newFixedThreadPool(SIMULATIONS);
			List<MoveChecking> futureLis = new ArrayList<MoveChecking>();
			for(int i=0; i<SIMULATIONS ;i++){
				futureLis.add(new MoveChecking(fullList, currentColour));
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
			
			for(Move m : simulationMoves){
				movesChecked += m.getChecked();
			}
			
			System.out.println("Moves checked: "+movesChecked);
			
			Move moveToConvert;
			if(simulationMoves != null && !simulationMoves.isEmpty()){
				System.out.println("Simulations run :"+ simulationMoves.size());
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
	
//	//Move weights
//	private final byte START        = 1;
//	private final short WIN         = 30000;
//	private final byte TAKE_PIECE   = 120;
//	private final byte LOSE_PIECE   = 100;
//	
//
//	private Move weightMoves(ArrayList<Move> mvs, int thisColour){
//		// First analysis board
//		// Get next set, find most probable move continue unless its game winning
//		// Get all your own set, take weights 
//		// Get next set, find most probable move continue unless its game winning
//		// Get all your own next set, take weights
//		// store move with its weight
//		// replace the last one with this one if the weight is higher
//		
//		byte zero = 0;
//		Move returnM = new Move(null,zero,zero,zero,zero,null, false, -10000000);
//		
//		int oppoColour =-1;
//		
//		if (thisColour == Player.BLACK){
//			oppoColour = Player.WHITE;
//		} else {
//			oppoColour = Player.BLACK;
//		}
//		
//		// FIXME, never finds the 5 deep winning possibility. Is this because it is always blocked?
//		
//		int counter = 0;
//		
//		for(Move m : mvs ){
//			counter++;
//			try{
//				/**
//				 * 1
//				 */
//				AnalysisBoard orig = AnalysisBoard.convB(Hnefatafl.b);
//				
//				orig.remove(m.getX(), m.getY());
//				
//				if(m.getTruth().getTake()){
//					orig.remove(m.getI(), m.getJ());
//					m.setWeight(m.getWeight()+TAKE_PIECE);
//				}
//				
//				if(m.getGameOver()){
//					return m;
//				}
//				
//				orig.setPosition(m.getI(), m.getJ(), m.getPiece().getChar());
//				
//				ArrayList<GameStatus> gs = Analysis.moves(orig, oppoColour);
//				
//				GameStatus mostLikely1 = new GameStatus(null,null);
//				
//				mostLikely1.setWeight(-1);
//				
//				try{
//					/**
//					 * 2
//					 */
//					
//					int randomIndex = (int) (Math.random()*gs.size());
//					if(randomIndex == gs.size()){
//						randomIndex -= 1;
//					}
//					mostLikely1 = gs.get(randomIndex);
//					
//					int weight = START;
//					if(mostLikely1.getMove().getGameOver()){
//						weight += WIN;
//					}
//					
//					if(mostLikely1.getMove().getTruth().getTake()){
//						for(Piece p : mostLikely1.getMove().getTruth().getPiece()){
//							weight+=LOSE_PIECE;
//						}
//					}
//					
//					mostLikely1.setWeight(weight);
//					counter ++;
//					
//					gs.clear();
//					gs.add(mostLikely1);
//					m.setWeight(m.getWeight()- mostLikely1.getWeight());
//					
//					
//					ArrayList<Board> boards = Analysis.doMoves(gs);
//					ArrayList<GameStatus> gs1 = new ArrayList<>();
//					
//					for(Board b : boards){
//						AnalysisBoard b1 = AnalysisBoard.convB(b);
//						
//						gs1.addAll(Analysis.moves(b1, thisColour));
//					}
//					
//					GameStatus mostLikely2 = new GameStatus(null,null);
//					
//					mostLikely2.setWeight(-1);
//					
//					try{
//						/**
//						 * 3
//						 */
//						
//						for(GameStatus g : gs1 ){
//							counter++;
//							weight = START;
//							if(g.getMove().getGameOver()){
//								weight += WIN;
//							}
//							
//							if(g.getMove().getTruth().getTake()){
//								for(Piece p : g.getMove().getTruth().getPiece()){
//									weight+=(TAKE_PIECE-15);
//								}
//							}
//							
//							g.setWeight(weight);
//							
//							if(mostLikely2.getWeight() < g.getWeight()){
//								mostLikely2 = g;
//							}	
//						}
//						
//						gs.clear();
//						gs.add(mostLikely2);
//						m.setWeight(m.getWeight()+mostLikely2.getWeight());
//						
//						ArrayList<Board> boards1 = Analysis.doMoves(gs);
//						ArrayList<GameStatus> gs2 = new ArrayList<>();
//						
//						for(Board b : boards1){
//							AnalysisBoard b1 = AnalysisBoard.convB(b);
//							
//							gs2.addAll(Analysis.moves(b1, oppoColour));
//						}
//						
//						GameStatus mostLikely3 = new GameStatus(null,null);
//						
//						mostLikely3.setWeight(-1);
//						
//						try {
//							/**
//							 * 4
//							 */
//							randomIndex = (int) (Math.random()*gs2.size());
//							if(randomIndex == gs2.size()){
//								randomIndex -= 1;
//							}
//							mostLikely3 = gs2.get(randomIndex);
//							
//							weight = START;
//							if(mostLikely3.getMove().getGameOver()){
//								weight += WIN;
//							}
//							
//							if(mostLikely3.getMove().getTruth().getTake()){
//								for(Piece p : mostLikely3.getMove().getTruth().getPiece()){
//									weight+=(LOSE_PIECE-30);
//								}
//							}
//							
//							mostLikely3.setWeight(weight);	
//							counter++;
//							
//							gs.clear();
//							gs.add(mostLikely3);
//							m.setWeight(m.getWeight()-mostLikely3.getWeight());
//							
//							boards1 = Analysis.doMoves(gs);
//							gs2 = new ArrayList<>();
//							
//							for(Board b : boards1){
//								AnalysisBoard b1 = AnalysisBoard.convB(b);
//								
//								gs2.addAll(Analysis.moves(b1, thisColour));
//							}
//							
//							mostLikely3 = new GameStatus(null,null);
//							
//							mostLikely3.setWeight(-1);
//							
//							try {
//								/**
//								 * 5
//								 */
//								for(GameStatus g : gs2 ){
//									counter++;
//									weight = START;
//									if(g.getMove().getGameOver()){
//										weight = (WIN-5000);
//									}
//									
//									if(g.getMove().getTruth().getTake()){
//										for(Piece p : g.getMove().getTruth().getPiece()){
//											weight+=(TAKE_PIECE-30);
//										}
//									}
//									
//									g.setWeight(weight);
//									
//									if(mostLikely3.getWeight() < g.getWeight()){
//										mostLikely3 = g;
//									}	
//								}
//								
//								gs.clear();
//								gs.add(mostLikely3);
//								m.setWeight(m.getWeight()+mostLikely3.getWeight());
//								
//								boards1 = Analysis.doMoves(gs);
//								gs2 = new ArrayList<>();
//								
//								for(Board b : boards1){
//									AnalysisBoard b1 = AnalysisBoard.convB(b);
//									
//									gs2.addAll(Analysis.moves(b1, oppoColour));
//								}
//								
//								mostLikely3 = new GameStatus(null,null);
//								
//								mostLikely3.setWeight(-1);
//								
////								try{
////									/**
////									 * 6
////									 */
////									for(GameStatus g : gs2 ){
////										
////										double weight = START;
////										if(g.getMove().getGameOver()){
////											System.out.println("Lose - 6 ahead");
////											weight = WIN;
////										}
////										
////										if(g.getMove().getTruth().getTake()){
////											for(Piece p : g.getMove().getTruth().getPiece()){
////												weight+=TAKE_PIECE;
////											}
////										}
////										
////										g.setWeight(weight);
////										
////										if(mostLikely3.getWeight() < g.getWeight()){
////											mostLikely3 = g;
////										}	
////									}
////									
////									gs.clear();
////									gs.add(mostLikely3);
////									m.setWeight(m.getWeight()-mostLikely3.getWeight());
////									
////									boards1 = Analysis.doMoves(gs);
////									gs2 = new ArrayList<>();
////									
////									for(Board b : boards1){
////										AnalysisBoard b1 = AnalysisBoard.convB(b);
////										
////										gs2.addAll(Analysis.moves(b1, Player.BLACK));
////									}
////									
////									mostLikely3 = new GameStatus(null,null);
////									
////									mostLikely3.setWeight(-1);
////									
////									try{
////										/**
////										 * 7
////										 */
////										for(GameStatus g : gs2 ){
////											
////											double weight = START;
////											if(g.getMove().getGameOver()){
////												weight = WIN;
////												System.out.println("Win - 7 ahead");
////											}
////											
////											if(g.getMove().getTruth().getTake()){
////												for(Piece p : g.getMove().getTruth().getPiece()){
////													weight+=TAKE_PIECE;
////												}
////											}
////											
////											g.setWeight(weight);
////											
////											if(mostLikely3.getWeight() < g.getWeight()){
////												mostLikely3 = g;
////											}	
////										}
////										
////										gs.clear();
////										gs.add(mostLikely3);
////										m.setWeight(m.getWeight()+mostLikely3.getWeight());
////										
////										boards1 = Analysis.doMoves(gs);
////										gs2 = new ArrayList<>();
////										
////										for(Board b : boards1){
////											AnalysisBoard b1 = AnalysisBoard.convB(b);
////											
////											gs2.addAll(Analysis.moves(b1, Player.WHITE));
////										}
////										
////										mostLikely3 = new GameStatus(null,null);
////										
////										mostLikely3.setWeight(-1);
////										
////									} catch (NullPointerException e7){
////										System.out.println("NullPointer - 7 ahead");
////										//do nothing its already skipped the block
////									}
////								} catch (NullPointerException e6){
////									System.out.println("NullPointer - 6 ahead");
////									//do nothing its already skipped the block
////								}
//							} catch (NullPointerException e5){
//								System.out.println("NullPointer - 5 ahead");
//								//do nothing its already skipped the block
//							}
//						} catch (NullPointerException e4){
//							System.out.println("NullPointer - 4 ahead");
//							//do nothing its already skipped the block
//						}
//					} catch (NullPointerException e3) {
//						System.out.println("NullPointer - 3 ahead");
//						//do nothing its already skipped the block
//					}
//				} catch (NullPointerException e2){
//					System.out.println("NullPointer - 2 ahead");
//					//do nothing its already skipped the block
//				}
//			} catch (NullPointerException e1){
//				System.out.println("NullPointer - 1 ahead");
//				//do nothing its already skipped the block
//			}
//		
//			if(m.getWeight() > returnM.getWeight()){
//				returnM = m;
//			}
//		}
//		
//		if(returnM.getPiece()==null){
//			return null;
//		}
//		
//		returnM.setChecked(counter);
//		return returnM;
//	}
}
