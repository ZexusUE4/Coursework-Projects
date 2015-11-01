package Geometry;

import java.awt.Color;
import java.awt.Graphics;

public class Rhombus extends ShapeComponent {

	private static final long serialVersionUID = -3910885563668688436L;

	
	public Rhombus(Coordinate C,Integer Width,Integer Height){
		TopLeft = C;
		this.Width = Width;
		this.Height = Height;
	}
	@Override
	public void Draw(Graphics g) {
		// TODO Auto-generated method stub
		int[] X = new int[4],Y = new int[4];
		X[0] = (TopLeft.x + Width/2);
		Y[0] = TopLeft.y;
		X[1] = TopLeft.x;
		Y[1] = (TopLeft.y + Height/2);
		X[2] = (TopLeft.x + Width/2);
		Y[2] = TopLeft.y + Height;
		X[3] = TopLeft.x + Width;
		Y[3] = (TopLeft.y + Height/2);
		if(Filled){
			g.setColor(this.color);
			g.fillPolygon(X, Y, 4);
		}
		g.setColor(Color.black);
		g.drawPolygon(X, Y, 4);
	}

}
