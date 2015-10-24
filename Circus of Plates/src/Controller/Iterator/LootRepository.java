package Controller.Iterator;

import java.util.ArrayList;

import Model.Loots.Loot;


public class LootRepository implements Container{

	private ArrayList<Loot> Repository;
	
	public LootRepository(ArrayList<Loot> loots){
		Repository = loots;
	}
	@Override
	public Iterator getIterator() {
		return new LootIterator();
	}
	
	private class LootIterator implements Iterator{
		
		int index = 0;
		
		public LootIterator(){
			index = -1;
		}
		@Override
		public boolean hasNext() {
			if(index >= Repository.size() - 1)
				return false;
			return true;
		}

		@Override
		public Object next() {
			if(this.hasNext()){
				return Repository.get(++index);
			}
			return null;
		}
		
	}

}
