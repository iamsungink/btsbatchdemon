package kr.co.ktp.bts.test;

import java.io.File;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Iterator;

import kr.co.ktp.bts.config.Configuration;
import kr.co.ktp.bts.util.DateTime;
import kr.co.ktp.bts.util.FileUtil;

public class TestSI {
	public static void main(String[] args){
		String fileDt = DateTime.getCurrent("yyyyMMddHHmm");
		System.out.println("fileDt >> " + fileDt);
		
		String minute = DateTime.addMinute(fileDt, "yyyyMMddHHmm", -30);
		System.out.println("minute >> " + minute);
		
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
		//c.add(Calendar.DATE, day);
		
		TestSI test = new TestSI();
		
		String current = test.getFormatDate(-1 , 0);
		System.out.println("current >> " + current);
		
		String current2 = DateTime.toString(c.getTime(), "yyyyMM");
		System.out.println("current2 >> " + current2);
		
		String strFileNmPrefix = DateTime.toString(c.getTime(), "yyyyMM")+"AAA";
		System.out.println(strFileNmPrefix);
		
		
		//----------------
		
		
		String startDt =  test.getFormatDate(-1, 0, "yyyyMM");
		System.out.println("startDt >> " + startDt);
		
		String os = System.getProperty("user.name").toLowerCase();
		System.out.println("os is ? " + os);
		
		try {
			java.net.InetAddress localMachine = java.net.InetAddress.getLocalHost();
			System.out.println("localMachine is ? " + localMachine);
			String hostname = localMachine.getHostName();
			System.out.println("hostname is ? " + hostname);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String lastday = test.getFormatDate(-1, 0, true);
		System.out.println("lastday >> " + lastday);
		
		
		Iterator fileList;
		
		try {
			int k=0;
			String strFileNm = "";
			//작업 파일 경로 내의 파일 검색하여 특정 Configuration.getConfig("FTP.SVR.PREF.IA") 로 시작하는 파일을 찾아 배치를 수행 .
			for(fileList = (FileUtil.getFileList(new File("v:\\btsbatch\\data\\"), true)).iterator();fileList.hasNext();){
				strFileNm = (String)fileList.next();
				System.out.println("strFileNm ? " + strFileNm);
				
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		String fileName = "201910_J732_20191109A021357_W001_001_00000.xml.zip";
		
		strFileNmPrefix = "201910_J732";
		String strFileExtention = "zip";
		
		
		System.out.println("Remote FileNm" + " : "+fileName +"("+strFileNmPrefix+":"+fileName.startsWith(strFileNmPrefix) + ", "+strFileExtention+":"+fileName.endsWith(strFileExtention)  +")");

	}
	
    protected String getFormatDate(int month, int day){
    	
    	Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, month);
		c.add(Calendar.DATE, day);
		
		String current = DateTime.toString(c.getTime(), "yyyyMMdd");
    	
    	return current;
    }
	
    protected String getFormatDate(int month, int day, String fmt){
    	
    	Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, month);
		c.add(Calendar.DATE, day);
		
		String current = DateTime.toString(c.getTime(), fmt);
    	
    	return current;
    }
    
    protected String getFormatDate(int month, int day, boolean isLastDD){
    	
    	Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, month);
		c.add(Calendar.DATE, day);
		
		String current = DateTime.toString(c.getTime(), "yyyyMMdd");
		if(isLastDD) {
			c.getActualMaximum(Calendar.DAY_OF_MONTH);
			current = DateTime.toString(c.getTime(), "yyyyMM") + c.getActualMaximum(Calendar.DAY_OF_MONTH);
		}else{
			current = DateTime.toString(c.getTime(), "yyyyMMdd");
		}
		return current;
    } 

}
