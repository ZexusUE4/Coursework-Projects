package UserInterface;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.lang.reflect.Constructor;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

import Geometry.Circle;
import Geometry.Coordinate;
import Geometry.Line;
import Geometry.Oval;
import Geometry.Rectangle;
import Geometry.SelectBox;
import Geometry.ShapeComponent;
import Geometry.SketchComponent;
import Geometry.Square;
import Geometry.TextComponent;
import Geometry.Triangle;
import SaveAndLoad.SaveAndLoad;

public class Sketch extends JPanel implements ActionListener {

	private static final long serialVersionUID = 3513465288463210679L;
	
	private Timer T;
	public static int CurState;
	public static Vector<Vector<SketchComponent> > States = new Vector<Vector<SketchComponent>>();
	public static Vector<SketchComponent> Shapes = new Vector<SketchComponent>();
	public static  Vector<SketchComponent> BeforeState = null;
	public static Vector<ShapeComponent> Selected = new Vector<ShapeComponent>();
	public SelectBox slct;
	public static boolean Ctrl = false;
	static int x , y , dx ,dy;
	public static JPanel ref;
	public static MouseInputListener mouse;
	public static Color ChoosenColor = Color.RED;
	static int cx,cy;
	static boolean flag = true,isDrawingText = false,SelectBoxExists = false;
	static SketchComponent drawingSHape;
	Sketch(){
		States.add(new Vector<SketchComponent>());
		States.add(new Vector<SketchComponent>());
		CurState = 1;
		Shapes = States.elementAt(CurState);
		T = new Timer(25,this);
		T.start();
		ref = this;
		mouse = new MouseInputAdapter() {
			@Override
			public void mousePressed (MouseEvent e)
			{
				if(isDrawingText){
					isDrawingText = false;
					if(drawingSHape != null)
						Shapes.add(drawingSHape);
				}
				x = e.getX();
				y = e.getY();
				PlaceShape();
			}
			@Override
			public void mouseReleased (MouseEvent e)
			{
				if(drawingSHape == null) return;
				drawingSHape.onPlacing();
				if(MainFrame.choose == MainFrame.ShapePick.LINE){
					Line L = (Line)(drawingSHape);
					L.slope = (L.End.y - L.Start.y)/(double)(L.End.x - L.Start.x);
				}
				if(MainFrame.choose == MainFrame.ShapePick.GRPSLCT && drawingSHape != null){
					SelectBox Bx = (SelectBox)drawingSHape;
					Selected = (Vector<ShapeComponent>) Bx.getContained();
				}
				if(MainFrame.choose != MainFrame.ShapePick.NOTH)
					SaveHistory(drawingSHape);
				drawingSHape = null;
			}
			@Override
			public void mouseDragged (MouseEvent e){
				if(flag){
					dx = e.getX() - x;
					dy = e.getY() - y;
				}
			}
			@Override
			public void mouseMoved(MouseEvent e){
				if(isDrawingText){
					cx = e.getX();
					cy = e.getY();
				}
			}
		};
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		repaint();
	}
	
