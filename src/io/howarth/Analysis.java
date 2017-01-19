package io.howarth;

import java.util.ArrayList;

public  class Analysis {
	
	
	public static ArrayList<Move> moves(ArrayList<Piece> pcs){
		
		ArrayList<Move> moves = new ArrayList<>();
		for(Piece p : pcs) {
			if(p.availableMoves()!=null){
				moves.addAll(p.availableMoves());
			}
		}
		
		return moves;
	}
	
	public static ArrayList<Move> oppoMoves(Piece[][] pieces, Move m){
		
		ArrayList<Move> oppoMoves = new ArrayList<>();
		
		Piece[][] pcs = new Piece[11][11];
		
		for(int i=0; i<11; i++){
			for(int j=0; j<11; j++){
				pcs[i][j] = pieces[i][j];
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
							oppoMoves.addAll(pieceMoves);
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
	
	public static ArrayList<Move> bfsOne(Piece[][] pcs, ArrayList<Move> mvs){
		ArrayList<Move> allOppoMoves = new ArrayList<>();
		
		for(Move m : mvs){
			allOppoMoves.addAll(oppoMoves(pcs, m));
		}
		
		return allOppoMoves;
	}
	
//	public static ArrayList<Move> bfsTwo(int colour){
//		
//		Board b = new Board();
//		Pieces p = new Pieces(b,colour);
//		
//		ArrayList<Move> moves = moves(p);
//		ArrayList<Move> output = new ArrayList<>();
//		
//		for(Move m : moves){
//			
//			b = new Board();
//			output.addAll(oppoMoves(new Pieces(b,colour),b, m));
//			
//		}
//		
//		return output;
//	}
	
}
