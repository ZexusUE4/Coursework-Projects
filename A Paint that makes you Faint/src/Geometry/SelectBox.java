package Geometry;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import UserInterface.Sketch;

public class SelectBox extends ShapeComponent {
	
	
	private static final long serialVersionUID = -4312397820226339206L;
	public  ResizableBox SelectBorder = new ResizableBox(this);
	public java.util.Vector<ShapeComponent> Contained;
	public SelectBox(Coordinate T,int w,int h){
		isSelectBox = true;
		TopLeft = T;
		Width = w;
		Height = h;
	}
	@Override
	public void Draw(Graphics g) {
		// TODO Auto-generated method stub
		if(!Visible) return;
		Graphics2D g2d = (Graphics2D) g.create();
		
        Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
        g2d.setStroke(dashed);
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawRect(TopLeft.x, TopLeft.y, Width, Height);
        g2d.dispose();
	}
	public java.util.Vector<ShapeComponent> getContained(){
		java.util.Vector<ShapeComponent> V = new java.util.Vector<ShapeComponent>();
		for(int i=0;i< Sketch.Shapes.size();i++){
			SketchComponent Y = Sketch.Shapes.elementAt(i);
			if(Y.isSelectBox || Y.isText) continue;
			ShapeComponent X = (ShapeComponent) Y;
			if(ResizableBorder.contain(TopLeft.x, TopLeft.y, Width, Height, X.TopLeft.x, X.TopLeft.y) && ResizableBorder.contain(TopLeft.x, TopLeft.y, Width, Height, X.TopLeft.x+ X.Width, X.TopLeft.y + X.Height)){
				V.add(X);
				X.Selected = true;
			}
		}
		Visible = false;
		Contained = V;
		return V;
	}
	public ResizableBox SelectBorder(){
		SelectBorder.setBounds(TopLeft.x - 8, TopLeft.y - 8, Width + 16, Height + 16);
		BorderIn = true;
		return SelectBorder;
	}
}
