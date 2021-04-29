package kr.co.ktp.bts.db;

import com.ibatis.sqlmap.client.SqlMapClient;

public abstract class AbstractService {
	
	/**
	 * sqlMap
	 */
	protected SqlMapClient sqlMap;
	
	protected AbstractService() {
		this.sqlMap = SqlMapConfig.getSqlMapInstance();
	}
	
	protected void handleException(Exception e) {
		e.printStackTrace();
	}

}
