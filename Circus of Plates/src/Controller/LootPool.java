package Controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Random;
import java.util.Stack;

import Model.Loots.Loot;

public class LootPool implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private HashMap<String , Stack <Loot>> PoolLoot;
	private LootFactory Fact ;
	private static LootPool instance = null;
	private LootPool()
	{
		Fact = LootFactory.getInstance();;
		PoolLoot = new HashMap<String , Stack <Loot>>();
	}
	
	public static LootPool getInstance()
	{
		if(instance == null)
		{
			instance = new LootPool();
		}
		return instance;
	}
	
	public Loot getLoot (String LootName , int x , int y) 
	{
		try{
			if(!PoolLoot.containsKey(LootName) )
			{
				Stack<Loot> creating= new Stack<Loot>();
				creating.push(Fact.createLoot(LootName, x, y));
				PoolLoot.put(LootName, creating);
			}
			else
			{
				Stack <Loot> Stk = PoolLoot.get(LootName);
				if(Stk.isEmpty())
				{
					Stk.push(Fact.createLoot(LootName, x, y));
				}
		}
		}catch (Exception e)
		{
			System.err.println(e.getMessage() + "ERROR");
		}
		Loot createdLoot= PoolLoot.get(LootName).pop();
		return recycleLoot(createdLoot,x,y);
	}
	
	public Loot recycleLoot(Loot L,int x,int y){
		if(!L.Name.equals("TimeShifter"))
			L.Colour = Loot.getRandomColor();
		Random rand = new Random();
		L.x = x;
		L.y = y;
		int rate = rand.nextInt(10);
		if(rate == 0 && !L.Name.equals("TimeShifter"))
			L.ColourShifter = true;
		else
			L.ColourShifter = false;
		return L;
	}
	
	public void addLoot(Loot x)
	{
		if(!PoolLoot.containsKey(x.Name) )
		{
			Stack <Loot> temp = new  Stack<Loot>();
			temp.push(x);
			PoolLoot.put(x.Name, temp);
		}
		else
		{
			PoolLoot.get(x.Name).push(x);
		}
	}
}
