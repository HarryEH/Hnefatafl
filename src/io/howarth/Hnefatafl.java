package io.howarth;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import io.howarth.pieces.Pieces;
import io.howarth.players.Player;

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
	
	public static boolean emitMove = false;
	public static String ip        = "localhost";
<<<<<<< HEAD
	private static DatagramSocket serverSocket;
	
	public static int moveNum = 0;
=======
>>>>>>> origin/no-display-udp-version
	
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
		
		final String player1   = "playerOne";
		final String player2   = "playerTwo";
		boolean      moveTest  = false;
		final int    MAX_MOVES = 200;

		if(args.length == 3) {
			if(args[0].length() == 1 && args[1].length() == 1){
				
<<<<<<< HEAD
				// Declaration block for any type of player.

				Pieces piecesOne = new Pieces(b,Player.WHITE);
				Pieces piecesTwo = new Pieces(b,Player.BLACK);
				
				// Command Line args input for player types
				final char playerType1 = args[0].toUpperCase().charAt(0);
				final char playerType2 = args[1].toUpperCase().charAt(0);
				
				// Set White Player
				Player playerWhite = TextHandler.playerType(playerType1, player1, piecesOne, b, Player.WHITE);
				
				// Set Black Player
				Player playerBlack = TextHandler.playerType(playerType2, player2, piecesTwo, b, Player.BLACK);
						
				// Set opponents
				playerWhite.setOpponent(playerBlack);
				playerBlack.setOpponent(playerWhite);
				
				ip = args[2];
				
				// Only do this code if we need to so if arg[0] || arg[1] is a but not if they both are
				
=======
				ip = args[2];
				
				// only do this code if we need to so if arg[0] || arg[1] is a but not if they both are
				
>>>>>>> origin/no-display-udp-version
				if ( ( args[0].toUpperCase().charAt(0) == 'A') || (args[1].toUpperCase().charAt(0) == 'A') ) {
					
					try {
						
						int portIn = -1;
						InetAddress IPAddress = InetAddress.getByName(ip);
						
						if ( args[0].toUpperCase().charAt(0) != 'A' && args[1].toUpperCase().charAt(0) == 'A' ) {
							portIn = 12001; // AI is white
						} else if ( args[0].toUpperCase().charAt(0) == 'A' && args[1].toUpperCase().charAt(0) != 'A' ) {
							portIn = 11001; // AI is black
						}
						
						serverSocket = new DatagramSocket(portIn);
						// We want to be asked to connect
						byte[] receiveData = new byte[1024];
						DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
<<<<<<< HEAD
						
						String s = "";
						
						while(!s.toLowerCase().contains("connect")) {
							serverSocket.receive(receivePacket);
							s = new String(receivePacket.getData());
						}
						
						//serverSocket.close();
						
						byte[] sendData;
						final String SENTENCE = "connect<EOF>";
						sendData = SENTENCE.getBytes();
=======
						serverSocket.receive(receivePacket);
						String hiFromServer = new String(receivePacket.getData());
						System.out.println("FROM SERVER: " + hiFromServer.trim());
						
						//serverSocket.close();
						
						byte[] sendData;
						String sentence = "connect<EOF>";
						sendData = sentence.getBytes();
>>>>>>> origin/no-display-udp-version
						
						int portOut = -1;
						if ( args[0].toUpperCase().charAt(0) != 'A' && args[1].toUpperCase().charAt(0) == 'A' ) {
							portOut = 12000; // AI is white
						} else if ( args[0].toUpperCase().charAt(0) == 'A' && args[1].toUpperCase().charAt(0) != 'A' ) {
							portOut = 11000; // AI is black
						}
						
						DatagramSocket clientSocket = new DatagramSocket();
						DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, portOut);
<<<<<<< HEAD
=======
						System.out.println(sendPacket.getAddress().getHostAddress());
						System.out.println(sendPacket.getPort());
						System.out.println(sendPacket.getSocketAddress());
>>>>>>> origin/no-display-udp-version
						
						clientSocket.send(sendPacket);
						
						try{ Thread.sleep(150);} catch (InterruptedException e){}
						
						clientSocket.close();
						serverSocket.close();
						
						if(args[1].toUpperCase().charAt(0) == 'A') {
							serverSocket.close();
						}
						
						emitMove = true;
						
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
				
<<<<<<< HEAD
				// exits while loop when white loses its king, or white manages to escape
				boolean gameStart = true;
				short counter = 0;
				moveNum = 0;
=======
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
				
				//exits while loop when white loses its king, or white manages to escape
				//canMakeMove boolean...
>>>>>>> origin/no-display-udp-version
				loopage:
				while (playerWhite.makeMove() && playerBlack.makeMove() && counter <= 200){
		            moveTest = false;
		            
					//Human WHITE PLAYER
					
		            while(!moveTest  && counter <= MAX_MOVES) {
		                
<<<<<<< HEAD
		            	if(playerType2 != 'A' && gameStart) {
							try {
								
=======
		            	/*if(playerType2 != 'A') {
							try {
								
								DatagramSocket serverSocket = new DatagramSocket(11001);
								
>>>>>>> origin/no-display-udp-version
								// We want to be asked to connect
			        			byte[] receiveData = new byte[1024];
			        			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			        			serverSocket.receive(receivePacket);
			        			
			        			String hiFromServer = new String(receivePacket.getData());

		        				System.out.println(hiFromServer.trim());
		        				
<<<<<<< HEAD
		        				gameStart = false;
		        				
=======
>>>>>>> origin/no-display-udp-version
		        				serverSocket.close();
		        				
							} catch (SocketException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException ex) {
								// TODO Auto-generated catch block
								ex.printStackTrace();
							}
	        				
<<<<<<< HEAD
		            	}
		            	
	            		moveTest = playerBlack.doMove();
	            		counter++;
	            		moveNum++;
	                    System.out.println("MOVE NUMBER: "+moveNum);
	            		
	            		
	            		if(!moveTest || counter >= MAX_MOVES) {
	            			System.out.println("Escape: "+counter);
=======
		            	} */
		            	
	            		moveTest = playerBlack.doMove();
	                    
	            		if(!moveTest){
>>>>>>> origin/no-display-udp-version
	                    	break loopage;
	                    }
		                
		                // Will only not exit while loop if the doMove() method returns false, (cont...)
		                // which it never does. Unless from when there has been an error in the code
	            		
	            		
	                    
		            } // End of black player while loop
		            
		            
					if (playerWhite.makeMove()) {
						
						//White  PLAYER
						moveTest =false;
						
<<<<<<< HEAD
						while(!moveTest || counter >= MAX_MOVES) { 
	                
	                    	moveTest = playerWhite.doMove();
	                    	counter++;
	                    	moveNum++;
	                    	System.out.println("MOVE NUMBER: "+moveNum);
	                        if(!moveTest || counter >= MAX_MOVES){
	                        	System.out.println("Escape: "+counter);
=======
						while(!moveTest) { 
	                
	                    	moveTest = playerWhite.doMove();
	                	
	                        if(!moveTest){
>>>>>>> origin/no-display-udp-version
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


