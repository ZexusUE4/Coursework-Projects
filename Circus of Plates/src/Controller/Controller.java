package Controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFileChooser;

import Controller.Memento.CareTaker;
import Controller.Memento.Originator;
import View.GUIButton;
import View.GamePane;

public class Controller {
	
	public Set<Integer> Pressed = new HashSet<Integer>();
	private GameState GState;
	public GamePane Pane;
	private static Controller instance;
	private JFileChooser fileChoos = new JFileChooser();
	//private File fileload;
	int returnval; 
	SaveLoad loader = new SaveLoad();
	
	public synchronized static Controller getInstance(){
		if(instance == null)
			instance = new Controller();
		return instance;
	}
	public KeyListener KS = new KeyListener(){
	    @Override
	    public synchronized void keyPressed(KeyEvent e) {
	    	if(e.getKeyCode()==KeyEvent.VK_F5)
	    	{
		          Controller Cont = Controller.getInstance();
		          Cont.Pane.pause();
		          JFileChooser chooser = new JFileChooser();
		          int option = chooser.showSaveDialog(chooser);
		          if (option == JFileChooser.APPROVE_OPTION) 
		          {
		            System.out.println(chooser.getSelectedFile().getAbsolutePath());
		            SaveLoad s = new  SaveLoad();
		            s.save(chooser.getSelectedFile());
		            Cont.Pane.resume();
		          }
		          else{
		        	  Cont.Pane.resume();
		          }
	    	}
	    	if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
	    		Controller.getInstance().Pane.setInMainMenu();
	    	}
	    	Pressed.add(e.getKeyCode());
	    }

	    @Override
	    public synchronized void keyReleased(KeyEvent e) {
	    	Pressed.remove(e.getKeyCode());
	    }
	    @Override
	    public void keyTyped(KeyEvent e) {}
	};
	
	public MouseListener MS = new MouseListener(){

		@Override
		public void mouseClicked(MouseEvent e) {
			ArrayList<GUIButton> Buts = Pane.MainMenu.getButtons();
			for(GUIButton B : Buts){
				if(B.getArea().contains(e.getPoint())){
					ButtAction(B.getAction());
				}
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	public void ButtAction(String Action){
		switch(Action){
		case "NewGame": new PickGameMode(); break;
		case "LoadGame" :
			returnval = fileChoos.showOpenDialog(null);
			if(returnval == JFileChooser.APPROVE_OPTION)
			{
				Pane.setInGame(loader.load(fileChoos.getSelectedFile()));
			}
			break;
		case "Exit" : System.exit(0); break;
		}
	}
	
	
	public void performMovement(int time){
		if(GState == null) return;
    	if(Pressed.contains(KeyEvent.VK_D) && GState.getPlayerTwo() != null)
    		GState.getPlayerTwo().move(4);
    	if(Pressed.contains(KeyEvent.VK_A) && GState.getPlayerTwo() != null)
    		GState.getPlayerTwo().move(-4);
    	if(Pressed.contains(KeyEvent.VK_RIGHT) && GState.getPlayerOne() != null)
    		GState.getPlayerOne().move(4);
    	if(Pressed.contains(KeyEvent.VK_LEFT) && GState.getPlayerOne() != null)
    		GState.getPlayerOne().move(-4);
		if(time % 50 == 0) //SpawnRate
			GState.spawn();
		if(time % 100 == 0)
			GState.recycle();
		if(time % 500 == 0){
			Originator O = Originator.getInstance();
			CareTaker CT = CareTaker.getInstance();
			CT.add(O.saveStateToMemento());
		}
		GState.moveLoots();
	}
	
	public void setGamePane(GamePane GP){
		Pane = GP;
	}
	
	public void setGameState(GameState GS){
		GState = GS;
	}
}
