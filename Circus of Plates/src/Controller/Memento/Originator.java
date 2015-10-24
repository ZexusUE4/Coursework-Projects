package Controller.Memento;

import Controller.GameState;
import Controller.SaveLoad;

public class Originator {
	private GameState GState;
	private static Originator instance;
	
	public static Originator getInstance(){
		if(instance == null)
			instance = new Originator();
		return instance;
	}
	public void set(GameState newState){
		GState = newState;
	}
	
	public GameState getState(){
		return GState;
	}
	
	public Memento saveStateToMemento(){
		SaveLoad C = new SaveLoad();
		return new Memento(C.copy());
	}
	
	public GameState getStateFromMemento(Memento Mem){
		return Mem.getState();
	}
	
}
