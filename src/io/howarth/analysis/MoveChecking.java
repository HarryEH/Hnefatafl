package io.howarth.analysis;

import io.howarth.Board;
import io.howarth.Hnefatafl;
import io.howarth.Move;
import io.howarth.pieces.Piece;
import io.howarth.players.Player;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class MoveChecking implements Callable<ArrayList<Move>> {

	private ArrayList<Move> mvs;
	private byte col;
	private final byte START        = 0;
	private final short WIN         = 30000;
	private final byte TAKE_PIECE   = 120;
	private final byte LOSE_PIECE   = 100;
	
	public MoveChecking(ArrayList<Move> m, byte thisColour){
		this.mvs = m;
		this.col = thisColour;
	}
	
	@Override
	public ArrayList<Move> call() throws Exception {
		// First analysis board
				// Get next set, find most probable move continue unless its game winning
				// Get all your own set, take weights 
				// Get next set, find most probable move continue unless its game winning
				// Get all your own next set, take weights
				// store move with its weight
				// replace the last one with this one if the weight is higher
		ArrayList<Move> rtnMvs = new ArrayList<>();
		long startTime = 0;
		
		loop:
		for(byte i=0;i<1000;i++){
			long a = System.nanoTime()/1000000 ;
			byte zero = 0;
			Move returnM = new Move(null,zero,zero,zero,zero,null, false, -10000000);
			
			int oppoColour =-1;
			
			if (col == Player.BLACK){
				oppoColour = Player.WHITE;
			} else {
				oppoColour = Player.BLACK;
			}
			
			// FIXME, never finds the 5 deep winning possibility. Is this because it is always blocked?
			
			int counter = 0;
			
			for(Move m : mvs ){
				counter++;
				try{
					/**
					 * 1
					 */
					AnalysisBoard orig = AnalysisBoard.convB(Hnefatafl.b);
					
					m.setWeight(0);
					
					orig.remove(m.getX(), m.getY());
					
					if(m.getTruth().getTake()){
						orig.remove(m.getI(), m.getJ());
						m.setWeight(m.getWeight()+TAKE_PIECE);
					}
					
					if(m.getGameOver()){
						m.setWeight(Integer.MAX_VALUE);
						rtnMvs.add(m);
						break loop;
					}
					
					orig.setPosition(m.getI(), m.getJ(), m.getPiece().getChar());
					
					ArrayList<GameStatus> gs = Analysis.moves(orig, oppoColour);
					
					GameStatus mostLikely1 = new GameStatus(null,null);
					
					mostLikely1.setWeight(-1);
					
					try{
						/**
						 * 2
						 */
						
						int randomIndex = (int) (Math.random()*gs.size());
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
						counter ++;
						
						gs.clear();
						gs.add(mostLikely1);
						m.setWeight(m.getWeight()- mostLikely1.getWeight());
						
						
						ArrayList<Board> boards = Analysis.doMoves(gs);
						ArrayList<GameStatus> gs1 = new ArrayList<>();
						
						for(Board b : boards){
							AnalysisBoard b1 = AnalysisBoard.convB(b);
							
							gs1.addAll(Analysis.moves(b1, col));
						}
						
						GameStatus mostLikely2 = new GameStatus(null,null);
						
						mostLikely2.setWeight(-1);
						
						try{
							/**
							 * 3
							 */
							
							for(GameStatus g : gs1 ){
								counter++;
								weight = START;
								if(g.getMove().getGameOver()){
									weight += WIN;
								}
								
								if(g.getMove().getTruth().getTake()){
									for(Piece p : g.getMove().getTruth().getPiece()){
										weight+=(TAKE_PIECE-15);
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
								randomIndex = (int) (Math.random()*gs2.size());
								if(randomIndex == gs2.size()){
									randomIndex -= 1;
								}
								mostLikely3 = gs2.get(randomIndex);
								
								weight = START;
								if(mostLikely3.getMove().getGameOver()){
									weight += WIN;
								}
								
								if(mostLikely3.getMove().getTruth().getTake()){
									for(Piece p : mostLikely3.getMove().getTruth().getPiece()){
										weight+=(LOSE_PIECE-30);
									}
								}
								
								mostLikely3.setWeight(weight);	
								counter++;
								
								gs.clear();
								gs.add(mostLikely3);
								m.setWeight(m.getWeight()-mostLikely3.getWeight());
								
								boards1 = Analysis.doMoves(gs);
								gs2 = new ArrayList<>();
								
								for(Board b : boards1){
									AnalysisBoard b1 = AnalysisBoard.convB(b);
									
									gs2.addAll(Analysis.moves(b1, col));
								}
								
								mostLikely3 = new GameStatus(null,null);
								
								mostLikely3.setWeight(-1);
								
								try {
									/**
									 * 5
									 */
									for(GameStatus g : gs2 ){
										counter++;
										weight = START;
										if(g.getMove().getGameOver()){
											weight = (WIN-5000);
										}
										
										if(g.getMove().getTruth().getTake()){
											for(Piece p : g.getMove().getTruth().getPiece()){
												weight+=(TAKE_PIECE-30);
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
									
//									try{
//										/**
//										 * 6
//										 */
//										for(GameStatus g : gs2 ){
//											
//											double weight = START;
//											if(g.getMove().getGameOver()){
//												System.out.println("Lose - 6 ahead");
//												weight = WIN;
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
//										m.setWeight(m.getWeight()-mostLikely3.getWeight());
//										
//										boards1 = Analysis.doMoves(gs);
//										gs2 = new ArrayList<>();
//										
//										for(Board b : boards1){
//											AnalysisBoard b1 = AnalysisBoard.convB(b);
//											
//											gs2.addAll(Analysis.moves(b1, Player.BLACK));
//										}
//										
//										mostLikely3 = new GameStatus(null,null);
//										
//										mostLikely3.setWeight(-1);
//										
//										try{
//											/**
//											 * 7
//											 */
//											for(GameStatus g : gs2 ){
//												
//												double weight = START;
//												if(g.getMove().getGameOver()){
//													weight = WIN;
//													System.out.println("Win - 7 ahead");
//												}
//												
//												if(g.getMove().getTruth().getTake()){
//													for(Piece p : g.getMove().getTruth().getPiece()){
//														weight+=TAKE_PIECE;
//													}
//												}
//												
//												g.setWeight(weight);
//												
//												if(mostLikely3.getWeight() < g.getWeight()){
//													mostLikely3 = g;
//												}	
//											}
//											
//											gs.clear();
//											gs.add(mostLikely3);
//											m.setWeight(m.getWeight()+mostLikely3.getWeight());
//											
//											boards1 = Analysis.doMoves(gs);
//											gs2 = new ArrayList<>();
//											
//											for(Board b : boards1){
//												AnalysisBoard b1 = AnalysisBoard.convB(b);
//												
//												gs2.addAll(Analysis.moves(b1, Player.WHITE));
//											}
//											
//											mostLikely3 = new GameStatus(null,null);
//											
//											mostLikely3.setWeight(-1);
//											
//										} catch (NullPointerException e7){
//											System.out.println("NullPointer - 7 ahead");
//											//do nothing its already skipped the block
//										}
//									} catch (NullPointerException e6){
//										System.out.println("NullPointer - 6 ahead");
//										//do nothing its already skipped the block
//									}
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
			
			returnM.setChecked(counter);
			if(returnM.getPiece()!=null){
				rtnMvs.add(returnM);
			}
			long a1 = System.nanoTime()/1000000;
			startTime += (a1-a);
			
			if(startTime >= 9400){
				break loop;
			}
			
		}
		return rtnMvs;	
	}

}
