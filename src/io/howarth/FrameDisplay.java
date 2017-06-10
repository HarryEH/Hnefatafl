package io.howarth;
import io.howarth.analysis.Analysis;
import io.howarth.analysis.AnalysisBoard;
import io.howarth.analysis.GameStatus;
import io.howarth.move.Move;
import io.howarth.move.PieceCoordinates;
import io.howarth.pieces.Piece;
import io.howarth.players.Player;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;


/**
 * FrameDisplay.java
 *
 * Class displays the state of the board in a Jframe window using buttons 'squares'
 *  on the board. Couldn't implement starting the game on a button click :(
 *
 * @version 0.1 12/1/17
 * d
 * @author Harry Howarth
 */

@SuppressWarnings("serial")
public class FrameDisplay extends JFrame   {

    private JFrame f = new JFrame("Hnefatafl");

    TextHandler input = new TextHandler();
    JButton[][] button = new JButton[11][11];
    private boolean coordsTest = false;
    private boolean takeTest = false;
    private boolean takenTest = false;
    //for use in the display.
    private char[] charArray = {'A','B','C','D','E','F','G','H','I','J','K'};

    /**
     * Constructor that sets up the Jframe window.
     */
    public FrameDisplay() {
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(650,650);
    }

    public void close(){
        f.setVisible(false);
        f.dispose();
    }

    public void winner(String winner){
        Container winnerPane = new Container();
        winnerPane.setLayout(new BorderLayout());
        winnerPane.add(winnerPanel(winner));
        f.add(winnerPane);
        f.setVisible(true);
    }

    private JPanel winnerPanel(String winner){
        getContentPane().removeAll();
        Container cont = getContentPane();
        cont.setSize(100,100);
        cont.setLayout(new FlowLayout());
        JTextField txt = new JTextField(20);
        txt.setText(winner);
        txt.setEditable(false);
        cont.add(txt);

        txt = new JTextField(30);
        txt.setText("Do nothing to play again");
        txt.setEditable(false);
        cont.add(txt);

        JButton b = new JButton("Quit");
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        cont.add(b);

        return (JPanel) cont;

    }

    /**
     * Calls the createBoardPanel method
     * Make the jframe window visible.
     *
     *
     * @return type is void
     */
    public void showPiecesOnBoard(Player current){
        Container boardPane = new Container();
        boardPane.setLayout(new BorderLayout());
        boardPane.add(createBoardPanel(current, null));//, BorderLayout.CENTER);
        f.add(boardPane);
        f.setVisible(true);
    }


