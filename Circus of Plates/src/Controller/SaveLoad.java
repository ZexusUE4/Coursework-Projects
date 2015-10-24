package Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SaveLoad {

	GameState CurrentState = null ;
	
	public void save (File file)
	{
		try {
			ObjectOutputStream os = new 
					ObjectOutputStream(new FileOutputStream(file));
			CurrentState = GameState.getInstance();
			os.writeObject(CurrentState);
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public GameState copy()
	{
		String url = System.getProperty("user.dir");
		File filetemp = new File(url + "\\copy.bin");
		try {
			filetemp.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(!filetemp.exists())
			filetemp.mkdir();
		save(filetemp);
		
		return load(filetemp);
	}
	public GameState load (File file)
	{
		ObjectInputStream is = null;
		LootFactory F = LootFactory.getInstance();
		F.createLoot("Oval", 0, 0);
		F.createLoot("Rectangle", 0, 0);
		F.createLoot("TimeShifter", 0, 0);
		F.createLoot("Plate", 0, 0);
		try {
			is = new 
					ObjectInputStream(new FileInputStream(file));
			try {
				CurrentState = (GameState) is.readObject();
			} catch (ClassNotFoundException e) {
				is.close();
				e.printStackTrace();
			}
		}catch( Exception e){
			e.printStackTrace();
		}
		return CurrentState;
	}
	
}
