package Controller.Memento;

import java.util.ArrayList;
import java.util.Random;

public class CareTaker {
	private ArrayList<Memento> MementoList = new ArrayList<Memento>();
	private static CareTaker instance;
	
	public static CareTaker getInstance(){
		if(instance == null)
			instance = new CareTaker();
		return instance;
	}
	
	public void add(Memento State){
		MementoList.add(State);
	}
	
	public Memento get(int index){
		return MementoList.get(index);
	}
	
	public Memento getRandom(){
		Random rand = new Random();
		return MementoList.get(rand.nextInt(MementoList.size()));
	}
	
	public void reset(){
		MementoList.clear();
	}

}
