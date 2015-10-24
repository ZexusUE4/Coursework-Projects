	package View;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import Controller.Controller;
import Controller.GameState;

public class InGameState implements PanelStateInterface {
	
	GameState GState;
	Controller Cont = Controller.getInstance();
	Image Background;
	int time = 0;
	
	public InGameState(){
		ImageIcon Im = new ImageIcon("Images\\Back2.png");
		Background = Im.getImage();
	}
	
	@Override
	public void paint(Graphics g) {
		g.drawImage(Background,0,0,null);
		time = (time + 1) % 6000;
    	GState.checkWinOrLose();
		Cont.performMovement(time);
		GState.getPlayerOne().draw(g);
		if(GState.getPlayerTwo() != null )
			GState.getPlayerTwo().draw(g);
		GState.paintLoots(g);
		
		g.setFont(new Font("Bodoni MT Black",Font.BOLD,32));
		g.setColor(Color.WHITE);
		g.drawString("Player 1: " + GState.getPlayerOne().Score, 100, 50);
		if(GState.getPlayerTwo() != null )
			g.drawString("Player 2: " + GState.getPlayerTwo().Score, 700, 50);
	}

	@Override
	public ArrayList<GUIButton> getButtons() {
		return new ArrayList<GUIButton>();
	}
	
	public void setGameState(GameState GS){
		GState = GS;
		Cont.setGameState(GS);
		GameState.instance = GS;
	}
	

}
