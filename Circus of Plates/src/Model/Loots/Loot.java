package Model.Loots;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import Controller.LootPool;
import Controller.LootStates.DormantLoot;
import Controller.LootStates.FallingLoot;
import Controller.LootStates.LootState;
import Controller.LootStates.WithPlayerLoot;

public abstract class Loot implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public String Name;
	public Color Colour;
	public boolean ColourShifter ;
	public Integer x,y;
	public int ColourTimer;
	private final int ChangeEvery = 10;
	static List<Color> Colors =  Arrays.asList(Color.red,
			Color.blue,Color.cyan,Color.yellow,Color.green,Color.white);
	
	private LootState State,WithPlayer,Falling,Dormant;
	
	public Loot(){
		WithPlayer = new WithPlayerLoot(this);
		Falling = new FallingLoot(this);
		Dormant = new DormantLoot();
		ColourTimer = 0;
		State = Dormant;
	}
	public static Color getRandomColor(){
		Random rand = new Random();
		int r = rand.nextInt(Colors.size());
		return Colors.get(r);
	}
	
	public abstract void drawShape(Graphics g);
	
	public void draw(Graphics g){
		ColourTimer = (ColourTimer + 1) % ChangeEvery;
		if(ColourShifter && ColourTimer % ChangeEvery == 0)
			Colour = getRandomColor();
		State.draw(g);
	}
	
	public void move(){
		State.move();
	}
	
	public void returnToPool(){
		LootPool Pool = LootPool.getInstance();
		Pool.addLoot(this);
	}
	
	public void drop(){
		State = Falling;
	}
	
	
	
	public LootState getState() {
		return State;
	}
	public void setState(LootState state) {
		State = state;
	}
	public LootState getWithPlayer() {
		return WithPlayer;
	}
	public LootState getFalling() {
		return Falling;
	}
	public LootState getDormant() {
		return Dormant;
	}
	
	public boolean isDormant(){
		return State == Dormant;
	}
	public boolean isFalling(){
		return State == Falling;
	}
	public boolean isWithPlayer(){
		return State == WithPlayer;
	}
	
}
