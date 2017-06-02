package io.howarth.players;
import java.util.ArrayList;

import io.howarth.Board;
import io.howarth.Hnefatafl;
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
	protected final byte  START       = 0;
	protected final short WIN         = 300;
	protected final byte  TAKE_PIECE  = 10;
	//protected final short LOSE_PIECE  = 3;

	public Player (String n, Pieces p, Board b, Player o) {
		name = n;
		pieces = p;
		board = b;
		opponent = o;
	}

	public Board getBoard() {
		return board;
	}

	public Player getOpponent() {
		return opponent;
	}

	public void setOpponent(Player p) {
		opponent = p;
	}

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

		if (Hnefatafl.b.getPiece(zero,zero) != null || Hnefatafl.b.getPiece(ten,zero) != null ||
				Hnefatafl.b.getPiece(zero,ten) != null || Hnefatafl.b.getPiece(ten,ten) != null ){
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
	//Added abstract method for classes that extend Player.
	public abstract boolean doMove();

	public void deletePiece(Piece p) {
		pieces.delete(p);
	}

	public String toString() {
		return name;
	}

	protected ArrayList<MoveWeight> weightBlackMoves(ArrayList<Move> mvs, char[][] data){

		// Convert to List of MoveWeight objects
		ArrayList<MoveWeight> moveWeight = new ArrayList<>(mvs.size());
		
		for(Move m : mvs) {

			AnalysisBoard b = new AnalysisBoard(arrayCopy(data));
			
			short kingToCorner = Analysis.kingToCorner(b.getData());
			
			if(kingToCorner == 0){
				kingToCorner = 10;
			}

			TakePiece q = Analysis.analyseBoard(m, AnalysisBoard.convAB(b));

			b.remove(m.getX(), m.getY());
			
			b.setPosition(m.getI(), m.getJ(), m.getPiece().getChar());

			// take piece weight
			short takePiece = 0;

			if (q.getTake()) {
				for(PieceCoordinates p : q.getPiece()) {
					if (b.getPiece(p.getX(),p.getY()) == 'k') {
						takePiece += WIN;
					}
					takePiece += TAKE_PIECE;
					b.remove(p.getX(),p.getY());
				}
			}

			short weight = (short) ((kingToCorner * 11) + takePiece);

			moveWeight.add( new MoveWeight(m, weight, b.getData(), q) );

		}

		return moveWeight;

	}

	protected ArrayList<MoveWeight> weightWhiteMoves(ArrayList<Move> mvs, char[][] data){

		// Convert to List of MoveWeight objects
		ArrayList<MoveWeight> moveWeight = new ArrayList<>(mvs.size());

		for (Move m : mvs) {

			AnalysisBoard b = new AnalysisBoard(arrayCopy(data));

			TakePiece q = Analysis.analyseBoard(m, AnalysisBoard.convAB(b));
			
			b.remove(m.getX(),m.getY());

			b.setPosition(m.getI(),m.getJ(),m.getPiece().getChar());

			// take piece weight
			short takePiece = 0;

			if ( q.getGameOver() ) {
				takePiece += WIN;
			}
			
			if (q.getTake()) {
				for(PieceCoordinates p : q.getPiece()){
					takePiece += TAKE_PIECE;
					b.remove(p.getX(),p.getY());
				}
			}

			byte kingToCorner = Analysis.kingToCorner(data);

			if (kingToCorner == 0) {
				kingToCorner = 10;
			}

			short weight = (short) (((10-kingToCorner)*11) + takePiece);

			moveWeight.add( new MoveWeight(m, weight, b.getData(),q) );

		}

		return moveWeight;


	}

	protected MoveWeight maxMoveWeight(ArrayList<MoveWeight> moveWeight) {

		if (moveWeight.isEmpty()){
			return null;
		}

		MoveWeight max = moveWeight.get(0);

		ArrayList<MoveWeight> maxWeights = new ArrayList<>();

		for(MoveWeight m : moveWeight) {
			if(m.getWeight() > max.getWeight()){
				max = m;
			}
		}

		maxWeights.add(max);

		for (MoveWeight m : moveWeight) {
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
	
	protected short moveWeight(ArrayList<MoveWeight> moveWeight){
		if(moveWeight.isEmpty()) return 0;
		
		short weight = 0;
		
		double d_2 = 0.5;
		//double d_3 = 0.036;
		
		for(MoveWeight move : moveWeight){
			short counter = 0;
			if(move!= null) {
				switch (counter) {
				case (0):
					weight += move.getWeight();
					break;
				case (1):
					weight -= move.getWeight();
					break;
				case (2):
					weight += (short)(d_2*move.getWeight());
					break;
				case (3):
					weight -= (short)(d_2*move.getWeight());
					break;
				}
				counter++;
			}	
		}
		
		return weight;
	}
	
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
	
	protected char[][] arrayCopy(char[][] data){
		char[][] rtn = new char[11][11];
		for(int i=0; i < 11;i++){
			for(int j=0; j < 11;j++){
				rtn[i][j] = data[i][j];
			}
		}
		return rtn;
	}

}
