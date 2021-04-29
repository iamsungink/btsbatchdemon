package kr.co.ktp.bts.demon;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import kr.co.ktp.bts.config.Configuration;
import kr.co.ktp.bts.service.DemonService;
import kr.co.ktp.bts.util.DateTime;
import kr.co.ktp.bts.util.FileUtil;
import kr.co.ktp.bts.util.FtpUtil;

public class DemonProcessIA extends ADemonProcess{
	
	private String PROCESS_NAME = "[KOS청구] ";
	
	private String inv_yyyymm = null;
	private String inv_flag = null;
	
	private String strFilePath;
	private String strFileNmPrefix;
	private String strFileExtention = "";
	
	@Override
	public void initialize(DemonInfo demonInfo) throws Exception {
		this.demonInfo = demonInfo;
		
		inv_yyyymm = getFormatDate(0 , 0, "yyyyMM");
		inv_flag = "1";
		
		try {
			strFilePath = Configuration.getConfig("FILE.PATH.DATA");
			
			JSONParser jsonParse = new JSONParser();
			JSONObject jsonObj = (JSONObject)jsonParse.parse(Configuration.getConfig("DEMON.SVR.JSON."+demonInfo.getStrWorkCd()));
			
			strFileNmPrefix = getFormatDate(-1 , 0, "yyyyMM") + "_" + jsonObj.get("filenmprefix");
			strFileExtention = (String) jsonObj.get("fileExtention");
			
		}catch (Exception e) {
			throw new Exception(this.getClass().getName() + " 초기화 Error : Configuration.getConfig() : " + e.toString());
		}
		
	}

	@Override
	public void runDemonProcess() {
		threadDemon = new Thread(this);
		threadDemon.start();
	}
	
	/**
	//=============================================================================
	// KOS청구
	//------------------------------------------------------------------------------
    //  IDX | FIELD             | VALUE
	//------------------------------------------------------------------------------
	//  [0] | strModuleID       | BAT00000_IA
	//  [1] | strWorkCd         | IA
	//  [2] | strWorkDt         | DateTime.getCurrent("yyyyMMdd")
	//  [3] | strWorkTm         | DateTime.getCurrent("HHmmss")
	//  [4] | strBrnCd          | 00000
	//  [5] | strUserCd         | DEMON
	//  [6] | strInvYyyyMm      | 
	//  [7] | strInvFlag        | 
	//  [8] | strSrchStrtDt     | YYYYMM01000000
	//  [9] | strSrchEndtDt     | YYYYMMDD235959
	//  [10]| strFilePath       | /app/arch/btsbatch/data/
	//  [11]| strFileNm         | 201908_J732_20190909A019632_W001_001_00000.xml.zip
	//  [12]| strFilePathBack   | /app/arch/btsbatch/data_back/IA
	//------------------------------------------------------------------------------
	//=============================================================================
	*/	
	@Override
	public void run() {
		printDemonLog("START");
		
		if(!executeFtpFileMove()){
			
			StringBuffer sbArgs = new StringBuffer();
			
			Iterator fileList;
			
			try {
				int k=0;
				String strFileNm = "";
				//작업 파일 경로 내의 파일 검색하여 특정 Configuration.getConfig("FTP.SVR.PREF.IA") 로 시작하는 파일을 찾아 배치를 수행 .
				for(fileList = (FileUtil.getFileList(new File(strFilePath), true)).iterator();fileList.hasNext();){
					strFileNm = (String)fileList.next();
					printDemonLog("Local FileNm" + " : "+strFileNm +"("+strFileNmPrefix+":"+strFileNm.startsWith(strFileNmPrefix) + ", "+strFileExtention+":"+strFileNm.endsWith(strFileExtention)  +")");
					if(strFileNm.startsWith(strFileNmPrefix) && strFileNm.endsWith(strFileExtention)){
						sbArgs.delete(0, sbArgs.length());
						sbArgs.append("BAT00000_"+demonInfo.getStrWorkCd());
						sbArgs.append(" ");
						sbArgs.append(demonInfo.getStrWorkCd());
						sbArgs.append(" ");
						sbArgs.append(DateTime.getCurrent("yyyyMMdd"));
						sbArgs.append(" ");
						sbArgs.append(DateTime.getCurrent("HHmmss"));
						sbArgs.append(" ");
						sbArgs.append("00000");
						sbArgs.append(" ");
						sbArgs.append("DEMON");
						sbArgs.append(" ");
						sbArgs.append(inv_yyyymm);
						sbArgs.append(" ");
						sbArgs.append(inv_flag);
						sbArgs.append(" ");
						sbArgs.append("YYYYMM01000000");
						sbArgs.append(" ");
						sbArgs.append("YYYYMMDD235959");
						sbArgs.append(" ");
						sbArgs.append(strFilePath);
						sbArgs.append(" ");
						sbArgs.append(strFileNm);
						sbArgs.append(" ");
						sbArgs.append(strFilePath.replaceAll("/data", "/data_back/IA"));
						
						int intRtnValue = executeBatchProcess(sbArgs);
						if(intRtnValue==DEMON_SUCCESS){
							printDemonLog(PROCESS_NAME+"RUNNING RESULT [DEMON_SUCCESS] ["+ demonInfo.toString() + "]");
						}
						if(intRtnValue==DEMON_NORECORD){
							printDemonLog(PROCESS_NAME+"RUNNING RESULT [DEMON_NORECORD] ["+ demonInfo.toString() + "]");
						}
						if(intRtnValue==DEMON_ERROR){
							printDemonLog(PROCESS_NAME+"RUNNING RESULT [DEMON_ERROR] ["+ demonInfo.toString() + "]");
						}
						
						k++;
					}
				}
				
			}catch (Exception e) {
				e.printStackTrace();
			}
			
		}else{
			printDemonLog("Ftp Server에서 NCUST Server로 파일을 이동하던 중 오류가 발생했습니다");
		}
		
		printDemonLog("END");
		
	}
	
