package kr.co.ktp.bts.demon;

public class DemonInfo {
	private String strWorkCd	= null;
	private String strYMDT		= null;
	private String strUSYN		= null;
	private String strMODE		= null;
	private String strINTV		= null;
	private String strSTRT		= null;
	private String strENDT		= null;
	private String strPARAM     = null;     // 프로세스 기본 PARAMETER
	private long  longRunIdx	= -1;
	
	public DemonInfo(String strWorkCd){
		setStrWorkCd(strWorkCd);
	}
	
	public DemonInfo(String strWorkCd, String strYMDT, String strUSYN, String strMODE, String strINTV, String strSTRT, String strENDT){
		setStrWorkCd(strWorkCd);
		setStrYMDT(strYMDT);
		setStrUSYN(strUSYN);
		setStrMODE(strMODE);
		setStrINTV(strINTV);
		setStrSTRT(strSTRT);
		setStrENDT(strENDT);
	}
	
	public void setStrWorkCd(String strWorkCd){
		this.strWorkCd = strWorkCd;
	}
	public void setStrYMDT(String strYMDT){
		this.strYMDT	= strYMDT;
	}
	public void setStrUSYN(String strUSYN){
		this.strUSYN	= strUSYN.toUpperCase();
	}
	public void setStrMODE(String strMODE){
		this.strMODE	= strMODE;
	}
	public void setStrINTV(String strINTV){
		this.strINTV	= strINTV;
	}
	public void setStrSTRT(String strSTRT){
		this.strSTRT	= strSTRT;
	}
	public void setStrENDT(String strENDT){
		this.strENDT	= strENDT;
	}
	public void setLongRunIdx(long idx){
		this.longRunIdx = idx;
	}
	
	public String getStrWorkCd(){
		return this.strWorkCd;
	}
	public String getStrYMDT(){
		return this.strYMDT;
	}
	public String getStrUSYN(){
		return this.strUSYN;
	}
	public String getStrMODE(){
		return this.strMODE;
	}
	public String getStrINTV(){
		return this.strINTV;
	}
	public String getStrSTRT(){
		return this.strSTRT;
	}
	public String getStrENDT(){
		return this.strENDT;
	}
	public long getLongRunIdx(){
		return this.longRunIdx;
	}

	public void clear(){
		if(strYMDT!=null){ setStrYMDT(""); }
		if(strUSYN!=null){ setStrUSYN(""); }
		if(strMODE!=null){ setStrMODE(""); }
		if(strINTV!=null){ setStrINTV(""); }
		if(strSTRT!=null){ setStrSTRT(""); }
		if(strENDT!=null){ setStrENDT(""); }
	}
	public String toString(){
		StringBuffer sbRtnToString = new StringBuffer();
		sbRtnToString.append("kr.co.ktp.bts.demon.DemonInfo ");
		sbRtnToString.append("strWorkCd=");
		sbRtnToString.append(strWorkCd==null?"":strWorkCd);
		sbRtnToString.append(", strYMDT=");
		sbRtnToString.append(strYMDT==null?"":strYMDT);
		sbRtnToString.append(", strUSYN=");
		sbRtnToString.append(strUSYN==null?"":strUSYN);
		sbRtnToString.append(", strMODE=");
		sbRtnToString.append(strMODE==null?"":strMODE);
		sbRtnToString.append(", strINTV=");
		sbRtnToString.append(strINTV==null?"":strINTV);
		sbRtnToString.append(", strSTRT=");
		sbRtnToString.append(strSTRT==null?"":strSTRT);
		sbRtnToString.append(", strENDT=");
		sbRtnToString.append(strENDT==null?"":strENDT);
		sbRtnToString.append(", longRunIdx=");
		sbRtnToString.append(longRunIdx+"");
		sbRtnToString.append(", strPARAM=");
		sbRtnToString.append(strPARAM==null?"":strPARAM);

		return sbRtnToString.toString();
	}
	public void setStrPARAM(String strPARAM) {
		this.strPARAM = strPARAM;
	}
	public String getStrPARAM() {
		return strPARAM;
	}

}