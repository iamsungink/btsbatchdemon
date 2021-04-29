package kr.co.ktp.bts.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	public static String logPath = null;

	public Logger() {
	}

	public static String dateTime() {
		return dateTime(null, "");
	}

	public static String dateTime(String fmt) {
		return dateTime(new Date(), fmt);
	}

	public static String dateTime(Date date) {
		return dateTime(date, "");
	}

	public static String dateTime(Date date, String fmt) {
		SimpleDateFormat formatter = null;
		if (date == null) {
			date = new Date();
			formatter = new SimpleDateFormat("yyyyMMdd");
		} else if (fmt.equals(""))
			formatter = new SimpleDateFormat("yyyyMMddhhmmssSS");
		else
			formatter = new SimpleDateFormat(fmt);
		String ret_date = "";
		ret_date = formatter.format(date);
		return ret_date;
	}

	public static void writeLog(String msg) {
		String today = dateTime();
		try {
			if (logPath == null) {
				logPath = Configuration.getConfig("BATCH.LOG.PATH");
			}
			File file = new File(logPath + "/DEFAULT_" + today + ".log");
			String filename = file.getAbsolutePath();
			FileWriter fw = new FileWriter(filename, true);
			PrintWriter writer = new PrintWriter(new BufferedWriter(fw), true);
			writer.println("[" + dateTime("yyyy-MM-dd hh:mm:ss") + "]\t" + msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeLog(String strWorkCd, String msg) {
		String today = dateTime();
		try {
			if(strWorkCd.equals("")){ strWorkCd = "DEFAULT"; }
			if (logPath == null) {
				logPath = Configuration.getConfig("BATCH.LOG.PATH");
			}
			File file = new File(logPath + "/" + strWorkCd + "_" + today + ".log");
			String filename = file.getAbsolutePath();
			FileWriter fw = new FileWriter(filename, true);
			PrintWriter writer = new PrintWriter(new BufferedWriter(fw), true);
			writer.println("[" + dateTime("yyyy-MM-dd hh:mm:ss") + "]\t" + msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *  Log 기록
	 * @param strWorkCd   배치작업코드(파일명의 앞에 붙일 name)
	 * @param ex        오류 
	 */
	public static void writeLog(String strWorkCd, Throwable ex){
		String today = dateTime();
		try {
			if (logPath == null) {
				logPath = Configuration.getConfig("BATCH.LOG.PATH");
			}
			File file = new File(logPath + "/" + strWorkCd + "_" + today + ".log");
			String filename = file.getAbsolutePath();
			FileWriter fw = new FileWriter(filename, true);
			PrintWriter writer = new PrintWriter(new BufferedWriter(fw), true);
			writer.println("[" + dateTime("yyyy-MM-dd HH:mm:ss") + "]\t");
			ex.printStackTrace(writer);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
