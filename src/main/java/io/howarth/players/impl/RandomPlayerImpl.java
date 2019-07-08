package io.howarth.players.impl;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import io.howarth.Board;
import io.howarth.Hnefatafl;
import io.howarth.TextHandler;
import io.howarth.analysis.Analysis;
import io.howarth.move.Move;
import io.howarth.move.PieceCoordinates;
import io.howarth.move.TakePiece;
import io.howarth.pieces.Piece;
import io.howarth.pieces.Pieces;
import io.howarth.players.Player;

/**
 * RandomPlayer.java
 * <p>
 * Concrete Class to represent a random computer generated chess player.
 *
 * @author Harry Howarth
 * @version 1.0 1st May 2015
 */
public class RandomPlayerImpl extends Player {

    /**
     * This constructs a RandomPlayer object.
     *
     * @param n The name of the player
     * @param p The player's set of Pieces [object]
     * @param b The board object of the game.
     * @param o The Player's opponent.
     */
    public RandomPlayerImpl(String n, Pieces p, Board b, Player o) {
        super(n, p, b, o);
    }

    /**
     * Boolean method -- Allows the RandomPlayer to move by choosing a random move from the availableMoves()
     *
     * @return true when a move has been performed.
     */
    public boolean doMove() {

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }

        Board board = this.getBoard();
        ArrayList<Move> fullList = new ArrayList<Move>();

        Pieces pieces = this.getPieces();
        ArrayList<Piece> pieceList = pieces.getData();

        //use of the for each enhanced for loop.
        for (Piece p : pieceList) {
            ArrayList<Move> instance = p.availableMoves();
            if (instance != null) {
                for (Move m : instance) {
                    fullList.add(m);
                }
            }
        }
        if (fullList != null && !fullList.isEmpty()) {
            int randomMove = (int) (Math.random() * fullList.size());

            Move moveToConvert = fullList.get(randomMove);

            // Convert the move objects parameters to basic types.
            byte x = moveToConvert.getX();
            byte y = moveToConvert.getY();
            byte i = moveToConvert.getI();
            byte j = moveToConvert.getJ();

            TakePiece q = Analysis.analyseBoard(moveToConvert, getBoard());

            Piece piece1 = moveToConvert.getPiece();

            if (q.getTake()) {//true if there is an enemy player to take.
                for (PieceCoordinates p : q.getPiece()) {
                    this.getOpponent().deletePiece(board.getPiece(p.getX(), p.getY()));
                    board.remove(p.getX(), p.getY());
                }
            }

            board.setPosition(i, j, piece1);
            piece1.setPosition(i, j);
            board.remove(x, y);

            if (Hnefatafl.emitMove) {
                emitUdpMove(moveToConvert);
            }

            return true;
        } else {
            return false;
        }
    }
}
