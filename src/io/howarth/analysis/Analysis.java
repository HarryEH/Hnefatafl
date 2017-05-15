package io.howarth.analysis;

import io.howarth.Board;
import io.howarth.move.Move;
import io.howarth.pieces.Piece;
import io.howarth.pieces.PieceCode;
import io.howarth.players.Player;

import java.util.ArrayList;

public final class Analysis {
	
	private static final byte BOARD_SIZE = 11;
	
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
	
	private native static byte[][] kingCorner(char[] board, byte x, byte y);
	
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
	
	/***
	 * Returns the number of black pieces that can get to the 9 corner blocking squares
	 * @param board
	 * @return
	 */
	public static byte cornerAccess(char[][] board){
		byte zero = 0;  byte one  = 1;  byte two  = 2;
		byte eight = 8; byte nine = 9;  byte ten = 10;
		
		// 0,2 2,0, 1,1 - 0,8 2,10 1,9  - 8,0 9,1 10,2 - 8,10 9,9 10,8
		byte firstThree  = (byte)(findAccess(zero, two, board)   + findAccess(two, zero, board)  + findAccess(one, one, board));
		byte sndThree    = (byte)(findAccess(zero, eight, board) + findAccess(two, ten, board)   + findAccess(one, nine, board));
		byte thirdThree  = (byte)(findAccess(eight, zero, board) + findAccess(nine, one, board)  + findAccess(ten, two, board));
		byte fourthThree = (byte)(findAccess(eight, ten, board)  + findAccess(nine, nine, board) + findAccess(ten, eight, board));
		
//		System.out.println("1st: "+ firstThree);
//		System.out.println("2nd: "+ sndThree);
//		System.out.println("3rd: "+ thirdThree);
//		System.out.println("4th: "+ fourthThree);
		
		return (byte)(firstThree + sndThree + thirdThree + fourthThree);
	}
	
	
	/***
	 * Returns an array of length two that is the min and the max number of pieces that you can take
	 * for a given list of moves.
	 * @param mvs
	 * @return
	 */
	public static byte[] minmaxTake(ArrayList<Move> mvs){
		byte[] rtn = new byte[2];
		byte min = Byte.MAX_VALUE;
		byte max = Byte.MIN_VALUE;
		
		if(mvs.isEmpty()){
			rtn[0] = 0; rtn[1] = 0;
			return rtn;
		} else if( mvs.size() == 1){
			if(mvs.get(0).getTruth().getTake()){
				rtn[0] = (byte)mvs.get(0).getTruth().getPiece().size(); 
				rtn[1] = (byte)mvs.get(0).getTruth().getPiece().size();
			} else {
				rtn[0] = 0; rtn[1] = 0;
			}
			return rtn;
		}
		
		for(Move m: mvs){
			if(m.getTruth().getTake()){
				if( m.getTruth().getPiece().size() > max){
					max = (byte)m.getTruth().getPiece().size();
				}
			} else {
				min = 0;
			}
		}
		
		rtn[0] = min; rtn[1] = max;
		return rtn ;
	}
	
	/***
	 * Function that tells you how many moves there are for the opposition on the board that threaten your pieces
	 * this if three opposition peices could take your one piece, this would return 3. Change this?
	 * @param board
	 * @param riskColour
	 * @return
	 */
	public static byte threatMoves(char[][] board, byte riskColour){
		Board data = AnalysisBoard.convAB(new AnalysisBoard(board));
		ArrayList<Move> mvs = new ArrayList<>();
		
		for(Piece[] p : data.getData()) {
			for(Piece p1 : p) {
				if(p1 != null){
					if(p1.getColour() != riskColour){
						ArrayList<Move> temp = p1.availableMoves();
						if (temp != null){
							for(Move m : temp){
								if(m.getTruth().getTake()){
									mvs.add(m);
								}
							}
						}
					}
				}
			}
		}
		byte num =0;
		for(Move m : mvs){
			num += (byte)m.getTruth().getPiece().size();
		}
		return num;
	}
	
