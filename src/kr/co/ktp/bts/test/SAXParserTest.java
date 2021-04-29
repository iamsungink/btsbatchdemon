package kr.co.ktp.bts.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import kr.co.ktp.bts.dto.BillInfoDTO;
import kr.co.ktp.bts.xml.BillInfoHandler;
import kr.co.ktp.bts.xml.XMLParserHandler;

import org.xml.sax.SAXException;


public class SAXParserTest {
	
	public static void main(String[] args){
		
		
		String strFilePath = "v:\\btsbatch\\data\\201909_J730_20191009A020862_W003_001_00001.xml";
		//String strFilePath = "v:\\btsbatch\\data\\testxml.xml";
		
		File file = new File(strFilePath);
		
		
		try {
			SAXParserFactory spf= SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			
			BillInfoHandler handler = new BillInfoHandler();
			sp.parse(file, handler);
			
			List<Map<String, Object>> list = handler.getBillInfoList();
			for(Map<String, Object> map : list ){
				System.out.println(map.toString());
			}
			
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
