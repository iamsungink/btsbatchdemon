package kr.co.ktp.bts.batch;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import kr.co.ktp.bts.config.Configuration;
import kr.co.ktp.bts.dto.DemonDTO;
import kr.co.ktp.bts.service.DemonService;
import kr.co.ktp.bts.util.DateTime;
import kr.co.ktp.bts.util.FileUtil;

public class BatchProcessTA extends ABatchProcess{

	protected String PROCESSID = "BatchProcessTA";
	private String strRunProcedure = "";

	@Override
	public void initialize(String[] args) {
		/**
		//=============================================================================
		// [프로그램명]
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
 		//  [10]| PROCEDURE         | SP_BTS_CUST_TRANS_KT_TEST
		//------------------------------------------------------------------------------
		//=============================================================================
		*/
		try{
			service = new DemonService();
			batchWorkInfo	= new BatchWorkInfo(
					args[0],		//	strModuleID
					args[1],		//	strWorkCd
					args[2],		//	strWorkDt
					args[3],		//	strWorkTm
					args[4],		//	strBrnCd
					args[5],		//	strUserCd
					args[6],		//	strInvYyyyMm
					args[7],		//	strInvFlag
					args[8],		//	strSrchStrtDt
					args[9]);		//	strSrchEndDt
			strRunProcedure = args[10];
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	@Override
	public void process() {
		if(batchWorkInfo==null){
			printBatchLog(PROCESSID+".process() : BatchWorkInfo 를 초기화하지 못하였습니다");
			System.exit(0);
		}
		printBatchLog("");
		printBatchLog("START");
		printBatchLog(batchWorkInfo.toString());
		
		try{
			
			saveBatchHtry("START", BATCH_RUNNING);
			
			if(!executeBatch()){
				throw new Exception(PROCESSID+".executeBatch() : Batch running error!");
			}
			
			if(strRunProcedure != null && strRunProcedure.startsWith("SP")){
				if(!executeBatchProcedure()){
					throw new Exception(PROCESSID+".executeBatchProcedure() : Procedure running error!");
				}
			}
			
			saveBatchHtry("END", BATCH_SUCCESS);
			
			printBatchLog(PROCESSID+" Result : [ SUCCESS ]");
			System.out.println("SUCCESS");
			
		}catch(Exception defaultE){
			printBatchLog(PROCESSID+".process() : " + defaultE.toString());
			try {
				saveBatchHtry("END", BATCH_ERROR);
			}catch (Exception e) {
				printBatchLog("saveBatchHtry(String strDev, String strWorkCmptFg) : " + e.toString());
			}
		}finally{
			printBatchLog("END");
		}
		
	}

	@Override
	public boolean executeBatch() throws Exception {
		
		printBatchLog( PROCESSID+".executeBatch()");
		
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			String execFile = null;
			
			if(FileUtil.isWindows()){
				execFile = Configuration.getConfig("FILE.PATH.SHELL")+System.getProperty("file.separator")+batchWorkInfo.getStrModuleID()+".bat";
			}else{
				execFile = Configuration.getConfig("FILE.PATH.SHELL")+batchWorkInfo.getStrModuleID()+".sh";
			}
			Runtime rt = Runtime.getRuntime();
			Process process = rt.exec(execFile);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			while ((line = br.readLine()) != null) {
				printBatchLog(line);
			}
			int exitVal = process.waitFor();
			
			String prog_id = null;
			if(batchWorkInfo.getStrModuleID().startsWith("bts_cust_trans")){
				prog_id = "bts_cust_trans";
			}else{
				prog_id = batchWorkInfo.getStrModuleID();
			}
			
			paramsMap.put("prog_id", prog_id);
			paramsMap.put("work_date", batchWorkInfo.getStrWorkDt());
			resultMap = service.getBtcBatchRslt(paramsMap);
			//xxxxx
			printBatchLog("####### batch result #######");
			if(resultMap != null){
				printBatchLog(resultMap.toString());
			}
			printBatchLog("############################");
			//xxxxx
			
			return resultMap != null && ((String)resultMap.get("WORK_RSLT_FLAG")).equals("CO")?true:false;
		} catch (Exception e) {
			throw new Exception(PROCESSID+".executeBatch() : Exception : " + e.toString());
		} finally{
			try{
				paramsMap.clear();
				paramsMap.put("iSndPhnId", "01062761387");
				paramsMap.put("iRcvPhnId", "01062761387");
				paramsMap.put("iCallback", "01062761387");
				paramsMap.put("iSndMsg", resultMap != null && ((String)resultMap.get("WORK_RSLT_FLAG")).equals("CO")?"[배치성공]"+batchWorkInfo.getStrModuleID():"[배치실패]"+batchWorkInfo.getStrModuleID());
				service.runProcedure(paramsMap, "SP_BTSSENDSMS");
			}catch(Exception e){
			}
		}
				
	}

	@Override
	public boolean executeBatchProcedure() throws Exception {
		
		printBatchLog( PROCESSID+".executeBatchProcedure() : [" + strRunProcedure + "]");
		
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			paramsMap = new HashMap<String, Object>();
			resultMap = new HashMap<String, Object>();
			
			paramsMap.put("iInvYyyymm", batchWorkInfo.getStrInvYyyyMm());
			paramsMap.put("iInvFlag", batchWorkInfo.getStrInvFlag());
			paramsMap.put("iStDate", batchWorkInfo.getStrSrchStrtDt());
			paramsMap.put("iEndDate", batchWorkInfo.getStrSrchEndDt());
			
			paramsMap.put("iWorkCd", batchWorkInfo.getStrWorkCd());
			paramsMap.put("iWorkDt", batchWorkInfo.getStrWorkDt());
			paramsMap.put("iWorkTm", batchWorkInfo.getStrWorkTm());
			
			resultMap = service.runProcedure(paramsMap, strRunProcedure);
			//xxxxx
			printBatchLog("###### procedure result #######");
			if(resultMap != null){
				printBatchLog(resultMap.toString());
			}
			printBatchLog("###############################");
			//xxxxx
			
			return resultMap != null && ((String)resultMap.get("oReturnStatus")).equals("S")?true:false;
		} catch (Exception e) {
			throw new Exception(PROCESSID+".executeBatchProcedure() : Exception : " + e.toString());
		} finally{
			try{
				paramsMap.clear();
				paramsMap.put("iSndPhnId", "01062761387");
				paramsMap.put("iRcvPhnId", "01062761387");
				paramsMap.put("iCallback", "01062761387");
				paramsMap.put("iSndMsg", resultMap != null && ((String)resultMap.get("oReturnStatus")).equals("S")?"[검증성공]"+batchWorkInfo.getStrModuleID():"[검증실패]"+batchWorkInfo.getStrModuleID());
				service.runProcedure(paramsMap, "SP_BTSSENDSMS");
			}catch(Exception e){
			}
		}
	}
	
