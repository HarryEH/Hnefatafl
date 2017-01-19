package io.howarth;
import io.howarth.pieces.PieceCode;

import java.util.ArrayList;
 

/**
* Piece.java 
*
* Class to represent Pieces
*
* @version 0.1 12/1/17
*
* @author Harry Howarth
*/

public abstract class Piece {

  // instance fields to store piece symbol, location, colour and board
  private char data;
  private int x,y;
  private int colour;
  private Board board;

  // constructor
  public Piece (int i, int ix, int iy, int c, Board b) {
    colour = c;
    
    data = PieceCode.intToChar(i, c);
    x = ix; 
    y = iy;
    board = b;
  }
  
  public boolean equals(Piece p) {
    return (data==p.data) && (x==p.x) && (y==p.y) 
           && (colour==p.colour);
  }

  public abstract ArrayList<Move> availableMoves();
  
  protected abstract TakePiece analyseBoard(int x, int y, int i, int j);

  public char getChar(int i) {
    return PieceCode.intToChar(i, colour);
  }

  public char getChar() {
    return data;
  }

  public void setPosition(int i, int j) {
    x = i; 
    y = j;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public int getColour() {
    return colour;
  }

  public char getColourChar() {
    if (colour==PieceCode.WHITE) return 'w'; else return 'b';
  }

  public Board getBoard() {
    return board;
  }
  
  public void setBoard(Board b) {
	  this.board = b;
  }

  public String toString () {
    return ""+data;
  }

}
