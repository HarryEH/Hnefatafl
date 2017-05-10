package io.howarth.move;

public class MoveWeight {
	
	private Move m;
	private short weight;
	
	public MoveWeight(Move move, short weight){
		this.m = move;
		this.weight = weight;
	}
	
	// Get
	public Move getMove(){
		return this.m;
	}
	
	public short getWeight(){
		return this.weight;
	}
	
	// Set 
	public void setWeight(short w){
		this.weight = w;
	}
	
}
