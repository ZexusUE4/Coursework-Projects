package Geometry;

import java.awt.Color;
import java.awt.Graphics;

public class Oval extends ShapeComponent {
	
	private static final long serialVersionUID = 1L;
	
	public Oval(Coordinate O, 	Integer Width,Integer Height){
		this.Name = "Oval";
		TopLeft = O;
		this.Width = Width;
		this.Height = Height;
		setBounds(TopLeft.x,TopLeft.y,Width,Height);
	}
	@Override
	public void Draw(Graphics g) {
		// TODO Auto-generated method stub
		if(Filled){
			g.setColor(this.color);
			g.fillOval(TopLeft.x, TopLeft.y, Width, Height);
		}
		g.setColor(Color.black);
		g.drawOval(TopLeft.x, TopLeft.y , Width , Height );
	}

}