	/**
	 * Function that gives you the number of pieces on the board for the given colour
	 * 
	 * @param board
	 * @param colour
	 * @return
	 */
	public static byte numberOfPieces(char[][] board, byte colour){
		byte counter =0;
		for(byte i=0;i<BOARD_SIZE;i++){
			for(byte j=0;j<BOARD_SIZE;j++){
				if(colour == Player.BLACK) {
					if(board[i][j] == 'P'){
						counter++;
					}
				} else {
					if(board[i][j] == 'p' || board[i][j] == 'k'){
						counter++;
					}
				}		  
			}	
		}
		return counter;
	}
	
	
	
	/***
	 * A function to tell you how many moves it is for the king to the corner of the board.
	 * Actually calculates the number of moves from any position on the board that isn't occupied by
	 * a pawn. 
	 * 
	 * @param board
	 * @return the number of moves it will take the king to reach the corner of the board 
	 */
	public static byte kingToCorner(char[][] board){
		
		// Check size
		byte[][] starter = new byte[BOARD_SIZE][BOARD_SIZE];
		
		byte iK = 0; byte jK = 0;
		
		for(byte i=0;i<BOARD_SIZE;i++){
			for(byte j=0;j<BOARD_SIZE;j++){
				if(board[i][j] == 'P' || board[i][j] == 'p'){
					starter[i][j] = -1;
				}  else {
					// remember king position
					if(board[i][j] == 'k') {
						iK = i;
						jK = j;
					}
					starter[i][j] = 0;
				}
			}
		}
		
		if( occupiedOrBounds(board, iK, (byte)(jK+1)) && occupiedOrBounds(board, iK, (byte)(jK-1)) 
				&& occupiedOrBounds(board, (byte)(iK+1), jK) && occupiedOrBounds(board, (byte)(iK-1), jK)){
			return 0;
		}
		
		// 90 then -90
		byte[][] bLOut = transposeMatrix(reverseRow(doIterations(reverseRow(transposeMatrix(starter)))));
		// 180 then 180
		byte[][] bROut = reverseRow(transposeMatrix(reverseRow(transposeMatrix(
				doIterations(reverseRow(transposeMatrix(reverseRow(transposeMatrix(starter)))))))));
		// 270 then 90
		byte[][] tROut = reverseRow(transposeMatrix(doIterations(reverseRow(transposeMatrix(
				reverseRow(transposeMatrix(reverseRow(transposeMatrix(starter)))))))));
		// Merge all 4 2d arrays
		byte[][] output0 = merge(tROut,merge(bROut,merge(doIterations(starter),bLOut)));
		
		return output0[iK][jK];
	}

	
	/******************************************************************************************/
	/**Helper Functions************************************************************************/
	/******************************************************************************************/
	
