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
				
		//Set opponents
		playerWhite.setOpponent(playerBlack);
		playerBlack.setOpponent(playerWhite);

		
		// First round moves
//		long a = System.nanoTime();
//		ArrayList<Move> firstSet = Analysis.moves(playerBlack.getPieces().getData());
//		long z = System.nanoTime();
//		System.out.println("Turn 1: " +firstSet.size());
//		System.out.println("Time taken: "+(z-a)/1000000+"ms");
//		
//		// Depth 1
//		a = System.nanoTime();
//		ArrayList<GameStatus> secondSet = Analysis.bfsInitial(playerBlack.getBoard().getData(), firstSet);
//		z = System.nanoTime();
//		System.out.println("Turn 2: " +secondSet.size());
//		System.out.println("Time taken: "+(z-a)/1000000+"ms");
//		
//		a = System.nanoTime();
//		ArrayList<GameStatus> canTake = new ArrayList<>();
//		for(GameStatus g : secondSet){
//			if (g.getMove().getTruth().getTake()){
//				canTake.add(g);
//			}
//		}
//		z = System.nanoTime();
//		System.out.println("Take Piece: " +canTake.size());
//		
//		// Depth 2
//		a = System.nanoTime();
//		ArrayList<GameStatus> thirdSet = Analysis.bfsGeneral(secondSet);
//		z = System.nanoTime();
//		System.out.println("Turn 3: " +thirdSet.size());
//		System.out.println("Time taken: "+(z-a)/1000000+"ms");
//		
//		a = System.nanoTime();
//		ArrayList<GameStatus> canTake2 = new ArrayList<>();
//		for(GameStatus g : thirdSet){
//			if (g.getMove().getTruth().getTake()){
//				canTake2.add(g);
//			}
//		}
//		z = System.nanoTime();
//		System.out.println("Take Piece: " +canTake2.size());
		
		
		
		
		// creating thread pool to execute task which implements Callable
//		ExecutorService es = Executors.newFixedThreadPool(270);
//	       
//		ArrayList<Future<ArrayList<GameStatus>>> r = new ArrayList<>();
//		
//		//int step = 38421;
//		int step = 2846;
//		int startPoint = 0;
//		
//		for(int i=0; i<270;i++){
//			r.add(es.submit(new SearchRunnable(thirdSet.subList(startPoint, startPoint+step))));
//			startPoint = startPoint + step;
//			System.out.println(startPoint);
//		}
////		System.out.println(startPoint);
//		
//		a = System.nanoTime();
//		
//		ArrayList<GameStatus> fourthSet = new ArrayList<>();
//		
//		try {
//			int counter=0;
//			for(Future<ArrayList<GameStatus>> qqq : r){
//				fourthSet.addAll(qqq.get());
//				
//				System.out.println(counter);
//				counter++;
//			}
//			z = System.nanoTime();
//			System.out.println("Turn 4: " +fourthSet.size());
//			System.out.println("Time taken: "+(z-a)/1000000+"ms");
//		} catch (ExecutionException e){
//			// do nothing
//			System.out.println("failed1");
//		} catch (InterruptedException ex) {
//			// do nothing
//			System.out.println("failed2");
//		}
	
		
		
		//this method shows the board on the GUI.
		t.showPiecesOnBoard(playerBlack);


		//exits while loop when white loses its king, or white manages to escape
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
					Thread.sleep(1000);
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
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}
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


