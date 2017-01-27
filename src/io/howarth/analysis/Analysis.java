package io.howarth.analysis;

import io.howarth.Board;
import io.howarth.Move;
import io.howarth.pieces.Piece;

import java.util.ArrayList;

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
	
	public static ArrayList<GameStatus> oppoMoves(Piece[][] pieces, Move m){
		
		ArrayList<GameStatus> oppoMoves = new ArrayList<>();
		
		Piece[][] pcs = new Piece[11][11];
		
		for(int i=0; i<11; i++){
			for(int j=0; j<11; j++){
				if(pieces[i][j]!=null){
					pcs[i][j] = pieces[i][j].copy();
				} else {
					pcs[i][j] = null;
				}
				
			}
		}
		
		// Construct new oppoPlayer for each move
		Board newB = new Board();
		newB.setPieces(pcs);
		
		newB.remove(m.getX(), m.getY());
		newB.setPosition(m.getI(), m.getJ(), m.getPiece());
		
		if(m.getTruth().getTake()){
			newB.getData()[m.getI()][m.getJ()] = null;
		}
		
		
		for(int i=0; i<11; i++){
			for(int j=0; j<11; j++){
				Piece p = newB.getData()[i][j];
				if (p != null){
					p.setBoard(newB);
					if (p.getColour() != m.getPiece().getColour()){
						ArrayList<Move> pieceMoves = p.availableMoves();
						if (pieceMoves!=null) {
							for(Move move : pieceMoves) {
								oppoMoves.add(new GameStatus(newB, move));
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
	
	public static ArrayList<GameStatus> bfsInitial(Piece[][] pcs, ArrayList<Move> mvs){
		ArrayList<GameStatus> allOppoMoves = new ArrayList<>();
		
		for(Move m : mvs){
			allOppoMoves.addAll(oppoMoves(pcs, m));
		}
		
		return allOppoMoves;
	}
	
	public static ArrayList<GameStatus> bfsGeneral(ArrayList<GameStatus> games){
		ArrayList<GameStatus> gs = new ArrayList<>();
		
		for(GameStatus g : games ){
			gs.addAll(oppoMoves(g.getBoard().getData(), g.getMove()));
		}
		
		return gs;
	}
	
	
}
