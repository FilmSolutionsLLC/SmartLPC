package com.fps.web.rest.dto;


public class ProjectInfoReportsDTO{
	
	private Integer admin_id;
	private Integer report_id;
	private Integer sequence;
	
	public Integer getAdmin_id() {
		return admin_id;
	}
	public void setAdmin_id(Integer admin_id) {
		this.admin_id = admin_id;
	}
	public Integer getReport_id() {
		return report_id;
	}
	public void setReport_id(Integer report_id) {
		this.report_id = report_id;
	}
	public Integer getSequence() {
		return sequence;
	}
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
	public ProjectInfoReportsDTO(Integer admin_id, Integer report_id, Integer sequence) {
		super();
		this.admin_id = admin_id;
		this.report_id = report_id;
		this.sequence = sequence;
	}
	public ProjectInfoReportsDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}