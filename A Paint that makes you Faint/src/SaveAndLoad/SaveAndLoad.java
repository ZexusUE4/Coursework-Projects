package SaveAndLoad;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import com.thoughtworks.xstream.XStream;
public class SaveAndLoad {
	
	 XStream x = new XStream();
	 public SaveAndLoad(){}
	 
	 public void save( Object v,String s )
	 {
		 String xml=new String();
		  xml=x.toXML(v);;
			 FileOutputStream f;	
			 
				try
				{
					if(!s.contains("xml"))
	                s+=".xml";
					f = new FileOutputStream (s);			    
				    new PrintStream(f).println (xml);
				    f.close();	
				}
				catch (IOException e)
				{
					System.err.println ("error");
				}
	 }
	public Object load(String s)
	 {
		String fileName = s;
		File file = new File(fileName);
		Object xml = x.fromXML(file);
		return xml;
	}

}
 


