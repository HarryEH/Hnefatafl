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
	
	
	
	
}
