package View;

import java.awt.Graphics;
import java.util.ArrayList;

import View.GUIButton;

public interface PanelStateInterface {
	
	public void paint(Graphics g);

	public ArrayList<GUIButton> getButtons();
	
}
