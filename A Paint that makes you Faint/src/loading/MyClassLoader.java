package loading;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MyClassLoader extends ClassLoader{
 
	public  int flag;
	public String s ;	
    public MyClassLoader(ClassLoader parent) {
        super(parent);
        flag=0;
        s=new String();
    }

    @SuppressWarnings("rawtypes")
	public Class loadClass(String name,String Url) throws ClassNotFoundException {
    	if(flag==0)
    	{
    		s=name;
    		flag=1;
    	}
        if(!s.equals(name))
        {
        	return super.loadClass(name);       
        }
        try {
        	File f = new File(Url);
            String url = f.toURI().toString();
            URL myUrl = new URL(url);
            URLConnection connection = myUrl.openConnection();
            InputStream input = connection.getInputStream();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int data = input.read();
            while(data != -1){
                buffer.write(data);
                data = input.read();
            }
            input.close();
            byte[] classData = buffer.toByteArray();
            return defineClass(s,classData,0, classData.length);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
