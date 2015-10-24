package Model.Characters;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import Controller.GameState;
import Model.Loots.Loot;

public class AI extends Character {

	private static final long serialVersionUID = 1L;
	
	Character Char;
	ArrayList<Loot> GSLoots;
	public AI(int Character,int x,int y){
		if(Character == 1)
			Char = new Olaf(x,y);
		else
			Char = new Scrat(x,y);
		Loots = new ArrayList<Loot>();
	}
	
	@Override
	public void draw(Graphics g) {
		SearchAndGo();
		Char.draw(g);
	}

	@Override
	public Rectangle getCollectorArea() {
		return Char.getCollectorArea();
	}

	public void move(int dx) {
		
	}
	
	public void SearchAndGo(){
		update();
		Loot x = getTargetLoot();
		if(x == null)
			return;
		interpolate(x.x);
	}
	
	public void interpolate(int x){
		if(Math.abs(Char.X - x) < 4){
			Char.X = x;
			return;
		}
		if(Char.X > x)
			Char.X-=4;
		else if(Char.X < x)
			Char.X+=4;
	}
	
	public Loot getTargetLoot(){
		Loot ret = null;
		double miniDist = Double.MAX_VALUE;
		Rectangle LootArea = Char.getCollectorArea();
		for(Loot L: GSLoots){
			if(!L.isFalling() || L.y > LootArea.y) continue;
			if(L.ColourShifter || L.Colour.equals(getTargetColour()) || getTargetColour().equals(Color.LIGHT_GRAY)){
				double dist = Math.sqrt(Math.pow(LootArea.x - L.x, 2) + Math.pow(LootArea.y - L.y, 2));
				if(dist < miniDist){
					ret = L;
					miniDist = dist;
				}
			}
		}
		return ret;
	}
	public void update(){
		GSLoots = GameState.getInstance().Loots;
		Char.Loots = Loots;
	}
	
	public Color getTargetColour(){
		if(Loots.size() > 0)
			return Loots.get(Loots.size()-1).Colour;
		else
			return Color.LIGHT_GRAY;
	}
	
}
