package Model.Characters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import Controller.Controller;
import Controller.GameState;
import Controller.Memento.CareTaker;
import Model.Loots.Loot;

public class ScoreObserver implements Observer,Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public void update(Observable Obj, Object attr) {
		@SuppressWarnings("unchecked")
		ArrayList<Loot> Loots = (ArrayList<Loot>) attr;
		Character Char = (Character) Obj;
		Loots = Char.Loots;
		Loot last = Loots.get(Loots.size()-1);
		
		if(last.Name.equals("TimeShifter")){
			Loots.remove(Loots.size()-1);
			last.setState(last.getDormant());
			CareTaker CT = CareTaker.getInstance();
			Controller Cont = Controller.getInstance();
			GameState randomized = null;
			try{
				randomized = CT.getRandom().getState();
			}catch(Exception e){
				return;
			}
			Cont.Pane.setInGame(randomized);
			return;
		}
		
		if(last.ColourShifter){
			last.ColourShifter = false;
			if(Loots.size() > 1){
				last.Colour = Loots.get(Loots.size()-2).Colour;
			}
		}
		if(Loots.size() < 3)
			return;
		if(Loots.get(Loots.size()-1).Colour.equals(Loots.get(Loots.size()-2).Colour) && 
				Loots.get(Loots.size()-2).Colour.equals(Loots.get(Loots.size()-3).Colour)){
			Char.Score++;
			int cnt = 3;
			while(cnt-- > 0){
				Loot Last = Loots.get(Loots.size()-1);
				Last.setState(Last.getDormant());
				Loots.remove(Loots.size()-1);
			}
		}
	}

}
