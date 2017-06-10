package io.howarth;

import io.howarth.pieces.Pieces;
import io.howarth.players.Player;
import io.howarth.players.impl.HumanPlayerImpl;
import io.howarth.players.impl.MonteCarloPlayerImpl;
import io.howarth.players.impl.CommunicationsPlayerImpl;
import io.howarth.players.impl.RandomPlayerImpl;

import javax.swing.*;

/**
 * TextHandler.java 
 *
 * Class deals with all input and output of the code, it has methods that 
 * converts coordinates and also outputs unicode chess pieces to the Jframe.
 *
 * @version 2.0 16/05/2017
 *
 * @author Harry Howarth 
 */
public final class TextHandler {
	
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
		c = new String(c+"").toUpperCase().charAt(0);
		switch (c) {
			case 'A': {
				if (colour == Player.WHITE){
					playerWhite = new CommunicationsPlayerImpl(s, b, p, null);
				} else {
					playerBlack = new CommunicationsPlayerImpl(s, b, p, null);
				}
				break;
			}
			case 'B': {
                if (colour == Player.WHITE) {
                    playerWhite = new RandomPlayerImpl(s, p, b, null);
                } else {
                    playerBlack = new RandomPlayerImpl(s, p, b, null);
                }
                break;
            }
			case 'C': {
				if (colour == Player.WHITE) {
					playerWhite = new MonteCarloPlayerImpl(s, p, b, null);
				} else {
					playerBlack = new RandomPlayerImpl(s, p, b, null);
				}
				break;
			}
			case 'D': {
				if (colour == Player.WHITE) {
					playerWhite = new HumanPlayerImpl(s, p, b, null);
				} else {
					playerBlack = new HumanPlayerImpl(s, p, b, null);
				}
				break;
			}
			default: {
				if (colour == Player.WHITE) {
					playerWhite = new RandomPlayerImpl(s, p, b, null);
				} else {
					playerBlack = new RandomPlayerImpl(s, p, b, null);
				}
				break;
			}
		} // end of switch
		// use int colour to decide between playerWhite and playerBlack each time.

		if (colour == Player.WHITE) {
			return playerWhite;
		}
		else {
			return playerBlack;
		}
	}// end of playerType
	
	/**
	 * Convert the string move a3-a4 to y,x,j,i where they are bytes. This method is missing input validation.
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
			moveCoords[1] = (byte) (10-Byte.parseByte(move.substring(1,2)));
			moveCoords[2] = convertLetterToNum(move.charAt(3));
			moveCoords[3] = (byte) (10-Byte.parseByte(move.substring(4,5)));
			
			return moveCoords;
		}
		case (6): {
			
			if(move.charAt(2) == '-'){ // Then the second number is 10 or 11
				moveCoords[0] = convertLetterToNum(move.charAt(0));
				moveCoords[1] = (byte) (10-Byte.parseByte(move.substring(1,2)));
				moveCoords[2] = convertLetterToNum(move.charAt(3));
				moveCoords[3] = (byte) (10-Byte.parseByte(move.substring(4,6)));
				
				return moveCoords;
			} else { // The first number is 10 or 11
				moveCoords[0] = convertLetterToNum(move.charAt(0));
				moveCoords[1] = (byte) (10-Byte.parseByte(move.substring(1,3)));
				moveCoords[2] = convertLetterToNum(move.charAt(4));
				moveCoords[3] = (byte) (10-Byte.parseByte(move.substring(5,6)));
				
				return moveCoords;
			}
			
		}
		case (7): {
			
			moveCoords[0] = convertLetterToNum(move.charAt(0));
			moveCoords[1] = (byte) (10-Byte.parseByte(move.substring(1,3)));
			moveCoords[2] = convertLetterToNum(move.charAt(4));
			moveCoords[3] = (byte) (10-Byte.parseByte(move.substring(5,7)));
			
			return moveCoords;
		}
		default: 
			return new byte[4];
		}// End of switch
	}// End of convertMoveString
	
	/**
	 * Function to convert a character board position to a numeric one.
     *
	 * @param ch this is the char to convert
	 * @return the board position number that relates to the letter provided
	 */
	private static byte convertLetterToNum(char ch){
		switch(ch) {
		case ('A'):
			return 0;
		case ('B'):
			return 1;
		case ('C'):
			return 2;
		case ('D'):
			return 3;
		case ('E'):
			return 4;
		case ('F'):
			return 5;
		case ('G'):
			return 6;
		case ('H'):
			return 7;
		case ('I'):
			return 8;
		case ('J'):
			return 9;
		case ('K'):
			return 10;
		default: 
			return Byte.MIN_VALUE;
		} // end of switch
	} // end of convertLetterToNum
	
	/**
	 * Function convert a numeric board square to a character
	 *
	 * @param num the number to convert
	 * @return the board position char that relates to the number provided
	 */
	public static char convertNumToLetter(byte num){
		switch(num) {
		case (0):
			return 'a';
		case (1):
			return 'b';
		case (2):
			return 'c';
		case (3):
			return 'd';
		case (4):
			return 'e';
		case (5):
			return 'f';
		case (6):
			return 'g';
		case (7):
			return 'h';
		case (8):
			return 'i';
		case (9):
			return 'j';
		case (10):
			return 'k';
		default: 
			return 'x';
		} // end of switch
	} // end of convertLetterToNum

    /**
     * Converts a char to a chess piece image.
     * @param ch the char of each piece.
     * @return Icon that shows a chess piece
     */
    public ImageIcon convert(char ch){
        //convert the data[j][i] to unicode chess pieces, for the jframe window.
        ImageIcon icon = new ImageIcon("icons/Chess_plt60.png");
        switch (ch){
            case 'p': icon = new ImageIcon(getClass().getResource("icons/Chess_plt60.png"));
                break;
            case 'P': icon = new ImageIcon(getClass().getResource("icons/Chess_pdt60.png"));
                break;
            case 'k': icon = new ImageIcon(getClass().getResource("icons/Chess_klt60.png"));
                break;
        }
        return icon;
    }
	
	
}
