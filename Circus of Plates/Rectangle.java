package Model.Loots;

import java.awt.Color;
import java.awt.Graphics;

public class Rectangle extends Loot {

	private static final long serialVersionUID = 1L;
	
	public Rectangle(Integer x , Integer y)
	{
		Name = "Rectangle";
		this.x=x;
		this.y=y;
		Colour = getRandomColor();

	}
	@Override
	public void drawShape(Graphics g) {
		g.setColor(Colour);
		g.fillRect(x-20, y-5,40 , 10);
		g.setColor(Color.black);
		g.drawRect(x-20, y-5,40 , 10);

	}
}
