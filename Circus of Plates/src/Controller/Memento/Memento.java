package Controller.Memento;

import Controller.GameState;

public class Memento {
	private GameState GState;
	
	public Memento(GameState GS){
		GState = GS;
	}
	
	public GameState getState(){
		return GState;
	}
}
