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
		
		try { Thread.sleep(500); } catch (InterruptedException e) {}
		
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
			// This will take a long time
			Move moveToConvert  = weightMoves(fullList, Player.WHITE);
				
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
	

	private Move weightMoves(ArrayList<Move> mvs, byte thisColour){
		
		// Convert to List of MoveWeight objects
		ArrayList<MoveWeight> moveWeight = new ArrayList<>(mvs.size());
		
		for(Move m : mvs) {
			moveWeight.add( new MoveWeight(m, (short)0) );
		}
		
		
		int randomMove = (int)(Math.random()*mvs.size());
		
		return mvs.get(randomMove);
		
	}
	
}
