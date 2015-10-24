package View;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

import Controller.Controller;
import Controller.GameState;

public class GamePane extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	private Controller Cont;
	public boolean isPaused;
	Timer T;
	public PanelStateInterface State,MainMenu,InGame;
	private static GamePane instance;
	int counter = 0;

	public GamePane(){
		
		Cont = Controller.getInstance();
		MainMenu = new MainMenuState();
		InGame = new InGameState();
		setInMainMenu();
		this.setBackground(Color.white);
		T = new Timer(10,this);
		T.start();
		this.setFocusable(true);
		requestFocus();
	}
	
	
	public void paint(Graphics g){
		
		super.paint(g);
		State.paint(g);
	}
	
	public synchronized static GamePane getInstance(){
		return instance;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		repaint();
	}
	
	
	public void setInMainMenu(){
		State = MainMenu;
		this.addMouseListener(Cont.MS);
		this.removeKeyListener(Cont.KS);
	}
	
	public void setInGame(GameState GS){
		InGameState IGS = (InGameState) InGame;
		IGS.setGameState(GS);
		State = InGame;
		GS.addObservers();
		this.removeMouseListener(Cont.MS);
		this.addKeyListener(Cont.KS);
	}
	
	public PanelStateInterface getMainMenu(){
		return MainMenu;
	}
	
	public PanelStateInterface getInGame(){
		return InGame;
	}
	
	public boolean inMainMenu(){
		return State == MainMenu;
	}
	
	public boolean inGame(){
		return State == InGame;
	}
	
	public void pause(){
		isPaused = true;
		T.stop();
	}
	public void resume(){
		isPaused = false;
		T.start();
	}
}
