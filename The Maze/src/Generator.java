import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;


public class Generator {
	char[][][] Maze;
	boolean[][][] path;
	Pos Start,End;
	int X,Y,Z,T,M;
	//T is the number of portals , M is the number of Monsters
	public Generator(int X,int Y,int Z,int T,int M){
		this.X = X;
		this.T = T;
		this.Y = Y;
		this.Z = Z;
		this.M = M;
		Maze = new char[Z][Y][X];
		for(int z=0;z<Z;z++)
			for(int y=0;y<Y;y++)
				for(int x=0;x<X;x++){
					Maze[z][y][x]='.';
				}
		int sx,sy,sz,ex,ey,ez;
		Random g = new Random();
		sx = g.nextInt(X);
		sy = g.nextInt(Y);
		sz = g.nextInt(Z);
		Start = new Pos(sx,sy,sz);
		Maze[sz][sy][sx]='S';
		ex = g.nextInt(X);
		ey = g.nextInt(Y);
		ez = g.nextInt(Z);
		while((sz==ez && Z!=1) || sx==ex || sy==ey){
			ex = g.nextInt(X);
			ey = g.nextInt(Y);
			ez = g.nextInt(Z);
		}
		End = new Pos(ex,ey,ez);
		path = new boolean[Z][Y][X];
		Maze[ez][ey][ex]='E';
		for(int z=0;z<Z;z++){
			int x = g.nextInt(X);
			int y = g.nextInt(Y);
			while(Maze[z][y][x]=='S' ||  Maze[z][y][x]=='E'){
				x = g.nextInt(X);
				y = g.nextInt(Y);
			}
			Maze[z][y][x]='L';
		}
		SLinkedList p = BF();
		for(int i=0;i<p.size();i++){
			Pos t = (Pos)(p.get(i));
			path[t.getZ()][t.getY()][t.getX()]=true;
		}
		for(int z1=0;z1<Z;z1++)
			for(int y1=0;y1<Y;y1++)
				for(int x1=0;x1<X;x1++){
					if(!path[z1][y1][x1] && g.nextInt(4)==1){
						Maze[z1][y1][x1]='#';
					}
				}
		for(int i=1;i<=T;i++){
			int x1,x2,y1,y2,z1,z2;
			x1 = g.nextInt(X);
			y1 = g.nextInt(Y);
			z1 = g.nextInt(Z);
			x2 = g.nextInt(X);
			y2 = g.nextInt(Y);
			z2 = g.nextInt(Z);
			int c = 0;
			while(Maze[z1][y1][x1]!='.' && c!= 40 ){
				x1 = g.nextInt(X);
				y1 = g.nextInt(Y);
				z1 = g.nextInt(Z);
				c++;
			}
			Maze[z1][y1][x1]=(char)(i+'0');
			while(Maze[z2][y2][x2]!='.' && c!= 40){
				x2 = g.nextInt(X);
				y2 = g.nextInt(Y);
				z2 = g.nextInt(Z);
				c++;
			}
			if(c>=40){
				System.err.println("Couldn't generate path with such details !");
				return;
			}
			Maze[z2][y2][x2]=(char)(i+'0');
		}
		for(int i=0;i<M;i++){
			int mtype = g.nextInt(4);
			char m = 0;
			if(mtype==1)
				m='O';
			else if(mtype==2)
				m='T';
			else
				m='N';
			int px,py,pz;
			px = g.nextInt(X);
			py = g.nextInt(Y);
			pz = g.nextInt(Z);
			int c = 0;
			while(Maze[pz][py][px]!='.' && c<=40){
				px = g.nextInt(X);
				py = g.nextInt(Y);
				pz = g.nextInt(Z);
				c++;
			}
			if(c>= 40){
				System.err.println("Couldn't generate path with such details !");
				return;
			}
			Maze[pz][py][px]=m;
		}
		try {
			writeMaze();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void writeMaze() throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter writer = new PrintWriter("E:\\CSED\\Javaz\\The Maze\\src\\Gen.txt", "UTF-8");
		writer.println(X + " " + Y + " " + Z);
		for(int z=0;z<Z;z++)
			for(int y=0;y<Y;y++){
				for(int x=0;x<X;x++){
					writer.print(Maze[z][y][x]);
				}
				writer.println();
			}
		writer.close();
	}
	public  SLinkedList BF(){
		Queue st = new Queue();
		boolean[][][] Visited = new boolean[Z][Y][X];
		SLinkedList start = new SLinkedList();
		start.add(Start);
		st.push(start);
		while(!st.isEmpty()){
			SLinkedList L = (SLinkedList)st.pop();
			Pos T = (Pos)(L.getNodeAt(L.size() -1).getElement());
			int x,y,z;
			x = T.getX();
			y = T.getY();
			z = T.getZ();
			Visited[z][y][x] = true;
			if(End.getX()==x && End.getY()==y && End.getZ()==z)
				return L;
			if(x+1 < X  && !Visited[z][y][x+1]){
				SLinkedList alt = (SLinkedList) L.sublist(0, L.size());
				alt.add(new Pos(x+1,y,z));
				Visited[z][y][x+1]=true;
				st.push(alt);
			}
			if(y+1 < Y &&  !Visited[z][y+1][x]){
				SLinkedList alt = (SLinkedList) L.sublist(0, L.size());
				Visited[z][y+1][x]=true;
				alt.add(new Pos(x,y+1,z));
				st.push(alt);
			}
			if(z+1 < Z && !Visited[z+1][y][x] && Maze[z][y][x]=='L'){
				SLinkedList alt = (SLinkedList) L.sublist(0, L.size());
				Visited[z+1][y][x]=true;
				alt.add(new Pos(x,y,z+1));
				st.push(alt);
			}
			if(x-1 >= 0 &&  !Visited[z][y][x-1]){
				SLinkedList alt = (SLinkedList) L.sublist(0, L.size());
				alt.add(new Pos(x-1,y,z));
				Visited[z][y][x-1]=true;
				st.push(alt);
			}
			if(y-1 >= 0 && !Visited[z][y-1][x]){
				SLinkedList alt = (SLinkedList) L.sublist(0, L.size());
				alt.add(new Pos(x,y-1,z));
				Visited[z][y-1][x]=true;
				st.push(alt);
			}
			if(z-1 >= 0 && !Visited[z-1][y][x] && Maze[z][y][x]=='L'){
				SLinkedList alt = (SLinkedList) L.sublist(0, L.size());
				alt.add(new Pos(x,y,z-1));
				Visited[z-1][y][x]=true;
				st.push(alt);
			}
		}
		return null;
	}
}
