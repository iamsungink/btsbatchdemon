package kr.co.ktp.bts.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.ktp.bts.db.AbstractService;
import kr.co.ktp.bts.dto.BillInfoDTO;
import kr.co.ktp.bts.dto.DemonDTO;

import org.apache.log4j.Logger;

public class DemonDAO extends AbstractService{
	
	private static final Logger logger = Logger.getLogger(DemonDAO.class);
	private static final String NAME_SPACE = "demonProcessor.";
	
	/**
	 * 배치이력등록
	 * @param DemonDTO
	 */
	public void startBatchHtry(DemonDTO dto) throws Exception{
		try {
			sqlMap.insert(NAME_SPACE+"startBatchHtry", dto);
		} catch (Exception e) {
			logger.error("insertBatchHtry exception", e);
			throw new Exception(e);
		}
	}
	
	/**
	 * 배치이력수정(작업완료구분 0:작업중,1:오류,2:완료)
	 * @param DemonDTO
	 */
	public void endBatchHtry(DemonDTO dto) throws Exception{
		try{
			sqlMap.update(NAME_SPACE+"endBatchHtry",dto);
			
		}catch (Exception e){
			logger.error("updateWorkCmptFg exception", e);
			throw new Exception(e);
		}
	}
	
	
	/**
	 * 프로시저 실행
	 * @param paramMap
	 * @param id
	 * @return
	 */
	public Map<String, Object> runProcedure(Map<String, Object> paramsMap, String id) {
		try {
			sqlMap.queryForObject(NAME_SPACE+id, paramsMap);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Map<String, Object> resultMap = null;
		String strWorkCd = (String)paramsMap.get("iWorkCd");
		if(strWorkCd.equals("TB")){
			resultMap = new HashMap<String, Object>();
			resultMap.put("oResultCnt", paramsMap.get("oResultCnt"));
			resultMap.put("oResultMsg", paramsMap.get("oResultMsg"));
		}else{
			resultMap = new HashMap<String, Object>();
			resultMap.put("oReturnStatus", paramsMap.get("oReturnStatus"));
			resultMap.put("oErrMsg", paramsMap.get("oErrMsg"));
		}
		
		return resultMap;
	}
	
	/**
	 * proc 실행결과 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getBtcBatchRslt(Map<String, Object> paramsMap) throws Exception{
		return (Map<String, Object>) sqlMap.queryForObject(NAME_SPACE+"getBtcBatchRslt", paramsMap);
	}
	
	/**
	 * 청구정보 조회
	 * @param paramsMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getBtsBillCycle(Map<String, Object> paramsMap) throws Exception{
		return (Map<String, Object>) sqlMap.queryForObject(NAME_SPACE+"getBtsBillCycle", paramsMap);
	}

	/**
	 * 배치프로그램정보
	 * @param paramsMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getBtcBatPgmToDoList(Map<String, Object> paramsMap) throws Exception {
		return (List<Map<String, Object>>) sqlMap.queryForList(NAME_SPACE+"getBtcBatPgmToDoList", paramsMap);
	}

	/**
	 * 당월청구정보 존재여부
	 * @param paramsMap
	 * @return
	 * @throws SQLException
	 */
	public boolean isExistBtsBillCycleMM(Map<String, Object> paramsMap) throws SQLException {
		return  (Boolean) sqlMap.queryForObject(NAME_SPACE+"isExistBtsBillCycleMM", paramsMap);
	}

	/**
	 * createBillInfo
	 * @param billInfoList
	 * @param invYyyyMm
	 * @return
	 * @throws Exception
	 */
	public int createBillInfo(List<Map<String, Object>> billInfoList, String invYyyyMm) throws Exception{
		int cnt = 0;
		ArrayList<Map<String, Object>> chageList = null;
		
		try {
			sqlMap.startTransaction();
			sqlMap.startBatch();
			
			for(Map<String, Object> map : billInfoList){
				map.put("invYyyyMm", invYyyyMm);
				chageList = (ArrayList<Map<String, Object>>) map.get("chageList");
				for(Map<String, Object> chageMap : chageList){
					
					map.put("chageAcrndItemId", chageMap.get("chageAcrndItemId"));
					map.put("chageDcBefAmt", chageMap.get("chageDcBefAmt"));
					
					sqlMap.insert(NAME_SPACE+"createBillInfo", map);
					cnt ++;
					if(cnt % 10000 == 0){
						sqlMap.executeBatch();
					}
				}
			}
			
			sqlMap.executeBatch();
			sqlMap.commitTransaction();
			
		} catch (Exception e) {
			logger.error("createBillInfo exception", e);
			throw new Exception(e);
		} finally{
			sqlMap.endTransaction();
		}
		return cnt;
	}

	public int deleteBillInfoTemp() throws Exception{
		int cnt = 0;
		
		try{
			cnt = sqlMap.delete(NAME_SPACE+"deleteBillInfoTemp");
		}catch (Exception e){
			logger.error("deleteBillInfoTemp exception", e);
			throw new Exception(e);
		}
		
		return cnt;
	}

	public int deleteKtfBanTemp() throws Exception{
		int cnt = 0;
		
		try{
			cnt = sqlMap.delete(NAME_SPACE+"deleteKtfBanTemp");
		}catch (Exception e){
			logger.error("deleteKtfBanTemp exception", e);
			throw new Exception(e);
		}
		
		return cnt;
	}
	
	public void insertKtfBanTemp(String operation)throws Exception{
		try {
			if(operation.equals("NDEV")){
				sqlMap.insert(NAME_SPACE+"insertKtfBanTempForNDEV");
			}else{
				sqlMap.insert(NAME_SPACE+"insertKtfBanTempForNBILL");
			}
			
		} catch (Exception e) {
			logger.error("insertKtfBanTemp exception", e);
			throw new Exception(e);
		}
	}
	
	public int deleteKtfAbnorm(Map<String, Object> paramsMap) throws Exception{
		int cnt = 0;
		
		try{
			cnt = sqlMap.delete(NAME_SPACE+"deleteKtfAbnorm", paramsMap);
		}catch (Exception e){
			logger.error("deleteKtfAbnorm exception", e);
			throw new Exception(e);
		}
		
		return cnt;
	}
	
	public int deleteKtfBanChrg(Map<String, Object> paramsMap) throws Exception{
		int cnt = 0;
		
		try{
			cnt = sqlMap.delete(NAME_SPACE+"deleteKtfBanChrg", paramsMap);
		}catch (Exception e){
			logger.error("deleteKtfBanChrg exception", e);
			throw new Exception(e);
		}
		
		return cnt;
	}

	public void insertKtfBanChrg(Map<String, Object> paramsMap) throws Exception{
		try {
			sqlMap.insert(NAME_SPACE+"insertKtfBanChrg", paramsMap);
		} catch (Exception e) {
			logger.error("insertKtfBanChrg exception", e);
			throw new Exception(e);
		}
	}
	
	public void insertKtfAbnorm(Map<String, Object> paramsMap) throws Exception{
		try {
			sqlMap.insert(NAME_SPACE+"insertKtfAbnorm", paramsMap);
		} catch (Exception e) {
			logger.error("insertKtfAbnorm exception", e);
			throw new Exception(e);
		}
	}
	
	public List<Map<String, Object>> getKosBanChrgReportList(Map<String, Object> paramsMap) throws Exception {
		return (List<Map<String, Object>>) sqlMap.queryForList(NAME_SPACE+"getKosBanChrgReportList", paramsMap);
	}


}
