package com.bornfire.xbrl.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "RT_MC_DEPARTMENT_SIGNOFF")
public class RT_MC_DEPARTMENT_SIGNOFF_ENTITY {

	@Id
	@Column(name = "ID")
	private BigDecimal id;

	@Column(name = "FORM_MODE")
	private String formMode;

	@Temporal(TemporalType.DATE)
	@Column(name = "REPORT_DATE")
	private Date reportDate;

	@Column(name = "TIMEPERIOD")
	private String timeperiod;

	@Column(name = "SNO")
	private Integer sno;

	@Column(name = "DEPARTMENT")
	private String department;

	@Column(name = "TABLES_NAME")
	private String tablesName;

	@Column(name = "DATA_POINTS")
	private Integer dataPoints;

	@Column(name = "MAKER_NAME")
	private String makerName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MAKER_DATETIME")
	private Date makerDatetime;

	@Column(name = "CHECKER_NAME")
	private String checkerName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CHECKER_DATETIME")
	private Date checkerDatetime;

	@Column(name = "DEPT_HEAD_NAME")
	private String deptHeadName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DEPT_HEAD_DATETIME")
	private Date deptHeadDatetime;

	@Column(name = "EVIDENCE_UPLOADED")
	private String evidenceUploaded;

	@Column(name = "ACTION")
	private String action;

	@Column(name = "ENTRY_USER")
	private String entryUser;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ENTRY_TIME")
	private Date entryTime;

	@Column(name = "MODIFY_USER")
	private String modifyUser;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFY_TIME")
	private Date modifyTime;

	@Column(name = "ENTITY_FLG")
	private String entityFlg;

	@Column(name = "DEL_FLG")
	private String delFlg;

	public BigDecimal getId() { return id; }
	public void setId(BigDecimal id) { this.id = id; }
	public String getFormMode() { return formMode; }
	public void setFormMode(String formMode) { this.formMode = formMode; }
	public Date getReportDate() { return reportDate; }
	public void setReportDate(Date reportDate) { this.reportDate = reportDate; }
	public String getTimeperiod() { return timeperiod; }
	public void setTimeperiod(String timeperiod) { this.timeperiod = timeperiod; }
	public Integer getSno() { return sno; }
	public void setSno(Integer sno) { this.sno = sno; }
	public String getDepartment() { return department; }
	public void setDepartment(String department) { this.department = department; }
	public String getTablesName() { return tablesName; }
	public void setTablesName(String tablesName) { this.tablesName = tablesName; }
	public Integer getDataPoints() { return dataPoints; }
	public void setDataPoints(Integer dataPoints) { this.dataPoints = dataPoints; }
	public String getMakerName() { return makerName; }
	public void setMakerName(String makerName) { this.makerName = makerName; }
	public Date getMakerDatetime() { return makerDatetime; }
	public void setMakerDatetime(Date makerDatetime) { this.makerDatetime = makerDatetime; }
	public String getCheckerName() { return checkerName; }
	public void setCheckerName(String checkerName) { this.checkerName = checkerName; }
	public Date getCheckerDatetime() { return checkerDatetime; }
	public void setCheckerDatetime(Date checkerDatetime) { this.checkerDatetime = checkerDatetime; }
	public String getDeptHeadName() { return deptHeadName; }
	public void setDeptHeadName(String deptHeadName) { this.deptHeadName = deptHeadName; }
	public Date getDeptHeadDatetime() { return deptHeadDatetime; }
	public void setDeptHeadDatetime(Date deptHeadDatetime) { this.deptHeadDatetime = deptHeadDatetime; }
	public String getEvidenceUploaded() { return evidenceUploaded; }
	public void setEvidenceUploaded(String evidenceUploaded) { this.evidenceUploaded = evidenceUploaded; }
	public String getAction() { return action; }
	public void setAction(String action) { this.action = action; }
	public String getEntryUser() { return entryUser; }
	public void setEntryUser(String entryUser) { this.entryUser = entryUser; }
	public Date getEntryTime() { return entryTime; }
	public void setEntryTime(Date entryTime) { this.entryTime = entryTime; }
	public String getModifyUser() { return modifyUser; }
	public void setModifyUser(String modifyUser) { this.modifyUser = modifyUser; }
	public Date getModifyTime() { return modifyTime; }
	public void setModifyTime(Date modifyTime) { this.modifyTime = modifyTime; }
	public String getEntityFlg() { return entityFlg; }
	public void setEntityFlg(String entityFlg) { this.entityFlg = entityFlg; }
	public String getDelFlg() { return delFlg; }
	public void setDelFlg(String delFlg) { this.delFlg = delFlg; }
}