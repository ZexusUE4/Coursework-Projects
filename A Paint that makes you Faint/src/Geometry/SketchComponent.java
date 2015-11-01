package Geometry;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

import javax.swing.JComponent;

public abstract class SketchComponent extends JComponent implements Serializable {


	private static final long serialVersionUID = 1L;
	public boolean isText = false,BorderIn = false,Deleted = false,isSelectBox = false,Selected = false,Visible = true,Filled = false,hasFocus = false;
	public int Width,Height;
	public String Name;
	public Color color;
	public Coordinate TopLeft;
	public void setColor(Color color) {
		this.color = color;
	}
	public abstract void Draw(Graphics g);
	public void onPlacing(){
		this.setBounds(new java.awt.Rectangle(TopLeft.x,TopLeft.y,Width,Height));
	}
}
