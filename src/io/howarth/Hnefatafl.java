package io.howarth;

import io.howarth.analysis.Analysis;
import io.howarth.analysis.AnalysisBoard;
import io.howarth.pieces.Piece;
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
	
	final public static String player1 = "playerOne";
	final public static String player2 = "playerTwo";
	public static boolean moveTest =false;
	private static final short SLEEP_TIME = 500;

	public static void main(String[] args) throws InterruptedException {
		run(args);	
	}
	
	private static void run(String[] args) {

		
		printL(args.length+"");
		if(args.length == 2) {
			if(args[0].length() == 1 && args[1].length() == 1){
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
		                if (playerType2 == 'A') {
		                    try {
								Thread.sleep(SLEEP_TIME);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		                } else {
		                	a = System.nanoTime();
		            		moveTest = playerBlack.doMove();
		            		a1 = System.nanoTime();
		            		System.out.println("Time to do move: "+(a1-a)/1000000.0+"ms");
		                    if(!moveTest){
		                    	break loopage;
		                    }
		                }
		                // Will only not exit while loop if the doMove() method returns false, (cont...)
		                // which it never does. Unless from when there has been an error in the code
		            } // End of black player while loop
		            
		            
		            /*******************************************************/
		            // Timing code
		            a = System.nanoTime();
		            System.out.println("Corner Access: "+Analysis.cornerAccess(AnalysisBoard.convB(b).getData()));
		    		a1 = System.nanoTime();
		    		System.out.println("Corner access: "+(a1-a)/1000.0+"us");
		            /******************************************************/
		            
		    		
					if (playerType1 != 'A' || playerType2 != 'A'){
						try {
							Thread.sleep(500);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}
					if (playerWhite.makeMove()) {
						
						//White  PLAYER
						moveTest =false;
						
						while(!moveTest) {
		                    if (playerType1 == 'A') {
		                        try {
									Thread.sleep(SLEEP_TIME);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
		                    } else {
		                    	a = System.nanoTime();
		                    	moveTest = playerWhite.doMove();
		                		a1 = System.nanoTime();
		                		System.out.println("Time to do move: "+(a1-a)/1000000.0+"ms");
		                        if(!moveTest){
		                        	break loopage;
		                        }
		                    }
						}
			
						if (playerType2 != 'A'){
							try {
								Thread.sleep(SLEEP_TIME);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
						}
					}
				}// End of game logic while loop
				
				
				/**************************************************/
				// Console print of the board
				for(Piece[] p : b.getData()){
					for(Piece pI : p){
						if(pI != null) print(pI.toString());
						else print("x");
					}
					printL("");
				}
				/**************************************************/
				
			}// End of if two args of right length
		}// End of if two args
	}// End of void run()
	
	
	private static void printL(String a){
		System.out.println(a);
	}
	
	private static void print(String a){
		System.out.print(a);
	}
}		


