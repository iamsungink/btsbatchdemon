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
				throw new Exception("Configuration.getConfig(\"DEMON.SVR.WORK.CODE\").split(\"_\") �� �������� ���Ͽ����ϴ�");
			}
			arrDemonInfo = new DemonInfo[arrWorkCd.length];
			for(int i=0; i<arrWorkCd.length; i++){
				arrDemonInfo[i] = new DemonInfo(arrWorkCd[i]);
			}
			longTrdInterval = Integer.parseInt(Configuration.getConfig("DEMON.SVR.SLEP.INTV"))*1000;
		} catch (Exception defaultE) {
			printDemonLog("DemonSvr�� �ʱ�ȭ�ϴ� �� ������ �߻� : "+defaultE.toString());
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
					//����ð�
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
								printDemonLog("DEMON.SVR.XXX."+arrWorkCd[i]+" �� property�� ��ϵ��� �ʾҽ��ϴ�"+defaultE.toString());
							}
						}
					
					}else{
						printDemonLog("'config.properties'�� 'DEMON.SVR.WORK.CODE'�� ��ϵ��� �ʾҽ��ϴ�");
					}
					
					for(int i=0; i<arrDemonInfo.length; i++){
						if(arrDemonInfo[i].getStrUSYN().equals("Y")){
							// ���� �������� ��ȸ
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
					printDemonLog("TmsDemonSvr ���� �� ������ �߻��߽��ϴ� : " + defaultE.toString());
				}
			}
			
		}finally{
			stopDaemonSvr();
		}
		
	}
	
	public boolean isRunBatch(Calendar calCurrent, DemonInfo demonInfo){
		boolean blnRtnValue = false;
		//������ ���� �ð��� ���� �ð� ������ �ð� �ܿ��� Skip�Ѵ�.
		int intStrt = Integer.parseInt(demonInfo.getStrSTRT());
		int intCurt = Integer.parseInt(DateTime.toString(calCurrent, "HH"));
		int intEndt = Integer.parseInt(demonInfo.getStrENDT());
		
		if(intStrt <= intCurt && intCurt <= intEndt){
			// ���� Index : (����ð�[Millisecond] - ���ؽð�[Millisecond]) / (�ֱ�*1000)
			// Demon�� ����� �� �ʱ� �ε��� ����
			if(demonInfo.getStrMODE().equals("I") && demonInfo.getLongRunIdx()==-1){
				long longStndMilli = DateTime.toCalendar(demonInfo.getStrYMDT(),"yyyyMMddHHmmss").getTimeInMillis();
				demonInfo.setLongRunIdx(((calCurrent.getTimeInMillis() - longStndMilli)/(Integer.parseInt(demonInfo.getStrINTV())*1000)));
			}

			// �����ð�(����ð�-intTrdInterval)
			long longCurrentMillis		= calCurrent.getTimeInMillis();
			long longZoneMillis			= longCurrentMillis-longTrdInterval;
			// ���ؽð�(�����ð�)
			long longRunningMillis		= getMode2DateTimeString(calCurrent, demonInfo).getTimeInMillis();
			
			if(demonInfo.getStrMODE().equals("I")){
				if(longCurrentMillis > longRunningMillis){
					// exception�� ���� ��츦 ����ؼ� 1�� �������� �ʰ� �ٽ� ����
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
			// ���� Running time = ���ؽð� + ((���� Index+1) * (�ֱ�*1000)
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
