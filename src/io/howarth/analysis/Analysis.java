package io.howarth.analysis;

import io.howarth.Move;
import io.howarth.pieces.Piece;
import io.howarth.pieces.PieceCode;

import java.util.ArrayList;
import java.util.Collection;

public final class Analysis {
	
	public static ArrayList<Move> moves(ArrayList<Piece> pcs){
		
		ArrayList<Move> moves = new ArrayList<>();
		for(Piece p : pcs) {
			if(p.availableMoves()!=null){
				moves.addAll(p.availableMoves());
			}
		}
		
		return moves;
	}
	
	private static ArrayList<GameStatus> oppoMoves(AnalysisBoard board, Move m){
		
		ArrayList<GameStatus> oppoMoves = new ArrayList<>();
		
//		// Construct new oppoPlayer for each move
//		for(char[] c : pieces){
//			System.out.println(Arrays.toString(c));
//		}
//		
		char[][] pieces = board.getData();
		
		board.remove(m.getX(), m.getY());
		
		if(m.getTruth().getTake()){
			board.getData()[m.getI()][m.getJ()] = 'x';
		}
		
		board.setPosition(m.getI(), m.getJ(), m.getPiece().getChar());
		
		for(int i=0; i<11; i++){
			for(int j=0; j<11; j++){
				
				Piece p = PieceCode.charToPiece(pieces[i][j], i, j, null);
				if (p != null){
					p.setBoard(AnalysisBoard.convAB(board));
					if (p.getColour() != m.getPiece().getColour()){
						ArrayList<Move> pieceMoves = p.availableMoves();
						
						if (pieceMoves!=null) {
							for(Move move : pieceMoves) {
								oppoMoves.add(new GameStatus(board, move));
							}
							
						}
					}
				}
			}
		}
		
		//give each piece in pieces the new board
		//move the pieces if required
		//then get the number of moves for those pieces	

		//get opponent 

		return oppoMoves;
	}
	
	public static ArrayList<GameStatus> bfsInitial(AnalysisBoard board, ArrayList<Move> mvs){
		ArrayList<GameStatus> allOppoMoves = new ArrayList<>();
		
		for(Move m : mvs){
			allOppoMoves.addAll(oppoMoves(board, m));
		}
		
		return allOppoMoves;
	}
	
	public static ArrayList<GameStatus> bfsGeneral(ArrayList<GameStatus> games){
		ArrayList<GameStatus> gs = new ArrayList<>();
		
		for(GameStatus g : games ){
			gs.addAll(oppoMoves(g.getBoard(), g.getMove()));
		}
		
		return gs;
	}
	
	
}
