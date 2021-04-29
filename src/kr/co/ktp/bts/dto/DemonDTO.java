package kr.co.ktp.bts.dto;

public class DemonDTO {
	
	//TB_BTCBATWORK
	private String work_cd;
	private String work_dy;
	private String work_tm;
	private String work_module_id;
	private String work_cmpt_fg;
	private String work_file_path;
	private String work_file_name;
	
	//TB_BTCBATCHRSLT
	private String prog_id;
	private String work_rslt_flag;
	
	public String getWork_cd() {
		return work_cd;
	}
	public void setWork_cd(String work_cd) {
		this.work_cd = work_cd;
	}
	public String getWork_dy() {
		return work_dy;
	}
	public void setWork_dy(String work_dy) {
		this.work_dy = work_dy;
	}
	public String getWork_tm() {
		return work_tm;
	}
	public void setWork_tm(String work_tm) {
		this.work_tm = work_tm;
	}
	public String getWork_module_id() {
		return work_module_id;
	}
	public void setWork_module_id(String work_module_id) {
		this.work_module_id = work_module_id;
	}
	public String getWork_cmpt_fg() {
		return work_cmpt_fg;
	}
	public void setWork_cmpt_fg(String work_cmpt_fg) {
		this.work_cmpt_fg = work_cmpt_fg;
	}
	public String getWork_file_path() {
		return work_file_path;
	}
	public void setWork_file_path(String work_file_path) {
		this.work_file_path = work_file_path;
	}
	public String getWork_file_name() {
		return work_file_name;
	}
	public void setWork_file_name(String work_file_name) {
		this.work_file_name = work_file_name;
	}
	public String getProg_id() {
		return prog_id;
	}
	public void setProg_id(String prog_id) {
		this.prog_id = prog_id;
	}
	public String getWork_rslt_flag() {
		return work_rslt_flag;
	}
	public void setWork_rslt_flag(String work_rslt_flag) {
		this.work_rslt_flag = work_rslt_flag;
	}
	
}
