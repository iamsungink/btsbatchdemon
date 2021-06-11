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
			
			// 개발서버와 운영서버를 구분하기 위해 JVM 의 argument를 가져온다.
            String sysProp = System.getProperty("gubun");
            System.out.println("서버 구분 (Mapper) : " + sysProp);
            
            String resource = "dev".equals(sysProp) ? "SqlMapConfig_dev.xml" : "SqlMapConfig.xml";
            
			Reader reader = Resources.getResourceAsReader(resource);
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
