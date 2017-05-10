package io.howarth;

import java.util.ArrayList;

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
	
	public static void main(String[] args) throws InterruptedException {
		try {
			
			double sumation = 0;
			double runs     = 5;
			
			for(int i =0; i < runs; i ++){
				double dub = run(args);
				// Change this to be == -1
				if(dub == 0){
					runs -= 1;
				} else {
					System.out.println("Time "+ (i+1) + ": is "+((int)(dub*100))/100.0+ " ms");
					sumation += dub;
				}
			}
			
			System.out.println("Average time taken for White "+ sumation/runs);
			
		} catch (Exception e) {
			System.out.println("There was an uncaught exception!");
		}
	}
	
	private static double run(String[] args) {
		
		final String player1 = "playerOne";
		final String player2 = "playerTwo";
		boolean moveTest =false;
		
		ArrayList<Double> timeSum = new ArrayList<>();

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
		                
	                	a = System.nanoTime();
	            		moveTest = playerBlack.doMove();
	            		a1 = System.nanoTime();
	            		//System.out.println("Black Time to do move: "+(a1-a)/1000000.0+"ms");
	                    if(!moveTest){
	                    	break loopage;
	                    }
		                
		                // Will only not exit while loop if the doMove() method returns false, (cont...)
		                // which it never does. Unless from when there has been an error in the code
		            } // End of black player while loop
		            
		            
		            /*******************************************************/
		            // Timing code
//		            a = System.nanoTime();
//		            System.out.println("Corner Access: "+Analysis.cornerAccess(AnalysisBoard.convB(b).getData()));
//		    		a1 = System.nanoTime();
//		    		System.out.println("Corner access: "+(a1-a)/1000.0+"us");
		            /******************************************************/
		            
		    		
					if (playerWhite.makeMove()) {
						
						//White  PLAYER
						moveTest =false;
						
						while(!moveTest) { 
	                    	a = System.nanoTime();
	                    	moveTest = playerWhite.doMove();
	                		a1 = System.nanoTime();
	                		timeSum.add(new Double((a1-a)/1000000.0));
	                        if(!moveTest){
	                        	break loopage;
	                        }  
						}
						
					}
				}// End of game logic while loop
				
				
				// Works out the average time taken for the white player
				double sum = 0;
				
				for (Double d : timeSum){
					sum+= d.doubleValue();
				} 
				
				
				b = new Board();
				return sum / (double) timeSum.size();
				
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
		return -1;
	}// End of void run()
	
}		


