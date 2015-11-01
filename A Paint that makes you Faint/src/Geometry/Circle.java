package Geometry;

import java.awt.Color;
import java.awt.Graphics;

public class Circle extends  ShapeComponent {

	private static final long serialVersionUID = -1314764928574248617L;
	public Circle(Coordinate O,Integer w,Integer h){
		
		this.Name = "Circle";
		isRegular = true;
		TopLeft = O;
		Width = Math.max(w, h) ;
		Height = Math.max(w, h) ;
		setBounds(TopLeft.x,TopLeft.y,w,h);
	}
	@Override
	public void Draw(Graphics g) {
		// TODO Auto-generated method stub
		if(Filled){
			g.setColor(this.color);
			g.fillOval(TopLeft.x, TopLeft.y, Math.max(Width , Height), Math.max(Width , Height));
		}
		g.setColor(Color.black);
		g.drawOval(TopLeft.x, TopLeft.y, Math.max(Width , Height), Math.max(Width , Height));
		
	}

	
}
