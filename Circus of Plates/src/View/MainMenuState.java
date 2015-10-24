package View;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;


public class MainMenuState implements PanelStateInterface {
	
	ArrayList<GUIButton> Butts;
	ImageIcon Background;
	Image Olaf;
	
	public MainMenuState(){
		Butts = new ArrayList<GUIButton>();
		Butts.add(new GUIButton(350,340,"NewGame"));
		Butts.add(new GUIButton(350,440,"LoadGame"));
		Butts.add(new GUIButton(700,580,"Exit"));
		Background = new ImageIcon("Images\\back3.jpg");
		Olaf = load();
	}
	
	public void paint(Graphics g) {
		g.drawImage(Background.getImage(),0,0,null);
		g.drawImage(Olaf,-10,250,null);
		for(GUIButton Bt : Butts){
			Bt.draw(g);
		}
	}
	
	private static Image load() {
		String url = "Images\\giphy.gif";
		ImageIcon Ic = new ImageIcon(url);
		return Ic.getImage();
    }
	
	public ArrayList<GUIButton> getButtons(){
		return Butts;
	}
	

}
