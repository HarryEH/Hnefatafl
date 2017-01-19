package io.howarth.analysis;

import io.howarth.Board;
import io.howarth.Move;

public class GameStatus {

	private Move move;
	private Board board;
	
	GameStatus(Board b, Move m){
		this.move = m;
		this.board = b;
	}
	
	public Move getMove(){return move;}
	
	public Board getBoard(){return board;}
	
	public void setMove(Move m){
		this.move = m;
	}
	
	public void setBoard(Board b){
		this.board = b;
	}
}
