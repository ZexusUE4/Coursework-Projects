import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
	static String[][] Maze;
	static int X,Y,Z;
	public static void main(String args[]){
		new Generator(8,8,2,2,10);
		Maze = readMaze();
		AI a = new AI();
		char[][][] M = new char[Z][Y][X];
		SLinkedList p = a.BF();
		if(p==null){
			System.err.println("No Path Found !");
			return;
		}
		for(int i=0;i<p.size();i++){
			Pos Pa = (Pos)(p.getNodeAt(i).getElement());
			M[Pa.getZ()][Pa.getY()][Pa.getX()]='P';
		}
		for(int z=0;z<Z;z++)
			for(int y=0;y<Y;y++){
				for(int x=0;x<X;x++){
					if(M[z][y][x]=='P')
						System.out.print('P');
					else
						System.out.print(Maze[z][y].charAt(x));
				}
				System.out.println();
			}
		System.out.println(a.path(p));
	}
	public static String[][] readMaze(){
		Scanner sc = null;
		try {
			sc = new Scanner(new File("E:\\CSED\\Javaz\\The Maze\\src\\Gen.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.err.println("Error reading File");
		}
		X = sc.nextInt();
		Y = sc.nextInt();
		Z = sc.nextInt();
		String[][] Maze_ = new String[Z][Y];
		for(int z=0;z<Z;z++){
			for(int y=0;y<Y;y++){
				Maze_[z][y]=sc.next();
			}
		}
		sc.close();
		return Maze_;
	}
}
