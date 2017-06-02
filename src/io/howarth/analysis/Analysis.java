package io.howarth.analysis;

import java.util.ArrayList;

import io.howarth.Board;
import io.howarth.move.Move;
import io.howarth.move.PieceCoordinates;
import io.howarth.move.TakePiece;
import io.howarth.pieces.Piece;
import io.howarth.pieces.PieceCode;

public final class Analysis {
	
	private static final byte BOARD_SIZE = 11;
	
	public static ArrayList<Move> moves(AnalysisBoard b, byte colour, boolean kingOnly) {
		
		// Create Board
		Board board = AnalysisBoard.convAB(b);
		
		ArrayList<Move> moves = new ArrayList<>();
		
		for (Piece[] pcs : board.getData()) {
			kingLoop:
			for(Piece p : pcs ){
				if(p != null && p.getColour() == colour){
					if(kingOnly && colour == PieceCode.WHITE){
						if(p.getChar() == 'k' && p.availableMoves()!=null){
							for(Move m : p.availableMoves()){
								moves.add(m);
							}
							break kingLoop;
						}
					} else {
						if(p.availableMoves()!=null){
							for(Move m : p.availableMoves()){
								moves.add(m);
							}
						}
					}
					
				}	
			}	
		}
		
		return moves;
	}
	
