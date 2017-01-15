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

		//this method shows the board on the GUI.
		t.showPiecesOnBoard(playerBlack);


		//exits while loop when one of the players lose their king, so makeMove() is really more of a
		//canMakeMove boolean...
		while (playerWhite.makeMove() && playerBlack.makeMove()){
            moveTest = false;
			//Human WHITE PLAYER
			while(!moveTest){
                if (playerType2 == 'A') {
                    Thread.sleep(50);
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
			if (playerBlack.makeMove()) {
				//Black  PLAYER
				moveTest =false;
				while(!moveTest){
                    if (playerType1 == 'A') {
                        Thread.sleep(50);
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
			System.out.println(playerWhite.toString()+" won the game.");
		}
		else {
			System.out.println(playerBlack.toString()+" won the game.");
		}
			
	}
}		