	public void PlaceShape(){
		dx = 0;
		dy = 0;
		SketchComponent C = null;
		if(MainFrame.choose==MainFrame.ShapePick.CSTMCLS){
			ClassLoader parent = loading.MyClassLoader.class.getClassLoader();
			loading.MyClassLoader classLoader = new loading.MyClassLoader(parent);
			try{
				String s="Geometry."+MainFrame.name;
				Class<?> Rlc = classLoader.loadClass(s,MainFrame.url);				
				Constructor<?> c = Rlc.getDeclaredConstructor(Coordinate.class,Integer.class , Integer.class );				 
				Object Rlo = c.newInstance (new Coordinate(x, y),0,0) ;				
				Class<?>[] param1 = new Class [1];
				param1[0] = Graphics.class;
				C=(SketchComponent) Rlo ;
			}
			catch (Exception e) {
			e.printStackTrace();
			}
		}
		else if(MainFrame.choose==MainFrame.ShapePick.GRPSLCT && !SelectBoxExists){
			C = new SelectBox(new Coordinate(x, y),0,0);
			SelectBoxExists = true;
			UnSelect();
		}
		else if(MainFrame.choose==MainFrame.ShapePick.RECTANGLE){
			 C = new Rectangle(new Coordinate(x, y),0,0);
		}
		else if(MainFrame.choose==MainFrame.ShapePick.SQUARE){
			 C = new Square(new Coordinate(x, y),0,0);
		}
		else if(MainFrame.choose==MainFrame.ShapePick.CIRCLE){
			 C = new Circle(new Coordinate(x, y),0,0);
		}
		else if(MainFrame.choose==MainFrame.ShapePick.OVAL){
			C = new Oval(new Coordinate(x, y),0,0);
		}
		else if(MainFrame.choose==MainFrame.ShapePick.TRIANGLE){
			C = new Triangle(new Coordinate(x, y),0,0);
		}
		else if(MainFrame.choose==MainFrame.ShapePick.LINE){
			C = new Line(new Coordinate(x, y),new Coordinate(x+dx,x+dy));
		}
		drawingSHape = C;
		flag = true;
	}
	public static void preDefinedDraw(int x,int y,int w,int h){
		if(MainFrame.choose==MainFrame.ShapePick.CSTMCLS){
			ClassLoader parent = loading.MyClassLoader.class.getClassLoader();
			loading.MyClassLoader classLoader = new loading.MyClassLoader(parent);
			try{
				String s="Geometry."+MainFrame.name;
				Class<?> Rlc = classLoader.loadClass(s,MainFrame.url);				
				Constructor<?> c = Rlc.getDeclaredConstructor(Coordinate.class,Integer.class , Integer.class );				 
				Object Rlo = c.newInstance (new Coordinate(x, y),0,0) ;				
				Class<?>[] param1=new Class [1];
				param1[0]=Graphics.class;
				SketchComponent C =(SketchComponent) Rlo ;
				Shapes.add(C);
			}
			catch (Exception e) {
			e.printStackTrace();
			}
		}
		else if(MainFrame.choose==MainFrame.ShapePick.RECTANGLE){
			 SketchComponent C = new Rectangle(new Coordinate(x,y),w,h);
			 Shapes.add(C);
		}
		else if(MainFrame.choose==MainFrame.ShapePick.SQUARE){
			 SketchComponent C = new Square(new Coordinate(x,y),w,h);
			 Shapes.add(C);
		}
		else if(MainFrame.choose==MainFrame.ShapePick.CIRCLE){
			 SketchComponent C = new Circle(new Coordinate(x,y),w,h);
			 Shapes.add(C);
		}
		else if(MainFrame.choose==MainFrame.ShapePick.OVAL){
			SketchComponent C = new Oval(new Coordinate(x,y),w,h);
			 Shapes.add(C);
		}
		else if(MainFrame.choose==MainFrame.ShapePick.TRIANGLE){
			SketchComponent C = new Triangle(new Coordinate(x,y),w,h);
			 Shapes.add(C);
		}
		else if(MainFrame.choose==MainFrame.ShapePick.LINE){
			SketchComponent C = new Line(new Coordinate(x,y),new Coordinate(x+w,y+h));
			 Shapes.add(C);
		}
		else{
			JOptionPane.showMessageDialog(new JFrame(), "No SketchComponentSelected !", "Dialog",
			        JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	public void paint(Graphics g){
		Shapes = States.elementAt(CurState);
		super.paint(g);
		boolean foundBox = false;
		for(int i=Shapes.size() - 1; i >= 0; i--){
			SketchComponent X = Shapes.elementAt(i);
			if( X == null) continue;
			if(!foundBox && X.isSelectBox){
				foundBox = true;
				slct = (SelectBox) X;
			}
			else if(foundBox && X.isSelectBox)
				X.Deleted = true;
			if(MainFrame.choose != MainFrame.ShapePick.GRPSLCT && X.isSelectBox){
				X.Deleted = true;
			}
		}
		for(int i = 0 ; i < Shapes.size() ; i++)
		{
			SketchComponent C = Shapes.elementAt(i);
			if(C == null || C.Deleted){
				Shapes.remove(i);
				i--;
				continue;
			}
			C.Draw(g);
			ShapeComponent Sh = null;
			try{
				Sh = (ShapeComponent) C;
			}catch(Exception e){
				
			}
			if(Sh != null){
				if(Sh.isSelectBox){
					if(!Sh.BorderIn){
						SelectBox B = (SelectBox) Sh;
						ref.add(B.SelectBorder());
						B.SelectBorder.requestFocus();
						System.out.println("Added");
					}
				}
				else if(!Sh.BorderIn){
					ref.add(Sh.Border());
				}
			}
		}
		if(drawingSHape!= null){
			if(!drawingSHape.isText){
				if(!drawingSHape.isSelectBox){
				ShapeComponent C = (ShapeComponent)drawingSHape;
					if(C.isLine){
						Line L = (Line)C;
						if(dx <0 && flag){
							L.End.x = dx+x;
							L.TopLeft.x = dx+x;
						}
						else if(flag){
							L.End.x=x;
							L.TopLeft.x=x;
						}
						if(dy <0 && flag){
							L.End.y=dy+y;
							L.TopLeft.y=dy+y;
						}
						else if(flag){
							L.Start.y=y;
							L.TopLeft.y=y;
						}
						L.End.x =x+dx;
						L.End.y = y+dy;
						L.Width=Math.abs(dx);
						L.Height=Math.abs(dy);
						L.setBounds(L.TopLeft.x,L.TopLeft.y,L.Width,L.Height);
						L.Draw(g);
					}
					else if(C.Name == "Square" || C.Name == "Circle")
					{
						C.Draw(g);
						if(dx <0 && flag && dy <0)
						{
							C.TopLeft.x=Math.max(dx , dy)+x;
							C.TopLeft.y=Math.max(dx , dy)+y;
							C.Width=Math.abs(Math.max(dx , dy));
							C.Height=Math.abs(Math.max(dx , dy));
						}
						else if( dx < 0 && flag )
						{
							C.TopLeft.x=dx+x;
							C.TopLeft.y=Sketch.y;
							C.Width=Math.abs(dx);
							C.Height=Math.abs(dx);
						}
						else if( dy < 0 && flag )
						{
							C.TopLeft.y=dy+y;
							C.TopLeft.x=Sketch.x;
							C.Width=Math.abs(dy);
							C.Height=Math.abs(dy);
						}
						else
						{
							C.Width = Math.min(dx , dy);
							C.Height= Math.min(dx,dy);
						}
						C.DrawBounds(C.TopLeft.x,C.TopLeft.y,C.Width,C.Height );
					}
					else if(!drawingSHape.isText)
					{
						C.Draw(g);
						if(dx <0 && flag)
							C.TopLeft.x=dx+x;
						else if(flag)
							C.TopLeft.x=Sketch.x;
						if(dy <0 && flag)
							C.TopLeft.y=dy+y;
						else if(flag)
							C.TopLeft.y=Sketch.y;
						C.Width=Math.abs(dx);
						C.Height=Math.abs(dy);
						C.DrawBounds(C.TopLeft.x,C.TopLeft.y,C.Width,C.Height );
					}
					
				}
				else{
					SelectBox C = (SelectBox) drawingSHape;
					C.Draw(g);
					if(dx <0 && flag)
						C.TopLeft.x=dx+x;
					else if(flag)
						C.TopLeft.x=Sketch.x;
					if(dy <0 && flag)
						C.TopLeft.y=dy+y;
					else if(flag)
						C.TopLeft.y=Sketch.y;
					C.Width=Math.abs(dx);
					C.Height=Math.abs(dy);
					C.setBounds(C.TopLeft.x,C.TopLeft.y,C.Width,C.Height );
				}
			}
			else if(drawingSHape != null && drawingSHape.isText && isDrawingText){
				TextComponent C = (TextComponent) drawingSHape;
				C.setColor(ChoosenColor);
				C.Draw(g);
				C.TopLeft = new Coordinate(cx,cy);
			}
		}
	}
	public static void SelectionNotify(){
		if(BeforeState != null){
			States.insertElementAt(BeforeState, CurState);
			CurState++;
			BeforeState = null;
		}
		for(int i = 0 ;i < Shapes.size();i++){
			SketchComponent C = Shapes.elementAt(i);
			ShapeComponent S = null;
			boolean casted = false;
			try{
				S = (ShapeComponent) C;
				casted = true;
			}
			catch(Exception e){
				
			}
			if(casted && MainFrame.choose != MainFrame.ShapePick.GRPSLCT && C.isSelectBox){
				SelectBox Box = (SelectBox) C;
				C.Deleted = true;
				ref.remove(Box.SelectBorder());
				SelectBoxExists = false;
			}
			if(casted && MainFrame.choose == MainFrame.ShapePick.NOTH && !isDrawingText){
				S.res.addMouseListener(S.res.getResizeListener());
				S.res.addMouseMotionListener(S.res.getResizeListener());
			}
			else if(casted){
				S.res.removeMouseListener(S.res.getResizeListener());
				S.res.removeMouseMotionListener(S.res.getResizeListener());
			}
		}
	}
	public static Object Copy(Object O){
		SaveAndLoad Saver = new SaveAndLoad();
		String url = System.getProperty("user.dir");
		url += "\\Cache.xml";
		Saver.save(O, url);
		return Saver.load(url);
	}
	public static void SaveHistory(SketchComponent Added){
		@SuppressWarnings("unchecked")
		Vector<SketchComponent> Loaded = (Vector<SketchComponent>)Copy(Shapes);
		if(Added != null)
			Loaded.add(Added);
		ref.removeAll();
		for(int i= States.size()-1; i > CurState ;i--){
			States.remove(i);
		}
		States.add(Loaded);
		CurState++;
		removeBorders(States.elementAt(CurState));
		Shapes = States.elementAt(CurState);
	}
	public static void Undo(){
		if(CurState == 0 )
			return;
		ref.removeAll();
		CurState--;
		removeBorders(States.elementAt(CurState));
		Shapes = States.elementAt(CurState);
	}
	public static void Redo(){
		if(CurState==States.size() - 1)
			return;
		ref.removeAll();
		CurState++;
		removeBorders(States.elementAt(CurState));
		Shapes = States.elementAt(CurState);
	}
	public static void removeBorders(Vector<SketchComponent> Sh){
		for(int i = Sh.size() - 1;i >= 0 ;i--){
			SketchComponent X = Sh.elementAt(i);
			ShapeComponent C = null;
			try{
				C = (ShapeComponent) X;
			}catch(Exception e){
				
			}
			if(C!= null){
				C.BorderIn = false;
			}
		}
	}
	public static void UnSelect(){
		for(int i=0;i<Shapes.size();i++)
			Shapes.elementAt(i).Selected = false;
	}
}
