package io.howarth.analysis;

import io.howarth.Board;
import io.howarth.Hnefatafl;
import io.howarth.move.Move;
import io.howarth.move.PieceCoordinates;
import io.howarth.move.TakePiece;
import io.howarth.players.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class MoveChecking implements Callable<ArrayList<Move>> {

	private List<Move>      mvs;
	private byte            col;
	private Board           b;

	
	public MoveChecking(List<Move> m, byte thisColour, Board board){
		this.mvs = m;
		this.col = thisColour;
		this.b   = board;
	}
	
	@SuppressWarnings("unused")
	@Override
	public ArrayList<Move> call() throws Exception {
		final byte      START_mc      = 0;
		final short     WIN_mc        = 300;
		final byte      TAKE_PIECE_mc = 10;
		final byte      LOSE_PIECE_mc = 10;
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
		for(short i=0; i<10000 ; i++){
			//This is to make sure that the callable runs inside the required time frame
			long a = System.nanoTime()/1000000 ;
			
			
			byte zero = 0;
			Move returnM = new Move(null, zero, zero, zero, zero);
			
			byte oppoColour =-1;
			
			if (col == Player.BLACK){
				oppoColour = Player.WHITE;
			} else {
				oppoColour = Player.BLACK;
			}
			
			int counter = 0;
			
			for(Move m : mvs ){
				counter++;
				try{
					/**
					 * 1
					 */
					AnalysisBoard orig = AnalysisBoard.convB(b);
					
					m.setWeight((short)0);
					
					short kingToCorner = Analysis.kingToCorner(orig.getData());
					
					if(kingToCorner == 0){
						kingToCorner = 10;
					}
				
					
					orig.remove(m.getX(), m.getY());
					
					TakePiece tP_1 = Analysis.analyseBoard(m, b);
					
					if(tP_1.getTake()){
						orig.remove(m.getI(), m.getJ());
						m.setWeight((short)(m.getWeight()+TAKE_PIECE_mc));
					}
					
					if(tP_1.getGameOver()){
						m.setWeight(Short.MAX_VALUE);
						rtnMvs.add(m);
						break loop;
					}
					
					m.setWeight( (short) (m.getWeight() + ((10-kingToCorner) * 11)) );
					
					orig.setPosition(m.getI(), m.getJ(), m.getPiece().getChar());
					
					ArrayList<GameStatus> gs = Analysis.gameStatus(orig, oppoColour, false);
					
					GameStatus mostLikely1 = new GameStatus(null,null);
					
					mostLikely1.setWeight((short)-1);
					
					try{
						/**
						 * 2
						 */
						
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
						
						if(tP_2.getTake()){
							for(PieceCoordinates p : tP_2.getPiece()){
								weight+=LOSE_PIECE_mc;
							}
						}
						
						mostLikely1.setWeight(weight);
						
						counter++;
						
						gs.clear();
						gs.add(mostLikely1);
						
						m.setWeight((short)(m.getWeight()- mostLikely1.getWeight()));
						
						ArrayList<Board> boards = Analysis.doMoves(gs);
						ArrayList<GameStatus> gs1 = new ArrayList<>();
						
						for(Board b : boards){
							AnalysisBoard b1 = AnalysisBoard.convB(b);
							
							gs1.addAll(Analysis.gameStatus(b1, col, false));
						}
						
						GameStatus mostLikely2 = new GameStatus(null,null);
						
						mostLikely2.setWeight((short)-1);
						
						try{
							/**
							 * 3
							 */
							
							for(GameStatus g : gs1 ){
								counter++;
								weight = START_mc;
								
								TakePiece tP_3 = Analysis.analyseBoard(g.getMove(), AnalysisBoard.convAB(g.getBoard()));
								g.setTakePiece(tP_3);
								if(tP_3.getGameOver()){
									weight += WIN_mc;
								}
								
								if(tP_3.getTake()){
									for(PieceCoordinates p : tP_3.getPiece()){
										weight+=(TAKE_PIECE_mc);
									}
								}
								
								g.setWeight(weight);
								
								if(mostLikely2.getWeight() < g.getWeight()){
									mostLikely2 = g;
								}	
							}
							
							gs.clear();
							gs.add(mostLikely2);
							m.setWeight((short)(m.getWeight()+ (0.5*mostLikely2.getWeight())));
							
							ArrayList<Board> boards1 = Analysis.doMoves(gs);
							
							
							
							ArrayList<GameStatus> gs2 = new ArrayList<>();
							
							for(Board b : boards1){
								AnalysisBoard b1 = AnalysisBoard.convB(b);
								
								gs2.addAll(Analysis.gameStatus(b1, oppoColour, false));
							}
							
							GameStatus mostLikely3 = new GameStatus(null,null);
							
							mostLikely3.setWeight((short)-1);
							
							try {
								/**
								 * 4
								 */
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
										weight+=(TAKE_PIECE_mc);
									}
								}
								
								mostLikely3.setWeight(weight);	
								counter++;
								
								gs.clear();
								gs.add(mostLikely3);
								m.setWeight((short)(m.getWeight()-(0.5*mostLikely3.getWeight())));
								
							} catch (NullPointerException e4){
								System.out.println("NullPointer - 4 ahead");
								// do nothing its already skipped the block
							}
						} catch (NullPointerException e3) {
							System.out.println("NullPointer - 3 ahead");
							// do nothing its already skipped the block
						}
					} catch (NullPointerException e2){
						System.out.println("NullPointer - 2 ahead");
						// do nothing its already skipped the block
					}
				} catch (NullPointerException e1){
					System.out.println("NullPointer - 1 ahead");
					// do nothing its already skipped the block
				}
			
				rtnMvs.add(m);
				long a1 = System.nanoTime()/1000000;
				
				if((a1-a) >= 8000){
					break loop;
				}
			}
			
			long a1 = System.nanoTime()/1000000;
			startTime += (a1-a);
			
			if(startTime >= 8000){
				break loop;
			}
			
		}
		return rtnMvs;	
	}
	
}

