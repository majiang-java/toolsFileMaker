package tools;

import java.io.FileInputStream;
import java.util.Properties;

public class PropertiesUtil {
	
	public static  Properties properties = new Properties();
	static {
		try{
			FileInputStream inputStream1 = new FileInputStream("out.properties");
			properties.load(inputStream1);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
    public static Properties  getInstance(){
    	return properties;
    }
	
    public static String getProperty(String key){
    	return (String) properties.getProperty(key); 
    }
}
