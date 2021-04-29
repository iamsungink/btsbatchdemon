package kr.co.ktp.bts.batch;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.net.ftp.FTPClient;

import kr.co.ktp.bts.config.Configuration;
import kr.co.ktp.bts.config.Logger;
import kr.co.ktp.bts.dto.DemonDTO;
import kr.co.ktp.bts.service.DemonService;
import kr.co.ktp.bts.util.FileUtil;
import kr.co.ktp.bts.util.FtpUtil;
import kr.co.ktp.bts.util.StreamUtil;

public abstract class ABatchProcess {
	protected final static String BATCH_RUNNING		= "0";
	protected final static String BATCH_ERROR		= "1";
	protected final static String BATCH_SUCCESS		= "2";
	protected final static String strDlmtCrLf		= "\n";
	protected BatchWorkInfo batchWorkInfo	= null;
	
	protected DemonService service					= null;
	
	/**---------------------------------------------------------------------------------------------------------
	 * Abstract Method
	 ---------------------------------------------------------------------------------------------------------*/
	/**
	 * 배치작업 초기화
	 * @see		1. arguments로 넘겨받은 값을 이용하여 BatchWorkInfo 셋팅.
	 */
	public abstract void initialize(String[] args);
	/**
	 * 프로세스 실행 Method
	 * @see		1. ABatchProcess를 Factoring한 클레스는 process() Method를 이용하여 실행함.
	 */
	public abstract void process();
	/**
	 * 배치작업 실행
	 * @return		true:성공, false:실패
	 * @throws BatchException
	 */
	public abstract boolean executeBatch() throws Exception;
	/**
	 * 프로시져작업 실행
	 * @return		true:성공, false:실패
	 * @throws BatchException
	 */
	public abstract boolean executeBatchProcedure() throws Exception;

	/**
	 * 파일명 변경
	 * @param sourceFile	: 원본파일
	 * @param targetFile	: 대상파일
	 * @throws BatchException
	 * @see		1. 파일이 위치한 폴더에서 작업을 할 경우, 작업이 다시 실행되는 현상을 배제.
	 * 			2. 파일명 파일명의 앞에 작업 중 표시 ('0_')를 붙여 다시 작업이 안되도록 처리.
	 * 			3. 작업을 파일명의 앞에 작업 중 표시 ('0_')이 붙지 않은 파일만 실행됨.
	 */
	public void renameFile(String strRunFlag) throws Exception{
		printBatchLog("renameFile(String strRunFlag)");
		File sourceFile	= new File(batchWorkInfo.getStrFilePath()+batchWorkInfo.getStrFileNm());
		File targetFile	= new File(batchWorkInfo.getStrFilePath()+BATCH_RUNNING+ "_"+batchWorkInfo.getStrFileNm());
		try{
			if(strRunFlag.equals(BATCH_RUNNING)){
				FileUtil.renameFile(sourceFile, targetFile);
			}
			if(strRunFlag.equals(BATCH_ERROR)){
				FileUtil.renameFile(targetFile, sourceFile);
			}
		}catch(Exception fileE){
			throw new Exception("BatchWorkMain.renameFile() : FileException : " + fileE.toString());
		}
	}	

	/**
	 * 파일 백업폴더로 이동
	 * @param sourceFile	: 원본파일
	 * @param targetFile	: 대상파일
	 * @throws BatchException
	 * @see		1. 파일 용량이 커서 copyFile()과 deleteFile()로 이동이 되지 않을 때.
	 * 			2. Shell script를 이용하여 파일 이동. (backup 등)
	 * 			3. moveFile.sh 파일을 새로 작성했을 때 권한문제 발생 함.
	 * 			4. 3항을 행결하기 위해 chmod를 실행하지만 여전히 권한을 바꾸지 못함. 
	 */
	public void backupFile(File sourceFile, File targetFile) throws Exception{
		printBatchLog("backupFile("+sourceFile.toString()+", "+targetFile.toString()+")");
		try{
			String execFile = null;
			
			if(FileUtil.isWindows()){
				execFile = Configuration.getConfig("FILE.PATH.SHELL")+System.getProperty("file.separator")+"moveFile.bat";
			}else{
				execFile = Configuration.getConfig("FILE.PATH.SHELL")+"moveFile.sh";
			}
			
			StringBuffer sbExecFile = new StringBuffer();
			sbExecFile.append(execFile);
			sbExecFile.append(" ");
			sbExecFile.append(sourceFile.toString());
			sbExecFile.append(" ");
			sbExecFile.append(targetFile.toString());
			
			Runtime rt = Runtime.getRuntime();
			Process process = rt.exec(sbExecFile.toString());

			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			while ((line = br.readLine()) != null) {
				printBatchLog(line);
			}
			int exitVal = process.waitFor();
		}catch(Exception fileE){
			throw new Exception("BatchWorkMain.backupFile() : FileException : " + fileE.toString());
		}
	}
	
