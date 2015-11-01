package Geometry;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

import UserInterface.Sketch;

public class ResizableBox extends JComponent {
	private static final long serialVersionUID = -749461293168044666L;
	public SelectBox Box;
	public KeyListener KeyAction = new KeyListener(){

		@Override
		public void keyPressed(KeyEvent k) {
			// TODO Auto-generated method stub
			int keycode = k.getKeyCode();
			System.out.println("Pressed");
			if(keycode == KeyEvent.VK_DELETE){
				Vector<SketchComponent> V = Sketch.Shapes;
				for(int i = 0;i< V.size();i++){
					ShapeComponent Shape = (ShapeComponent) V.elementAt(i);
					if(Shape.Selected){
						Shape.Deleted = true;
						Sketch.ref.remove(Shape.Border());
					}
				}
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
public ResizableBox(Component comp) {
    this(comp, new ResizableBorder());
  }



public ResizableBox(Component comp, ResizableBorder border) {
	 Box = (SelectBox)comp;
    setLayout(new BorderLayout());
    add(comp);
    setBorder(border);
    addMouseListener(getResizeListener());
    addMouseMotionListener(getResizeListener());
    addKeyListener(KeyAction);
  }
	@SuppressWarnings("unchecked")
	public void requestFocus() {
		// TODO Auto-generated method stub
		super.requestFocus();
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
  private void resize(int dx,int dy,int dw,int dh) {
	  Vector<SketchComponent> V = Sketch.Shapes;
	  for(int i = 0;i<V.size();i++){
		  SketchComponent Q = V.elementAt(i);
		  if(Q.Selected){
			  ShapeComponent G = (ShapeComponent) Q;
			  G.res.setBounds(G.res.getX() + dx ,G.res.getY() + dy, G.res.getWidth() + dw, G.res.getHeight() + dh);
		  }
	  }
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
      if(!hasFocus()) requestFocus();
      repaint();
    }

    public void mouseDragged(MouseEvent me) {
      if (startPos != null) {

        int x = getX();
        int y = getY();
        int w = getWidth();
        int h = getHeight();

        int dx = me.getX() - startPos.x;
        int dy = me.getY() - startPos.y;
 
        switch (cursor) {
          case Cursor.N_RESIZE_CURSOR:
            if (!(h - dy < 50)) {
              setBounds(x, y + dy, w, h - dy);
             resize(0,dy,0,-dy);
            }
            break;

          case Cursor.S_RESIZE_CURSOR:
            if (!(h + dy < 50)) {
              setBounds(x, y, w, h + dy);
              startPos = me.getPoint();
              resize(0,0,0,dy);
            }
            break;

          case Cursor.W_RESIZE_CURSOR:
            if (!(w - dx < 50)) {
              setBounds(x + dx, y, w - dx, h);
              resize(dx,0,-dx,0);
            }
            break;

          case Cursor.E_RESIZE_CURSOR:
            if (!(w + dx < 50)) {
              setBounds(x, y, w + dx, h);
             startPos = me.getPoint();
              resize(0,0,dx,0);
            }
            break;

          case Cursor.NW_RESIZE_CURSOR:
            if (!(w - dx < 50) && !(h - dy < 50)) {
              setBounds(x + dx, y + dy, w - dx, h - dy);
              resize(dx,dy,-dx,-dy);
            }
            break;

          case Cursor.NE_RESIZE_CURSOR:
            if (!(w + dx < 50) && !(h - dy < 50)) {
              setBounds(x, y + dy, w + dx, h - dy);
              startPos = new Point(me.getX(), startPos.y);
              resize(0,dy,dx,-dy);
            }
            break;

          case Cursor.SW_RESIZE_CURSOR:
            if (!(w - dx < 50) && !(h + dy < 50)) {
              setBounds(x + dx, y, w - dx, h + dy);
              startPos = new Point(startPos.x, me.getY());
              resize(dx,0,-dx,dy);
            }
            break;

          case Cursor.SE_RESIZE_CURSOR:
            if (!(w + dx < 50) && !(h + dy < 50)) {
              setBounds(x, y, w + dx, h + dy);
              startPos = me.getPoint();
              resize(0,0,dx,dy);
            }
          break;

          case Cursor.MOVE_CURSOR:
            Vector<SketchComponent> V = Sketch.Shapes;
            setBounds(getX() + dx, getY() + dy, getWidth() , getHeight());
            for(int i = 0;i<V.size();i++){
      		  SketchComponent Q = V.elementAt(i);
      		  if(Q.Selected){
      			  ShapeComponent G = (ShapeComponent) Q;
      			  G.res.setBounds(G.res.getX() + dx , G.res.getY() + dy, G.res.getWidth(), G.res.getHeight());
      		  }
      	  }
            if (getParent() != null) {
              ((JComponent)getParent()).revalidate();
            }
          }


          setCursor(Cursor.getPredefinedCursor(cursor));
        }
     }

   public void mouseReleased(MouseEvent mouseEvent) {
     startPos = null;
    }
  };
	@Override
	public void setBounds(int x,int y,int w,int h){
		super.setBounds(x, y, w, h);
		Box.DrawBounds(x, y, w, h);
	}

	public MouseInputListener getResizeListener() {
		return resizeListener;
	}
}
