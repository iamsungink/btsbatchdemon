package kr.co.ktp.bts.util;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;
import java.net.SocketTimeoutException;

public class StreamUtil {
	
	public static void printString(PrintStream printStream, String string){
		printStream.print(string);
		printStream.flush();
	}
	
	public static void release(OutputStream outputStream){
		if (outputStream == null) return;
		try {
			outputStream.flush();
			outputStream.close();
		}catch (IOException localIOException){
			
		}
	}
	
	public static void release(Reader reader){
		if (reader == null) return;
		try {
			reader.close();
		}catch (IOException localIOException){
		}
	}
	
	public static void release(Writer writer){
		if (writer == null) return;
		try {
			writer.flush();
			writer.close();
		}catch (IOException localIOException) {
		}
	}
	
	 public static String readLine(Reader reader){
		 String readLine = null;
		 CharArrayWriter charArrayWriter = new CharArrayWriter();
		 try {
			 boolean isEOF = false;
			 boolean isLeft = true;
			 
			 while ((!(isEOF)) && (isLeft)) {
				 int readChar = reader.read();
				 
				 if (readChar == -1) {
					 isEOF = true;
					 isLeft = false;
				 }else if (readChar == 10) {
					 isLeft = false;
				 }else if ((readChar != 13) && (readChar != 10)) {
					 charArrayWriter.write((char)readChar);
				 }
			 }
			 if ((isEOF) && (charArrayWriter.size() == 0)){
				 return null;
			 }
				 
			 
		 }catch (SocketTimeoutException ste){
		 }catch (IOException ioe){
		 }finally {
			 boolean isLeft;
			 boolean isEOF;
			 release(charArrayWriter); 
		 }
		 release(charArrayWriter);
		 return readLine;
	 
	 }

}
