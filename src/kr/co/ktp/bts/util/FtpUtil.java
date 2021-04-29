/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kr.co.ktp.bts.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

/**
 * This is an example program demonstrating how to use the FTPClient class.
 * This program connects to an FTP server and retrieves the specified
 * file.  If the -s flag is used, it stores the local file at the FTP server.
 * Just so you can see what's happening, all reply strings are printed.
 * If the -b flag is used, a binary transfer is assumed (default is ASCII).
 * See below for further options.
 */
public final class FtpUtil{
	
	public static FTPClient connectFTP(String pSvrip, String pSvrport, String pUsername, String pPassword) throws Exception{
		boolean localActive = false;
		
		FTPClient ftp = new FTPClient();
		
		ftp.setListHiddenFiles(false);
		
		// suppress login details
		ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out), true));
		
		try{
			int reply;
			
			ftp.connect(pSvrip, Integer.parseInt(pSvrport));
			System.out.println("Connected to " + pSvrip + " on "+ftp.getRemotePort());
			
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)){
				ftp.disconnect();
				throw new Exception("connectFTP() : FTP server refused connection : ");
			}

		} catch (IOException e) {
			e.printStackTrace();
			
			if (ftp.isConnected()){
				try{
					ftp.disconnect();
				}catch (IOException f){
					// do nothing
				}
			}
			throw new Exception("connectFTP() : Could not connect to server : " + e.toString());
		} 
		
		try {
			if (!ftp.login(pUsername, pPassword)){
				ftp.logout();
			}
			System.out.println("Remote system is " + ftp.getSystemType());
			
			if (localActive) {
				ftp.enterLocalActiveMode();
			}else {
				ftp.enterLocalPassiveMode();
			}
			
			ftp.setUseEPSVwithIPv4(false);
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("connectFTP() : IOException : " + e.toString());
		} 
		return ftp;
	}
	
	public static void putFile(FTPClient ftp, String pRemote, String pLocal) throws Exception{
		
		InputStream input;
		
		try {
			input = new FileInputStream(pLocal);
			
			ftp.storeFile(pRemote, input);
			
			input.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new Exception("connectFTP() : FileNotFoundException : " + e.toString());
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("connectFTP() : IOException : " + e.toString());
		} 
	}

}

