package io.howarth.pieces;

import io.howarth.Board;
import io.howarth.Player;


/**
* PieceCode.java
*
* Provides static methods and variables to manage codes for
* different pieces on the chess board, and to return characters
* for display on the console.
*
* @version 1.0 26 January 2015
*
* @author H Howarth
*/

public final class PieceCode {
    
  // static variables for colours and pieces   
  public static final int WHITE = 1;
  
  public static final int KING = 3;
  public static final int PAWN = 2;

  /**
   * method to return the symbol of a piece, given its numerical code and colour
   *
   * @param colour the colour of the piece
   * @param i the number of the piece
   *
   */
  public static char intToChar(int i, int colour) {
    char data;
    switch (i) {
      case KING: if (colour==WHITE) data = 'k'; else data = 'x';
      break;
      default: if (colour==WHITE) data = 'p'; else data = 'P';
    }
    return data;
  }

  /**
   * method to return the numerical code of a piece, given its symbol
   *
   * @param ch the char of the piece
   *
   */
  public static int charToInt(char ch) {
    int i;
    switch (ch) {
      case 'K': case 'k': i = KING;
      break;
      default: i = 2;
    }
    return i;
  }
  
  
  public static Piece charToPiece(char ch, int x, int y, Board b){
	  
	  switch (ch) {
      	case 'k': return new King(x,y,Player.WHITE,b);
      	case 'p': return new Pawn(x,y,Player.WHITE,b);
      	case 'P': return new Pawn(x,y,Player.BLACK,b);
      	default: return null;
	  }
  }

}