	public void saveBatchHtry(String strDev, String strWorkCmptFg) {
		if(strDev.equals("START")){
			DemonDTO dto = new DemonDTO();
			dto.setWork_cd(batchWorkInfo.getStrWorkCd());
			dto.setWork_dy(batchWorkInfo.getStrWorkDt());
			dto.setWork_tm(batchWorkInfo.getStrWorkTm());
			dto.setWork_module_id(batchWorkInfo.getStrModuleID());
			dto.setWork_cmpt_fg(strWorkCmptFg);
			
			try {
				service.startBatchHtry(dto);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if(strDev.equals("END")){
			DemonDTO dto = new DemonDTO();
			dto.setWork_cd(batchWorkInfo.getStrWorkCd());
			dto.setWork_dy(batchWorkInfo.getStrWorkDt());
			dto.setWork_tm(batchWorkInfo.getStrWorkTm());
			dto.setWork_cmpt_fg(strWorkCmptFg);
			
			try {
				service.endBatchHtry(dto);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
	public static void main(String args[]){
		args = new String[9];
		args[0] = "bts_cust_trans_KT_test";
		args[1] = "TA";
		args[2] = DateTime.getCurrent("yyyyMMdd");
		args[3] = DateTime.getCurrent("HHmmss");
		args[4] = "00000";
		args[5] = "DEMON";
		args[6] = "SP_BTS_CUST_TRANS_KT_TEST";
		args[7] = "20190701000000";
		args[8] = "20190731235959";
		args[9] = "201908";
		args[10] = "1";
		BatchProcessTA bib = new BatchProcessTA();
		bib.initialize(args);
		bib.process();
	}

}