	/***
	 * Helper function for kingToCorner
	 * @param ary
	 * @return
	 */
	private static byte[][] doIterations(byte[][] ary){
		
		for(byte j=1;j<BOARD_SIZE;j++){
			if(ary[0][j] == -1){
				break;
			} else {
				ary[0][j] = 1;
			}
		}
		
		for(byte i=1;i<BOARD_SIZE;i++){
			if(ary[i][0] == -1){
				break;
			} else {
				ary[i][0] = 1;
			}
		}
		
		for(byte q=1; q<10;q++){
			for(byte i=0;i<BOARD_SIZE;i++){
				for(byte j=0;j<BOARD_SIZE;j++){
					if(ary[i][j] == q){
						
						loop1:
						for(byte jj=1;jj<BOARD_SIZE;jj++){
							if(j+jj <= 10 && (!(i==0 && j+jj == 10)&&!(i==10 && j+jj == 10)
									&&!(i==10 && j+jj == 0)&&!(i==0 && j+jj == 0))){	
								if(ary[i][j+jj] == -1){
									break loop1;
								} else {
									if(ary[i][j+jj]==0){
										ary[i][j+jj] = (byte) (q + 1);
									} else if(ary[i][j+jj] == -2){
										ary[i][j+jj] = (byte) (q + 2);
										break loop1;
									}
								}
							}
						}
						
						loop2:
						for(byte ii=1;ii<BOARD_SIZE;ii++){
							if(i+ii <= 10 && (!(i+ii==0 && j == 10)&&!(i+ii==10 && j == 10)
									&&!(i+ii==10 && j == 0)&&!(i+ii==0 && j == 0))){
								if(ary[ii+i][j] == -1){
									break loop2;
								} else {
									if(ary[ii+i][j] == 0){
										ary[ii+i][j] = (byte) (q + 1);
									} else if(ary[ii+i][j] == -2){
										ary[ii+i][j] = (byte) (q + 2);
										break loop2;
									}
								}
							}
						}
						
						loop3:
						for(byte jj=1;jj<BOARD_SIZE;jj++){
							if(j-jj >= 0 && (!(i==0 && j-jj == 10)&&!(i==10 && j-jj == 10)
									&&!(i==10 && j-jj == 0)&&!(i==0 && j-jj == 0))){	
								if(ary[i][j-jj] == -1){
									break loop3;
								} else {
									if(ary[i][j-jj]==0){
										ary[i][j-jj] = (byte) (q + 1);
									} else if(ary[i][j-jj] == -2){
										ary[i][j-jj] = (byte) (q + 2);
										break loop3;
									}
								}
							}
						}
							
						loop4:
						for(byte ii=1;ii<BOARD_SIZE;ii++){
							if(i-ii >= 0 && (!(i-ii==0 && j == 10)&&!(i-ii==10 && j == 10)
									&&!(i-ii==10 && j== 0)&&!(i-ii==0 && j== 0))){
								if(ary[i-ii][j] == -1){
									break loop4;
								} else {
									if(ary[i-ii][j] == 0){
										ary[i-ii][j] = (byte) (q + 1);
									} else if(ary[i-ii][j] == -2){
										ary[i-ii][j] = (byte) (q + 2);
										break loop4;
									}
								}
							}
						}
					}	
				}
			}
		}
		
		return ary;
	}
	/***
	 * Helper function for kingToCorner
	 * @param m
	 * @return
	 */
	private static byte[][] transposeMatrix(byte [][] m){
        byte[][] temp = new byte[m[0].length][m.length];
        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < m[0].length; j++)
                temp[j][i] = m[i][j];
        return temp;
    }
	/***
	 * Helper function for kingToCorner
	 * @param m
	 * @return
	 */
	private static byte[][] reverseRow(byte[][] m){
		 
		for(byte[] inputArray : m){
			byte temp;

			for (int i = 0; i < inputArray.length/2; i++) {
				temp = inputArray[i];
	       
				inputArray[i] = inputArray[inputArray.length-1-i];
	       
				inputArray[inputArray.length-1-i] = temp;
			}
		}
		
		return m;
	
	}
	/***
	 * Helper function for kingToCorner
	 * @param m, n
	 * @return
	 */
	private static byte[][] merge(byte[][] m, byte[][] n){
		try{
			if(m.length == n.length && m[0].length == n[0].length){	
				for(byte i=0;i<BOARD_SIZE;i++){
					for(byte j=0;j<BOARD_SIZE;j++){
						if(m[i][j] > n[i][j] || m[i][j] == 0){
							if(n[i][j]!=0){
								m[i][j] = n[i][j];
							}
						}
					}
				}	
				return m;
			} else {return null;}
		} catch (IndexOutOfBoundsException e){
			return null;
		}	
	}
	
	
	private static byte findAccess(byte x, byte y, char[][] data){
		
		byte count = 0;
		
		if(data[x][y] != 'x'){
			return count;
		}
		
		//row
		loop1:
		for(byte i = 2; i < BOARD_SIZE; i++){
			if (data[i][y] == 'P'){
				count++;
				break loop1;
			}
		}
		//column
		loop2:
		for(byte i = 0; i < BOARD_SIZE; i++){
			if (data[x][i] == 'P'){
				count++;
				break loop2;
			}
		}
		//row 
		loop3:
		for(byte i = 2; i > 0; i--){
			if (data[i][y] == 'P'){
				count++;
				break loop3;
			}
		}
		//column
		loop4:
		for(byte i = 0; i > 0; i--){
			if (data[x][i] == 'P'){
				count++;
				break loop4;
			}
		}
		
		return count;
	}
	
	private static boolean occupiedOrBounds(char[][] board, byte x, byte y){
		return !(x < 11 && x > 0 && y > 0 && y < 11) || board[x][y] != 'x';
	}
	
}
