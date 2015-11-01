package Geometry;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class TextComponent extends SketchComponent{

	private static final long serialVersionUID = 1L;
	
	public Font font;
	public String Text;
	public TextComponent(String Text,Font f){
		Name = "Text";
		TopLeft = new Coordinate(0,0);
		isText = true;
		this.Text = Text;
		this.font = f;
	}
	@Override
	public void Draw(Graphics g){
		g.setColor(this.color);
		g.setFont(this.font);
		g.drawString(this.Text, TopLeft.x, TopLeft.y);
		g.setColor(Color.BLACK);
	}

}
