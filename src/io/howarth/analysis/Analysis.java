package io.howarth.analysis;

import io.howarth.Board;
import io.howarth.Move;
import io.howarth.pieces.Piece;
import io.howarth.pieces.PieceCode;
import io.howarth.players.Player;

import java.util.ArrayList;
import java.util.Arrays;

public final class Analysis {
	
	public static ArrayList<GameStatus> moves(AnalysisBoard b, byte colour, boolean kingOnly){
		
		//Create Board
		Board board = AnalysisBoard.convAB(b);
		
		ArrayList<GameStatus> moves = new ArrayList<>();
		for(Piece[] pcs : board.getData()) {
			kingLoop:
			for(Piece p : pcs ){
				if(p != null && p.getColour() == colour){
					if(kingOnly && colour == PieceCode.WHITE){
						if(p.getChar() == 'k' && p.availableMoves()!=null){
							for(Move m : p.availableMoves()){
								moves.add(new GameStatus(b,m));
							}
							break kingLoop;
						}
					} else {
						if(p.availableMoves()!=null){
							for(Move m : p.availableMoves()){
								moves.add(new GameStatus(b,m));
							}
						}
					}
					
				}	
			}	
		}
		
		return moves;
	}
	
	public static ArrayList<Board> doMoves(ArrayList<GameStatus> gs){
		
		ArrayList<Board> rtn = new ArrayList<>();
		
		for(GameStatus g : gs) {
			
			AnalysisBoard b1 = g.getBoard();
			
			Board b = AnalysisBoard.convAB(b1);
			
			b.remove(g.getMove().getX(), g.getMove().getY());
			
			if(g.getMove().getTruth().getTake()){
				b.remove(g.getMove().getI(), g.getMove().getJ());
			}
			
			b.setPosition(g.getMove().getI(), g.getMove().getJ(), g.getMove().getPiece());
			
			
			rtn.add(b);
			
		}
		
		
		return rtn;
	}
	
	
	public static byte kingToCorner(ArrayList<AnalysisBoard> b){
		
		boolean cornerEmpty = true;
		byte counter = 0;
		
		while(cornerEmpty){
			counter++;
			
			ArrayList<Board> nextB = new ArrayList<>();
			
			for(AnalysisBoard b1 : b){
				ArrayList<GameStatus> gs = moves(b1, Player.WHITE, true);
				if(gs == null || gs.isEmpty()){
					// Do nothing
				} else {
					ArrayList<Board> boards = doMoves(gs);
					// Check Boards
					for(Board bd : boards){
						if(bd.occupied(0, 0) ||bd.occupied(10, 0) ||
								bd.occupied(0, 10) || bd.occupied(10, 10)){
							System.out.println("0,0: " + bd.occupied(0, 0));
							System.out.println("10,0: " + bd.occupied(10, 0));
							System.out.println("0,10: " + bd.occupied(0, 10));
							System.out.println("10,10: " + bd.occupied(10, 10));
							return counter;
						}
					}
					nextB.addAll(boards);
				}
			}
			
			b.clear();
			
			for(Board b2 : nextB){
				b.add(AnalysisBoard.convB(b2));
			}
			
			if(counter > 5){
				return -2;
			}
		}
		
		return -1;
	}
	