	public static ArrayList<GameStatus> gameStatus(AnalysisBoard b, byte colour, boolean kingOnly) {
		
		// Create Board
		Board board = AnalysisBoard.convAB(b);
		
		ArrayList<GameStatus> moves = new ArrayList<>();
		
		for (Piece[] pcs : board.getData()) {
			kingLoop:
			for(Piece p : pcs ){
				if(p != null && p.getColour() == colour){
					if(kingOnly && colour == PieceCode.WHITE){
						if(p.getChar() == 'k' && p.availableMoves()!=null){
							for(Move m : p.availableMoves()){
								moves.add(new GameStatus(b, m));
							}
							break kingLoop;
						}
					} else {
						if(p.availableMoves()!=null){
							for(Move m : p.availableMoves()){
								moves.add(new GameStatus(b, m));
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
			
			if(g.getTakePiece().getTake()){
				b.remove(g.getMove().getI(), g.getMove().getJ());
			}
			
			b.setPosition(g.getMove().getI(), g.getMove().getJ(), g.getMove().getPiece());
			
			
			rtn.add(b);
			
		}
		
		
		return rtn;
	}

	
	//private native static byte[] kingCorner(char[] board, byte x, byte y);
	
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
	
	public static TakePiece analyseBoard(Move m, Board b){
		
		byte i = m.getI();
		byte j = m.getJ();
		
		byte col = m.getPiece().getColour();
//		b.remove(x, y);
//		b.setPosition(i, j, b.getPiece(x, y));
	
		Piece take;
		Piece help;
		Piece help1;
		Piece help2;
		
		ArrayList<PieceCoordinates> takePiece = new ArrayList<>();
		TakePiece tp = new TakePiece(takePiece, false, (Analysis.isCorner(i, j) && m.getPiece().getChar() == 'k'));
		
		if(!b.occupiedOrBounds((byte)(i-1),j) && !b.occupiedOrBounds((byte)(i+1),j) 
				&& !b.occupiedOrBounds(i,(byte)(j-1)) && !b.occupiedOrBounds(i,(byte)(j+1)) ) {
			return tp;
		}
		
		final byte one  =  1;
		final byte two  =  2;
		final byte five =  5;
		
		if (i>0){
			take = b.getPiece((byte)(i-one),j);
			if (i >1){
				help = b.getPiece((byte)(i-two),j);
				if (take!=null) {
					
					if (take.getColour() != col && (take.getChar() == 'P' || take.getChar() == 'p')){
						if (help!=null){
							if (help.getColour() == col){
								tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
								tp.setTake(true);
							}
						} else if ( (i-2==0 && j == 0) || (i-2==0 && j == 10) || 
								((i-2==5 && j == 5) && (b.getPiece(five,five)==null || b.getPiece(five,five).getColour() == col )) ) {
							tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
							tp.setTake(true);
						}
					}
				}
			}
		}
		
		
		if(i<10){
			take = b.getPiece((byte)(i+one),j);
			if(i<9) {
				help = b.getPiece((byte)(i+two),j);
				if (take!=null) {
					if (take.getColour() != col && (take.getChar() == 'P' || take.getChar() == 'p')){
						if (help!=null){
							if (help.getColour() == col){
								tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
								tp.setTake(true);
							}
						} else if ( (i+2==10 && j == 0) || (i+2==10 && j == 10) || 
								((i+2==5 && j == 5) && (b.getPiece(five,five)==null || b.getPiece(five,five).getColour() == col )) ) {
							tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
							tp.setTake(true);
						}
					}
				}
			}
		}
		
		
		if(j>0){
			
			take = b.getPiece(i,(byte)(j-one));
			if(j>1){
				help = b.getPiece(i,(byte)(j-two));
				if (take!=null) {
					if (take.getColour() != col && (take.getChar() == 'P' || take.getChar() == 'p')){
						if (help!=null){
							if (help.getColour() == col){
								tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
								tp.setTake(true);
							}
						} else if ( (i==10 && j-2 == 0) || (i==0 && j-2 == 0) || 
								((i==5 && j-2 == 5) && (b.getPiece(five,five)==null || b.getPiece(five,five).getColour() == col )) ) {
							tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
							tp.setTake(true);
						}
					}
				}
			}
			
		}
		
		if(j<10){
			take = b.getPiece(i,(byte)(j+one));
			if(j<9){
				help = b.getPiece(i,(byte)(j+two));
				if (take!=null) {
					if (take.getColour() != col && (take.getChar() == 'P' || take.getChar() == 'p')){
						if (help!=null){
							if (help.getColour() == col){
								tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
								tp.setTake(true);
							}
						} else if ( (i==10 && j+2 == 10) || (i==0 && j+2 == 10) || 
								((i==5 && j+2 == 5) && (b.getPiece(five,five)==null || b.getPiece(five,five).getColour() == col )) ) {
							tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
							tp.setTake(true);
						}
					}
				}
			}
		}
		
		// Code that will be needed to take a king. Check from all the sides
		if (col != PieceCode.WHITE) {
			// From below
			
			take = b.getPiece(i,(byte)(j+one));
			help = b.getPiece(i,(byte)(j+two));//above
			help1 = b.getPiece((byte)(i+one),(byte)(j+one));//left
			help2 = b.getPiece((byte)(i-one),(byte)(j+one));//right

			if (take!=null) {
				if (take.getColour() != col && (take.getChar() == 'K' || take.getChar() == 'k')) {
					
					if ( (help!=null || (i==5 && j+2 ==5) || isCorner(i, (byte)(j+2)) || b.outOfRange(i, (byte)(j+2)) ) 
						 && (help1 != null || (i+1==5 && j+1 ==5) || isCorner((byte)(i+1), (byte)(j+1)) || b.outOfRange((byte)(i+1), (byte)(j+1)) ) 
							&& (help2 != null || (i-1==5 && j+1 ==5) || isCorner((byte)(i-1), (byte)(j+1)) || b.outOfRange((byte)(i-1), (byte)(j+1)) ) ) {
						if (help == null) {
							if(help1 == null) {
								if (help2.getColour() == col) {
									tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
									tp.setTake(true);
									tp.setGameOver(true);
								}
							} else if (help2 == null) {
								if (help1.getColour() == col) {
									tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
									tp.setTake(true);
									tp.setGameOver(true);
								}
							} else {
								if (help1.getColour() == col && help2.getColour() == col) {
									tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
									tp.setTake(true);
									tp.setGameOver(true);
								}
							}
						} 
						
						if (help1 == null) {
							if (help2 == null) {
								if (help.getColour() == col) {
									tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
									tp.setTake(true);
									tp.setGameOver(true);
								}
							} else if (help!=null) {
								if (help.getColour() == col && help2.getColour() == col) {
									tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
									tp.setTake(true);
									tp.setGameOver(true);
								}
							}
						} 
						
						if (help2 == null) {
							if(help != null && help1 != null) {
								if (help.getColour() == col && help1.getColour() == col) {
									tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
									tp.setTake(true);
									tp.setGameOver(true);
								}
							}
						} 
						
						if(help != null && help1 != null && help2 != null) {
							if ( help1.getColour() == col 
										&& help.getColour() == col
										&& help2.getColour() == col){
								tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
								tp.setTake(true);
								tp.setGameOver(true);
							}
						}
					}
				}
			}
			
			
			// From above
			
			take = b.getPiece(i,(byte)(j-one));
			help = b.getPiece(i,(byte)(j-two));
			help1 = b.getPiece((byte)(i-one),(byte)(j-one));
			help2 = b.getPiece((byte)(i+one),(byte)(j-one));
			
			if (take!=null) {
				if (take.getColour() != col && (take.getChar() == 'K' || take.getChar() == 'k')) {
					if ( (help!=null || (i==5 && j-2 ==5) || isCorner(i, (byte)(j-2)) || b.outOfRange(i, (byte)(j-2)) ) 
							&& (help1 != null || (i-1==5 && j-1 ==5) || isCorner((byte)(i-1), (byte)(j-1)) || b.outOfRange((byte)(i-1), (byte)(j-1)) ) 
							&& (help2 != null || (i+1==5 && j-1 ==5) || isCorner((byte)(i+1), (byte)(j-1)) || b.outOfRange((byte)(i+1), (byte)(j-1)) ) ) {
						if (help == null) {
							if(help1 == null) {
								if (help2.getColour() == col) {
									tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
									tp.setTake(true);
									tp.setGameOver(true);
								}
							} else if (help2 == null) {
								if (help1.getColour() == col) {
									tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
									tp.setTake(true);
									tp.setGameOver(true);
								}
							} else {
								if (help1.getColour() == col && help2.getColour() == col) {
									tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
									tp.setTake(true);
									tp.setGameOver(true);
								}
							}
						} 
						
						if (help1 == null) {
							if (help2 == null) {
								if (help.getColour() == col) {
									tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
									tp.setTake(true);
									tp.setGameOver(true);
								}
							} else if (help!=null) {
								if (help.getColour() == col && help2.getColour() == col) {
									tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
									tp.setTake(true);
									tp.setGameOver(true);
								}
							}
						} 
						
						if (help2 == null) {
							if(help != null && help1 != null) {
								if (help.getColour() == col && help1.getColour() == col) {
									tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
									tp.setTake(true);
									tp.setGameOver(true);
								}
							}
						} 
						
						if(help != null && help1 != null && help2 != null) {
							if ( help1.getColour() == col 
										&& help.getColour() == col
										&& help2.getColour() == col){
								tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
								tp.setTake(true);
								tp.setGameOver(true);
							}
						}
					}
				}
			}
			
			
			
			// From left
			
			take = b.getPiece((byte)(i+one),j);
			help = b.getPiece((byte)(i+two),j);
			help1 = b.getPiece((byte)(i+one),(byte)(j+one));
			help2 = b.getPiece((byte)(i+one),(byte)(j-one));
			

			if (take!=null) {
				if (take.getColour() != col && (take.getChar() == 'K' || take.getChar() == 'k')){
					if ( (help!=null || (i+2==5 && j ==5) || isCorner((byte)(i+2), (byte)(j)) || b.outOfRange((byte)(i+2), (byte)(j)) ) 
							&& (help1 != null || (i+1==5 && j+1 ==5) || isCorner((byte)(i+1), (byte)(j+1)) || b.outOfRange((byte)(i+1), (byte)(j+1)) ) 
							&& (help2 != null || (i+1==5 && j-1 ==5) || isCorner((byte)(i+1), (byte)(j-1)) || b.outOfRange((byte)(i+1), (byte)(j-1)) ) ) {
						
						if (help == null) {
							if(help1 == null) {
								if (help2.getColour() == col) {
									tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
									tp.setTake(true);
									tp.setGameOver(true);
								}
							} else if (help2 == null) {
								if (help1.getColour() == col) {
									tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
									tp.setTake(true);
									tp.setGameOver(true);
								}
							} else {
								if (help1.getColour() == col && help2.getColour() == col) {
									tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
									tp.setTake(true);
									tp.setGameOver(true);
								}
							}
						} 
						
						if (help1 == null) {
							if (help2 == null) {
								if (help.getColour() == col) {
									tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
									tp.setTake(true);
									tp.setGameOver(true);
								}
							} else if (help!=null) {
								if (help.getColour() == col && help2.getColour() == col) {
									tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
									tp.setTake(true);
									tp.setGameOver(true);
								}
							}
						} 
						
						if (help2 == null) {
							if(help != null && help1 != null) {
								if (help.getColour() == col && help1.getColour() == col) {
									tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
									tp.setTake(true);
									tp.setGameOver(true);
								}
							}
						} 
						
						if(help != null && help1 != null && help2 != null) {
							if ( help1.getColour() == col 
										&& help.getColour() == col
										&& help2.getColour() == col){
								tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
								tp.setTake(true);
								tp.setGameOver(true);
							}
						}
						
					}
				}
			}
			
				
			take = b.getPiece((byte)(i-one),j);
			help = b.getPiece((byte)(i-two),j);
			help1 = b.getPiece((byte)(i-one),(byte)(j-one));
			help2 = b.getPiece((byte)(i-one),(byte)(j+one));
			
			if (take!=null) {
				if (take.getColour() != col && (take.getChar() == 'K' || take.getChar() == 'k')){
					if ( (help!=null ||( i-2==5 && j ==5) || isCorner((byte)(i-2), (byte)(j)) || b.outOfRange((byte)(i-2), (byte)(j)) ) 
							&& (help1 != null || (i-1==5 && j-1 ==5) || isCorner((byte)(i-1), (byte)(j-1)) || b.outOfRange((byte)(i-1), (byte)(j-1)) ) 
							&& (help2 != null || (i-1==5 && j+1 ==5) ||isCorner((byte)(i-1), (byte)(j+1)) || b.outOfRange((byte)(i-1), (byte)(j+1)) ) ) {
						
						if (help == null) {
							if(help1 == null) {
								if (help2.getColour() == col) {
									tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
									tp.setTake(true);
									tp.setGameOver(true);
								}
							} else if (help2 == null) {
								if (help1.getColour() == col) {
									tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
									tp.setTake(true);
									tp.setGameOver(true);
								}
							} else {
								if (help1.getColour() == col && help2.getColour() == col) {
									tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
									tp.setTake(true);
									tp.setGameOver(true);
								}
							}
						} 
						
						if (help1 == null) {
							if (help2 == null) {
								if (help.getColour() == col) {
									tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
									tp.setTake(true);
									tp.setGameOver(true);
								}
							} else if (help!=null) {
								if (help.getColour() == col && help2.getColour() == col) {
									tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
									tp.setTake(true);
									tp.setGameOver(true);
								}
							}
						} 
						
						if (help2 == null) {
							if(help != null && help1 != null) {
								if (help.getColour() == col && help1.getColour() == col) {
									tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
									tp.setTake(true);
									tp.setGameOver(true);
								}
							}
						} 
						
						if(help != null && help1 != null && help2 != null) {
							if ( help1.getColour() == col 
										&& help.getColour() == col
										&& help2.getColour() == col){
								tp.getPiece().add(new PieceCoordinates(take.getX(), take.getY()));
								tp.setTake(true);
								tp.setGameOver(true);
							}
						}
					}
				}
			}
			
		}

		return tp;
	}
	
	public static short cornerCheck(char[][] data){
		short rtn = 0;
		
		final short PIECE_WGT = 10;
		
		if(data[0][2] == 'P'){
			rtn += PIECE_WGT;
		}
		if(data[1][1] == 'P'){
			rtn += PIECE_WGT*2;
		}
		if(data[2][0] == 'P'){
			rtn += PIECE_WGT;
		}
		if(data[8][0] == 'P'){
			rtn += PIECE_WGT;
		}
		if(data[9][1] == 'P'){
			rtn += PIECE_WGT*2;
		}
		if(data[10][2] == 'P'){
			rtn += PIECE_WGT;
		}
		if(data[1][9] == 'P'){
			rtn += PIECE_WGT*2;
		}
		if(data[0][8] == 'P'){
			rtn += PIECE_WGT;
		}
		if(data[2][10] == 'P'){
			rtn += PIECE_WGT;
		}
		if(data[9][9] == 'P'){
			rtn += PIECE_WGT*2;
		}
		if(data[8][10] == 'P'){
			rtn += PIECE_WGT;
		}
		if(data[10][8] == 'P'){
			rtn += PIECE_WGT;
		}
		
		return rtn;
	}
	
	
	public static boolean isCorner(byte x, byte y){
		return (x==0 && y==0) || (x==0 && y==10)|| (x==10 && y==10) || (x==10 && y==0);
	}
	
	public static boolean kingIsCorner(char pc, byte x, byte y){
		return pc=='k' && isCorner(x,y);
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
		
		for(byte q=1; q<7;q++){
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
	
	private static boolean occupiedOrBounds(char[][] board, byte x, byte y){
		return !(x < 11 && x > 0 && y > 0 && y < 11) || board[x][y] != 'x';
	}
	
}
