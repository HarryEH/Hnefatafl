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
			
			Move moveToConvert = null;
			
			
			ArrayList<Move> weightedMoves = runSimulationsWhite(fullList);
			
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
				
				if(tP_1.getTake()) {
					for(PieceCoordinates p1 : tP_1.getPiece()) {
						orig.remove(p1.getX(), p1.getY()); // do the first move
						m.setWeight((short) (m.getWeight() + TAKE_PIECE));
					}
				}
				
				// do the first move
				orig.remove(m.getX(),m.getY());
				orig.setPosition(m.getI(), m.getJ(), m.getPiece().getChar());
				
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
						
						orig_copy.remove(mv2.getX(),mv2.getY());
						orig_copy.setPosition(mv2.getI(), mv2.getJ(), mv2.getPiece().getChar());
						
						if(tP_2.getTake()){
							for(PieceCoordinates p : tP_2.getPiece()){
								orig_copy.remove(p.getX(), p.getY());
								mv2.setWeight((short) (mv2.getWeight()+TAKE_PIECE));
							}
						}
						
						if(!tP_2.getGameOver()){
							mv2.setWeight((short) (mv2.getWeight()+WIN));
						} 
						
						if(mv2.getWeight() >= test.getWeight()){
							test = mv2;
						}
					}
					
					m.setWeight((short)(m.getWeight() - test.getWeight()));
					
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
