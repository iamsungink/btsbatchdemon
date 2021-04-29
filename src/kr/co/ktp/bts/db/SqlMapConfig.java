package kr.co.ktp.bts.db;

import java.io.Reader;
import java.nio.charset.Charset;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

public class SqlMapConfig {
	
	/**
	 * sqlMap
	 */
	private static SqlMapClient sqlMap;
	
	static {
		try {
			Charset charset = Charset.forName("UTF-8");
			Resources.setCharset(charset);
			
			Reader reader = Resources.getResourceAsReader("SqlMapConfig.xml");
			sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
			
		}catch (Exception ioe) {
			ioe.printStackTrace();
		}
		
	}
	
	/**
	 * getSqlMapInstance
	 * 
	 * @param SqlMapClient
	 */
	public static SqlMapClient getSqlMapInstance() {
		return sqlMap;
	}

}
