package io.howarth.analysis;

import io.howarth.Board;
import io.howarth.Move;
import io.howarth.pieces.Piece;
import io.howarth.pieces.PieceCode;

import java.util.ArrayList;
import java.util.Arrays;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation.ANONYMOUS;

public final class Analysis {
	
	public static ArrayList<Move> moves(ArrayList<Piece> pcs){
		
		ArrayList<Move> moves = new ArrayList<>();
		for(Piece p : pcs) {
			if(p.availableMoves()!=null){
				moves.addAll(p.availableMoves());
			}
		}
		
		return moves;
	}
	
	private static ArrayList<GameStatus> oppoMoves(char[][] pieces, Move m){
		
		ArrayList<GameStatus> oppoMoves = new ArrayList<>();
		
//		// Construct new oppoPlayer for each move
//		for(char[] c : pieces){
//			System.out.println(Arrays.toString(c));
//		}
//		
		
		AnalysisBoard newB = new AnalysisBoard();
		newB.setPieces(pieces);
		
		newB.remove(m.getX(), m.getY());
		
		if(m.getTruth().getTake()){
			newB.getData()[m.getI()][m.getJ()] = 'x';
		}
		
		newB.setPosition(m.getI(), m.getJ(), m.getPiece().getChar());
		
		for(int i=0; i<11; i++){
			for(int j=0; j<11; j++){
				
				Piece p = PieceCode.charToPiece(pieces[i][j], i, j, null);
				if (p != null){
					p.setBoard(AnalysisBoard.convAB(newB));
					System.out.println(p.getColour());
					System.out.println(m.getPiece().getColour());
					if (p.getColour() != m.getPiece().getColour()){
						ArrayList<Move> pieceMoves = p.availableMoves();
						try {
							System.out.println(pieceMoves.size());
						} catch (NullPointerException e){
							System.out.println("hello");
						}
						
						if (pieceMoves!=null) {
							for(Move move : pieceMoves) {
								oppoMoves.add(new GameStatus(newB, move));
							}
							
						}
					}
				}
			}
		}
		
		//give each piece in pieces the new board
		//move the pieces if required
		//then get the number of moves for those pieces	

		//get opponent 

		return oppoMoves;
	}
	
	public static ArrayList<GameStatus> bfsInitial(AnalysisBoard board, ArrayList<Move> mvs){
		ArrayList<GameStatus> allOppoMoves = new ArrayList<>();
		
		
		for(Move m : mvs){
			char[][] pcs = new char[11][11];
			for(int i=0; i<11; i++){
				for(int j=0; j<11; j++){
					pcs[i][j] = board.getData()[i][j];
				}
			}
			allOppoMoves.addAll(oppoMoves(pcs, m));
		}
		
		return allOppoMoves;
	}
	
//	public static ArrayList<GameStatus> bfsGeneral(ArrayList<GameStatus> games){
//		ArrayList<GameStatus> gs = new ArrayList<>();
//		
//		for(GameStatus g : games ){
//			gs.addAll(oppoMoves(g.getBoard().getData(), g.getMove()));
//		}
//		
//		return gs;
//	}
	
	
}
