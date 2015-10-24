package View;

import javax.swing.JFrame;

import Controller.Controller;

public class View extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	public View(){
		this.setSize(1000, 700);
		Controller Cont = Controller.getInstance();
		GamePane GB = new GamePane();
		Cont.setGamePane(GB);
		add(GB);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
	}

}
