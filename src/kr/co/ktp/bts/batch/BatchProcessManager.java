package kr.co.ktp.bts.batch;

import kr.co.ktp.bts.config.Logger;

public class BatchProcessManager {
	public BatchProcessManager(){}
	
	public void running(String[] args){
		Logger.writeLog("");
		Logger.writeLog("---------------------------------------------------------------------------------");
		Logger.writeLog("|[                          BatchWorkMain running                              ]|");
		Logger.writeLog("---------------------------------------------------------------------------------");
		
		for(int i=0; i<args.length; i++){
			Logger.writeLog("args["+i+"] : " + args[i]);
		}
		
		try {
			ABatchProcess batchProcess = BatchProcessFactory.createBatchProcess(args[1]);
			batchProcess.initialize(args);
			Logger.writeLog("call ABatchProcess.process()");
			batchProcess.process();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			Logger.writeLog("ClassNotFoundException : " + e.toString());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			Logger.writeLog("IllegalAccessException : " + e.toString());
		} catch (InstantiationException e) {
			e.printStackTrace();
			Logger.writeLog("InstantiationException : " + e.toString());
		}finally{
			Logger.writeLog("---------------------------------------------------------------------------------");
			Logger.writeLog("|[                          BatchWorkMain closing                              ]|");
			Logger.writeLog("---------------------------------------------------------------------------------");
			System.exit(0);
		}
		
	}
	
	public static void main(String[] args){
		BatchProcessManager batch = new BatchProcessManager();
		batch.running(args);
	}

}
