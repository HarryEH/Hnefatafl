package io.howarth;

import java.util.ArrayList;

public  class Analysis {
	
	
	public static ArrayList<Move> moves(Pieces p){
		ArrayList<Move> moves = new ArrayList<>();
		for(Piece pc : p.getData()){
			if(pc.availableMoves()!=null){
				moves.addAll(pc.availableMoves());
			}
		}
		return moves;
	}
	
	public static ArrayList<Move> oppoMoves(Pieces p, Board b, Move m){
		
		ArrayList<Move> oppoMoves = new ArrayList<>();
		//construct new oppoPlayer for each move
			
		b.remove(m.getX(), m.getY());
		b.setPosition(m.getI(), m.getJ(), m.getPiece());
		
		if(m.getTruth().getTake()){
			p.delete(m.getTruth().getPiece());
		}
		
		oppoMoves.addAll(moves(p));
		
		//give each piece in pieces the new board
		//move the pieces if required
		//then get the number of moves for those pieces	

		//get opponent 

		return oppoMoves;
	}
	
	public static ArrayList<Move> bfsOne(int colour){
		
		Board b = new Board();
		Pieces p = new Pieces(b,colour);
		
		ArrayList<Move> moves = moves(p);
		ArrayList<Move> output = new ArrayList<>();
		
		for(Move m : moves){
			
			b = new Board();
			output.addAll(oppoMoves(new Pieces(b,colour),b, m));
			
		}
		
		return output;
	}
	
	public static ArrayList<Move> bfsTwo(int colour){
		
		Board b = new Board();
		Pieces p = new Pieces(b,colour);
		
		ArrayList<Move> moves = moves(p);
		ArrayList<Move> output = new ArrayList<>();
		
		for(Move m : moves){
			
			b = new Board();
			output.addAll(oppoMoves(new Pieces(b,colour),b, m));
			
		}
		
		return output;
	}
	
}
