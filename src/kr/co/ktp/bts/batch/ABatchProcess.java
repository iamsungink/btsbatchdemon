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
	 * ��ġ�۾� �ʱ�ȭ
	 * @see		1. arguments�� �Ѱܹ��� ���� �̿��Ͽ� BatchWorkInfo ����.
	 */
	public abstract void initialize(String[] args);
	/**
	 * ���μ��� ���� Method
	 * @see		1. ABatchProcess�� Factoring�� Ŭ������ process() Method�� �̿��Ͽ� ������.
	 */
	public abstract void process();
	/**
	 * ��ġ�۾� ����
	 * @return		true:����, false:����
	 * @throws BatchException
	 */
	public abstract boolean executeBatch() throws Exception;
	/**
	 * ���ν����۾� ����
	 * @return		true:����, false:����
	 * @throws BatchException
	 */
	public abstract boolean executeBatchProcedure() throws Exception;

	/**
	 * ���ϸ� ����
	 * @param sourceFile	: ��������
	 * @param targetFile	: �������
	 * @throws BatchException
	 * @see		1. ������ ��ġ�� �������� �۾��� �� ���, �۾��� �ٽ� ����Ǵ� ������ ����.
	 * 			2. ���ϸ� ���ϸ��� �տ� �۾� �� ǥ�� ('0_')�� �ٿ� �ٽ� �۾��� �ȵǵ��� ó��.
	 * 			3. �۾��� ���ϸ��� �տ� �۾� �� ǥ�� ('0_')�� ���� ���� ���ϸ� �����.
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
	 * ���� ��������� �̵�
	 * @param sourceFile	: ��������
	 * @param targetFile	: �������
	 * @throws BatchException
	 * @see		1. ���� �뷮�� Ŀ�� copyFile()�� deleteFile()�� �̵��� ���� ���� ��.
	 * 			2. Shell script�� �̿��Ͽ� ���� �̵�. (backup ��)
	 * 			3. moveFile.sh ������ ���� �ۼ����� �� ���ѹ��� �߻� ��.
	 * 			4. 3���� ����ϱ� ���� chmod�� ���������� ������ ������ �ٲ��� ����. 
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
	 * FTP ���� ����
	 * @param localPath : ������ ����
	 * @param remoteFile : ������ ���ϸ�
	 * @see 1. ���� �� Ftp�� remoteFile�� �̸����� ���۵�.
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
	 * ���� ����
	 ---------------------------------------------------------------------------------------------------------*/

	/**
	 * File Write : StreamUtil�� �̿�.
	 * @param printStream
	 * @param stringBuffer
	 */
	public void printString(PrintStream printStream, StringBuffer stringBuffer){
		printBatchLog("printString(PrintStream printStream, StringBuffer stringBuffer)");
		StreamUtil.printString(printStream, stringBuffer.toString());
		StreamUtil.release(printStream);
	}
	
    /**
	 * log�� ����� (Batch�۾��� ���ؼ� �۾��� ��� ���ϸ� �տ� "BATCH_"��� ���������Ͽ� �α��ۼ�)
	 * @param strDev	: �αױ��� (START:���۷α� �ۼ�, END:����α� �ۼ�, ������:Working log �ۼ�)
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
