package kr.co.ktp.bts.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import kr.co.ktp.bts.dao.DemonDAO;
import kr.co.ktp.bts.dto.BillInfoDTO;
import kr.co.ktp.bts.dto.DemonDTO;

public class DemonService {
	
	private DemonDAO dao = new DemonDAO();
	
	/**
	 * 배치이력등록
	 * @param DemonDTO
	 * @throws Exception 
	 */
	public void startBatchHtry(DemonDTO dto) throws Exception{
		dao.startBatchHtry(dto);
	}
	
	/**
	 * 배치이력수정(작업완료구분 0:작업중,1:오류,2:완료)
	 * @param DemonDTO
	 * @throws Exception 
	 */
	public void endBatchHtry(DemonDTO dto) throws Exception{
		dao.endBatchHtry(dto);
	}
	
	
	/**
	 * 프로시저실행
	 * @param paramMap
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> runProcedure(Map<String, Object> paramsMap, String id) throws Exception {
		return dao.runProcedure(paramsMap, id);
	}
	
	/**
	 * proc 실행결과 조회
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getBtcBatchRslt(Map<String, Object> paramsMap) throws Exception{
		return dao.getBtcBatchRslt(paramsMap);
	}
	
	/**
	 * 청구정보 조회
	 * @param paramsMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getBtsBillCycle(Map<String, Object> paramsMap) throws Exception{
		return dao.getBtsBillCycle(paramsMap);
	}

	/**
	 * 배치프로그램정보 조회
	 * @param paramsMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getBtcBatPgmToDoList(Map<String, Object> paramsMap) throws Exception {
		return dao.getBtcBatPgmToDoList(paramsMap);
	}

	/**
	 * 당월 청구정보 존재여부
	 * @param paramsMap
	 * @return
	 * @throws SQLException
	 */
	public boolean isExistBtsBillCycleMM(Map<String, Object> paramsMap) throws SQLException {
		return dao.isExistBtsBillCycleMM(paramsMap);
	}

	/**
	 * createBillInfo
	 * @param billInfoList
	 * @param invYyyyMm
	 * @return
	 * @throws Exception
	 */
	public int createBillInfo(List<Map<String, Object>> billInfoList, String invYyyyMm) throws Exception {
		return dao.createBillInfo(billInfoList, invYyyyMm);
		
	}

	public int deleteBillInfoTemp() throws Exception {
		return dao.deleteBillInfoTemp();
		
	}
	
	public int deleteKtfBanTemp() throws Exception {
		return dao.deleteKtfBanTemp();
		
	}
	
	public void insertKtfBanTemp(String operation)throws Exception{
		dao.insertKtfBanTemp(operation);
	}
	
	public int deleteKtfAbnorm(Map<String, Object> paramsMap) throws Exception{
		return dao.deleteKtfAbnorm(paramsMap);
	}
	
	public int deleteKtfBanChrg(Map<String, Object> paramsMap) throws Exception{
		return dao.deleteKtfBanChrg(paramsMap);
	}

	public void insertKtfBanChrg(Map<String, Object> paramsMap) throws Exception{
		dao.insertKtfBanChrg(paramsMap);
		
	}
	
	public void insertKtfAbnorm(Map<String, Object> paramsMap) throws Exception{
		dao.insertKtfAbnorm(paramsMap);
		
	}
	
	public List<Map<String, Object>> getKosBanChrgReportList(Map<String, Object> paramsMap) throws Exception {
		return dao.getKosBanChrgReportList(paramsMap);
	}

}
