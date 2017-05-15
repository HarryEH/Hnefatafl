package io.howarth.analysis;

import java.util.ArrayList;

public class Coords {

	private byte x;
	private byte y;
	private boolean corner;
	private ArrayList<Coords> next;
	
	public Coords(byte x, byte y, boolean corner){
		this.x = x;
		this.y = y;
		this.corner = corner;
	}
	
	public Coords(byte x, byte y){
		this.x = x;
		this.y = y;
		this.corner = false;
	}
	
	public ArrayList<Coords> getFutureCoords(){
		return this.next;
	}
	
	public void setFutureCoords(ArrayList<Coords> in){
		this.next = in;
	}
	
	public byte getX(){
		return this.x;
	}
	
	public byte getY(){
		return this.y;
	}
	
	public boolean getCorner(){
		return this.corner;
	}
}
