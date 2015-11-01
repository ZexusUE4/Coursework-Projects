package Geometry;

import java.io.Serializable;

public final class Coordinate implements Serializable {

	private static final long serialVersionUID = 7535416246906652871L;
	public int x;
	public int y;
	public Coordinate(int x,int y){
		this.x = x;
		this.y = y;
	}
	public Coordinate MidPoint(Coordinate target){
		Coordinate Mid = new Coordinate(0,0);
		Mid.x = (this.x + target.x)/2;
		Mid.y = (this.y + target.y)/2;
		return Mid;
	}
}
