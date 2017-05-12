package io.howarth;

import io.howarth.analysis.Analysis;
import io.howarth.analysis.AnalysisBoard;
import io.howarth.pieces.Pieces;
import io.howarth.players.Player;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Hnefatafl.java 
 *
 * Hnefatafl has a main method that runs the game, declares Board, two Pieces, TextHandler (for input)
 * FrameDisplay (for the GUI), two player objects which each declare move objects based on the input
 * that they receive. 
 *
 * @version 0.1 12/1/17
 *
 * @author Harry Howarth 
 */

public class Hnefatafl {

	public static Board b = new Board();
	
	public static void main(String[] args) throws InterruptedException {
		try {
			
			run(args); // could do a loop here and just switch round the cmd args every time??? easy way to implement this.
				
		} catch (Exception e) {
			System.out.println("There was an uncaught exception!");
			e.printStackTrace();
		}
	}
	
	/***
	 * 
	 * @param args these are the command line input arguments
	 * @return void
	 */
	private static void run(String[] args) {
		
		final String player1 = "playerOne";
		final String player2 = "playerTwo";
		boolean moveTest     = false;

		if(args.length == 2) {
			if(args[0].length() == 1 && args[1].length() == 1){
				
				// only do this code if we need to so if arg[0] || arg[1] is a but not if they both are
				
				if ( ( args[0].charAt(0) == 'A' && args[1].charAt(0) != 'A' ) || 
						( args[0].charAt(0) != 'A' && args[1].charAt(0) == 'A' ) ) {
					try {
						
						DatagramSocket clientSocket = new DatagramSocket();
						InetAddress IPAddress = InetAddress.getByName("localhost");
						
						// We want to be asked to connect
						byte[] receiveData = new byte[1024];
						DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
						clientSocket.receive(receivePacket);
						String hiFromServer = new String(receivePacket.getData());
						System.out.println("FROM SERVER:" + hiFromServer);
						
						
						byte[] sendData = new byte[1024];
						
						String sentence = "connect<EOF>";
						sendData = sentence.getBytes();
						
						int port = -1;
						if ( args[0].charAt(0) == 'A' && args[1].charAt(0) != 'A' ) {
							port = 12000; // AI is white
						} else if ( args[0].charAt(0) != 'A' && args[1].charAt(0) == 'A' ) {
							port = 11000; // AI is black
						}
						
						DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
						clientSocket.send(sendPacket);
						
						clientSocket.close();
						
					} catch (SocketException e) {
						e.printStackTrace();
					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					System.out.println(" connection code ignored");
				}
				
				//Declaration block for any type of player.

				Pieces piecesOne = new Pieces(b,Player.WHITE);
				Pieces piecesTwo = new Pieces(b,Player.BLACK);
				
				// Command Line args input for player types
				final char playerType1 = args[0].toUpperCase().charAt(0);
				final char playerType2 = args[1].toUpperCase().charAt(0);
				
				//set White Player
				Player playerWhite = TextHandler.playerType(playerType1, player1, piecesOne, b, Player.WHITE);
				
				//set Black Player
				Player playerBlack = TextHandler.playerType(playerType2, player2, piecesTwo, b, Player.BLACK);
						
				//Set opponents
				playerWhite.setOpponent(playerBlack);
				playerBlack.setOpponent(playerWhite);
				
				long a, a1;
				
				//exits while loop when white loses its king, or white manages to escape
				//canMakeMove boolean...
				loopage:
				while (playerWhite.makeMove() && playerBlack.makeMove()){
		            moveTest = false;
					//Human WHITE PLAYER
					
		            while(!moveTest) {
		                
	                	a = System.nanoTime();
	            		moveTest = playerBlack.doMove();
	            		a1 = System.nanoTime();
	            		System.out.println("Black moved: "+ (a1-a)/1000000.0 + " ms");
	                    if(!moveTest){
	                    	break loopage;
	                    }
		                
		                // Will only not exit while loop if the doMove() method returns false, (cont...)
		                // which it never does. Unless from when there has been an error in the code
	                    
		            } // End of black player while loop
		            
					if (playerWhite.makeMove()) {
						
						//White  PLAYER
						moveTest =false;
						
						while(!moveTest) { 
	                    	a = System.nanoTime();
	                    	moveTest = playerWhite.doMove();
	                		a1 = System.nanoTime();
	                		System.out.println("White moved: "+ (a1-a)/1000000.0 + " ms");
	                        if(!moveTest){
	                        	break loopage;
	                        }  
						}
						
					}
				}// End of game logic while loop
				
			
				// Resets the board
				b = new Board();
				
				/**************************************************/
				// Console print of the board
//				for(Piece[] p : b.getData()){
//					for(Piece pI : p){
//						if(pI != null) System.out.print(pI.toString());
//						else System.out.print("x");
//					}
//					System.out.println("");
//				}
				/**************************************************/
				
			}// End of if two args of right length
		}// End of if two args
	}// End of void run()
	
}		


