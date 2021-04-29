package kr.co.ktp.bts.batch;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import kr.co.ktp.bts.config.Configuration;
import kr.co.ktp.bts.dto.BillInfoDTO;
import kr.co.ktp.bts.dto.DemonDTO;
import kr.co.ktp.bts.service.DemonService;
import kr.co.ktp.bts.util.DateTime;
import kr.co.ktp.bts.util.FileUtil;
import kr.co.ktp.bts.xml.BillInfoHandler;

public class BatchProcessIB extends ABatchProcess{

	protected String PROCESSID = "BatchProcessIB";
	
	@Override
	public void initialize(String[] args) {
		/**
		//=============================================================================
		// [프로그램명]
		//------------------------------------------------------------------------------
	    //  IDX | FIELD             | VALUE
		//------------------------------------------------------------------------------
		//  [0] | strModuleID       | BAT00000_IB
		//  [1] | strWorkCd         | IB
		//  [2] | strWorkDt         | DateTime.getCurrent("yyyyMMdd")
		//  [3] | strWorkTm         | DateTime.getCurrent("HHmmss")
		//  [4] | strBrnCd          | 00000
		//  [5] | strUserCd         | DEMON
		//  [6] | strInvYyyyMm      |
		//  [7] | strInvFlag        |
		//  [8] | strSrchStrtDt     |
		//  [9] | strSrchEndtDt     |
		//  [10]| strFilePath       | /app/arch/btsbatch/data/
		//  [11]| strFileNm         | 201908_J732_20190909A019632_W001_001_00000.xml
		//  [12]| strFilePathBack   | /app/arch/btsbatch/data_back/IB
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
			batchWorkInfo.setStrFilePath(args[10]);
			batchWorkInfo.setStrFileNm(args[11]);
			batchWorkInfo.setStrFilePathBack(args[12]);
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
			
			renameFile(BATCH_RUNNING);
			
			if(!executeBatch()){
				throw new Exception(PROCESSID+".executeBatch() : Batch running error!");
			}
			
			//파일백업
			backupFile(new File(batchWorkInfo.getStrFilePath()+BATCH_RUNNING + "_"+batchWorkInfo.getStrFileNm()), new File(batchWorkInfo.getStrFilePathBack()+batchWorkInfo.getStrFileNm()));
			
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
		}
		finally{
			printBatchLog("END");
		}
		
	}

	@Override
	public boolean executeBatch() throws Exception {
		
		boolean isSucc = false;
		printBatchLog( PROCESSID+".executeBatch()");
		
		int cnt = 0;
		
		try{
			
			File xmlFile = new File(batchWorkInfo.getStrFilePath()+BATCH_RUNNING + "_"+batchWorkInfo.getStrFileNm());
			
			SAXParserFactory spf= SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			
			BillInfoHandler handler = new BillInfoHandler();
			sp.parse(xmlFile, handler);
			
			List<Map<String, Object>> billInfoList = handler.getBillInfoList();
			
			cnt= service.createBillInfo(billInfoList, batchWorkInfo.getStrInvYyyyMm());
			
			printBatchLog(PROCESSID+".executeBatch() : upload : " + cnt);
			
			isSucc = true;
		}catch (Error e){
			e.printStackTrace();
			throw new Exception("Error!!! : " + e.toString());
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(PROCESSID+".executeBatch() : Exception : " + e.toString());
		}
		
		return isSucc;
	}

	@Override
	public boolean executeBatchProcedure() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void saveBatchHtry(String strDev, String strWorkCmptFg) {
		if(strDev.equals("START")){
			DemonDTO dto = new DemonDTO();
			dto.setWork_cd(batchWorkInfo.getStrWorkCd());
			dto.setWork_dy(batchWorkInfo.getStrWorkDt());
			dto.setWork_tm(batchWorkInfo.getStrWorkTm());
			dto.setWork_module_id(batchWorkInfo.getStrModuleID());
			dto.setWork_cmpt_fg(strWorkCmptFg);
			dto.setWork_file_path(batchWorkInfo.getStrFilePathBack());
			dto.setWork_file_name(batchWorkInfo.getStrFileNm());
			
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
		args[0] = "BAT00000_IB";
		args[1] = "IB";
		args[2] = DateTime.getCurrent("yyyyMMdd");
		args[3] = DateTime.getCurrent("HHmmss");
		args[4] = "00000";
		args[5] = "DEMON";
		args[6] = "V:/btsbatch/data/";
		args[7] = "201908_J732_20190909A019632_W001_001_00000.xml";
		args[8] = "V:/btsbatch/data_back/IB/";
		BatchProcessIB bib = new BatchProcessIB();
		bib.initialize(args);
		bib.process();
	}

}