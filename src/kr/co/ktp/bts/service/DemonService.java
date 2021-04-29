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
	 * ��ġ�̷µ��
	 * @param DemonDTO
	 * @throws Exception 
	 */
	public void startBatchHtry(DemonDTO dto) throws Exception{
		dao.startBatchHtry(dto);
	}
	
	/**
	 * ��ġ�̷¼���(�۾��Ϸᱸ�� 0:�۾���,1:����,2:�Ϸ�)
	 * @param DemonDTO
	 * @throws Exception 
	 */
	public void endBatchHtry(DemonDTO dto) throws Exception{
		dao.endBatchHtry(dto);
	}
	
	
	/**
	 * ���ν�������
	 * @param paramMap
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> runProcedure(Map<String, Object> paramsMap, String id) throws Exception {
		return dao.runProcedure(paramsMap, id);
	}
	
	/**
	 * proc ������ ��ȸ
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getBtcBatchRslt(Map<String, Object> paramsMap) throws Exception{
		return dao.getBtcBatchRslt(paramsMap);
	}
	
	/**
	 * û������ ��ȸ
	 * @param paramsMap
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getBtsBillCycle(Map<String, Object> paramsMap) throws Exception{
		return dao.getBtsBillCycle(paramsMap);
	}

	/**
	 * ��ġ���α׷����� ��ȸ
	 * @param paramsMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getBtcBatPgmToDoList(Map<String, Object> paramsMap) throws Exception {
		return dao.getBtcBatPgmToDoList(paramsMap);
	}

	/**
	 * ��� û������ ���翩��
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
