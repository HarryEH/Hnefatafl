package io.howarth.players.impl;

import io.howarth.Board;
import io.howarth.Move;
import io.howarth.analysis.MoveChecking;
import io.howarth.pieces.Piece;
import io.howarth.pieces.Pieces;
import io.howarth.players.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * RandomDepthPlayer.java 
 *
 * Concrete Class to be an AI player, makes Move object and
 * does the move.
 *
 * @version 1.0 10/2/17
 *
 * @author Harry Howarth 
 */
public class RandomDepthPlayerImpl extends Player {

	public RandomDepthPlayerImpl(String n, Pieces p, Board b, Player o) {
		super(n, p, b, o);
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
}
