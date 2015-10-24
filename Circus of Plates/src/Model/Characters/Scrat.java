package Model.Characters;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import Controller.ImageGetter;
import Model.Loots.Loot;

public class Scrat extends Character {

	private static final long serialVersionUID = 1L;

	public Scrat(int x,int y){
		Name = "Scrat";
		Loots = new ArrayList<Loot>();
		Score = 0;
		Speed = 2;
		X = x;
		Y = y;
	}
	
	@Override
	public void draw(Graphics g) {
		ImageGetter Ig = new ImageGetter();
		Ig.draw(g, "Scrat1", X, Y);
		for(int i = 0 ; i < Loots.size();i++){
			Loot L = Loots.get(i);
			L.x = X;
			L.y = Y+15 - i*10;
		}
//		Rectangle Ar = getCollectorArea();
//		g.drawRect(Ar.x, Ar.y, Ar.width, Ar.height);
	}

	@Override
	public Rectangle getCollectorArea() {
		int x = 0,y = 0;
		if(Loots.size() > 0){
			x = Loots.get(Loots.size()-1).x - 20;
			y = Loots.get(Loots.size()-1).y - 10;
			return new Rectangle(x,y,40,10);
		}
		x = X - 20;
		y = Y + 10;
		return new Rectangle(x,y,40,10);
	}
	
	public void move(int dx) {
		if(X + dx < 970 && X + dx > 0)
			X+=dx;
	}
}
