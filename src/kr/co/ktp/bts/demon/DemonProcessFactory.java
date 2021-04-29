package kr.co.ktp.bts.demon;

public class DemonProcessFactory {
	
	public static ADemonProcess createDemonProcess(String strWorkCd) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
		String stClassName = DemonProcessFactory.class.getName();
		String stPackageName = stClassName.substring(0, stClassName.lastIndexOf("."));
		return (ADemonProcess)(Class.forName(stPackageName + ".DemonProcess" + strWorkCd)).newInstance();
	}
	
	
}
