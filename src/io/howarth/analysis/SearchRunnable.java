package io.howarth.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class SearchRunnable implements Callable<ArrayList<GameStatus>>{

	private ArrayList<GameStatus> game = new ArrayList<>();
	
	public SearchRunnable(List<GameStatus> list){
		this.game.addAll(list);
	}
	
	@Override
	public ArrayList<GameStatus> call() throws Exception {
		return Analysis.bfsGeneral(game);
	}

}
