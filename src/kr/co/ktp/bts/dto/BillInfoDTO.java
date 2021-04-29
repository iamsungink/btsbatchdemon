package kr.co.ktp.bts.dto;

public class BillInfoDTO {
	
	private String invYyyymm;
	
	private String billInfoNo;
	
	private String billRefRepSvcContId;
	
	private String billRefMobileNo;
	
	private String chageAcrndItemId;
	
	private int chageDcBefAmt;

	public String getInvYyyymm() {
		return invYyyymm;
	}

	public void setInvYyyymm(String invYyyymm) {
		this.invYyyymm = invYyyymm;
	}

	public String getBillInfoNo() {
		return billInfoNo;
	}

	public void setBillInfoNo(String billInfoNo) {
		this.billInfoNo = billInfoNo;
	}

	public String getBillRefRepSvcContId() {
		return billRefRepSvcContId;
	}

	public void setBillRefRepSvcContId(String billRefRepSvcContId) {
		this.billRefRepSvcContId = billRefRepSvcContId;
	}

	public String getBillRefMobileNo() {
		return billRefMobileNo;
	}

	public void setBillRefMobileNo(String billRefMobileNo) {
		this.billRefMobileNo = billRefMobileNo;
	}

	public String getChageAcrndItemId() {
		return chageAcrndItemId;
	}

	public void setChageAcrndItemId(String chageAcrndItemId) {
		this.chageAcrndItemId = chageAcrndItemId;
	}

	public int getChageDcBefAmt() {
		return chageDcBefAmt;
	}

	public void setChageDcBefAmt(int chageDcBefAmt) {
		this.chageDcBefAmt = chageDcBefAmt;
	}

	@Override
	public String toString() {
		return "BillInfoDTO [invYyyymm=" + invYyyymm + ", billInfoNo="
				+ billInfoNo + ", billRefRepSvcContId=" + billRefRepSvcContId
				+ ", billRefMobileNo=" + billRefMobileNo
				+ ", chageAcrndItemId=" + chageAcrndItemId + ", chageDcBefAmt="
				+ chageDcBefAmt + "]";
	}

}