	public static int kingToCorner_James(char[][] board){
		
		// Check size
		// Check letters x, p, k, P
		
		final byte BOARD_SIZE = 11;

		byte[][] starter = new byte[BOARD_SIZE][BOARD_SIZE];
		
		for(byte i=0;i<BOARD_SIZE;i++){
			for(byte j=0;j<BOARD_SIZE;j++){
				if(board[i][j] == 'P'){
					starter[i][j] = -1;
				} else if (board[i][j] == 'p'){
					starter[i][j] = -2;
				} else {
					starter[i][j] = 0;
				}
			}
		}
		
		
		//start from 0,0
		
		for(byte j=1;j<BOARD_SIZE;j++){
			if(starter[0][j] == -1){
				break;
			} else {
				starter[0][j] = 1;
			}
		}
		
		for(byte i=1;i<BOARD_SIZE;i++){
			if(starter[i][0] == -1){
				break;
			} else {
				starter[i][0] = 1;
			}
		}
		
		for(byte[] q : starter){
			System.out.println(Arrays.toString(q));
		}
		
		for(byte q=1; q<10;q++){
			for(byte i=0;i<BOARD_SIZE;i++){
				for(byte j=0;j<BOARD_SIZE;j++){
					if(starter[i][j] == q){
						
						loop1:
						for(byte jj=1;jj<BOARD_SIZE;jj++){
							if(j+jj <= 10 && (!(i==0 && j+jj == 10)&&!(i==10 && j+jj == 10)
									&&!(i==10 && j+jj == 0)&&!(i==0 && j+jj == 0))){	
								if(starter[i][j+jj] == -1){
									break loop1;
								} else {
									if(starter[i][j+jj]==0){
										if(i == 5 && j+jj ==5 ){
											System.out.println("hello0: "+q+", "+jj);
										}
										starter[i][j+jj] = (byte) (q + 1);
									} else if(starter[i][j+jj] == -2){
										starter[i][j+jj] = (byte) (q + 2);
										System.out.println("hello0.1: "+i+", "+(j+jj));
										break loop1;
									}
								}
							}
						}
						
						loop2:
						for(byte ii=1;ii<BOARD_SIZE;ii++){
							if(i+ii <= 10 && (!(i+ii==0 && j == 10)&&!(i+ii==10 && j == 10)
									&&!(i+ii==10 && j == 0)&&!(i+ii==0 && j == 0))){
								if(starter[ii+i][j] == -1){
									break loop2;
								} else {
									if(starter[ii+i][j] == 0){
										if(i+ii == 5 && j ==5 ){
											System.out.println("hello1");
										}
										starter[ii+i][j] = (byte) (q + 1);
									} else if(starter[ii+i][j] == -2){
										starter[ii+i][j] = (byte) (q + 2);
										break loop2;
									}
								}
							}
						}
						
						loop3:
						for(byte jj=1;jj<BOARD_SIZE;jj++){
							if(j-jj >= 0 && (!(i==0 && j-jj == 10)&&!(i==10 && j-jj == 10)
									&&!(i==10 && j-jj == 0)&&!(i==0 && j-jj == 0))){	
								if(starter[i][j-jj] == -1){
									break loop3;
								} else {
									if(starter[i][j-jj]==0){
										if(i == 5 && j-jj ==5 ){
											System.out.println("hello2");
										}
										starter[i][j-jj] = (byte) (q + 1);
									} else if(starter[i][j-jj] == -2){
										starter[i][j-jj] = (byte) (q + 2);
										break loop3;
									}
								}
							}
						}
							
						loop4:
						for(byte ii=1;ii<BOARD_SIZE;ii++){
							if(i-ii >= 0 && (!(i-ii==0 && j == 10)&&!(i-ii==10 && j == 10)
									&&!(i-ii==10 && j== 0)&&!(i-ii==0 && j== 0))){
								if(starter[i-ii][j] == -1){
									break loop4;
								} else {
									if(starter[i-ii][j] == 0){
										if(i-ii == 5 && j ==5 ){
											System.out.println("hello3");
										}
										starter[i-ii][j] = (byte) (q + 1);
									} else if(starter[i-ii][j] == -2){
										starter[i-ii][j] = (byte) (q + 2);
										break loop4;
									}
								}
							}
						}
					}	
				}
			}
		}
		
		for(byte[] q : starter){
			System.out.println(Arrays.toString(q));
		}
		
//		byte[][] tL = new byte[BOARD_SIZE][BOARD_SIZE];
//		byte[][] tR = new byte[BOARD_SIZE][BOARD_SIZE];
//		byte[][] bL = new byte[BOARD_SIZE][BOARD_SIZE];
//		byte[][] bR = new byte[BOARD_SIZE][BOARD_SIZE];
		
		
		
		return -1;
	}
	
	
}
