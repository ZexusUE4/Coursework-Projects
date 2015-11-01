package Geometry;

import java.awt.Color;
import java.awt.Graphics;

public class Triangle extends ShapeComponent {
	
	private static final long serialVersionUID = 1L;
	
	public Triangle(Coordinate C, Integer x,Integer y){
		this.Name = "Triangle";
		TopLeft = C;
		Width = x;
		Height = y;
		setBounds(TopLeft.x,TopLeft.y,x,y);
	}
	@Override
	public void Draw(Graphics g) {
		// TODO Auto-generated method stub
		g.setColor(this.color);
		int[] X = new int[3],Y = new int[3];
		X[0] = TopLeft.x;
		Y[0] = TopLeft.y + Height;
		X[1] = TopLeft.x + Width;
		Y[1] = TopLeft.y + Height;
		//vertex
		Coordinate Vertex = TopLeft.MidPoint(new Coordinate(TopLeft.x + Width,TopLeft.y));
		X[2] = Vertex.x;
		Y[2] = Vertex.y;
		if(Filled){
			g.setColor(this.color);
			g.fillPolygon(X, Y, 3);
		}
		g.setColor(Color.black);
		g.drawPolygon(X, Y, 3);
	}
	
}
