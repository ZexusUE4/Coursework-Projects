package Geometry;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

import UserInterface.MainFrame;
import UserInterface.Sketch;


public class Resizable extends JComponent implements Serializable{

	private static final long serialVersionUID = -749461293168044666L;
	public ShapeComponent Shape;
	private int dx,dy;
	@SuppressWarnings("unchecked")
	@Override
	public void requestFocus() {
		// TODO Auto-generated method stub
		super.requestFocus();
		if(MainFrame.choose == MainFrame.ShapePick.PCKCOLOR){
			MainFrame.ChoosenColor = Shape.color;
			MainFrame.SelectedColorPanel.setBackground(Shape.color);
		}
		if(hasFocus())  return;
		if(Sketch.BeforeState != null){
		      for(int i = Sketch.States.size()-1; i > Sketch.CurState;i--){
		    	  Sketch.States.remove(i);
		      }
		     Sketch.States.insertElementAt(Sketch.BeforeState, Sketch.CurState);
		     Sketch.removeBorders(Sketch.BeforeState);
		     Sketch.CurState++;
		}
	      Sketch.BeforeState = (Vector<SketchComponent>) Sketch.Copy(Sketch.Shapes);
	      System.out.println(Sketch.CurState);
	}
	public KeyListener KeyAction = new KeyListener(){
		@Override
		public void keyPressed(KeyEvent k) {
			// TODO Auto-generated method stub
			int keycode = k.getKeyCode();
			if(keycode == KeyEvent.VK_DELETE){
				Shape.Deleted = true;
				Sketch.ref.remove(Shape.Border());
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	};
	public Resizable(Component comp) {
    this(comp, new ResizableBorder());
	}

  public Resizable(Component comp, ResizableBorder border) {
	 Shape = (ShapeComponent)comp;
    setLayout(new BorderLayout());
    add(comp);
    setBorder(border);
    this.addKeyListener(KeyAction);
  }

  private void resize() {
      if (getParent() != null) {
        ((JComponent)getParent()).revalidate();
      }
  }

  private  MouseInputListener resizeListener = new MouseInputAdapter() {
    public void mouseMoved(MouseEvent me) {
      if (hasFocus()) {
          ResizableBorder border = (ResizableBorder)getBorder();
          setCursor(Cursor.getPredefinedCursor(border.getCursor(me)));
      }
    }

    public void mouseExited(MouseEvent mouseEvent) {
       setCursor(Cursor.getDefaultCursor());
    }

    private int cursor;
    private Point startPos = null;

	public void mousePressed(MouseEvent me) {
      ResizableBorder border = (ResizableBorder)getBorder();
      cursor = border.getCursor(me);
      startPos = me.getPoint();
      Sketch.UnSelect();
      if(!hasFocus())requestFocus();
      Shape.Selected = true;
      repaint();
    }

    public void mouseDragged(MouseEvent me) {

      if (startPos != null) {

        int x = getX();
        int y = getY();
        int w = getWidth();
        int h = getHeight();

        dx = me.getX() - startPos.x;
        dy = me.getY() - startPos.y;
 
        switch (cursor) {
          case Cursor.N_RESIZE_CURSOR:
            if (!(h - dy < 50)) {
              setBounds(x, y + dy, w, h - dy);
             resize();
            }
            break;

          case Cursor.S_RESIZE_CURSOR:
            if (!(h + dy < 50)) {
              setBounds(x, y, w, h + dy);
              startPos = me.getPoint();
              resize();
            }
            break;

          case Cursor.W_RESIZE_CURSOR:
            if (!(w - dx < 50)) {
              setBounds(x + dx, y, w - dx, h);
              resize();
            }
            break;

          case Cursor.E_RESIZE_CURSOR:
            if (!(w + dx < 50)) {
              setBounds(x, y, w + dx, h);
             startPos = me.getPoint();
              resize();
            }
            break;

          case Cursor.NW_RESIZE_CURSOR:
            if (!(w - dx < 50) && !(h - dy < 50)) {
              setBounds(x + dx, y + dy, w - dx, h - dy);
              resize();
            }
            break;

          case Cursor.NE_RESIZE_CURSOR:
            if (!(w + dx < 50) && !(h - dy < 50)) {
              setBounds(x, y + dy, w + dx, h - dy);
              startPos = new Point(me.getX(), startPos.y);
              resize();
            }
            break;

          case Cursor.SW_RESIZE_CURSOR:
            if (!(w - dx < 50) && !(h + dy < 50)) {
              setBounds(x + dx, y, w - dx, h + dy);
              startPos = new Point(startPos.x, me.getY());
              resize();
            }
            break;

          case Cursor.SE_RESIZE_CURSOR:
            if (!(w + dx < 50) && !(h + dy < 50)) {
              setBounds(x, y, w + dx, h + dy);
              startPos = me.getPoint();
              resize();
            }
          break;

          case Cursor.MOVE_CURSOR:
              Rectangle bounds = getBounds();
              bounds.translate(dx, dy);
              setBounds(bounds);
              resize();
          }

          setCursor(Cursor.getPredefinedCursor(cursor));
        }
     }

   public void mouseReleased(MouseEvent mouseEvent) {
     startPos = null;
   }
  };
	public void setBounds(int x,int y,int w,int h){
		if(Shape.isRegular){
			super.setBounds(x, y, Math.max(w,h),Math.max(w, h) );
		}
		else{
			super.setBounds(x, y, w, h);
		}
		Shape.DrawBounds(x + 8, y + 8, (w - 16) < 0 ? 0: w-16, (h - 16) < 0 ? 0: h-16);
	}

	public MouseInputListener getResizeListener() {
		return resizeListener;
	}
}