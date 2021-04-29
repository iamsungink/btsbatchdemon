package kr.co.ktp.bts.demon;

import java.util.Calendar;

import kr.co.ktp.bts.config.Configuration;
import kr.co.ktp.bts.config.Logger;
import kr.co.ktp.bts.util.DateTime;

public class DemonSvr implements Runnable{
	
	private Thread trdRunner				= null;
	private boolean blnStopFlag				= false;
	private long longTrdInterval			= 0L;
	private String[] arrWorkCd				= null;
	private DemonInfo[] arrDemonInfo		= null;
	
	public DemonSvr(){
		printDemonLog("START");
		try {
			arrWorkCd = Configuration.getConfig("DEMON.SVR.WORK.CODE").split("_");
			if(arrWorkCd==null || arrWorkCd.length<1){
				throw new Exception("Configuration.getConfig(\"DEMON.SVR.WORK.CODE\").split(\"_\") 를 실행하지 못하였습니다");
			}
			arrDemonInfo = new DemonInfo[arrWorkCd.length];
			for(int i=0; i<arrWorkCd.length; i++){
				arrDemonInfo[i] = new DemonInfo(arrWorkCd[i]);
			}
			longTrdInterval = Integer.parseInt(Configuration.getConfig("DEMON.SVR.SLEP.INTV"))*1000;
		} catch (Exception defaultE) {
			printDemonLog("DemonSvr을 초기화하던 중 오류가 발생 : "+defaultE.toString());
		}
	}
	
	private void runDemonSvr(){
		trdRunner = new Thread(this);
		trdRunner.start();
	}
	
	public void stopDaemonSvr(){
		printDemonLog("END");
		blnStopFlag = true;
		try {
			trdRunner.interrupt();
		} catch (Exception e){
			printDemonLog("stopDaemonSvr() : trdRunner.interrupt() :" + e.toString());
		}
	}
	
