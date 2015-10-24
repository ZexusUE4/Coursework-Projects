package Controller;

import java.awt.Graphics;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JOptionPane;

import Controller.Iterator.Iterator;
import Controller.Iterator.LootRepository;
import Model.Characters.Character;
import Model.Loots.Loot;

public class GameState implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public ArrayList<Loot> Loots = new ArrayList<Loot>();
	private Character P1;
	private Character P2;
	public static GameState instance;
	
	public static synchronized GameState getInstance(){
		return instance;
	}
	
	public GameState(Character p1,Character p2){
		Loots = new ArrayList<Loot>();
		P1 = p1;
		P2 = p2;
		instance = this;
	}
	
	
	public void spawn(){
		LootPool Pool = LootPool.getInstance();
		ArrayList<String> LS = new ArrayList<String>(Arrays.asList("Plate","Oval","Rectangle","TimeShifter"));
		Random rand = new Random();
		int rate = rand.nextInt(30);
		String Name;
		if(rate != 0)
			Name = LS.get(rand.nextInt(LS.size()-1));
		else
			Name = "TimeShifter";
		Loot toAdd = Pool.getLoot(Name, rand.nextInt(1000), 0);
		toAdd.drop();
		Loots.add(toAdd);
	}
	
	//called every 10 secs or something
	public void recycle(){
		for(int i = 0; i < Loots.size();i++){
			Loot L = Loots.get(i);
			if(L.isDormant()){
				L.returnToPool();
				Loots.remove(i);
				i--;
			}
		}
	}
	
	public Character getPlayerOne(){
		return P1;
	}
	public Character getPlayerTwo(){
		return P2;
	}

	public void paintLoots(Graphics g) {
		LootRepository LP = new LootRepository(Loots);
		Iterator It = LP.getIterator();
		while(It.hasNext()){
			Loot L = (Loot) It.next();
			L.draw(g);
		}
		
	}
	
	public void checkWinOrLose(){
		if(Controller.getInstance().Pane.isPaused)
			return;
		if(P1.Loots.size() == 20){
			Controller.getInstance().Pane.pause();
			JOptionPane.showMessageDialog(Controller.getInstance().Pane, "Player 2 Wins !");
			Controller.getInstance().Pane.setInMainMenu();
			Controller.getInstance().Pane.resume();
		}else if(P2 != null && P2.Loots.size() == 20){
			Controller.getInstance().Pane.pause();
			JOptionPane.showMessageDialog(Controller.getInstance().Pane, "Player 1 Wins !");
			Controller.getInstance().Pane.setInMainMenu();
			Controller.getInstance().Pane.resume();
		}else if(P1.Score == 10){
			Controller.getInstance().Pane.pause();
			JOptionPane.showMessageDialog(Controller.getInstance().Pane, "Player 1 Wins !");
			Controller.getInstance().Pane.setInMainMenu();
			Controller.getInstance().Pane.resume();
		}else if(P2 != null && P2.Score == 10){
			Controller.getInstance().Pane.pause();
			JOptionPane.showMessageDialog(Controller.getInstance().Pane, "Player 2 Wins !");
			Controller.getInstance().Pane.setInMainMenu();
			Controller.getInstance().Pane.resume();
		}
	}
	public void addObservers(){
		P1.addObserver(P1.ScObs);
		if(P2 != null)
			P2.addObserver(P2.ScObs);
	}
	public void moveLoots() {
//		for(Loot L: Loots)
//			L.move();
		LootRepository LP = new LootRepository(Loots);
		Iterator It = LP.getIterator();
		while(It.hasNext()){
			Loot L = (Loot) It.next();
			L.move();
		}
		
	}
}