	/**
	 * FTP 파일 전송
	 * @param localPath : 전송할 파일
	 * @param remoteFile : 전송할 파일명
	 * @see 1. 전송 시 Ftp에 remoteFile의 이름으로 전송됨.
	 * @throws Exception
	 */
	public void sendFile(String localPath, String remoteFile) throws Exception{
		printBatchLog("sendFile("+localPath+", "+remoteFile+")");
		
		FTPClient ftp = null;
		try{
			ftp = FtpUtil.connectFTP(Configuration.getConfig("FTP.SVR.IP."+batchWorkInfo.getStrWorkCd())
									,Configuration.getConfig("FTP.SVR.PORT."+batchWorkInfo.getStrWorkCd())
									,Configuration.getConfig("FTP.SVR.USER."+batchWorkInfo.getStrWorkCd())
									,Configuration.getConfig("FTP.SVR.PASS."+batchWorkInfo.getStrWorkCd()));
			
			if(!Configuration.getConfig("FTP.SVR.WDIR."+batchWorkInfo.getStrWorkCd()).equals("")){
				ftp.changeWorkingDirectory(Configuration.getConfig("FTP.SVR.WDIR."+batchWorkInfo.getStrWorkCd()));
			}
			
			FtpUtil.putFile(ftp, remoteFile, localPath);
			
			ftp.noop();
			ftp.logout();
			
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("connectFTP() : error! : " + e.toString());
		}finally{
			if (ftp.isConnected()){
				try{
					ftp.disconnect();
				} catch (IOException f){
					//do nothing
				}
			}
		}
		
		printBatchLog("send ok! : ["+remoteFile+"]");
	}
	
	
	/**---------------------------------------------------------------------------------------------------------
	 * 파일 관련
	 ---------------------------------------------------------------------------------------------------------*/

	/**
	 * File Write : StreamUtil을 이용.
	 * @param printStream
	 * @param stringBuffer
	 */
	public void printString(PrintStream printStream, StringBuffer stringBuffer){
		printBatchLog("printString(PrintStream printStream, StringBuffer stringBuffer)");
		StreamUtil.printString(printStream, stringBuffer.toString());
		StreamUtil.release(printStream);
	}
	
    /**
	 * log를 기록함 (Batch작업을 통해서 작업된 경우 파일명 앞에 "BATCH_"라는 문구삽입하여 로그작성)
	 * @param strDev	: 로그구분 (START:시작로그 작성, END:종료로그 작성, 나머지:Working log 작성)
	 */
    protected void printBatchLog(String strDev){
    	if(strDev.equals("START")){
			Logger.writeLog("BATCH_"+ batchWorkInfo.getStrWorkCd(),"=========================================================================");
			Logger.writeLog("BATCH_"+ batchWorkInfo.getStrWorkCd(),"=                          BATCH "+ batchWorkInfo.getStrWorkCd() +" START                               =");
			Logger.writeLog("BATCH_"+ batchWorkInfo.getStrWorkCd(),"=========================================================================");
		}else if(strDev.equals("END")){
			Logger.writeLog("BATCH_"+ batchWorkInfo.getStrWorkCd(),"=========================================================================");
			Logger.writeLog("BATCH_"+ batchWorkInfo.getStrWorkCd(),"=                          BATCH "+ batchWorkInfo.getStrWorkCd() +" END                                 =");
			Logger.writeLog("BATCH_"+ batchWorkInfo.getStrWorkCd(),"=========================================================================");
		}else{
			Logger.writeLog("BATCH_"+ batchWorkInfo.getStrWorkCd(),strDev);
		}
    }
    /*
	protected void printBatchLog(String strDev, Throwable e){		
			Logger.writeLog("BATCH_"+ batchWorkInfo.getStrWorkCd(),strDev);
			Logger.writeLog("BATCH_"+ batchWorkInfo.getStrWorkCd(),e);	
	}
	*/
	
	

}
