package Geometry;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.io.Serializable;

import javax.swing.SwingConstants;
import javax.swing.border.Border;
// ResizableBorder.java 

public class ResizableBorder implements Border , Serializable {
	
	private static final long serialVersionUID = -6516641788352933261L;


private int dist = 8;

  int locations[] = 
  {
    SwingConstants.NORTH, SwingConstants.SOUTH, SwingConstants.WEST, SwingConstants.EAST, SwingConstants.NORTH_WEST, SwingConstants.NORTH_EAST, SwingConstants.SOUTH_WEST,SwingConstants.SOUTH_EAST };

  int cursors[] =
  { 
    Cursor.N_RESIZE_CURSOR, Cursor.S_RESIZE_CURSOR, Cursor.W_RESIZE_CURSOR,  Cursor.E_RESIZE_CURSOR, Cursor.NW_RESIZE_CURSOR, Cursor.NE_RESIZE_CURSOR,Cursor.SW_RESIZE_CURSOR, Cursor.SE_RESIZE_CURSOR
  };

 public Insets getBorderInsets(Component component) {
	  
      return new Insets(dist, dist, dist, dist);
      }

  public boolean isBorderOpaque() {
      return false;
  }
  public void paintBorder(Component component, Graphics g, int x, int y,
                          int w, int h) {
	  //g.setClip(x - 10, y - 10, component.getWidth() + 20, component.getHeight() + 20);
      if (component.hasFocus()) {
    	  g.setColor(Color.DARK_GRAY);
          g.drawArc(10, 10, 10, 10, 10, 10);
	  		Graphics2D g2d = (Graphics2D) g.create();
	        Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
	        g2d.setStroke(dashed);
	        g2d.setColor(Color.DARK_GRAY);
	        g2d.drawRect(x + dist - 2, y + dist - 2, w - 2* dist + 2, h - 2 * dist + 2);
	        g2d.dispose();
        for (int i = 0; i < locations.length; i++) {
          Rectangle rect = getRectangle(x, y, w, h, locations[i]);
          g.setColor(Color.cyan);
          g.fillRect(rect.x, rect.y, rect.width - 1, rect.height - 1);
          g.setColor(Color.BLUE);
          g.drawRect(rect.x, rect.y, rect.width - 1, rect.height - 1);
        }
      }
  }

  private Rectangle getRectangle(int x, int y, int w, int h, int location) {
      switch (location) {
      case SwingConstants.NORTH:
          return new Rectangle(x + w / 2 - dist / 2, y , dist, dist);
      case SwingConstants.SOUTH:
          return new Rectangle(x + w / 2 - dist / 2 , y + h - dist, dist, dist);
      case SwingConstants.WEST:
          return new Rectangle(x , y + h / 2 - dist / 2, dist, dist);
      case SwingConstants.EAST:
          return new Rectangle(x + w - dist, y + h / 2 - dist / 2, dist,dist);
      case SwingConstants.NORTH_WEST:
          return new Rectangle(x , y , dist, dist);
      case SwingConstants.NORTH_EAST:
          return new Rectangle(x + w - dist , y, dist, dist);
      case SwingConstants.SOUTH_WEST:
          return new Rectangle(x , y + h - dist, dist, dist);
      case SwingConstants.SOUTH_EAST:
          return new Rectangle(x + w  - dist, y + h - dist, dist, dist);
      }
      return null;
  }

  public int getCursor(MouseEvent me) {
      Component c = me.getComponent();
      int w = c.getWidth();
      int h = c.getHeight();

      for (int i = 0; i < locations.length; i++) {
          Rectangle rect = getRectangle(0, 0, w, h, locations[i]);
          if (rect.contains(me.getPoint()))
              return cursors[i];
      }

      return Cursor.MOVE_CURSOR;
  }
  public static boolean contain(int x,int y,int w,int h,int px,int py){
	  Rectangle rect = new Rectangle(x,y,w,h);
	return rect.contains(new Point(px,py));
  }
}

