package Geometry;

import java.awt.Graphics;

public class Line extends ShapeComponent {

	private static final long serialVersionUID = 1L;
	
	public Coordinate Start,End;
	public double slope;
	public Line(Coordinate S,Coordinate E){
		isLine = true;
		this.Name = "Line";
		Start = S;
		TopLeft = new Coordinate(0,0);
		End = E;
	}
	@Override
	public void Draw(Graphics g) {
		// TODO Auto-generated method stub
		g.setColor(this.color);
		g.drawLine(Start.x, Start.y, End.x, End.y);
	}
	@Override
	public void DrawBounds(int x,int y,int w,int h){
		super.DrawBounds(x, y, w, h);
		if(slope > 0){
			Start.x = x ;
			Start.y = y ;
			End.x = x + w;
			End.y = y + h;
		}
		else{
			Start.x = x + w;
			Start.y = y ;
			End.x = x ;
			End.y = y +  h;
		}
	}
}
