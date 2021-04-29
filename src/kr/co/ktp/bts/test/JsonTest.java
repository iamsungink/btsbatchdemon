package kr.co.ktp.bts.test;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import kr.co.ktp.bts.config.Configuration;

public class JsonTest {
	
	public static void main(String[] args){
		
		String jsonData = "";
		try {
			jsonData = Configuration.getConfig("DEMON.SVR.JSON.IA");
			System.out.println("jsonData >> " + jsonData);
			
			JSONParser jsonParse = new JSONParser();
			JSONObject jsonObj = (JSONObject)jsonParse.parse(jsonData);
			System.out.println(jsonObj.get("filenmprefix"));
			
			String temp1 = (String) jsonObj.get("filenmprefix");
			System.out.println("temp >> " + temp1);

			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

}
