package io.howarth;

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
		
		System.out.println(playerWhite.getPieces().getData().size());
		System.out.println(playerBlack.getPieces().getData().size());
		
		//Set opponents
		playerWhite.setOpponent(playerBlack);
		playerBlack.setOpponent(playerWhite);

		
//		System.out.println("Turn 1: " +Analysis.moves(playerBlack.getPieces()).size());
//		System.out.println("Turn 2: " +Analysis.bfsOne(Player.BLACK).size());
//		
//		System.out.println("Turn 1: " +Analysis.moves(playerWhite.getPieces()).size());
//		System.out.println("Turn 2: " +Analysis.bfsOne(Player.WHITE).size());
//		
//		
//		//reset block
//		b = new Board();
//		piecesOne = new Pieces(b,Player.WHITE);
//		piecesTwo = new Pieces(b,Player.BLACK);
//		playerWhite = input.playerType(playerType1,player1,piecesOne,b,Player.WHITE);
//		playerBlack = input.playerType(playerType2,player2,piecesTwo,b,Player.BLACK);
//		playerWhite.setOpponent(playerBlack);
//		playerBlack.setOpponent(playerWhite);
		
		//this method shows the board on the GUI.
		t.showPiecesOnBoard(playerBlack);


		//exits while loop when one of the players lose their king, so makeMove() is really more of a
		//canMakeMove boolean...
		while (playerWhite.makeMove() && playerBlack.makeMove()){
            moveTest = false;
			//Human WHITE PLAYER
			while(!moveTest){
                if (playerType2 == 'A') {
                    try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                } else {
                    moveTest = playerBlack.doMove();
                }
                //will only not exit while loop if the doMove() method returns false,
                //which it never does. (apart from when there has been an error in the code.)
            }
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
							Thread.sleep(50);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    } else {
                        moveTest = playerWhite.doMove();
                    }
				}
				t.showPiecesOnBoard(playerBlack);
				if (playerType2 != 'A'){
					try {
						Thread.sleep(500);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
		
		// TODO in here make it not console based
		if (!playerBlack.makeMove()) {
			
			for(Piece p : playerWhite.getPieces().getData()){
				System.out.println("End: "+ p.getChar());
			}
			System.out.println(playerBlack.getPieces().getData().size());
			
			t.winner(playerWhite.toString()+" WON!");
			
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
		else {
			
			t.winner(playerBlack.toString()+" WON!");
			
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
}		


