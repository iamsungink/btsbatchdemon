package kr.co.ktp.bts.batch;

public class BatchWorkInfo implements Cloneable{
	private String		strModuleID			= null;		// 배치작업 모듈 ID
	private String		strWorkCd			= null;		// 배치작업구분코드
	private String		strWorkDt			= null;		// 배치작업 실행일
	private String		strWorkTm			= null;		// 배치작업 실행시간
	private String		strBrnCd			= null;		// 센터코드 (작업자)
	private String		strUserCd			= null;		// 사용자코드 (작업자)
	private String		strInvYyyyMm		= null;
	private String		strInvFlag			= null;
	private String		strSrchStrtDt		= null;		// 조회 시작일자 (추출등의 작업에서 기간의 시작에 해당)
	private String		strSrchEndDt		= null;		// 조회 종료일자 (추출등의 작업에서 기간의 종료에 해당)
	private String		strFilePath			= null;		// 작업파일 경로
	private String		strFileNm			= null;		// 작업파일 명
	private String		strFilePathBack		= null;		// 백업파일 경로
	public BatchWorkInfo(
							String strModuleID,
							String strWorkCd,
							String strWorkDt,
							String strWorkTm,
							String strBrnCd,
							String strUserCd,
							String strInvYyyyMm,
							String strInvFlag,
							String strSrchStrtDt,
							String strSrchEndDt){
		setStrModuleID(strModuleID);
		setStrWorkCd(strWorkCd);
		setStrWorkDt(strWorkDt);
		setStrWorkTm(strWorkTm);
		setStrBrnCd(strBrnCd);
		setStrUserCd(strUserCd);
		setStrInvYyyyMm(strInvYyyyMm);
		setStrInvFlag(strInvFlag);
		setStrSrchStrtDt(strSrchStrtDt);
		setStrSrchEndDt(strSrchEndDt);
	}
	
	public void setStrModuleID(String strModuleID){ this.strModuleID = strModuleID; }
	public void setStrWorkCd(String strWorkCd){ this.strWorkCd = strWorkCd; }
	public void setStrWorkDt(String strWorkDt){ this.strWorkDt = strWorkDt; }
	public void setStrWorkTm(String strWorkTm){ this.strWorkTm = strWorkTm; }
	public void setStrBrnCd(String strBrnCd){ this.strBrnCd = strBrnCd; }
	public void setStrUserCd(String strUserCd){ this.strUserCd = strUserCd; }
	public void setStrInvYyyyMm(String strInvYyyyMm){ this.strInvYyyyMm = strInvYyyyMm; }
	public void setStrInvFlag(String strInvFlag){ this.strInvFlag = strInvFlag; }
	public void setStrSrchStrtDt(String strSrchStrtDt){ this.strSrchStrtDt = strSrchStrtDt; }
	public void setStrSrchEndDt(String strSrchEndDt){ this.strSrchEndDt = strSrchEndDt; }
	public void setStrFilePath(String strFilePath){ this.strFilePath = strFilePath; }
	public void setStrFileNm(String strFileNm){ this.strFileNm = strFileNm; }
	public void setStrFilePathBack(String strFilePathBack){ this.strFilePathBack = strFilePathBack; }

	public String getStrModuleID(){ return this.strModuleID; }
	public String getStrWorkCd(){ return this.strWorkCd; }
	public String getStrWorkDt(){ return this.strWorkDt; }
	public String getStrWorkTm(){ return this.strWorkTm; }
	public String getStrBrnCd(){ return this.strBrnCd; }
	public String getStrUserCd(){ return this.strUserCd; }
	public String getStrInvYyyyMm(){ return this.strInvYyyyMm; }
	public String getStrInvFlag(){ return this.strInvFlag; }
	public String getStrSrchStrtDt(){ return this.strSrchStrtDt; }
	public String getStrSrchEndDt(){ return this.strSrchEndDt; }
	public String getStrFilePath(){ return this.strFilePath; }
	public String getStrFileNm(){ return this.strFileNm; }
	public String getStrFilePathBack(){ return this.strFilePathBack; }
	public String toString(){
		StringBuffer sbRtnToString= new StringBuffer();
		sbRtnToString.append("kr.co.ktp.bts.batch.BatchWorkInfo ");
		sbRtnToString.append("strModuleID=" + getStrModuleID() + ", ");
		sbRtnToString.append("strWorkCd=" + getStrWorkCd() + ", ");
		sbRtnToString.append("strWorkDt=" + getStrWorkDt() + ", ");
		sbRtnToString.append("strWorkTm=" + getStrWorkTm() + ", ");
		sbRtnToString.append("strBrnCd=" + getStrBrnCd() + ", ");
		sbRtnToString.append("strUserCd=" + getStrUserCd() + ", ");
		sbRtnToString.append("strSrchStrtDt=" + getStrSrchStrtDt() + ", ");
		sbRtnToString.append("strSrchEndDt=" + getStrSrchEndDt() + ", ");
		sbRtnToString.append("strInvYyyyMm=" + getStrInvYyyyMm());
		sbRtnToString.append("strInvFlag=" + getStrInvFlag());
		return sbRtnToString.toString();
	}

}
