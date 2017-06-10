package io.howarth;

import io.howarth.pieces.Piece;


/**
 * Board.java
 * <p>
 * Class to represent a board
 *
 * @author Harry Howarth
 * @version 0.1 12/1/17
 */
public class Board {

    private static final int BOARD_SIZE = 11;

    // each board has an array of Piece objects, which stores the chess pieces at each location
    // on the board
    private Piece[][] data;

    /**
     * Constructor, creates an 11x11 board for Piece objects. Starts as null throughout
     */
    public Board() {
        data = new Piece[BOARD_SIZE][BOARD_SIZE];
        for (byte i = 0; i < BOARD_SIZE; i++)
            for (byte j = 0; j < BOARD_SIZE; j++) {
                data[i][j] = null;
            }
    }

    /**
     * Method to determine whether a board position is occupied or out of bounds
     *
     * @param i first board coordinate
     * @param j second board coordinate
     * @return method returns true if a particular location is occupied by a piece or out of bounds
     */
    public boolean occupiedOrBounds(byte i, byte j) {
        return outOfRange(i, j) || (data[i][j] != null);
    } // occupied or out of bounds, for while loops

    /**
     * Method to determine whether a board position is occupied
     *
     * @param i first board coordinate
     * @param j secondboard coordinate
     * @return method returns true if the position provided is not equal to null in the 2d array
     */
    public boolean occupied(byte i, byte j) {
        return (data[i][j] != null);
    }

    /**
     * Method to determine whether a provided position is within the range of the board. False is it on the board
     *
     * @param i first board coordinate
     * @param j second board coordinate
     * @return method returns true if a particular location is off the board
     */
    public boolean outOfRange(byte i, byte j) {
        return (i < 0) || (i >= BOARD_SIZE)
                || (j < 0) || (j >= BOARD_SIZE);
    }

    /**
     * Method to remove a piece from the given coordinates on the board
     *
     * @param i first board coordinate
     * @param j second board coordinate
     */
    public void remove(byte i, byte j) {
        data[i][j] = null;
    }

    /**
     * Method to place a piece at a particular location
     *
     * @param i
     * @param j
     * @param p piece to place
     */
    public void setPosition(byte i, byte j, Piece p) {
        data[i][j] = p;
    }

    /**
     * Method to return the chess piece at the given location
     *
     * @param x first part of coordinates
     * @param y second part of coordinates
     * @return the piece at teh given location or null;
     */
    public Piece getPiece(byte x, byte y) {
        if (this.outOfRange(x, y)) return null;

        return data[x][y];
    }

    /**
     * Method to set the array of chess pieces on the board
     *
     * @param data 2d array of piece objects
     */
    public void setPieces(Piece[][] data) {
        this.data = data;
    }

    /**
     * Method to return the array of chess pieces on the board
     *
     * @return array of chess pieces on the board
     */
    public Piece[][] getData() {
        return data;
    }

}
