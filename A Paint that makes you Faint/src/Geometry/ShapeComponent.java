package Geometry;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.event.MouseInputListener;

public abstract class ShapeComponent extends SketchComponent {

	private static final long serialVersionUID = 1L;
	
	public boolean isLine = false,isRegular = false;
	public Resizable res = new Resizable(this);
	public MouseInputListener mouse;
	public Resizable Border(){
		res.setBounds(TopLeft.x - 8, TopLeft.y - 8, Width + 16, Height + 16);
		BorderIn = true;
		return res;
	}
	Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public Resizable getResizable(){
		return res;
	}
	public abstract void Draw(Graphics g);
	public void DrawBounds(int x,int y,int w,int h){
		TopLeft.x = x ;
		TopLeft.y = y;
		Width = w;
		Height = h;
	}
}