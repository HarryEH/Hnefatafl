package io.howarth.players.impl;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import io.howarth.Board;
import io.howarth.Hnefatafl;
import io.howarth.TextHandler;
import io.howarth.analysis.Analysis;
import io.howarth.analysis.MoveChecking;
import io.howarth.move.Move;
import io.howarth.move.PieceCoordinates;
import io.howarth.move.TakePiece;
import io.howarth.pieces.Piece;
import io.howarth.pieces.Pieces;
import io.howarth.players.Player;


/**
 * RandomDepthPlayerImpl.java 
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
			
			ExecutorService service = Executors.newFixedThreadPool(SIMULATIONS);
			List<MoveChecking> futureLis = new ArrayList<MoveChecking>();
			for(int i=0; i<SIMULATIONS ;i++){
				futureLis.add(new MoveChecking(fullList, currentColour, getBoard()));
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
			} else {
				return false;
			}		
		} else {
			return false;
		}
	}
}

