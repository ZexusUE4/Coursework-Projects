package Controller;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

public class ImageGetter {
	
	private static ImageGetter instance;
	
	public static synchronized ImageGetter getInstance(){
		if(instance == null)
			instance = new ImageGetter();
		return instance;
	}
	public Image getImage(String name){
		ImageIcon Ic = new ImageIcon("Images\\"+name+".png");
		return Ic.getImage();
	}
	
	public void draw(Graphics g,String name,int x,int y){
		g.drawImage(getImage(name),x,y,null);
	}
}