	public void run(){
		try{
			while(!blnStopFlag){
				try{
					//현재시간
					Calendar calCurrentDate = DateTime.toCalendar(DateTime.getCurrent("yyyyMMddHHmmss"), "yyyyMMddHHmmss");
					
					if(arrWorkCd.length>0){
						for(int i=0; i<arrDemonInfo.length; i++){
							arrDemonInfo[i].clear();
							try {
								arrDemonInfo[i].setStrYMDT(Configuration.getConfig("DEMON.SVR.YMDT."+arrDemonInfo[i].getStrWorkCd()));
								arrDemonInfo[i].setStrUSYN(Configuration.getConfig("DEMON.SVR.USYN."+arrDemonInfo[i].getStrWorkCd()));
								arrDemonInfo[i].setStrMODE(Configuration.getConfig("DEMON.SVR.MODE."+arrDemonInfo[i].getStrWorkCd()));
								arrDemonInfo[i].setStrINTV(Configuration.getConfig("DEMON.SVR.INTV."+arrDemonInfo[i].getStrWorkCd()));
								arrDemonInfo[i].setStrSTRT(Configuration.getConfig("DEMON.SVR.STRT."+arrDemonInfo[i].getStrWorkCd()));
								arrDemonInfo[i].setStrENDT(Configuration.getConfig("DEMON.SVR.ENDT."+arrDemonInfo[i].getStrWorkCd()));
								
							}catch (Exception defaultE) {
								printDemonLog("arrDemonInfo["+i+"] : " + arrDemonInfo[i].toString());
								printDemonLog("DEMON.SVR.XXX."+arrWorkCd[i]+" 의 property가 등록되지 않았습니다"+defaultE.toString());
							}
						}
					
					}else{
						printDemonLog("'config.properties'에 'DEMON.SVR.WORK.CODE'가 등록되지 않았습니다");
					}
					
					for(int i=0; i<arrDemonInfo.length; i++){
						if(arrDemonInfo[i].getStrUSYN().equals("Y")){
							// 실행 가능한지 조회
							if(isRunBatch(calCurrentDate, arrDemonInfo[i])){
								ADemonProcess demonProcess;
								try {
									demonProcess = DemonProcessFactory.createDemonProcess(arrDemonInfo[i].getStrWorkCd());
									demonProcess.initialize(arrDemonInfo[i]);
									demonProcess.runDemonProcess();
									printDemonLog(arrDemonInfo[i].getStrWorkCd() + " RUNNING TIME [" + DateTime.getCurrent("yyyy-MM-dd HH:mm:ss") + "]" + " [" + arrDemonInfo[i].toString() + "]");
									
								}catch (ClassNotFoundException cnfE) {
									printDemonLog(arrDemonInfo[i].getStrWorkCd() + " RUNNING ERROR [" + DateTime.getCurrent("yyyy-MM-dd HH:mm:ss") + "]" + " [" + arrDemonInfo[i].toString() + "]" + cnfE.toString());
								} catch (IllegalAccessException iaE) {
									printDemonLog(arrDemonInfo[i].getStrWorkCd() + " RUNNING ERROR [" + DateTime.getCurrent("yyyy-MM-dd HH:mm:ss") + "]" + " [" + arrDemonInfo[i].toString() + "]" + iaE.toString());
								} catch (InstantiationException iE) {
									printDemonLog(arrDemonInfo[i].getStrWorkCd() + " RUNNING ERROR [" + DateTime.getCurrent("yyyy-MM-dd HH:mm:ss") + "]" + " [" + arrDemonInfo[i].toString() + "]" + iE.toString());
								} catch ( Exception demonE){
									printDemonLog(arrDemonInfo[i].getStrWorkCd() + " RUNNING ERROR [" + DateTime.getCurrent("yyyy-MM-dd HH:mm:ss") + "]" + " [" + arrDemonInfo[i].toString() + "]" + demonE.toString());
								}
							}
						}
					}
					
					// Thead sleep
					try {
						Thread.sleep(longTrdInterval);
					} catch (InterruptedException iE) {
						printDemonLog(" TspDemonSvr.run() => Thread.sleep(longTrdInterval) : " + iE.toString());
					}
					
				}catch(Exception defaultE){
					defaultE.printStackTrace();//xxxxx
					printDemonLog("TmsDemonSvr 실행 중 오류가 발생했습니다 : " + defaultE.toString());
				}
			}
			
		}finally{
			stopDaemonSvr();
		}
		
	}
	
	public boolean isRunBatch(Calendar calCurrent, DemonInfo demonInfo){
		boolean blnRtnValue = false;
		//설정된 시작 시간과 종료 시간 사이의 시간 외에는 Skip한다.
		int intStrt = Integer.parseInt(demonInfo.getStrSTRT());
		int intCurt = Integer.parseInt(DateTime.toString(calCurrent, "HH"));
		int intEndt = Integer.parseInt(demonInfo.getStrENDT());
		
		if(intStrt <= intCurt && intCurt <= intEndt){
			// 현재 Index : (현재시간[Millisecond] - 기준시간[Millisecond]) / (주기*1000)
			// Demon이 실행될 때 초기 인덱스 셋팅
			if(demonInfo.getStrMODE().equals("I") && demonInfo.getLongRunIdx()==-1){
				long longStndMilli = DateTime.toCalendar(demonInfo.getStrYMDT(),"yyyyMMddHHmmss").getTimeInMillis();
				demonInfo.setLongRunIdx(((calCurrent.getTimeInMillis() - longStndMilli)/(Integer.parseInt(demonInfo.getStrINTV())*1000)));
			}

			// 범위시간(현재시간-intTrdInterval)
			long longCurrentMillis		= calCurrent.getTimeInMillis();
			long longZoneMillis			= longCurrentMillis-longTrdInterval;
			// 기준시간(설정시간)
			long longRunningMillis		= getMode2DateTimeString(calCurrent, demonInfo).getTimeInMillis();
			
			if(demonInfo.getStrMODE().equals("I")){
				if(longCurrentMillis > longRunningMillis){
					// exception이 있을 경우를 대비해서 1씩 증가하지 않고 다시 설정
					long longStndMilli = DateTime.toCalendar(demonInfo.getStrYMDT(),"yyyyMMddHHmmss").getTimeInMillis();
					demonInfo.setLongRunIdx(((calCurrent.getTimeInMillis() - longStndMilli)/(Integer.parseInt(demonInfo.getStrINTV())*1000)));

					blnRtnValue = true;
				}
			}else{
				if(longZoneMillis<longRunningMillis && longRunningMillis<=longCurrentMillis){
					blnRtnValue = true;
				}
			}
		}
		
		return blnRtnValue;
	}
	
