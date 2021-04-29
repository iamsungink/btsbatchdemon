package kr.co.ktp.bts.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import kr.co.ktp.bts.config.Configuration;
import kr.co.ktp.bts.util.DateTime;
import kr.co.ktp.bts.util.FileUtil;
import kr.co.ktp.bts.util.FtpUtil;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class FtpTest {
	public static void main(String[] args){
		FtpTest test = new FtpTest();
		test.executeFtpFileMove();
		
	}
	
	private boolean executeFtpFileMove(){
		boolean blnRtnValue = false;
		
		String day = DateTime.getCurrent("yyyyMMdd");
		System.out.println("day >> " + day);
		
		
		FTPClient ftp = null;
		try{
			ftp = FtpUtil.connectFTP("192.168.130.160"
					   , "21"
					   , "arch"
					   , "nehem2_a_60");
			
			/*
			ftp.changeWorkingDirectory("temp");
			
			String aaa = DateTime.getCurrent("yyyyMMdd");
			System.out.println("aaa >> " + aaa);
			String minus = DateTime.addDays(aaa,"yyyyMMdd",-1);
			System.out.println("dminusay >> " + minus);
			
			
			
			for (FTPFile f : ftp.listFiles()) {
				System.out.println("~~~~~~~~download~~~~~~~~~~");
				System.out.println(f.getRawListing());
				System.out.println(f.toFormattedString());
				System.out.println(f.getName());
				System.out.println("~~~~~~~~download~~~~~~~~~~");
			}
			
			
            
			for (String s : ftp.listNames()) {
				System.out.println("~~~~~~~~listNames~~~~~~~~~~");
                System.out.println(s);
                System.out.println("s " +s + "(" + s.indexOf("a1"));
                
                OutputStream output;
                output = new FileOutputStream("c:/tss_batch/data/"+s);
                ftp.retrieveFile(s, output);
                output.close();
                System.out.println("~~~~~~~~listNames~~~~~~~~~~");
            }
            */
			
			System.out.println("1 pwd : " + ftp.pwd());
			
			System.out.println(ftp.cwd("/app/arch/btsbatch/remote/data"));
			
			System.out.println("2 pwd : " + ftp.pwd());
			
			
			/*
			for (FTPFile f : ftp.listFiles("/app/arch/btsbatch/remote/data")) {
				System.out.println("~~~~~~~~test~~~~~~~~~~");
				
				if(f.isDirectory()){
					System.out.println(f.getName() + " 디렉토리입니다.");
					
				}else{
					System.out.println(f.getName() + "파일을 다운로드 합니다.");
					
					OutputStream output;
					output = new FileOutputStream(Configuration.getConfig("FILE.PATH.DATA")+"/"+f.getName());
					ftp.retrieveFile(f.getName(), output);
					output.close();
				}
			}
			*/
			
			Iterator fileList;
			
			int k=0;
			String strFileNm = "";
			
			//작업 파일 경로 내의 파일 검색하여 특정 Configuration.getConfig("FTP.SVR.PREF.IA") 로 시작하는 파일을 찾아 배치를 수행 .
			for(fileList = (FileUtil.getFileList(new File("/app/arch/btsbatch/data_back/IB/"), true)).iterator();fileList.hasNext();){
				strFileNm = (String)fileList.next();
				
				InputStream input;
				input = new FileInputStream("/app/arch/btsbatch/data_back/IB/"+strFileNm);
				ftp.storeFile(strFileNm, input);
				input.close();
				
				k++;
				
			}
			
			
			
			
			
			ftp.noop();
			ftp.logout();
			
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if (ftp.isConnected()){
				try{
					ftp.disconnect();
				} catch (IOException f){
					//do nothing
				}
			}
		}
		
		return blnRtnValue;
	}

}
