package View;

import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class GUIButton {
	ImageIcon Img;
	String Action;
	int x,y;
	
	public GUIButton(int x,int y,String Name){
		Img = new ImageIcon("Images\\"+ Name + ".png");
		Action = Name;
		this.x = x;
		this.y = y;
	}
	
	public Rectangle getArea(){
		return new Rectangle(x,y,Img.getIconWidth(),Img.getIconHeight());
	}
	
	public void draw(Graphics g){
		g.drawImage(Img.getImage(),x,y,null);
	}
	
	public String getAction(){
		return Action;
	}
}
