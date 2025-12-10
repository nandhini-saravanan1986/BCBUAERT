package com.bornfire.xbrl.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name="RT_NET_POSITION_LIMIT_NOOP")
@IdClass(RT_Noop_net_position_id_class.class)
public class RT_Noop_net_position_entity implements Serializable {

	@Id
	private Date	report_date;
	private Date	fedai_conv_date;
	private BigDecimal	fedai_rate;
	@Id
	private String	currency;
	private BigDecimal	ready_exchage_position_in_ac;
	private BigDecimal	cbs_fx_position_ac;
	private BigDecimal	mtm_ac;
	private BigDecimal	total_noop_in_ac;
	private BigDecimal	total_noop_in_lc;
	public Date getReport_date() {
		return report_date;
	}
	public void setReport_date(Date report_date) {
		this.report_date = report_date;
	}
	public Date getFedai_conv_date() {
		return fedai_conv_date;
	}
	public void setFedai_conv_date(Date fedai_conv_date) {
		this.fedai_conv_date = fedai_conv_date;
	}
	public BigDecimal getFedai_rate() {
		return fedai_rate;
	}
	public void setFedai_rate(BigDecimal fedai_rate) {
		this.fedai_rate = fedai_rate;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public BigDecimal getReady_exchage_position_in_ac() {
		return ready_exchage_position_in_ac;
	}
	public void setReady_exchage_position_in_ac(BigDecimal ready_exchage_position_in_ac) {
		this.ready_exchage_position_in_ac = ready_exchage_position_in_ac;
	}
	public BigDecimal getCbs_fx_position_ac() {
		return cbs_fx_position_ac;
	}
	public void setCbs_fx_position_ac(BigDecimal cbs_fx_position_ac) {
		this.cbs_fx_position_ac = cbs_fx_position_ac;
	}
	public BigDecimal getMtm_ac() {
		return mtm_ac;
	}
	public void setMtm_ac(BigDecimal mtm_ac) {
		this.mtm_ac = mtm_ac;
	}
	public BigDecimal getTotal_noop_in_ac() {
		return total_noop_in_ac;
	}
	public void setTotal_noop_in_ac(BigDecimal total_noop_in_ac) {
		this.total_noop_in_ac = total_noop_in_ac;
	}
	public BigDecimal getTotal_noop_in_lc() {
		return total_noop_in_lc;
	}
	public void setTotal_noop_in_lc(BigDecimal total_noop_in_lc) {
		this.total_noop_in_lc = total_noop_in_lc;
	}
	@Override
	public String toString() {
		return "RT_Noop_net_position_entity [report_date=" + report_date + ", fedai_conv_date=" + fedai_conv_date
				+ ", fedai_rate=" + fedai_rate + ", currency=" + currency + ", ready_exchage_position_in_ac="
				+ ready_exchage_position_in_ac + ", cbs_fx_position_ac=" + cbs_fx_position_ac + ", mtm_ac=" + mtm_ac
				+ ", total_noop_in_ac=" + total_noop_in_ac + ", total_noop_in_lc=" + total_noop_in_lc + "]";
	}
	public RT_Noop_net_position_entity(Date report_date, Date fedai_conv_date, BigDecimal fedai_rate, String currency,
			BigDecimal ready_exchage_position_in_ac, BigDecimal cbs_fx_position_ac, BigDecimal mtm_ac,
			BigDecimal total_noop_in_ac, BigDecimal total_noop_in_lc) {
		super();
		this.report_date = report_date;
		this.fedai_conv_date = fedai_conv_date;
		this.fedai_rate = fedai_rate;
		this.currency = currency;
		this.ready_exchage_position_in_ac = ready_exchage_position_in_ac;
		this.cbs_fx_position_ac = cbs_fx_position_ac;
		this.mtm_ac = mtm_ac;
		this.total_noop_in_ac = total_noop_in_ac;
		this.total_noop_in_lc = total_noop_in_lc;
	}
	public RT_Noop_net_position_entity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	
	
}
