package Model.Characters;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;

import Model.Loots.Loot;

public abstract class 
Character extends Observable implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public ArrayList<Loot> Loots;
	public String Name;
	public int Score,X,Y;
	protected int Speed;
	public ScoreObserver ScObs;
	public Character(){
		ScObs = new ScoreObserver();
		addObserver(ScObs);
	}
	
	public abstract void draw(Graphics g);
	
	public void addLoot(Loot L){
		Loots.add(L);
		L.setState(L.getWithPlayer());
		setChanged();
		notifyObservers(Loots);
	}
	
	public abstract Rectangle getCollectorArea();
	
	public abstract void move(int dx);
}
