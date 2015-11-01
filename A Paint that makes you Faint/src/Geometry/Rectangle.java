package Geometry;

import java.awt.Color;
import java.awt.Graphics;

public class Rectangle extends ShapeComponent {
	
	private static final long serialVersionUID = 1L;
	
	public Rectangle(Coordinate C,Integer w,Integer h){
		this.Name = "Rectangle";
		TopLeft  = C;
		Width = w;
		Height = h;
		setBounds(TopLeft.x,TopLeft.y,w,h);
	}
	@Override
	public void Draw(Graphics g) {
		// TODO Auto-generated method stub
		if(Filled){
			g.setColor(this.color);
			g.fillRect(TopLeft.x, TopLeft.y, Width, Height);
		}
		g.setColor(Color.black);
		g.drawRect(TopLeft.x - 1, TopLeft.y - 1, Width + 1, Height + 1);
	}

}
