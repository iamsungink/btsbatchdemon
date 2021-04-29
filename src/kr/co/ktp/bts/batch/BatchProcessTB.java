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

public class BatchProcessTB extends ABatchProcess{

	protected String PROCESSID = "BatchProcessTB";
	private String strRunProcedure = "";
	private String strWorkCmptFg = "";

	@Override
	public void initialize(String[] args) {
		/**
		//=============================================================================
		// [프로그램명]
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
 		//  [10]| PROCEDURE         | SP_BTSSTICKCARE_ADD
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
			
			if(!executeBatchProcedure()){
				throw new Exception(PROCESSID+".executeBatchProcedure() : Procedure running error!");
			}
			
			printBatchLog(PROCESSID+" Result : [ SUCCESS ]");
			strWorkCmptFg = BATCH_SUCCESS;
			System.out.println("SUCCESS");
			
		}catch(Exception e){
			printBatchLog(PROCESSID+".process() : " + e.toString());
			strWorkCmptFg = BATCH_ERROR;
		}finally{
			printBatchLog("END");
			saveBatchHtry("END", strWorkCmptFg);
		}
		
	}

	@Override
	public boolean executeBatch() throws Exception {
		return false;
	}

	@Override
	public boolean executeBatchProcedure() throws Exception {
		
		printBatchLog( PROCESSID+".executeBatchProcedure() : [" + strRunProcedure + "]");
		
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			paramsMap = new HashMap<String, Object>();
			resultMap = new HashMap<String, Object>();
			
			paramsMap.put("iProcerId", "DEMON");
			
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
			
			return resultMap != null && ((Integer)resultMap.get("oResultCnt") > -1)?true:false;
		} catch (Exception e) {
			throw new Exception(PROCESSID+".executeBatchProcedure() : Exception : " + e.toString());
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
		args[0] = "BTSSTICKCARE_ADD";
		args[1] = "TB";
		args[2] = DateTime.getCurrent("yyyyMMdd");
		args[3] = DateTime.getCurrent("HHmmss");
		args[4] = "00000";
		args[5] = "DEMON";
		args[6] = "201910";
		args[7] = "1";
		args[8] = "YYYYMM01000000";
		args[9] = "YYYYMMDD235959";
		args[10] = "SP_BTSSTICKCARE_ADD";
		BatchProcessTB bib = new BatchProcessTB();
		bib.initialize(args);
		bib.process();
	}

}
