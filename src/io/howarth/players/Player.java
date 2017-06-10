package io.howarth.players;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

import io.howarth.Board;
import io.howarth.Hnefatafl;
import io.howarth.TextHandler;
import io.howarth.analysis.Analysis;
import io.howarth.analysis.AnalysisBoard;
import io.howarth.move.Move;
import io.howarth.move.MoveWeight;
import io.howarth.move.PieceCoordinates;
import io.howarth.move.TakePiece;
import io.howarth.pieces.Piece;
import io.howarth.pieces.Pieces;


/**
 * Player.java 
 *
 * Abstract class to represent a player
 *
 * @version 0.1 12/1/17
 *
 * @author Harry Howarth
 */

public abstract class Player {

	public static final byte BLACK = 0;
	public static final byte WHITE = 1;

	private String name;
	private Pieces pieces;
	private Board board;
	private Player opponent;

	//Move weights
	protected final byte TAKE_PIECE  = 10;

    /**
     * Player's constructor
     * @param name String name
     * @param pieces
     * @param board
     * @param opponent
     */
	public Player (String name, Pieces pieces, Board board, Player opponent) {
		this.name = name;
		this.pieces = pieces;
		this.board = board;
		this.opponent = opponent;
	}

	public Board getBoard() {
		return board;
	}

    /**
     * Getter for the Player's Opponent
     * @return Player Object, the player's opponent
     */
	public Player getOpponent() {
		return this.opponent;
	}

    /**
     * Setter for this.Player's opponent
     * @param p Player - the opponent
     */
	public void setOpponent(Player p) {
		this.opponent = p;
	}

    /**
     * Getter that returns the Pieces object for the player
     * @return Player's Pieces object
     */
	public Pieces getPieces() {
		return pieces;
	}

	/***
	 * Checks to see if the player can make their move.
	 * @return boolean - true if this player can make a move
	 */
	public boolean makeMove(){

		byte zero = 0;
		byte ten  = 10;

		if (getBoard().getPiece(zero,zero) != null || getBoard().getPiece(ten,zero) != null ||
                getBoard().getPiece(zero,ten) != null || getBoard().getPiece(ten,ten) != null ){
			return false;
		}

		for(int i =0;i<pieces.getData().size();i++){
			if (pieces.getData().get(i).toString().equals("k") && pieces.getData().get(i).getColour() == Player.WHITE){
				return true;
			} 
		}

		for(int i =0;i<getOpponent().getPieces().getData().size();i++){
			if (getOpponent().getPieces().getData().get(i).toString().equals("k") && getOpponent().getPieces().getData().get(i).getColour() == Player.WHITE){
				return true;
			} 
		}

		return false;

	}

    /**
     * Method that does the move for a player. Needs to be implemented
     * @return true if a move was made successfully and false if not.
     */
	public abstract boolean doMove();

    /**
     * Move to delete a piece from the Player's list of pieces
     * @param p
     */
	public void deletePiece(Piece p) {
		pieces.delete(p);
	}

    /**
     * Method that implements the toString - just returns the player's name
     * @return a String - the name of the player
     */
	public String toString() {
		return name;
	}

    /**
     * Method to take a list of Moves and return the largest
     * @param moveWeight Arraylist of moves
     * @return the move with the largest weight, or a random move from the list of moves with largest weights
     */
	protected Move maxMove(ArrayList<Move> moveWeight) {

		if (moveWeight.isEmpty()){
			return null;
		}

		Move max = moveWeight.get(0);

		ArrayList<Move> maxWeights = new ArrayList<>();

		for(Move m : moveWeight) {
			if(m.getWeight() > max.getWeight()){
				max = m;
			}
		}

		maxWeights.add(max);

		for (Move m : moveWeight) {
			if(m.getWeight() == max.getWeight()){
				maxWeights.add(m);
			}
		}

		int randomMove = (int)(Math.random()*maxWeights.size());

		if(randomMove > maxWeights.size() && maxWeights.size() != 0) {
			return maxWeights.get(maxWeights.size() - 1);
		} 

		return maxWeights.get(randomMove);
	}

	/**
	 * Method to take a 2d array and returns a copy of it
	 * @param data 2d character array that represents the board
	 * @return 2d character array that represents the board
     */
	protected char[][] arrayCopy(char[][] data){
		char[][] rtn = new char[11][11];
		for(int i=0; i < 11;i++) {
			for (int j = 0; j < 11; j++) {
				rtn[i][j] = data[i][j];
			}
		}
		return rtn;
	}

    /**
     * Method to emit a move over UDP
     *
     * @param m the move you wish to emit over udp
     */
	protected void emitUdpMove(Move m){
		byte x = m.getX();
		byte y = m.getY();
		byte i = m.getI();
		byte j = m.getJ();
		try {

			DatagramSocket clientSocket = new DatagramSocket();
			InetAddress IPAddress = InetAddress.getByName(Hnefatafl.ip);

			byte[] sendData;

			String move = TextHandler.convertNumToLetter(y)+""+(10-x)+"-"+TextHandler.convertNumToLetter(j)+""+(10-i)+"<EOF>";
			sendData = move.getBytes();


			int PORT = 11000;

			if(getPieces().getColour() == WHITE){
				PORT = 12000;
			}

			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, PORT);
			clientSocket.send(sendPacket);

			try { Thread.sleep(150); } catch (InterruptedException e) { }

			clientSocket.close();

		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
