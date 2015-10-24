package Controller;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import Model.Loots.Loot;



public class LootFactory {

	private HashMap <String , Class<?> > LFactory ;
	private String Url = System.getProperty("user.dir") +"\\Dynamic Classes";
	private static LootFactory instance = null;
	int NumberOfCalls = 0;
	private LootFactory() {
		LFactory = new HashMap<String , Class<?> >();
	}
	
	public static LootFactory getInstance()
	{
		if(instance == null)
		{
			instance = new LootFactory();
		}
		return instance;
	}
	
	public Loot createLoot (String LootName , Integer x , Integer y){
		try{
			Class<?> lClass ;
			if(!LFactory.containsKey(LootName))
			{
				ClassLoader parent = MyClassLoader.class.getClassLoader();
				MyClassLoader classLoader = new MyClassLoader(parent);
				String PackageName = "Model.Loots." + LootName;
				lClass = classLoader.loadClass(PackageName,Url + "\\" 
											+ LootName + ".class");
				LFactory.put(LootName, lClass);
			}
			
			lClass = LFactory.get(LootName); //use the loaded class
			
			Constructor<?> c = lClass.getDeclaredConstructor(Integer.class , Integer.class );
			Object Rlo =  c.newInstance (x,y);
			return (Loot) Rlo; 
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
