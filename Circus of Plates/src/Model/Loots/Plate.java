package Model.Loots;

import java.awt.Color;
import java.awt.Graphics;


public class Plate extends Loot {
	
	private static final long serialVersionUID = 1L;
	
	public Plate(Integer x,Integer y){
		Name = "Plate" ;
		this.x = x;
		this.y = y;
		Colour = getRandomColor();
	}
	@Override
	public void drawShape(Graphics g) {
		
		int[] X = new int[4];
		int[] Y = new int[4];
		X[0] = x - 20;
		X[1] = x + 20;
		X[2] = x + 12;
		X[3] = x - 12;
		
		Y[0] = y - 5;
		Y[1] = y - 5;
		Y[2] = y + 5;
		Y[3] = y + 5;
		g.setColor(Colour);
		g.fillPolygon(X, Y, 4);
		g.setColor(Color.black);
		g.drawPolygon(X, Y, 4);
	}
}
