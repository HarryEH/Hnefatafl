package io.howarth;

import io.howarth.pieces.Pieces;
import io.howarth.players.Player;
import io.howarth.players.impl.BlackPlayerImpl;
import io.howarth.players.impl.HumanPlayerImpl;
import io.howarth.players.impl.RandomPlayerImpl;
import io.howarth.players.impl.WhitePlayerImpl;

/**
 * TextHandler.java 
 *
 * Class deals with all input and output of the code, it has methods that 
 * converts coordinates and also outputs unicode chess pieces to the Jframe.
 *
 * @version 1.0 30 April 2015
 *
 * @author Harry Howarth 
 */
public abstract class TextHandler {
	
	// Declarations for the three methods below.

	/**
	 * Determines which type the Player is based on the input that has been taken.
	 * @param c the char that determines the Player type
	 * @param s the name of the Player
	 * @param p the set of pieces the Player will use
	 * @param b board object that the Player is using.
	 * @param colour
	 * @return Player object
	 */
	public static Player playerType(char c, String s, Pieces p,Board b, int colour){
		//use a switch to declare the type of player object.
		Player playerWhite = null;
		Player playerBlack = null;
		switch (c){
			case 'A': if (colour == Player.WHITE){
				playerWhite = new HumanPlayerImpl(s,p,b,null);
			} else {
				playerBlack = new HumanPlayerImpl(s,p,b,null);
			}
			break;
			case 'C': if (colour == Player.WHITE) {
				playerWhite = new RandomPlayerImpl(s,p,b,null);
			} else {
				playerBlack = new RandomPlayerImpl(s,p,b,null);
			}
			break;
			default: if (colour == Player.WHITE) {
				playerWhite = new WhitePlayerImpl(s,p,b,null);
			} else {
				playerBlack = new BlackPlayerImpl(s,p,b,null);
			}
			break;
		}
		//use int colour to decide between playerWhite and playerBlack each time.

		if (colour == Player.WHITE) {
			return playerWhite;
		}
		else {
			return playerBlack;
		}
	}
	
//	/**
//	 * Converts a char to a chess piece image.
//	 * @param ch the char of each piece.
//	 * @return Icon that shows a chess piece
//	 */
//	public ImageIcon convert(char ch){
//		//convert the data[j][i] to unicode chess pieces, for the jframe window.
//		ImageIcon icon = new ImageIcon("icons/Chess_plt60.png");
//		switch (ch){
//		case 'p': icon = new ImageIcon(getClass().getResource("icons/Chess_plt60.png"));
//		break;
//		case 'P': icon = new ImageIcon(getClass().getResource("icons/Chess_pdt60.png"));
//		break;
//		case 'k': icon = new ImageIcon(getClass().getResource("icons/Chess_klt60.png"));
//		break;
//		}
//		return icon;
//	}
	
	
	/**
	 * Convert the string move a3-a4 to x,y,j,i where they are bytes. This method is missing input validation.
	 * 
	 * @param move this is the move in the form 'a5-a2'
	 * @return this is an array of length 4, [x,y,i,j]
	 */
	public static byte[] convertMoveString(String move){
		
		byte[] moveCoords = new byte[4];
		
		int strLen = move.length();
		
		switch(strLen){
		case (5): {
			
			moveCoords[0] = convertLetterToNum(move.charAt(0));
			moveCoords[1] = (byte) (Byte.parseByte(move.substring(1,2)) - 1);
			moveCoords[2] = convertLetterToNum(move.charAt(3));
			moveCoords[3] = (byte) (Byte.parseByte(move.substring(4,5)) - 1);
			
			return moveCoords;
		}
		case (6): {
			
			if(move.charAt(2) == '-'){ // Then the second number is 10 or 11
				moveCoords[0] = convertLetterToNum(move.charAt(0));
				moveCoords[1] = (byte) (Byte.parseByte(move.substring(1,2)) - 1);
				moveCoords[2] = convertLetterToNum(move.charAt(3));
				moveCoords[3] = (byte) (Byte.parseByte(move.substring(4,6)) - 1);
				
				return moveCoords;
			} else { // The first number is 10 or 11
				moveCoords[0] = convertLetterToNum(move.charAt(0));
				moveCoords[1] = (byte) (Byte.parseByte(move.substring(1,3)) - 1);
				moveCoords[2] = convertLetterToNum(move.charAt(4));
				moveCoords[3] = (byte) (Byte.parseByte(move.substring(5,6)) - 1);
				
				return moveCoords;
			}
			
		}
		case (7): {
			
			moveCoords[0] = convertLetterToNum(move.charAt(0));
			moveCoords[1] = (byte) (Byte.parseByte(move.substring(1,3)) - 1);
			moveCoords[2] = convertLetterToNum(move.charAt(4));
			moveCoords[3] = (byte) (Byte.parseByte(move.substring(5,7)) - 1);
			
			return moveCoords;
		}
		default: 
			return new byte[4];
		}// End of switch
	}// End of convertMoveString
	
	/**
	 * 
	 * @param ch this is the char to convert
	 * @return 
	 */
	private static byte convertLetterToNum(char ch){
		switch(ch) {
		case ('a'):
			return 0;
		case ('b'):
			return 1;
		case ('c'):
			return 2;
		case ('d'):
			return 3;
		case ('e'):
			return 4;
		case ('f'):
			return 5;
		case ('g'):
			return 6;
		case ('h'):
			return 7;
		case ('i'):
			return 8;
		case ('j'):
			return 9;
		case ('k'):
			return 10;
		default: 
			return Byte.MIN_VALUE;
		} // end of switch
	} // end of convertLetterToNum
	
	
}
