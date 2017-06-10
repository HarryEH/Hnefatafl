package io.howarth;

import io.howarth.pieces.Piece;
import io.howarth.pieces.Pieces;
import io.howarth.players.Player;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Hnefatafl.java
 * <p>
 * Hnefatafl has a main method that runs the game, declares Board, two Pieces, TextHandler (for input)
 * FrameDisplay (for the GUI), two player objects which each declare move objects based on the input
 * that they receive.
 *
 * @author Harry Howarth
 * @version 0.1 12/1/17
 */

public class Hnefatafl {

    public static boolean emitMove = false;
    public static String ip = "localhost";
    public static DatagramSocket serverSocket;
    public static Board b = new Board();
    public static boolean moveTest = false;

    private static long SLEEP_TIME = 500;

    public static int moveNum = 0;

    public static void main(String[] args) throws InterruptedException {
        try {
            run(args); // could do a loop here and just switch round the cmd args every time??? easy way to implement this.
        } catch (Exception e) {
            System.out.println("There was an uncaught exception!");
            e.printStackTrace();
        }
    }

    /***
     * @param args these are the command line input arguments
     * @return void
     */
    private static void run(String[] args) {

        FrameDisplay t = new FrameDisplay();

        final String player1 = "playerOne";
        final String player2 = "playerTwo";

        final int MAX_MOVES = 200;

        if (args.length == 3) {
            if (args[0].length() == 1 && args[1].length() == 1) {


                // Declaration block for any type of player.

                Pieces piecesOne = new Pieces(b, Player.WHITE);
                Pieces piecesTwo = new Pieces(b, Player.BLACK);

                // Command Line args input for player types
                final char playerType1 = args[0].toUpperCase().charAt(0);
                final char playerType2 = args[1].toUpperCase().charAt(0);

                // Set White Player
                Player playerWhite = TextHandler.playerType(playerType1, player1, piecesOne, b, Player.WHITE);

                // Set Black Player
                Player playerBlack = TextHandler.playerType(playerType2, player2, piecesTwo, b, Player.BLACK);

                // Set opponents
                playerWhite.setOpponent(playerBlack);
                playerBlack.setOpponent(playerWhite);

                ip = args[2];

                // Only do this code if we need to so if arg[0] || arg[1] is a but not if they both are

                if ((args[0].toUpperCase().charAt(0) == 'A') || (args[1].toUpperCase().charAt(0) == 'A')) {

                    try {

                        int portIn = -1;
                        InetAddress IPAddress = InetAddress.getByName(ip);

                        if (args[0].toUpperCase().charAt(0) != 'A' && args[1].toUpperCase().charAt(0) == 'A') {
                            portIn = 12001; // AI is white
                        } else if (args[0].toUpperCase().charAt(0) == 'A' && args[1].toUpperCase().charAt(0) != 'A') {
                            portIn = 11001; // AI is black
                        }

                        serverSocket = new DatagramSocket(portIn);
                        // We want to be asked to connect
                        byte[] receiveData = new byte[1024];
                        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                        String s = "";

                        while (!s.toLowerCase().contains("connect")) {
                            serverSocket.receive(receivePacket);
                            s = new String(receivePacket.getData());
                        }

                        //serverSocket.close();

                        byte[] sendData;
                        final String SENTENCE = "connect<EOF>";
                        sendData = SENTENCE.getBytes();

                        int portOut = -1;
                        if (args[0].toUpperCase().charAt(0) != 'A' && args[1].toUpperCase().charAt(0) == 'A') {
                            portOut = 12000; // AI is white
                        } else if (args[0].toUpperCase().charAt(0) == 'A' && args[1].toUpperCase().charAt(0) != 'A') {
                            portOut = 11000; // AI is black
                        }

                        DatagramSocket clientSocket = new DatagramSocket();
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, portOut);

                        clientSocket.send(sendPacket);

                        try {
                            Thread.sleep(150);
                        } catch (InterruptedException e) {
                        }

                        clientSocket.close();

                        if (args[1].toUpperCase().charAt(0) == 'A') {
                            serverSocket.close();
                        }

                        emitMove = true;

                    } catch (SocketException e) {
                        e.printStackTrace();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println(" connection code ignored");
                }

                //this method shows the board on the GUI.
                t.showPiecesOnBoard(playerBlack);

                short counter = 0;
                moveNum = 0;
                loopage:
                while (playerWhite.makeMove() && playerBlack.makeMove() && counter <= 200) {
                    moveTest = false;

                    //Human WHITE PLAYER

                    while (!moveTest && counter <= MAX_MOVES) {

                        if (emitMove && moveNum == 0 && playerType1 == 'A') {
                            try {
                                // We want to be asked to connect
                                byte[] receiveData = new byte[1024];
                                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                                int portIn = -1;

                                if (args[0].toUpperCase().charAt(0) != 'A' && args[1].toUpperCase().charAt(0) == 'A') {
                                    portIn = 12001; // AI is white
                                } else if (args[0].toUpperCase().charAt(0) == 'A' && args[1].toUpperCase().charAt(0) != 'A') {
                                    portIn = 11001; // AI is black
                                }
                                serverSocket = new DatagramSocket(portIn);
                                serverSocket.receive(receivePacket);

                                String hiFromServer = new String(receivePacket.getData());

                                System.out.println(hiFromServer.trim());

                                serverSocket.close();

                            } catch (SocketException e) {
                                e.printStackTrace();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }

                        if (playerType2 == 'D') {
                            try {
                                Thread.sleep(SLEEP_TIME);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            moveTest = playerBlack.doMove();
                            counter++;
                            moveNum++;
                            System.out.println("MOVE NUMBER: " + moveNum);

                            if (!moveTest || counter >= MAX_MOVES) {
                                System.out.println("Escape: " + counter);
                                break loopage;
                            }
                        }
                    } // End of black player while loop

                    if (playerType2 == 'D') {
                        moveNum++;
                        System.out.println("MOVE NUMBER: " + moveNum);

                        if (!moveTest || counter >= MAX_MOVES) {
                            System.out.println("Escape: " + counter);
                            break loopage;
                        }
                    }

                    t.showPiecesOnBoard(playerWhite);

                    if (playerWhite.makeMove()) {
                        // White Player
                        moveTest = false;

                        while (!moveTest || counter >= MAX_MOVES) {

                            if (playerWhite.makeMove()) {

                                //White  PLAYER
                                moveTest = false;

                                while (!moveTest) {
                                    if (playerType1 == 'A') {
                                        try {
                                            Thread.sleep(SLEEP_TIME);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        long w_one = System.nanoTime() / 1000000;
                                        moveTest = playerWhite.doMove();
                                        long w_two = System.nanoTime() / 1000000;
                                        System.out.println("White Time taken: " + (w_two - w_one) + "ms");
                                    }
                                }

                                counter++;
                                moveNum++;
                                System.out.println("MOVE NUMBER: " + moveNum);
                                if (!moveTest || counter >= MAX_MOVES) {
                                    System.out.println("Escape: " + counter);
                                    break loopage;
                                }
                            }
                        }
                    }
                    t.showPiecesOnBoard(playerBlack);
                }// End of game logic while loop

                /**************************************************/
                // Console print of the board
                for (Piece[] p : b.getData()) {
                    for (Piece pI : p) {
                        if (pI != null) System.out.print(pI.toString());
                        else System.out.print("x");
                    }
                    System.out.println("");
                }
                /**************************************************/

                // Resets the board
                b = new Board();


            }// End of if two args of right length
        }// End of if two args
    }// End of void run()

}		


