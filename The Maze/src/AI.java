
public class AI {
	String[][] Maze;
	int X,Y,Z;
	SLinkedList DFS,BFS;
	Pos[][] Tele;
	Pos Start,End;
	public AI(){
		Maze = Main.readMaze();
		X = Main.X;
		Y=Main.Y;
		Z=Main.Z;
		Tele = new Pos[X*Y*Z][2];
		for(int z =0;z<Z;z++){
			for(int y=0;y<Y;y++){
				for(int x=0;x<X;x++){
					if(Maze[z][y].charAt(x)=='S'){
						Start = new Pos(x,y,z);
					}
					if(Maze[z][y].charAt(x)=='E'){
						End = new Pos(x,y,z);
					}
					if(Character.isDigit(Maze[z][y].charAt(x))){
						if(Tele[Maze[z][y].charAt(x)-'0'][0]!=null)
							Tele[Maze[z][y].charAt(x)-'0'][1] = new Pos(x,y,z);
						else
							Tele[Maze[z][y].charAt(x)-'0'][0] = new Pos(x,y,z);
					}
				}
			}
		}
	}
	public  SLinkedList DF(){
		Stack st = new Stack();
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
			if(x+1 < X && mazeCheck(Maze[z][y].charAt(x+1)) && !Visited[z][y][x+1]){
				SLinkedList alt = (SLinkedList) L.sublist(0, L.size());
				alt.add(new Pos(x+1,y,z));
				st.push(alt);
			}
			if(y+1 < Y && mazeCheck(Maze[z][y+1].charAt(x)) && !Visited[z][y+1][x]){
				SLinkedList alt = (SLinkedList) L.sublist(0, L.size());
				alt.add(new Pos(x,y+1,z));
				st.push(alt);
			}
			if(z+1 < Z && Maze[z][y].charAt(x)=='L' && !Visited[z+1][y][x] ){
				SLinkedList alt = (SLinkedList) L.sublist(0, L.size());
				alt.add(new Pos(x,y,z+1));
				st.push(alt);
			}
			if(x-1 >= 0 && mazeCheck(Maze[z][y].charAt(x-1)) && !Visited[z][y][x-1]){
				SLinkedList alt = (SLinkedList) L.sublist(0, L.size());
				alt.add(new Pos(x-1,y,z));
				st.push(alt);
			}
			if(y-1 >= 0 && mazeCheck(Maze[z][y-1].charAt(x))  && !Visited[z][y-1][x]){
				SLinkedList alt = (SLinkedList) L.sublist(0, L.size());
				alt.add(new Pos(x,y-1,z));
				st.push(alt);
			}
			if(z-1 >= 0 && Maze[z][y].charAt(x)=='L' && !Visited[z-1][y][x] ){
				SLinkedList alt = (SLinkedList) L.sublist(0, L.size());
				alt.add(new Pos(x,y,z-1));
				st.push(alt);
			}
			if(Character.isDigit(Maze[z][y].charAt(x))){
				SLinkedList alt = (SLinkedList) L.sublist(0, L.size());
				Pos one = Tele[Maze[z][y].charAt(x)-'0'][0];
				Pos two = Tele[Maze[z][y].charAt(x)-'0'][1];
				if(one.getX()==x && one.getY()==y && one.getZ()==z && !Visited[two.getZ()][two.getY()][two.getX()]){
					alt.add(two);
					st.push(alt);
				}
				else if(!Visited[one.getZ()][one.getY()][one.getX()]){
					alt.add(one);
					st.push(alt);
				}
			}
		}
		return null;
	}
	public boolean mazeCheck(char c){
		if(c != '#' && c!='O' && c!='T' && c!= 'N')
			return true;
		return false;
	}
	public SLinkedList BF(){
		Queue q = new Queue();
		SLinkedList start = new SLinkedList();
		boolean[][][] Visited = new boolean[Z][Y][X];
		Visited[Start.getZ()][Start.getY()][Start.getX()] = true;
		start.add(Start);
		q.push(start);
		while(!q.isEmpty()){
			SLinkedList L = (SLinkedList) q.pop();
			Pos t = (Pos)L.get(L.size()-1);
			int x,y,z;
			x = t.getX();
			y = t.getY();
			z = t.getZ();
			if(End.getX()==x && End.getY()==y && End.getZ()==z)
				return L;
			if(x+1 < X && mazeCheck(Maze[z][y].charAt(x+1)) && !Visited[z][y][x+1]){
				SLinkedList alt = (SLinkedList) L.sublist(0, L.size());
				alt.add(new Pos(x+1,y,z));
				Visited[z][y][x+1]=true;
				q.push(alt);
			}
			if(y+1 < Y && mazeCheck(Maze[z][y+1].charAt(x)) && !Visited[z][y+1][x]){
				SLinkedList alt = (SLinkedList) L.sublist(0, L.size());
				alt.add(new Pos(x,y+1,z));
				Visited[z][y+1][x]=true;
				q.push(alt);
			}
			if(z+1 < Z && Maze[z][y].charAt(x)=='L' && !Visited[z+1][y][x] ){
				SLinkedList alt = (SLinkedList) L.sublist(0, L.size());
				alt.add(new Pos(x,y,z+1));
				Visited[z+1][y][x]=true;
				q.push(alt);
			}
			if(x-1 >= 0 && mazeCheck(Maze[z][y].charAt(x-1)) && !Visited[z][y][x-1]){
				SLinkedList alt = (SLinkedList) L.sublist(0, L.size());
				alt.add(new Pos(x-1,y,z));
				Visited[z][y][x-1]=true;
				q.push(alt);
			}
			if(y-1 >= 0 && mazeCheck(Maze[z][y-1].charAt(x))  && !Visited[z][y-1][x]){
				SLinkedList alt = (SLinkedList) L.sublist(0, L.size());
				alt.add(new Pos(x,y-1,z));
				Visited[z][y-1][x]=true;
				q.push(alt);
			}
			if(z-1 >= 0 && Maze[z][y].charAt(x)=='L' && !Visited[z-1][y][x] ){
				SLinkedList alt = (SLinkedList) L.sublist(0, L.size());
				alt.add(new Pos(x,y,z-1));
				Visited[z-1][y][x]=true;
				q.push(alt);
			}
			if(Character.isDigit(Maze[z][y].charAt(x))){
				SLinkedList alt = (SLinkedList) L.sublist(0, L.size());
				Pos one = Tele[Maze[z][y].charAt(x)-'0'][0];
				Pos two = Tele[Maze[z][y].charAt(x)-'0'][1];
				if(one.getX()==x && one.getY()==y && one.getZ()==z && !Visited[two.getZ()][two.getY()][two.getX()]){
					alt.add(two);
					q.push(alt);
					Visited[two.getZ()][two.getY()][two.getX()]=true;
				}
				else if(!Visited[one.getZ()][one.getY()][one.getX()]){
					alt.add(one);
					Visited[one.getZ()][one.getY()][one.getX()]=true;
					q.push(alt);
				}
			}
		}
		return null;
	}
	public String path(SLinkedList k){
		String str = new String();
		Pos s = (Pos)k.get(0);
		int x,y,z;
		x = s.getX();
		y = s.getY();
		z = s.getZ();
		for(int i=1;i<k.size();i++){
			Pos p = (Pos)k.get(i);
			Pos n = null;
			if(i+1 < k.size()){
				n = (Pos)k.get(i+1);
			}
			if(x-p.getX()==1)
				str+="L";
			else if(y-p.getY()==1)
				str+="U";
			else if(z-p.getZ()==1)
					str+="A";
			else if(x-p.getX()==-1)
				str+="R";
			else if(y-p.getY()==-1)
				str+="D";
			else if(z-p.getZ()==-1)
				str+="V";
			x = p.getX();
			y = p.getY();
			z = p.getZ();
			if(Character.isDigit(Maze[p.getZ()][p.getY()].charAt(p.getX()))){
				if(Maze[n.getZ()][n.getY()].charAt(n.getX())==Maze[p.getZ()][p.getY()].charAt(p.getX())){
					str+="T";
					i++;
					p = (Pos)(k.get(i));
					x = p.getX();
					y = p.getY();
					z = p.getZ();
				}
			}
		}
		return str;
	}
}
