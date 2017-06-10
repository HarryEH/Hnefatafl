package io.howarth.players.impl;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import io.howarth.Board;
import io.howarth.TextHandler;
import io.howarth.analysis.Analysis;
import io.howarth.move.Move;
import io.howarth.move.PieceCoordinates;
import io.howarth.move.TakePiece;
import io.howarth.pieces.Piece;
import io.howarth.pieces.Pieces;
import io.howarth.players.Player;
/**
 * HumanPlayer.java 
 *
 *Class that extends player. Completes the moves and takes the user's input.
 *
 * @version 1.5 1st May 2015
 *
 * @author Harry Howarth 
 */
public class CommunicationsPlayerImpl extends Player {
	
	public CommunicationsPlayerImpl(String n, Board b, Pieces p, Player o) {
		super(n, p, b, o);
	}
	
	/**
	 * Boolean method -- Allows the HumanPLayer to make a move by taking their input.
     * code removed because now taken from the board directly.
	 * @return true when a move has been performed.
	 */
	public boolean doMove(){
		
		int portIn;
		
		if ( this.getPieces().getColour() != WHITE ) {
			portIn = 12001; // AI is white
		} else {
			portIn = 11001; // AI is black
		}
	
		try {
			
			DatagramSocket serverSocket = new DatagramSocket(portIn);
			// We want to be asked to connect
			byte[] receiveData = new byte[1024];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			boolean t = true;
			while(t) {
				
				serverSocket.receive(receivePacket);
				String hiFromServer = new String(receivePacket.getData());
				String testS;
				
				if ( getPieces().getColour() == WHITE ) {
					testS = "White"; // AI is white
				} else {
					testS = "Black"; // AI is black
				}
				
				if(hiFromServer.trim().contains(testS+"_Move")) {
					
					hiFromServer = hiFromServer.trim().substring(11, hiFromServer.trim().length());
					String[] input = hiFromServer.split("<");
					String move = input[0];
					
					byte[] m = TextHandler.convertMoveString(move);
					
					Piece piece1 = getBoard().getPiece(m[1],m[0]);
					
					TakePiece p = Analysis.analyseBoard(new Move(piece1, m[1],m[0],m[3],m[2]), getBoard());
			
					if (p.getTake()) { // true if there is an enemy player to take.
						for(PieceCoordinates pc : p.getPiece()){
							this.getOpponent().deletePiece(getBoard().getPiece(pc.getX(),pc.getY()));
							getBoard().remove(pc.getX(),pc.getY());
						}
					}
					
					getBoard().setPosition(m[3], m[2], piece1);
					piece1.setPosition(m[3], m[2]);
					getBoard().remove(m[1],m[0]);
							
					serverSocket.close();
					
					return true;
						
				} else if (hiFromServer.contains("_Move")) {
					// do nothing
				} else {
					System.out.println(hiFromServer.trim());
					serverSocket.close();
					return false;
				}
			}	
			serverSocket.close();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}

}
