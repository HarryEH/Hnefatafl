package io.howarth.pieces.impl;

import java.util.ArrayList;

import io.howarth.Board;
import io.howarth.move.Move;
import io.howarth.pieces.Piece;
import io.howarth.pieces.PieceCode;


/**
 * King.java
 * <p>
 * Concrete class to represent a King piece.
 *
 * @author Harry Howarth
 * @version 2.0 12/1/17
 */

public class KingImpl extends Piece {

    public KingImpl(byte ix, byte iy, byte c, Board b) {
        super(PieceCode.KING, ix, iy, c, b);
    }

    /**
     * Method that gets all the available moves of either the black or white piece.Implements
     * abstract method from Piece class.
     *
     * @return ArrayList of Move objects.
     */
    public ArrayList<Move> availableMoves() {
        if (getColour() == PieceCode.WHITE) {
            return whiteKing();
        } else {
            return null;
        }
    }

    private ArrayList<Move> whiteKing() {
        byte x = getX();
        byte y = getY();

        // Make 0 moves faster
        if (getBoard().occupiedOrBounds(x, (byte) (y + 1)) && getBoard().occupiedOrBounds(x, (byte) (y - 1))
                && getBoard().occupiedOrBounds((byte) (x + 1), y) && getBoard().occupiedOrBounds((byte) (x - 1), y)) {
            return null;
        }

        ArrayList<Move> moveLis = new ArrayList<Move>();

        Move m;

        boolean testUp = true;
        boolean testDown = true;
        boolean testRight = true;
        boolean testLeft = true;

        for (byte loopCounter = 1; loopCounter < 11; loopCounter++) {

            if (testUp) { // Generate moves moving 'up' the board

                byte up = (byte) (y + loopCounter);

                if (getBoard().occupiedOrBounds(x, up)) {
                    testUp = false; // Won't come back into this logic block again
                } else {

                    m = new Move(this, x, y, x, up);
                    moveLis.add(m);

                }
            }

            if (testDown) { // Generate moves moving 'down' the board

                byte down = (byte) (y - loopCounter);

                if (getBoard().occupiedOrBounds(x, down)) {
                    testDown = false; // Won't come back into this logic block again
                } else {

                    m = new Move(this, x, y, x, down);
                    moveLis.add(m);
                }
            }

            if (testLeft) { // Generate moves moving 'left' across the board

                byte left = (byte) (x - loopCounter);

                if (getBoard().occupiedOrBounds(left, y)) {
                    testLeft = false; // Won't come back into this logic block again
                } else {


                    m = new Move(this, x, y, left, y);
                    moveLis.add(m);
                }
            }

            if (testRight) { // Generate moves moving 'left' across the board

                byte right = (byte) (x + loopCounter);

                if (getBoard().occupiedOrBounds(right, y)) {
                    testRight = false; // Won't come back into this logic block again
                } else {


                    m = new Move(this, x, y, right, y);
                    moveLis.add(m);
                }
            }

            if (!(testRight || testLeft || testUp || testDown)) {
                return moveLis;
            }
        }
        return moveLis;
    }


}

