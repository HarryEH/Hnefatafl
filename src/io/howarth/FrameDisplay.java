package io.howarth;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
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
 *
 * @author Harry Howarth 
 */

@SuppressWarnings("serial")
public class FrameDisplay extends JFrame   {

    private JFrame f = new JFrame("Hnefatafl");

    TextHandler input = new TextHandler();
    JButton[][] button = new JButton[11][11];
    private boolean coordsTest = false;
    //for use in the display.
    private char[] charArray = {'A','B','C','D','E','F','G','H','I','J','K'};

    /**
     * Constructor that sets up the Jframe window.
     */
    public FrameDisplay() {
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(850,850);
    }

    /**
     * This makes the start menu visible
     */
    public void startMenu() {
        Container menuPane = new Container();
        menuPane.setLayout(new BorderLayout());
        menuPane.add(menuItem());
        f.add(menuPane);
        f.setVisible(true);
    }


    /**
     * Populates the start menu
     * @return JPanel with start menu content on
     */
    private JPanel menuItem() {
        Container menuPane = getContentPane();
        menuPane.setLayout(new BoxLayout(menuPane, BoxLayout.PAGE_AXIS));


        Container flowPaneText1 = new Container();
        flowPaneText1.setLayout(new FlowLayout());
        JTextField field = new JTextField(20);
        field.setText("Player1");
        flowPaneText1.add(field);

        menuPane.add(flowPaneText1);

        Container flowPaneBut1 = new Container();
        flowPaneBut1.setLayout(new FlowLayout());
        JRadioButton option1 = new JRadioButton("P1 Human");
        JRadioButton option2 = new JRadioButton("P1 Random");

        JRadioButton option3 = new JRadioButton("P1 Aggressive");
        flowPaneBut1.add(option1);
        flowPaneBut1.add(option2);
        flowPaneBut1.add(option3);

        menuPane.add(flowPaneBut1);

        Container flowPaneText2 = new Container();
        flowPaneText2.setLayout(new FlowLayout());
        JTextField field2 = new JTextField(20);
        field2.setText("Player2");
        flowPaneText2.add(field2);
        menuPane.add(flowPaneText2);

        Container flowPaneBut2 = new Container();
        flowPaneBut2.setLayout(new FlowLayout());
        JRadioButton option4 = new JRadioButton("P2 Human");
        JRadioButton option5 = new JRadioButton("P2 Random");
        JRadioButton option6 = new JRadioButton("P2 Aggressive");
        flowPaneBut2.add(option4);
        flowPaneBut2.add(option5);
        flowPaneBut2.add(option6);

        menuPane.add(flowPaneBut2);

        JButton submit = new JButton("Start");
        submit.addActionListener((e)->{
                String player1 = field.getText();
                String player2 = field2.getText();

                //player one type
                if (option1.isSelected()) Hnefatafl.playerType1 = 'A';
                if (option2.isSelected()) Hnefatafl.playerType1 = 'B';
                if (option3.isSelected()) Hnefatafl.playerType1 = 'C';
                //player two type
                if (option4.isSelected()) Hnefatafl.playerType2 = 'A';
                if (option5.isSelected()) Hnefatafl.playerType2 = 'B';
                if (option6.isSelected()) Hnefatafl.playerType2 = 'C';

                if (!( option1.isSelected() || option3.isSelected()|| option2.isSelected())){
                    Hnefatafl.playerType1 = 'C';
                }
                if (!( option4.isSelected() || option5.isSelected()|| option6.isSelected())){
                    Hnefatafl.playerType2 = 'C';
                }

                Hnefatafl.player1=player1;
                Hnefatafl.player2=player2;
                Hnefatafl.truth = false;

        });
        menuPane.add(submit);

        return (JPanel) menuPane;
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
    private JPanel createBoardPanel(Player current, int[][] coords){
        Container updatePane = getContentPane();
        updatePane.setLayout(new GridLayout(11,11));
        updatePane.removeAll();
        Piece[][] data = Hnefatafl.b.getData();
        for(int i =0; i<11;i++) {
            for(int j=0;j<11;j++) {
                coordsTest = false;
                if (coords != null){

                    loop:
                    for (int jj = 0; jj < coords.length ;jj++){
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
                                
                                int x = 0;
                                for (int c = 0; c < charArray.length;c++) {
                                	
                                    if (charArray[c] == moveText.charAt(0)) {
                                        x = c;
                                        break;
                                    }
                                }
                                
                                int y;
                                boolean shift = false;
                                if(moveText.charAt(2) != ' '){
                                	y = Integer.parseInt(""+moveText.charAt(1)+moveText.charAt(2))-1;
                                	shift = true;
                                } else {
                                	y = Integer.parseInt(""+moveText.charAt(1))-1;
                                }
                                
                                int x1 = 0;
                                for (int c =0; c< charArray.length;c++) {
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
                                
                                int y1;
                        
                                if(shift) {
                                	if(moveText.length() >6){
                                		y1 = Integer.parseInt(""+moveText.charAt(5)+moveText.charAt(6))-1;
                                	} else {
                                		y1 = Integer.parseInt(""+moveText.charAt(5))-1;
                                	}
                                } else {
                                	if(moveText.length() > 5){
                                		y1 = Integer.parseInt(""+moveText.charAt(4)+moveText.charAt(5))-1;
                                	} else {
                                		y1 = Integer.parseInt(""+moveText.charAt(4))-1;
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
                                    
                                	System.out.println("Take a piece : "+chosenMove.getTruth().getTake());
                                    
                                	if (chosenMove.getGameOver()){
                                    	// new panel
                                		// lets you restart the game somehow
                                    }
                                	
                                	if (chosenMove.getTruth().getTake()) {
                                		
                                    	// true if there is an enemy player to take.
                                    	// need to chose the correct piece to take
                                		
                                        current.getOpponent().deletePiece(
                                        		Hnefatafl.b.getPiece(chosenMove.getTruth().getPiece().getX(), chosenMove.getTruth().getPiece().getY()));
                                        Hnefatafl.b.remove(chosenMove.getTruth().getPiece().getX(),chosenMove.getTruth().getPiece().getY());
                                    } 
                                    
                                    Hnefatafl.b.setPosition(x1, y1, piece1);
                                    piece1.setPosition(x1, y1);
                                    Hnefatafl.b.remove(x,y);

                                    Hnefatafl.moveTest = true;       
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
                                    
                                    // code to redraw the board with different colours.
                                    Container boardPane = new Container();
                                    boardPane.setLayout(new BorderLayout());
                                    if (ml != null) {
                                        int[][] newCoords = new int[ml.size()][4];

                                        // need a counter so pointless to used enhanced for loop.
                                        for (int ii =0;ii<ml.size();ii++) {
                                            newCoords[ii][0] = ml.get(ii).getI();
                                            newCoords[ii][1] = ml.get(ii).getJ();
                                            newCoords[ii][2] = ml.get(ii).getX();
                                            newCoords[ii][3] = ml.get(ii).getY();

                                        }

                                        boardPane.add(createBoardPanel(current, newCoords));
                                        // this gets called when a move has successfully occurred
                                        // Chess.moveTest = true;
                                    } else {
                                        boardPane.add(createBoardPanel(current, null));
                                    }
                                    //this actually makes the changes
                                    f.add(boardPane);
                                    f.setVisible(true);
                                    //changes the colours and also makes more buttons clickable
                                } catch (ArrayIndexOutOfBoundsException exp) {
                                	exp.printStackTrace();
                                    System.out.println("What you tried to do is not possible");
                                }
                            });
                        }
                    }
                }

                // Colors the board gray/white as a chess board would be.
                // There are differences in the way to do this for OS X and windows, this was written on a MAC
                // which is why I use setBorderPainted -- been told this wasn't required on windows PCs.
                button[i][j].setOpaque(true);
                button[i][j].setBorderPainted(false);

                if (coordsTest) {
                    button[i][j].setBackground(Color.GREEN);
                } else if ((i+(j)) % 2 == 0){
                    button[i][j].setBackground(Color.GRAY);
                }
                else {
                    button[i][j].setBackground(Color.WHITE);
                }

                updatePane.add(button[i][j]);
            }
        }
        //casts the container to a JPanel
        return (JPanel) updatePane;
    }

}

