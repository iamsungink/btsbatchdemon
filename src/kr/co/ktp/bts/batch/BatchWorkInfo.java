package kr.co.ktp.bts.batch;

public class BatchWorkInfo implements Cloneable{
	private String		strModuleID			= null;		// ��ġ�۾� ��� ID
	private String		strWorkCd			= null;		// ��ġ�۾������ڵ�
	private String		strWorkDt			= null;		// ��ġ�۾� ������
	private String		strWorkTm			= null;		// ��ġ�۾� ����ð�
	private String		strBrnCd			= null;		// �����ڵ� (�۾���)
	private String		strUserCd			= null;		// ������ڵ� (�۾���)
	private String		strInvYyyyMm		= null;
	private String		strInvFlag			= null;
	private String		strSrchStrtDt		= null;		// ��ȸ �������� (������� �۾����� �Ⱓ�� ���ۿ� �ش�)
	private String		strSrchEndDt		= null;		// ��ȸ �������� (������� �۾����� �Ⱓ�� ���ῡ �ش�)
	private String		strFilePath			= null;		// �۾����� ���
	private String		strFileNm			= null;		// �۾����� ��
	private String		strFilePathBack		= null;		// ������� ���
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
