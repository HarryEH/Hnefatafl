package io.howarth.pieces;
import io.howarth.Board;
import io.howarth.move.Move;
import io.howarth.move.TakePiece;

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
  private byte x,y;
  private byte colour;
  private Board board;

  // constructor
  public Piece (byte i, byte ix, byte iy, byte c, Board b) {
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
  
  protected abstract TakePiece analyseBoard(byte x, byte y, byte i, byte j);

  public char getChar(int i) {
    return PieceCode.intToChar(i, colour);
  }

  public char getChar() {
    return data;
  }

  public void setPosition(byte x1, byte y1) {
    x = x1; 
    y = y1;
  }

  public byte getX() {
    return x;
  }

  public byte getY() {
    return y;
  }

  public byte getColour() {
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
