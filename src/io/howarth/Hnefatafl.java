package io.howarth;

import io.howarth.analysis.Analysis;
import io.howarth.analysis.AnalysisBoard;
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
	public static String player1;
	public static String player2;
	public static char playerType1;
	public static char playerType2;
	public static boolean truth = true;
	public static boolean moveTest =false;
	private static final short SLEEP_TIME = 500;


	public static void main(String[] args) throws InterruptedException {
		run();	
	}
	
	private static void run(){
		FrameDisplay t = new FrameDisplay();
		t.startMenu();

		while (truth) {
			System.out.print("");
		}
		//Declaration block for any type of player.

		Pieces piecesOne = new Pieces(b,Player.WHITE);
		Pieces piecesTwo = new Pieces(b,Player.BLACK);
		
		//handle input etc.
		TextHandler input = new TextHandler();
		
		//set White Player
		Player playerWhite = input.playerType(playerType1,player1,piecesOne,b,Player.WHITE);
		
		//set Black Player
		Player playerBlack = input.playerType(playerType2,player2,piecesTwo,b,Player.BLACK);
				
		//Set opponents
		playerWhite.setOpponent(playerBlack);
		playerBlack.setOpponent(playerWhite);
		
		long a, a1;
		
		//this method shows the board on the GUI.
		t.showPiecesOnBoard(playerBlack);
		
		//exits while loop when white loses its king, or white manages to escape
		//canMakeMove boolean...
		int COUNTER = 0;
		loopage:
		while (playerWhite.makeMove() && playerBlack.makeMove()){
            moveTest = false;
			//Human WHITE PLAYER
			
            while(!moveTest){
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
                    COUNTER++;
                }
                //will only not exit while loop if the doMove() method returns false,
                //which it never does. (apart from when there has been an error in the code.)
            }
            a = System.nanoTime();
            System.out.println("Corner Access: "+Analysis.cornerAccess(AnalysisBoard.convB(b).getData()));
    		a1 = System.nanoTime();
    		System.out.println("Corner access: "+(a1-a)/1000000.0+"ms");
            
            
			t.showPiecesOnBoard(playerWhite);
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
				
				while(!moveTest){
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
                        COUNTER++;
                    }
				}
				t.showPiecesOnBoard(playerBlack);
				if (playerType2 != 'A'){
					try {
						Thread.sleep(SLEEP_TIME);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
		
		System.out.println(COUNTER);
		try{
			Thread.sleep(5000);
		} catch (InterruptedException e){
			// do nothing
		}
		
		t.winner("Game Over");
		
		try{
			Thread.sleep(5000);
		} catch (InterruptedException e){
			// do nothing
		}
		
		t.close();
		
		b = new Board();
		player1 = null;
		player2 = null;
		playerType1 = '.';
		playerType2 = '.';
		truth = true;
		moveTest =false;
		run();
	}
}		


