package io.howarth.players;

import io.howarth.Board;
import io.howarth.Hnefatafl;
import io.howarth.Move;
import io.howarth.Player;
import io.howarth.analysis.Analysis;
import io.howarth.analysis.AnalysisBoard;
import io.howarth.analysis.GameStatus;
import io.howarth.pieces.Piece;
import io.howarth.pieces.PieceCode;
import io.howarth.pieces.Pieces;

import java.util.ArrayList;



/**
 * AggressivePlayer.java 
 *
 * Concrete Class to be an AI player, makes Move object and
 * does the move.
 *
 * @version 1.0 19/1/17
 *
 * @author Harry Howarth 
 */
public class BadlyWeightedPlayer extends Player {

	private String name;
	private Pieces pieces;
	
	public BadlyWeightedPlayer(String n, Pieces p, Board b, Player o) {
		super(n, p, b, o);
		this.name = n;
		this.pieces = p;
	}

	@Override
	public boolean makeMove() {
		if (Hnefatafl.b.getPiece(0,0) != null || Hnefatafl.b.getPiece(10,0) != null ||
				Hnefatafl.b.getPiece(0,10) != null || Hnefatafl.b.getPiece(10,10) != null ){
			return false;
		}
		
		// true is white false is black
		int myColour = pieces.getColour();
		
		if (myColour == PieceCode.WHITE){
			for(int i =0;i<pieces.getData().size();i++){
				if (pieces.getData().get(i).toString().equals("k")){
					return true;
				}
			}
		} else {
			// if opponent is a white then check if it has its king.
			for(int i =0;i<getOpponent().getPieces().getData().size();i++){
				if (getOpponent().getPieces().getData().get(i).toString().equals("k")){
					return true;
				}
			}
		}

		
		return false;
	}

	// This is random obviously
	@Override
	public boolean doMove() {
		Board board = this.getBoard();
		ArrayList<Move> fullList = new ArrayList<Move>();

		Pieces pieces = this.getPieces();
		ArrayList<Piece> pieceList = pieces.getData();

		//use of the for each enhanced for loop.
		for (Piece p: pieceList){
			ArrayList<Move> instance = p.availableMoves();
			if (instance !=null){
				for (Move m: instance){
					fullList.add(m);
				}
			}
		}
		
		// This will take a long time
		Move moveToConvert = weightMoves(fullList);
		
		//convert the move objects parameters to basic types.
		int x = moveToConvert.getX();
		int y = moveToConvert.getY();
		int i = moveToConvert.getI();
		int j = moveToConvert.getJ();
		boolean b = moveToConvert.getTruth().getTake();
		Piece piece1 = moveToConvert.getPiece();

		if (b) {//true if there is an enemy player to take.
			for(Piece p : moveToConvert.getTruth().getPiece()){
				this.getOpponent().deletePiece(p);
				board.remove(p.getX(),p.getY());
			}
		}
		board.setPosition(i, j, piece1);
		piece1.setPosition(i, j);
		board.remove(x,y);
		
		return true;
	}
	
	//Move weights
	private final double START = 1;
	private final double WIN  = 1000;
	private final double LOSE_GAME = -100;
	private final double TAKE_PIECE  = 120;
	private final double AVOID_TAKE = 10;
	private final double LOSE_PAWN  = -150;
	private final double NEITHER = -25;
		
	private Move weightMoves(ArrayList<Move> mvs){
			
		int colour0 = -1;
		int colour1 = -1;
		
		if(mvs.get(0).getPiece().getColour() == Player.WHITE){
			colour0 = Player.WHITE;
			colour1 = Player.BLACK;
		} else {
			colour1 = Player.WHITE;
			colour0 = Player.BLACK;
		}
		
		AnalysisBoard b = AnalysisBoard.convB(Hnefatafl.b);
		ArrayList<GameStatus> gs = new ArrayList<>();
		ArrayList<Double> d = new ArrayList<>();
		
		for(Move m : mvs){
			gs.add(new GameStatus(b,m));
			
			double weight = START;
			
			if(m.getGameOver()){
//				weight += WIN;
			} else if(m.getTruth().getTake()){
				for(Piece p : m.getTruth().getPiece()){
//					weight += TAKE_PIECE;
				}	
			} else {
//				weight += NEITHER;
			}
			
			
			d.add(weight);
		}
		
		
		// First depth analysis
		ArrayList<Board> bds         = Analysis.doMoves(gs);
		ArrayList<GameStatus> bestGs = new ArrayList<>();
		for(Board bd : bds ) {
			ArrayList<Double> weights = new ArrayList<Double>();
			AnalysisBoard board = AnalysisBoard.convB(bd);
			ArrayList<GameStatus> mvs1 = Analysis.moves(board, colour1);
			
			for(GameStatus g : mvs1) {
				double weightInst = START;
				
				Move m = g.getMove();
				
				if(m.getGameOver()){
					weightInst += WIN;
				} else if(m.getTruth().getTake()){
					for(Piece p : m.getTruth().getPiece()){
						weightInst += TAKE_PIECE;
					}	
				} else {
					weightInst += NEITHER;
				}
				weights.add(weightInst);
			}
			
			int gsIndex = 0;
			int counter = 1;
			Double d1 = weights.get(0);
			for(Double d2 : weights.subList(1,weights.size())){
				if(d2 > d1){
					d1 = d2;
					gsIndex = counter;
				}
				counter++;
			}
			mvs1.get(gsIndex).setWeight(d1);
			bestGs.add(mvs1.get(gsIndex));
		}
		
		int moveCount = 0;
		
			
		ArrayList<Board> bds1 = Analysis.doMoves(bestGs);
		
		for(Board bd1 : bds1){
			
			AnalysisBoard board1 = AnalysisBoard.convB(bd1);
			ArrayList<GameStatus> mvs2 = Analysis.moves(board1, colour0);
			
			for(GameStatus g : mvs2) {
				double weight = d.get(moveCount);
				
				Move m = g.getMove();
				
				if(m.getTruth().getTake()){
					for(Piece p : m.getTruth().getPiece()){
						weight += TAKE_PIECE;
					}	
				}
				
				if(m.getGameOver()){
					weight += WIN;
					System.out.println("win three deep");
				}
				d.set(moveCount, weight);
			}
			
			moveCount++;
		}
		
		ArrayList<Double> compare = new ArrayList<>();
		for(GameStatus g : bestGs){
			compare.add(g.getWeight());
		}
		
		ArrayList<Double> diffs = new ArrayList<>();
		
		for(int i=0;i<compare.size();i++){
			diffs.add(d.get(i) - compare.get(i));
		}
		
		ArrayList<Integer> rtnVals = new ArrayList<>();
		Double test = diffs.get(0);
		for(int i=1;i<diffs.size();i++) {
			if(diffs.get(i) >= test){
				rtnVals.add(i);
			}
		}
		if(rtnVals.isEmpty()){
			return mvs.get(0);
		} else {
			ArrayList<Move> poss = new ArrayList<>();
			for(Integer i : rtnVals) {
				poss.add(mvs.get(i));
			}
			
			int random = (int) Math.random()*poss.size();
			
			return poss.get(random);
		}
	
	}


}
