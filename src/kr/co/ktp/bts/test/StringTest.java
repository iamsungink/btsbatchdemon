package kr.co.ktp.bts.test;

import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;

public class StringTest {
	
	public static void main(String[] args) throws UnsupportedEncodingException {

		String source = "대한민국";
		
		// java파일 기본 encoding
		System.out.println("file encoding : "
				+ System.getProperty("file.encoding"));
		
		// 기본 encoding의 바이트 배열
		byte[] chbyte = source.getBytes();
		for (byte b : chbyte) {
			System.out.printf("%02x ", b);
		}
		System.out.println();
		System.out.println("기본 encoding 문자열 길이 : "+new String(chbyte).length());
		System.out.println("기본 encoding 바이트 길이 : "+chbyte.length);
		System.out.println("기본 encoding 문자열      : "+new String(chbyte));
		System.out.println();

		// euc-kr encoding의 바이트 배열
		byte[] krbyte = source.getBytes("euc-kr");
		for (byte b : krbyte) {
			System.out.printf("%02x ", b);
		}
		System.out.println();
		System.out.println("euc-kr 문자열 길이 : "+new String(krbyte).length());
		System.out.println("euc-kr 바이트 길이 : "+krbyte.length);
		System.out.println("euc-kr 문자열 : "+new String(krbyte, "euc-kr"));
		
		String paramList = "a|0,b|1,c|2";
		String[] arrDelKey = paramList.split(",");
		for(int i=0;i<arrDelKey.length;i++){
			System.out.println("arrDelKey["+i+"] " +  arrDelKey[i]);
//			String[] arrtemp = arrDelKey[i].split("|");
//			System.out.println(arrtemp.length);
//			System.out.println("0 " + arrtemp[0]);
//			System.out.println("1 " + arrtemp[1]);
//			System.out.println("2 " + arrtemp[2]);
			StringTokenizer tokens = new StringTokenizer( arrDelKey[i], "|" );
			System.out.println("0 " + tokens.nextToken());
			System.out.println("1 " + tokens.nextToken());
			
		}

	}
	
	

}
