package io.howarth.analysis;

import io.howarth.Board;
import io.howarth.Move;
import io.howarth.pieces.Piece;
import io.howarth.pieces.PieceCode;

import java.util.ArrayList;

public final class Analysis {
	
	public static ArrayList<GameStatus> moves(AnalysisBoard b, int colour){
		
		//Create Board
		Board board = AnalysisBoard.convAB(b);
		
		ArrayList<GameStatus> moves = new ArrayList<>();
		for(Piece[] pcs : board.getData()) {
			for(Piece p : pcs ){
				if(p != null && p.getColour() == colour){
					if(p.availableMoves()!=null){
						for(Move m : p.availableMoves()){
							moves.add(new GameStatus(b,m));
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
	
	
	
}
