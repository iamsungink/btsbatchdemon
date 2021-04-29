package kr.co.ktp.bts.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
	
	public static List getFileList(File path, boolean includePath) throws Exception{
		List fileList = new ArrayList();
		
		if ((path == null) || (path.getPath().length() == 0)) {
			throw new Exception("File not exist");
		}
		
		if (!(path.canRead())) {
			throw new Exception("File can not read");
		}
		
		File[] files = path.listFiles();
		for (int listIndex = 0; listIndex < files.length; ++listIndex) {
			File file = files[listIndex];
			if ((includePath) && (!(file.isHidden())))
				fileList.add(file.getName());
			else if ((file.isFile()) && (file.isHidden())) {
				fileList.add(file.getName());
			}
		}
		return fileList;
	}
	
	public static long getFileSize(File file) throws Exception {
		if(file == null || file.getName().length() == 0){
			throw new Exception("File not exist");
		}else{
			return file.length();
		}
	}
	
	public static void deleteFile(File file) throws Exception {
		if ((file == null) || (file.getName().length() == 0)) {
			throw new Exception("File not exist");
		}
		file.delete();
	}
	
	public static void renameFile(File sourceFile, File targetFile){
		sourceFile.renameTo(targetFile);
	}
	
	public static boolean isWindows() {
		String os = System.getProperty("os.name").toLowerCase();
		return (os.indexOf("win") >= 0);
	}
	
	public static boolean isMac() {
		String os = System.getProperty("os.name").toLowerCase();
		return (os.indexOf("mac") >= 0);
	}
	
	public static boolean isUnix() {
		String os = System.getProperty("os.name").toLowerCase();
		return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);
	}
	
	public static boolean isSolaris() {
		String os = System.getProperty("os.name").toLowerCase();
		return (os.indexOf("sunos") >= 0);
	}
	
}
