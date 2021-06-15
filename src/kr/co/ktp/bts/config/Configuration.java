package kr.co.ktp.bts.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import kr.co.ktp.bts.util.FileUtil;

public class Configuration {
	private static final String propertiesFile = "config";
	
	public static String getConfig(String configId) throws Exception {
		/*
		ResourceBundle rb = null;
		try {
			Properties props = System.getProperties();
			String osName = System.getProperty("os.name");
			if(osName.toLowerCase().startsWith("window")){
				props = new Properties();
				FileInputStream in = new FileInputStream("v:/btsbatch/conf/" + propertiesFile + ".properties");
				props.load(in);
				in.close();
				return props.getProperty(configId);
			}else{
				rb = ResourceBundle.getBundle(propertiesFile);
				return rb.getString(configId);
			}
		}catch(MissingResourceException e) {
			e.printStackTrace(System.err);
			throw new Exception("Can't read the properties file" + e.getMessage());
		}
		*/
        String sysProp = System.getProperty("gubun");
        System.out.println("서버 구분 (Config) : " + sysProp);
        String resource = "";
        String preResource = "dev".equals(sysProp) ? "config_dev.properties" : "config.properties";
        
        resource = "config"+System.getProperty("file.separator")+preResource;
        System.out.println("resource : " + resource);
        
		Properties props = new Properties();
		try {
			FileInputStream in = new FileInputStream(resource);
			props.load(in);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return props.getProperty(configId);
		
	}

}
