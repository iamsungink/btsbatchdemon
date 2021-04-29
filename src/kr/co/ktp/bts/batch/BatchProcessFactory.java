package kr.co.ktp.bts.batch;

public class BatchProcessFactory {
	public static ABatchProcess createBatchProcess(String strWorkCd) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		String stClassName = BatchProcessFactory.class.getName();
		String stPackageName = stClassName.substring(0, stClassName.lastIndexOf("."));
		return (ABatchProcess)(Class.forName(stPackageName + ".BatchProcess" + strWorkCd)).newInstance();
	}
}
