package io.howarth.analysis;

import io.howarth.Move;

public class GameStatus {

	private Move move;
	private AnalysisBoard board;
	private double weight = 1;
	
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
	
	public void setWeight(double d){
		this.weight = d;
	}
	
	public double getWeight(){
		return this.weight;
	}
}
