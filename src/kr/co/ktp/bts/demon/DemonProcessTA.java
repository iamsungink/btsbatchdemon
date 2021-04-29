package kr.co.ktp.bts.demon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.ktp.bts.config.Configuration;
import kr.co.ktp.bts.service.DemonService;
import kr.co.ktp.bts.util.DateTime;

public class DemonProcessTA extends ADemonProcess{
	
	private String PROCESS_NAME = "총산 ";
	
	private List<Map<String, Object>> btcBatPgmList  = null;
	
	private String inv_yyyymm = null;
	private String inv_flag = null;
	private String work_proc_stage = null;
	private String inv_start_date = null;
	private String inv_end_date = null;
	
	private DemonService service = new DemonService();
	
	@Override
	public void initialize(DemonInfo demonInfo) throws Exception {
		this.demonInfo = demonInfo;
		
		inv_yyyymm = getFormatDate(0 , 0, "yyyyMM");
		inv_flag = "1";
		
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		paramsMap.put("inv_yyyymm", inv_yyyymm);
		paramsMap.put("inv_flag", inv_flag);
		
		if(service.isExistBtsBillCycleMM(paramsMap)){
			resultMap = service.getBtsBillCycle(paramsMap);
			work_proc_stage = (String)resultMap.get("WORK_PROC_STAGE");
			inv_start_date = (String)resultMap.get("INV_START_DATE");
			inv_end_date = (String)resultMap.get("INV_END_DATE");
		}else{
			work_proc_stage = "00";
			inv_start_date = getFormatDate(-1 , 0, "yyyyMM")+"01000000";
			inv_end_date= getFormatDate(-1, 0, true)+"235959";
		}
		
		paramsMap.clear();
		paramsMap.put("work_bef_stage", work_proc_stage);
		
		btcBatPgmList = service.getBtcBatPgmToDoList(paramsMap);
		
	}

	@Override
	public void runDemonProcess() {
		threadDemon = new Thread(this);
		threadDemon.start();
	}
	
	/**
	//=============================================================================
	// 총산
	//------------------------------------------------------------------------------
    //  IDX | FIELD             | VALUE
	//------------------------------------------------------------------------------
	//  [0] | strModuleID       | bts_cust_trans_KT_test
	//  [1] | strWorkCd         | TA
	//  [2] | strWorkDt         | DateTime.getCurrent("yyyyMMdd")
	//  [3] | strWorkTm         | DateTime.getCurrent("HHmmss")
	//  [4] | strBrnCd          | 00000
	//  [5] | strUserCd         | DEMON
	//  [6] | strInvYyyyMm      |
	//  [7] | strInvFlag        | 
	//  [8] | strSrchStrtDt     | 
	//  [9] | strSrchEndtDt     |
	//  [10]| strRunProcedure   | SP_BTS_CUST_TRANS
	//------------------------------------------------------------------------------
	//=============================================================================
	*/	
	@Override
	public void run() {
		printDemonLog("START");
		
		StringBuffer sbArgs = new StringBuffer();
		
		for(int i=0; i<btcBatPgmList.size(); i++){
			Map<String, Object> BtcBatPgm = btcBatPgmList.get(i);
			sbArgs.delete(0, sbArgs.length());
			sbArgs.append((String)BtcBatPgm.get("PGM_ID"));
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
			sbArgs.append(inv_start_date);
			sbArgs.append(" ");
			sbArgs.append(inv_end_date);
			sbArgs.append(" ");
			sbArgs.append((String)BtcBatPgm.get("PROC"));
			
			int intRtnValue = executeBatchProcess(sbArgs);
			if(intRtnValue==DEMON_SUCCESS){
				printDemonLog(PROCESS_NAME+"RUNNING RESULT [DEMON_SUCCESS] ["+ demonInfo.toString() + "]");
			}
			if(intRtnValue==DEMON_NORECORD){
				printDemonLog(PROCESS_NAME+"RUNNING RESULT [DEMON_NORECORD] ["+ demonInfo.toString() + "]");
			}
			if(intRtnValue==DEMON_ERROR){
				printDemonLog(PROCESS_NAME+"RUNNING RESULT [DEMON_ERROR] ["+ demonInfo.toString() + "]");
				break;
			}			
		}
		
		printDemonLog("END");
		
	}
	
	public static void main(String[] args)throws Exception{
		
		DemonInfo arrDemonInfo = new DemonInfo("TA");
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
