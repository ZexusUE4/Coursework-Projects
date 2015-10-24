package Model.Loots;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.ImageIcon;

import Controller.ImageGetter;

public class TimeShifter extends Loot {

	private static final long serialVersionUID = 1L;
	
	public ImageIcon IC;
	
	public TimeShifter(Integer x , Integer y){
		Name = "TimeShifter";
		this.x = x;
		this.y = y;
		Colour = Color.black;
		IC = new ImageIcon("Images\\Time.png");
	}
	
	@Override
	public void drawShape(Graphics g) {
		ImageGetter Ig = new ImageGetter();
		Ig.draw(g, "Time", x, y);
		
	}

}
