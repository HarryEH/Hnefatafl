package io.howarth;

import io.howarth.pieces.Pieces;
import io.howarth.players.Player;
import io.howarth.players.impl.WhitePlayerImpl;
import io.howarth.players.impl.HumanPlayerImpl;
import io.howarth.players.impl.MatlabPlayerImpl;
import io.howarth.players.impl.RandomDepthPlayerImpl;
import io.howarth.players.impl.RandomPlayerImpl;

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
	
	//Declarations for the three methods below.

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
		case 'B': if (colour == Player.WHITE){
			playerWhite = new RandomPlayerImpl(s,p,b,null);
		} else {
			playerBlack = new RandomPlayerImpl(s,p,b,null);
		}
		break;
		case 'C': if (colour == Player.WHITE){
			playerWhite = new WhitePlayerImpl(s,p,b,null);
		} else {
			playerBlack = new MatlabPlayerImpl(s,p,b,null);
		}
		break;
		case 'D': if (colour == Player.WHITE){
			playerWhite = new RandomDepthPlayerImpl(s,p,b,null);
		} else {
			playerBlack = new RandomDepthPlayerImpl(s,p,b,null);
		}
		break;
		case 'E': if (colour == Player.WHITE){
			playerWhite = new WhitePlayerImpl(s,p,b,null);
		} else {
			playerBlack = new WhitePlayerImpl(s,p,b,null);
		}
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
}
