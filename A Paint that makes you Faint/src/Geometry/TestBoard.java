package Geometry;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
 
public class TestBoard extends JPanel implements ActionListener {
	
 
	private static final long serialVersionUID = 3513465288463210679L;
	

	private Timer T;
	int cx,cy,cw,ch;
	public class shape{
		public int x,y,h,w;
	}
	shape temp;
	Vector<shape> objects = new Vector<shape>();
	TestBoard(JFrame f){
		T = new Timer(25,this);
		T.start();
        setBounds(10,10,250,250);
        setBackground(Color.WHITE);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed (MouseEvent e)
			{
				temp = new shape();
				temp.x = e.getX();
				temp.y = e.getY();
			}
			@Override
			public void mouseReleased(MouseEvent e){
				temp.w = temp.x - e.getX();
				temp.h = temp.y - e.getY();
				objects.add(temp);
			}
		});
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
	    repaint();
	}
	public void paint(Graphics g){
	    super.paintComponent(g);
        for(int i=0;i<objects.size();i++){
        	shape t = objects.elementAt(i);
        	g.drawRect(t.x, t.y, t.w, t.h);
        }
	}
 
	}