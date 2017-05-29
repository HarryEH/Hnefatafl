package io.howarth.analysis;

import io.howarth.move.Move;
import io.howarth.move.TakePiece;

public class GameStatus {

	private Move move;
	private AnalysisBoard board;
	
	private short weight;
	private TakePiece tP;
	
	public GameStatus(AnalysisBoard b, Move m){
		this.move = m;
		this.board = b;
	}
	
	public Move getMove(){return move;}
	
	public AnalysisBoard getBoard(){return board;}
	
	public void setMove(Move m){
		this.move = m;
	}
	
	public void setBoard(AnalysisBoard b){
		this.board = b;
	}
	
	public short getWeight(){
		return this.weight;
	}

	public void setWeight(short weight){
		this.weight = weight;
	}
	
	public TakePiece getTakePiece(){
		return this.tP;
	}

	public void setTakePiece(TakePiece tP){
		this.tP = tP;
	}
	
}
