package kr.co.ktp.bts.demon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.ktp.bts.config.Configuration;
import kr.co.ktp.bts.service.DemonService;
import kr.co.ktp.bts.util.DateTime;

public class DemonProcessTB extends ADemonProcess{
	
	private String PROCESS_NAME = "[중점고객 ARPU 및 사용량 추이] ";
	
	private String[][] arrProcedure = null;
	
	private String inv_yyyymm = null;
	private String inv_flag = null;
	
	private DemonService service = new DemonService();
	
	@Override
	public void initialize(DemonInfo demonInfo) throws Exception {
		this.demonInfo = demonInfo;
		
		inv_yyyymm = getFormatDate(0 , 0, "yyyyMM");
		inv_flag = "1";
		
		arrProcedure = new String[2][];
		try {
			String trid = "";
			String proc = "";
			try {
				trid = Configuration.getConfig("DEMON.SVR.TRID."+demonInfo.getStrWorkCd());
				proc = Configuration.getConfig("DEMON.SVR.PROC."+demonInfo.getStrWorkCd());
			} catch (Exception e) {
				throw new Exception(this.getClass().getName() + " 초기화 Error : Configuration.getConfig() : " + e.toString());
			}
			arrProcedure[0] = trid.split("/");
			arrProcedure[1]	= proc.split("/");
			if(arrProcedure[0].length != arrProcedure[1].length){
				throw new Exception("Configuration의 DEMON.SVR.TRID.TA 와 DEMON.SVR.PROC.TA 의 수는 같아야 합니다");
			}
		} catch (Exception e) {
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
	// [중점고객 ARPU 및 사용량 추이]
	//------------------------------------------------------------------------------
    //  IDX | FIELD             | VALUE
	//------------------------------------------------------------------------------
	//  [0] | strModuleID       | BTSSTICKCARE_ADD
	//  [1] | strWorkCd         | TB
	//  [2] | strWorkDt         | DateTime.getCurrent("yyyyMMdd")
	//  [3] | strWorkTm         | DateTime.getCurrent("HHmmss")
	//  [4] | strBrnCd          | 00000
	//  [5] | strUserCd         | DEMON
	//  [6] | strInvYyyyMm      |
	//  [7] | strInvFlag        | 
	//  [8] | strSrchStrtDt     | 
	//  [9] | strSrchEndtDt     |
	//  [10]| strRunProcedure   | SP_BTSSTICKCARE_ADD
	//------------------------------------------------------------------------------
	//=============================================================================
	*/	
	@Override
	public void run() {
		printDemonLog("START");
		
		StringBuffer sbArgs = new StringBuffer();
		sbArgs.delete(0, sbArgs.length());
		sbArgs.append(arrProcedure[0][0]);
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
		sbArgs.append(arrProcedure[1][0]);
		
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
		
		printDemonLog("END");
		
	}
	
	public static void main(String[] args)throws Exception{
		
		DemonInfo arrDemonInfo = new DemonInfo("TB");
		arrDemonInfo.setStrYMDT(Configuration.getConfig("DEMON.SVR.YMDT."+arrDemonInfo.getStrWorkCd()));
		arrDemonInfo.setStrUSYN(Configuration.getConfig("DEMON.SVR.USYN."+arrDemonInfo.getStrWorkCd()));
		arrDemonInfo.setStrMODE(Configuration.getConfig("DEMON.SVR.MODE."+arrDemonInfo.getStrWorkCd()));
		arrDemonInfo.setStrINTV(Configuration.getConfig("DEMON.SVR.INTV."+arrDemonInfo.getStrWorkCd()));
		arrDemonInfo.setStrSTRT(Configuration.getConfig("DEMON.SVR.STRT."+arrDemonInfo.getStrWorkCd()));
		arrDemonInfo.setStrENDT(Configuration.getConfig("DEMON.SVR.ENDT."+arrDemonInfo.getStrWorkCd()));
	    
		ADemonProcess demonProcess = DemonProcessFactory.createDemonProcess(arrDemonInfo.getStrWorkCd());
		demonProcess.initialize(arrDemonInfo);
		demonProcess.runDemonProcess();
	}

}
