package Model.Loots;

import java.awt.Color;
import java.awt.Graphics;

public class Oval extends Loot {

	private static final long serialVersionUID = 1L;
	
	public Oval(Integer x,Integer y){
		Name = "Oval";
		this.x = x;
		this.y = y;
		Colour = getRandomColor();
	}
	@Override
	public void drawShape(Graphics g) {
		g.setColor(Colour);
		g.fillOval(x-20, y-5, 40 , 10);
		g.setColor(Color.black);
		g.drawOval(x-20, y-5, 40 , 10);
	}

}