	private boolean executeFtpFileMove(){
		boolean blnRtnValue = false;
		
		String strRemotePath = "";
		String strRemoteBackupPath = "";
		String fileName = null;
		
		try {
			strRemotePath = Configuration.getConfig("FTP.SVR.WDIR."+demonInfo.getStrWorkCd());
			strRemoteBackupPath =  Configuration.getConfig("FTP.SVR.BDIR."+demonInfo.getStrWorkCd());
		}catch (Exception e1) {
			e1.printStackTrace();
		}
		
		FTPClient ftp = null;
		try{
			ftp = FtpUtil.connectFTP(Configuration.getConfig("FTP.SVR.IP."+demonInfo.getStrWorkCd())
									,Configuration.getConfig("FTP.SVR.PORT."+demonInfo.getStrWorkCd())
									,Configuration.getConfig("FTP.SVR.USER."+demonInfo.getStrWorkCd())
									,Configuration.getConfig("FTP.SVR.PASS."+demonInfo.getStrWorkCd()));
			
			
			ftp.cwd(strRemotePath);
			
			for (FTPFile f : ftp.listFiles()) {
				if(!f.isDirectory()){
					fileName = f.getName();
					printDemonLog("Remote FileNm" + " : "+fileName +"("+strFileNmPrefix+":"+fileName.startsWith(strFileNmPrefix)  + ", "+strFileExtention+":"+fileName.endsWith(strFileExtention)  +")");
					
					if(fileName.startsWith(strFileNmPrefix) && fileName.endsWith(strFileExtention)){
						System.out.println(f.getName() + " 파일을 다운로드 합니다.");
						
						OutputStream output;
						output = new FileOutputStream(Configuration.getConfig("FILE.PATH.DATA")+"/"+fileName);
						ftp.setFileType(FTP.BINARY_FILE_TYPE);
						ftp.retrieveFile(fileName, output);
						output.close();
					}
				}else{
					System.out.println(f.getName() + " 디렉토리입니다.");
				}
			}
			
			ftp.noop();
			ftp.logout();
			
		}catch(Exception e){
			e.printStackTrace();
			blnRtnValue = true;
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
	
	public static void main(String[] args)throws Exception{
		
		DemonInfo arrDemonInfo = new DemonInfo("IA");
		arrDemonInfo.setStrYMDT(Configuration.getConfig("DEMON.SVR.YMDT."+arrDemonInfo.getStrWorkCd()));
		arrDemonInfo.setStrUSYN(Configuration.getConfig("DEMON.SVR.USYN."+arrDemonInfo.getStrWorkCd()));
		arrDemonInfo.setStrMODE(Configuration.getConfig("DEMON.SVR.MODE."+arrDemonInfo.getStrWorkCd()));
		arrDemonInfo.setStrINTV(Configuration.getConfig("DEMON.SVR.INTV."+arrDemonInfo.getStrWorkCd()));
		arrDemonInfo.setStrSTRT(Configuration.getConfig("DEMON.SVR.STRT."+arrDemonInfo.getStrWorkCd()));
		arrDemonInfo.setStrENDT(Configuration.getConfig("DEMON.SVR.ENDT."+arrDemonInfo.getStrWorkCd()));
		arrDemonInfo.setStrPARAM(Configuration.getConfig("DEMON.SVR.PARAM."+arrDemonInfo.getStrWorkCd()));
	    
		ADemonProcess demonProcess = DemonProcessFactory.createDemonProcess(arrDemonInfo.getStrWorkCd());
		demonProcess.initialize(arrDemonInfo);
		demonProcess.runDemonProcess();
	}

}
