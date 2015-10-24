package Controller.LootStates;

import java.awt.Graphics;
import java.awt.Point;
import java.io.Serializable;

import Controller.GameState;
import Model.Characters.Character;
import Model.Loots.Loot;

public class FallingLoot implements LootState,Serializable {

	private static final long serialVersionUID = 1L;
	
	private Loot loot;

	public FallingLoot(Loot L){
		loot = L;
	}
	
	@Override
	public void draw(Graphics g) {
		loot.drawShape(g);
	}

	@Override
	public void move() {
		loot.y+=2;
		//outside the boarders to be recycled later
		if(loot.y > 700){
			loot.setState(loot.getDormant());
			return;
		}
		Character P1 = GameState.getInstance().getPlayerOne();
		Character P2 = GameState.getInstance().getPlayerTwo();
		Point p = new Point(loot.x,loot.y);
		if(P1 != null && P1.getCollectorArea().contains(p))
			P1.addLoot(loot);
		else if(P2 != null && P2.getCollectorArea().contains(p))
			P2.addLoot(loot);
	}

}
