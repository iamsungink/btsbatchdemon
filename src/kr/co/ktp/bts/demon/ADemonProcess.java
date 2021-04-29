package kr.co.ktp.bts.demon;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Calendar;

import kr.co.ktp.bts.config.Configuration;
import kr.co.ktp.bts.config.Logger;
import kr.co.ktp.bts.util.DateTime;

public abstract class ADemonProcess implements Runnable{
	protected static final int DEMON_SUCCESS	= 0;	// 상수 : 작업성공
	protected static final int DEMON_ERROR		= 1;	// 상수 : 에러
	protected static final int DEMON_NORECORD	= 2;	// 상수 : 데이터 없음
	protected DemonInfo demonInfo 				= null;	// Demon 정보
	protected Thread threadDemon 				= null;	// Demon Thread

	/**
	 * 배치작업 실행 프로세스
	 * @param sbArgs
	 * @return
	 */
	protected int executeBatchProcess(StringBuffer sbArgs){
		int intRtnValue	= DEMON_ERROR;
		StringBuffer sbProcExec = new StringBuffer();
		try {
			sbProcExec.append(Configuration.getConfig("BATCH.RUN.SHELL"));
			sbProcExec.append(" ");
			sbProcExec.append(sbArgs);

			int intRunningPosition = 0;
			try{
				Runtime rt = Runtime.getRuntime();
				// IOException
				printDemonLog("-------------------------------------------------------------------------");
				printDemonLog("[sbProcExec] : " + sbProcExec.toString());
				printDemonLog("-------------------------------------------------------------------------");
				intRunningPosition = 1;
				Process process = rt.exec(sbProcExec.toString());
				intRunningPosition = 2;

				BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line = null;
				// IOException
				while ((line = br.readLine()) != null) {
					printDemonLog(line);
					
					if(line.indexOf("SUCCESS")>-1){
						intRtnValue	= DEMON_SUCCESS;
					}
					if(line.indexOf("[NO RECORDE]")>-1){
						intRtnValue	= DEMON_NORECORD;
					}
				}
				intRunningPosition = 3;
				// InterruptedException
				int exitVal = process.waitFor();
			}catch(Exception defaultE){
				printDemonLog("ADemonProcess.executeBatchProcess() : ["+intRunningPosition+"] " + defaultE.toString());
				intRtnValue = DEMON_ERROR;
			}
		} catch (Exception defaultE) {
			printDemonLog("ADemonProcess.executeBatchProcess() : Configuration.getConfig(\"BATCH.RUN.XXXXX\") 읽어오지 못하였습니다");
			intRtnValue = DEMON_ERROR;
		}

		return intRtnValue;
	}

	/**
	 * Demon Log Writer
	 * @param strDev
	 */
	protected void printDemonLog(String strDev){
		if(strDev.equals("START")){
			Logger.writeLog("DEMON_"+ demonInfo.getStrWorkCd(),"=========================================================================");
			Logger.writeLog("DEMON_"+ demonInfo.getStrWorkCd(),"=                          DEMON "+ demonInfo.getStrWorkCd() +" START                               =");
			Logger.writeLog("DEMON_"+ demonInfo.getStrWorkCd(),"=========================================================================");
		}else if(strDev.equals("END")){
			Logger.writeLog("DEMON_"+ demonInfo.getStrWorkCd(),"=========================================================================");
			Logger.writeLog("DEMON_"+ demonInfo.getStrWorkCd(),"=                          DEMON "+ demonInfo.getStrWorkCd() +" END                                 =");
			Logger.writeLog("DEMON_"+ demonInfo.getStrWorkCd(),"=========================================================================");
		}else{
			Logger.writeLog("DEMON_"+ demonInfo.getStrWorkCd(),strDev);
		}
	}	
    protected String getFormatDate(int month, int day){
    	
    	Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, month);
		c.add(Calendar.DATE, day);
		
		String current = DateTime.toString(c.getTime(), "yyyyMMdd");
    	
    	return current;
    }

    protected String getFormatDate(int month, int day, String fmt){
    	
    	if(fmt == null) return getFormatDate(month, day);
    	
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
	/**---------------------------------------------------------------------------------------------------------
	 * Abstract Method
	 ---------------------------------------------------------------------------------------------------------*/
	public abstract void initialize(DemonInfo demonInfo) throws Exception;
	public abstract void runDemonProcess();

}
