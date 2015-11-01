package Geometry;

import java.awt.Color;
import java.awt.Graphics;

public class Square extends ShapeComponent {
	private static final long serialVersionUID = 1L;
	
	public Square(Coordinate C,Integer w,Integer h){
		isRegular = true;
		this.Name = "Square";
		TopLeft  = C;
		Width = w;
		Height =h ;
		setBounds(TopLeft.x,TopLeft.y,Math.max(w, h),Math.max(w, h));
	}
	@Override
	public void Draw(Graphics g) {
		// TODO Auto-generated method stub
		if(Filled){
			g.setColor(this.color);
			g.fillRect(TopLeft.x, TopLeft.y, Math.min(Width, Height ) , Math.min(Width, Height ) );
		}
		g.setColor(Color.black);
		g.drawRect(TopLeft.x - 1, TopLeft.y - 1,  Math.min(Width, Height ) + 1,  Math.min(Width, Height ) + 1);
	}


}
