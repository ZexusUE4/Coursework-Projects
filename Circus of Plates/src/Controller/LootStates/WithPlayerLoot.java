package Controller.LootStates;

import java.awt.Graphics;
import java.io.Serializable;

import Model.Loots.Loot;

public class WithPlayerLoot implements LootState,Serializable {

	private static final long serialVersionUID = 1L;
	
	private Loot loot;

	public WithPlayerLoot(Loot L){
		loot = L;
	}
	
	@Override
	public void draw(Graphics g) {
		loot.drawShape(g);
	}

	@Override
	public void move() {
		
	}

}
