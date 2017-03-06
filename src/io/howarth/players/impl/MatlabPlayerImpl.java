package io.howarth.players.impl;

import io.howarth.Board;
import io.howarth.Hnefatafl;
import io.howarth.Move;
import io.howarth.TakePiece;
import io.howarth.analysis.AnalysisBoard;
import io.howarth.pieces.Piece;
import io.howarth.pieces.Pieces;
import io.howarth.players.Player;

import java.util.ArrayList;

public class MatlabPlayerImpl extends Player {
	
	/**
	 * This constructs a RandomPlayer object. 
	 * @param n The name of the player
	 * @param p The player's set of Pieces [object]
	 * @param b The board object of the game.
	 * @param o The Player's opponent.
	 */
	public MatlabPlayerImpl(String n, Pieces p, Board b, Player o) {
		super(n, p, b, o);
	}
	
	/**
	 * Boolean method -- Allows the MatlabPlayer to move by choosing a random move from the availableMoves()
	 * @return true when a move has been performed.
	 */
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
			
			if(!fullList.isEmpty()){

				// This will take a long time
				Move moveToConvert;
				if(fullList.get(0).getPiece().getColour()==Player.BLACK){
					moveToConvert = weightMoves(fullList, Player.BLACK);
				} else {
					moveToConvert = weightMoves(fullList, Player.WHITE);
				}
					
				
				//convert the move objects parameters to basic types.
				byte x = moveToConvert.getX();
				byte y = moveToConvert.getY();
				byte i = moveToConvert.getI();
				byte j = moveToConvert.getJ();
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
			} else {
				return false;
			}
		}
		

		@SuppressWarnings("unused")
		private Move weightMoves(ArrayList<Move> mvs, byte thisColour){
			// First analysis board
			// Get next set, find most probable move continue unless its game winning
			// Get all your own set, take weights 
			// Get next set, find most probable move continue unless its game winning
			// Get all your own next set, take weights
			// store move with its weight
			// replace the last one with this one if the weight is higher
			
			byte zero = 0;
			Move returnM = new Move(null,zero,zero,zero,zero,new TakePiece(new ArrayList<>(), false), false, -10000000);
			
			byte oppoColour =-1;
			
			if (thisColour == Player.BLACK){
				oppoColour = Player.WHITE;
			} else {
				oppoColour = Player.BLACK;
			}
			
			System.out.println("Number of moves to analyse: "+mvs.size());
			for(Move m : mvs ){
				try{
					/**
					 * 1
					 */
					if(m.getGameOver()){
						System.out.println("MATLAB won the game");
						return m;
					}
					
					AnalysisBoard board = AnalysisBoard.convB(Hnefatafl.b);
					
					if(!board.outOfRange(m.getI(), (byte) (m.getJ()-1))){
						if(board.getPiece(m.getI(), (byte)(m.getJ()-1)) == 'k'){
							System.out.println("MATLAB moved next to the king");
							return m;
						} 
					}
					
					if(!board.outOfRange(m.getI(), (byte) (m.getJ()+1))){
						if (board.getPiece(m.getI(), (byte)(m.getJ()+1)) == 'k'){
							System.out.println("MATLAB moved next to the king");
							return m;
						} 
					}
					
					if(!board.outOfRange((byte) (m.getI()-1), m.getJ())){
						if (board.getPiece((byte)(m.getI()-1), m.getJ()) == 'k'){
							System.out.println("MATLAB moved next to the king");
							return m;
						} 
					}
					
					if(!board.outOfRange((byte) (m.getI()+1), m.getJ())){
						if (board.getPiece((byte)(m.getI()+1), m.getJ()) == 'k'){
							System.out.println("MATLAB moved next to the king");
							return m;
						}
					}
					
					if(m.getTruth().getTake()){
						if(m.getTruth().getPiece().size() > returnM.getTruth().getPiece().size()){
							returnM = m;
						}
					}
					
					if(m.getWeight() > returnM.getWeight()){
						returnM = m;
					}
					
				} catch (Exception uhoh){
					uhoh.printStackTrace();
				}
			}
			
			if(returnM.getPiece()==null){
				int randomMove = (int)(Math.random()*mvs.size());
				System.out.println("MATLAB did a random move");
				return mvs.get(randomMove);
			}
			
			System.out.println("MATLAB took a piece");
			return returnM;
		}
}