	public Calendar getMode2DateTimeString(Calendar calCurrent, DemonInfo demonInfo){
		StringBuffer sbDateTimeString = new StringBuffer();
		if(demonInfo.getStrMODE().equals("Y")){
			sbDateTimeString.append(DateTime.toString(calCurrent.getTime(), "yyyy"));
			sbDateTimeString.append(demonInfo.getStrYMDT().substring(4));
		}
		if(demonInfo.getStrMODE().equals("M")){
			sbDateTimeString.append(DateTime.toString(calCurrent.getTime(), "yyyyMM"));
			sbDateTimeString.append(demonInfo.getStrYMDT().substring(6));
		}
		if(demonInfo.getStrMODE().equals("D")){
			sbDateTimeString.append(DateTime.toString(calCurrent.getTime(), "yyyyMMdd"));
			sbDateTimeString.append(demonInfo.getStrYMDT().substring(8));
		}
		if(demonInfo.getStrMODE().equals("H")){
			sbDateTimeString.append(DateTime.toString(calCurrent.getTime(), "yyyyMMddHH"));
			sbDateTimeString.append(demonInfo.getStrYMDT().substring(10));
		}
		if(demonInfo.getStrMODE().equals("m")){
			sbDateTimeString.append(DateTime.toString(calCurrent.getTime(), "yyyyMMddHHmm"));
			sbDateTimeString.append(demonInfo.getStrYMDT().substring(12));
		}
		if(demonInfo.getStrMODE().equals("I")){
			// 다음 Running time = 기준시간 + ((현재 Index+1) * (주기*1000)
			Calendar tmpCalendar = Calendar.getInstance();
			tmpCalendar.setTimeInMillis(DateTime.toCalendar(demonInfo.getStrYMDT(),"yyyyMMddHHmmss").getTimeInMillis() + ((demonInfo.getLongRunIdx()+1) * (Integer.parseInt(demonInfo.getStrINTV())*1000)));
			sbDateTimeString.append(DateTime.toString(tmpCalendar, "yyyyMMddHHmmss"));
		}
//		printDemonLog("DEMON","sbDateTimeString : ["+sbDateTimeString.toString()+"]");

		return DateTime.toCalendar(sbDateTimeString.toString(), "yyyyMMddHHmmss");
	}
	
	public void printDemonLog(String strDev){
		if(strDev.equals("START")){
			Logger.writeLog("DEMON","=========================================================================");
			Logger.writeLog("DEMON","=                          DEMON SERVER START                           =");
			Logger.writeLog("DEMON","=========================================================================");
		}else if(strDev.equals("END")){
			Logger.writeLog("DEMON","=========================================================================");
			Logger.writeLog("DEMON","=                          DEMON SERVER END                             =");
			Logger.writeLog("DEMON","=========================================================================");
		}else{
			Logger.writeLog("DEMON", strDev);
		}
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args){
		DemonSvr demonSvr = new DemonSvr();
		demonSvr.runDemonSvr();
	}
	

}