    /**
     * This method fills the JFrame with buttons and creates a board in an 11x11 grid
     * and draws out the board with unicode chess pieces.
     *
     *
     * @return Jpanel which is used in the method showPiecesOnBoard
     */
    private JPanel createBoardPanel(Player current, byte[][] coords){
        Container updatePane = getContentPane();
        updatePane.setLayout(new GridLayout(11,11));
        updatePane.removeAll();
        Piece[][] data = Hnefatafl.b.getData();

        byte currentColour = (byte)current.getOpponent().getPieces().getColour();
        //Get all  next set moves
        AnalysisBoard boardAnal = AnalysisBoard.convB(Hnefatafl.b);
        ArrayList<GameStatus> gsPlusOne = Analysis.gameStatus(boardAnal, currentColour, false);

        for(byte i =0; i<11;i++) {
            for(byte j=0;j<11;j++) {
                coordsTest = false;
                if (coords != null) {

                    loop:
                    for (short jj = 0; jj < coords.length ;jj++){
                        if (j == coords[jj][0] && i == coords[jj][1]){
                            coordsTest = true;
                            if (data[j][i] == null) {
                                button[i][j] = new JButton(charArray[coords[jj][2]] + "" + (coords[jj][3] + 1) + " " + charArray[j] + "" + (i + 1));
                            }   else {
                                button[i][j] = new JButton(charArray[coords[jj][2]] + "" + (coords[jj][3] + 1) + " " + charArray[j] + "" + (i + 1),
                                        input.convert((data[j][i]).toString().charAt(0)));
                            }

                            button[i][j].addActionListener((e) -> {
                                //todo add listener here
                                String moveText = ((JButton)e.getSource()).getText();

                                byte x = 0;
                                for (byte c = 0; c < charArray.length;c++) {

                                    if (charArray[c] == moveText.charAt(0)) {
                                        x = c;
                                        break;
                                    }
                                }

                                byte y;
                                boolean shift = false;
                                if(moveText.charAt(2) != ' '){
                                    y = (byte) (Byte.parseByte(""+moveText.charAt(1)+moveText.charAt(2))- 1);
                                    shift = true;
                                } else {
                                    y = (byte) (Byte.parseByte(""+moveText.charAt(1))-1);
                                }

                                byte x1 = 0;
                                for (byte c =0; c< charArray.length;c++) {
                                    if (shift){
                                        if (charArray[c] == moveText.charAt(4)) {
                                            x1=c;
                                            break;
                                        }
                                    } else {
                                        if (charArray[c] == moveText.charAt(3)) {
                                            x1=c;
                                            break;
                                        }
                                    }

                                }

                                byte y1;

                                if(shift) {
                                    if(moveText.length() >6){
                                        y1 = (byte) (Byte.parseByte(""+moveText.charAt(5)+moveText.charAt(6))-1);
                                    } else {
                                        y1 = (byte) (Byte.parseByte(""+moveText.charAt(5))-1);
                                    }
                                } else {
                                    if(moveText.length() > 5){
                                        y1 = (byte) (Byte.parseByte(""+moveText.charAt(4)+moveText.charAt(5))-1);
                                    } else {
                                        y1 = (byte) (Byte.parseByte(""+moveText.charAt(4))-1);
                                    }
                                }

                                Piece piece1 = Hnefatafl.b.getPiece(x, y);
                                ArrayList<Move> mLis = piece1.availableMoves();
                                Move chosenMove = null;
                                for(Move m : mLis) {
                                    if (m.getI() == x1 && m.getJ()==y1){
                                        chosenMove = m;
                                    }
                                }

                                if (chosenMove != null) {

//                                    if (chosenMove.getTruth().getTake()) {
//                                        // true if there is an enemy player to take.
//                                        // need to chose the correct piece to take
//                                        for(PieceCoordinates p : chosenMove.getTruth().getPiece()){
//                                            current.getOpponent().deletePiece(
//                                                    Hnefatafl.b.getPiece(p.getX(),p.getY()));
//                                            Hnefatafl.b.remove(p.getX(),p.getY());
//                                        }
//                                    }
//
//                                    Hnefatafl.b.setPosition(x1, y1, piece1);
//                                    piece1.setPosition(x1, y1);
//                                    Hnefatafl.b.remove(x,y);
//
//                                    Hnefatafl.moveTest = true;
//
//                                    if (chosenMove.getGameOver()){
//                                        // new panel
//                                        // lets you restart the game somehow
//                                    }

                                }
                            });
                            break loop;
                        }
                    }
                }

                if (data[j][i] == null) {
                    // if the is no piece in this place.
                    if (!coordsTest){
                        button[i][j] = new JButton(" ");
                    }
                }
                else {
                    // draws the unicode chess pieces in place. Uses the convert method
                    // from the TextHandler object input.
                    if (!coordsTest) {
                        button[i][j] = new JButton("", input.convert((data[j][i]).toString().charAt(0)));
                        if (data[j][i].getColour() == current.getPieces().getColour()){
                            // only add the listener if it is the current player's piece.
                            button[i][j].addActionListener((e) -> {
                                // get index of the button pressed
                                int xIndex = -1;
                                int yIndex = -1;
                                loop:
                                for (int y = 0; y < 11; y++) {
                                    for (int x = 0; x < 11; x++) {
                                        if (button[x][y] != null) {
                                            if (button[x][y].equals(e.getSource())) {
                                                // this way round because of the way data array is.
                                                yIndex = y;
                                                xIndex = x;
                                                // stops going round the loop when the match is found.
                                                break loop;
                                            }
                                        }
                                    }
                                }
                                try {
                                    ArrayList<Move> ml = data[yIndex][xIndex].availableMoves();

                                    //TODO
//                                    if (ml!=null){
//                                    	System.out.println("MOVES: "+ml.size());
//
//                                        for(Move m : ml){
//                                        	System.out.println("MOVE: "+m.getI()+", "+m.getJ());
//                                        }
//                                    }

//                                    // code to redraw the board with different colours.
//                                    Container boardPane = new Container();
//                                    boardPane.setLayout(new BorderLayout());
//                                    if (ml != null) {
//
//
//
//                                        byte colour = -1;
//
//                                        if (ml.get(0).getPiece().getColour() == Player.BLACK){
//                                            colour = Player.WHITE;
//                                        } else {
//                                            colour = Player.BLACK;
//                                        }
//
//                                        byte[][] newCoords = new byte[ml.size()][6];
//
//                                        // need a counter so pointless to used enhanced for loop.
//                                        for (int ii =0;ii<ml.size();ii++) {
//
//                                            AnalysisBoard board = AnalysisBoard.convB(Hnefatafl.b);
//
//                                            newCoords[ii][0] = ml.get(ii).getI();
//                                            newCoords[ii][1] = ml.get(ii).getJ();
//                                            newCoords[ii][2] = ml.get(ii).getX();
//                                            newCoords[ii][3] = ml.get(ii).getY();
//                                            // can it take
//                                            byte take = 0;
//                                            if (ml.get(ii).getTruth().getTake()){
//                                                take = 1;
//                                            }
//
//                                            board.remove(ml.get(ii).getX(), ml.get(ii).getY());
//
//                                            if(ml.get(ii).getTruth().getTake()){
//                                                board.remove(ml.get(ii).getI(), ml.get(ii).getJ());
//                                            }
//
//                                            board.setPosition(ml.get(ii).getI(), ml.get(ii).getJ(), ml.get(ii).getPiece().getChar());
//
//                                            ArrayList<GameStatus> gs = Analysis.gameStatus(board, colour, false);
//
//                                            newCoords[ii][4] = take;
//                                            //can it be taken
//
//                                            byte taken = 0;
//
//                                            for(GameStatus g : gs){
//                                                // Only two pieces should've moved at this stage
//
//                                                if (g.getMove().getTruth().getTake()){
//
//                                                    for(PieceCoordinates p : g.getMove().getTruth().getPiece()){
//
//                                                        if(p.getX()==ml.get(ii).getI() && p.getY() ==ml.get(ii).getJ()){
//                                                            taken = 1;
//                                                        }
//                                                    }
//                                                }
//                                            }
//                                            newCoords[ii][5] = taken;
//                                        }
//
//                                        boardPane.add(createBoardPanel(current, newCoords));
//                                        // this gets called when a move has successfully occurred
//                                        // Chess.moveTest = true;
//                                    } else {
//                                        boardPane.add(createBoardPanel(current, null));
//                                    }
//                                    //this actually makes the changes
//                                    f.add(boardPane);
//                                    f.setVisible(true);
                                    //changes the colours and also makes more buttons clickable
                                } catch (ArrayIndexOutOfBoundsException exp) {
                                    exp.printStackTrace();
                                    System.out.println("What you tried to do is not possible");
                                }
                            });
                        }
                    }
                }

                // Colors the board gray/white
                // There are differences in the way to do this for OS X and windows, this was written on a MAC
                // which is why I use setBorderPainted -- been told this wasn't required on windows PCs.
                button[i][j].setOpaque(true);
                button[i][j].setBorderPainted(false);

                if (coordsTest) {

                    //loop around coords to find take / taken stuff
                    for(int xi =0; xi < coords.length;xi++){
                        if( i==coords[xi][1] && j==coords[xi][0]){
                            if (coords[xi][4]==1){
                                takeTest = true;
                            }
                            if (coords[xi][5]==1){
                                takenTest = true;
                            }
                        }
                    }

                    // Two separate logic blocks so that green takes priority
                    if (takenTest){
                        button[i][j].setBackground(Color.RED);
                        takenTest = false;
                    } else {
                        button[i][j].setBackground(Color.BLUE);
                    }

                    if (takeTest){
                        button[i][j].setBackground(Color.GREEN);
                        takeTest = false;
                    }

                } else if ((i==5 && j==5) || (i==0 && j==10) ||
                        (i==0 && j==0) || (i==10 && j==10)
                        || (i==10 && j==0)){
                    button[i][j].setBackground(Color.MAGENTA);
                } else if ((i+(j)) % 2 == 0){
                    button[i][j].setBackground(Color.GRAY);
                }
                else {
                    button[i][j].setBackground(Color.WHITE);
                }

//                for(GameStatus g : gsPlusOne){
//                    if(g.getMove().getTruth().getTake()){
//                        for(PieceCoordinates p : g.getMove().getTruth().getPiece()){
//                            if (p.getX() == j && p.getY()==i){
//                                button[i][j].setBackground(Color.RED);
//                            }
//                        }
//                    }
//                }

                updatePane.add(button[i][j]);
            }
        }
        //casts the container to a JPanel
        return (JPanel) updatePane;
    }

}

