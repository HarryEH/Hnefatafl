package io.howarth.move;

public class MoveWeight {
	
	private Move m;
	private short weight;
	private char[][] data;
	private TakePiece tp;
	
	public MoveWeight(Move move, short weight, char[][] data, TakePiece tp){
		this.m = move;
		this.weight = weight;
		this.data = data;
		this.tp = tp;
	}
	
	// Get
	public Move getMove(){
		return this.m;
	}
	
	public short getWeight(){
		return this.weight;
	}
	
	public char[][] getData(){
		return this.data;
	}
	
	public TakePiece getTakePiece(){
		return this.tp;
	}
	
	// Set 
	public void setWeight(short w){
		this.weight = w;
	}
	
<<<<<<< HEAD
	public void setData(char[][] data){
		this.data = data;
	}
	
=======
>>>>>>> origin/no-display-udp-version
	@Override
	public boolean equals(Object obj){
		if (obj instanceof MoveWeight){
			return ((MoveWeight) obj).getMove().equals(this.m) && this.weight == ((MoveWeight) obj).getWeight();
		} else {
			return false;
		}
	}
	
}
