package com.bornfire.xbrl.entities;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "BCBUAE_MM_DATA")
public class RT_MmData {
	
	@Id
	private String SI_NO;
	
	@DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date bank_date;

    private String bank_name;
    private String head_office_subsidiary;
    private String subsidiary;
    private String bank_symbol;
    private String conventional_islamic;
    private String local_foreign;
    private String cbuae_tiering;

    private BigDecimal deal_no;
    private String customer_id;
    private String counterparty_name;
    private String final_rating_banks;
    private String final_rating_cbuae;
    private String country_of_risk;
    private String cbuae_geographical_zone;
    private String deal_type;

    private Date value_date;
    private Date maturity_date;
     private String initial_maturity;
    private String initial_maturity_rounded;
    private String initial_maturity_period;
    private String residual_maturity;
    private String residual_maturity_rounded;
    private String maturity_period;
    private String currency;
    private String principal;
    private String principal_aed;
    private String interest_profit_rate;
    private Date report_submit_date;
    private Date report_from_date;
    private Date report_to_date;
    private Date report_date;
    private String entity_flg;
    private String modify_flg;
    private String del_flg;
	public String getSI_NO() {
		return SI_NO;
	}
	public void setSI_NO(String sI_NO) {
		SI_NO = sI_NO;
	}
	public Date getBank_date() {
		return bank_date;
	}
	public void setBank_date(Date bank_date) {
		this.bank_date = bank_date;
	}
	public String getBank_name() {
		return bank_name;
	}
	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}
	public String getHead_office_subsidiary() {
		return head_office_subsidiary;
	}
	public void setHead_office_subsidiary(String head_office_subsidiary) {
		this.head_office_subsidiary = head_office_subsidiary;
	}
	public String getSubsidiary() {
		return subsidiary;
	}
	public void setSubsidiary(String subsidiary) {
		this.subsidiary = subsidiary;
	}
	public String getBank_symbol() {
		return bank_symbol;
	}
	public void setBank_symbol(String bank_symbol) {
		this.bank_symbol = bank_symbol;
	}
	public String getConventional_islamic() {
		return conventional_islamic;
	}
	public void setConventional_islamic(String conventional_islamic) {
		this.conventional_islamic = conventional_islamic;
	}
	public String getLocal_foreign() {
		return local_foreign;
	}
	public void setLocal_foreign(String local_foreign) {
		this.local_foreign = local_foreign;
	}
	public String getCbuae_tiering() {
		return cbuae_tiering;
	}
	public void setCbuae_tiering(String cbuae_tiering) {
		this.cbuae_tiering = cbuae_tiering;
	}
	public BigDecimal getDeal_no() {
		return deal_no;
	}
	public void setDeal_no(BigDecimal deal_no) {
		this.deal_no = deal_no;
	}
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}
	public String getCounterparty_name() {
		return counterparty_name;
	}
	public void setCounterparty_name(String counterparty_name) {
		this.counterparty_name = counterparty_name;
	}
	public String getFinal_rating_banks() {
		return final_rating_banks;
	}
	public void setFinal_rating_banks(String final_rating_banks) {
		this.final_rating_banks = final_rating_banks;
	}
	public String getFinal_rating_cbuae() {
		return final_rating_cbuae;
	}
	public void setFinal_rating_cbuae(String final_rating_cbuae) {
		this.final_rating_cbuae = final_rating_cbuae;
	}
	public String getCountry_of_risk() {
		return country_of_risk;
	}
	public void setCountry_of_risk(String country_of_risk) {
		this.country_of_risk = country_of_risk;
	}
	public String getCbuae_geographical_zone() {
		return cbuae_geographical_zone;
	}
	public void setCbuae_geographical_zone(String cbuae_geographical_zone) {
		this.cbuae_geographical_zone = cbuae_geographical_zone;
	}
	public String getDeal_type() {
		return deal_type;
	}
	public void setDeal_type(String deal_type) {
		this.deal_type = deal_type;
	}
	public Date getValue_date() {
		return value_date;
	}
	public void setValue_date(Date value_date) {
		this.value_date = value_date;
	}
	public Date getMaturity_date() {
		return maturity_date;
	}
	public void setMaturity_date(Date maturity_date) {
		this.maturity_date = maturity_date;
	}
	public String getInitial_maturity() {
		return initial_maturity;
	}
	public void setInitial_maturity(String initial_maturity) {
		this.initial_maturity = initial_maturity;
	}
	public String getInitial_maturity_rounded() {
		return initial_maturity_rounded;
	}
	public void setInitial_maturity_rounded(String initial_maturity_rounded) {
		this.initial_maturity_rounded = initial_maturity_rounded;
	}
	public String getInitial_maturity_period() {
		return initial_maturity_period;
	}
	public void setInitial_maturity_period(String initial_maturity_period) {
		this.initial_maturity_period = initial_maturity_period;
	}
	public String getResidual_maturity() {
		return residual_maturity;
	}
	public void setResidual_maturity(String residual_maturity) {
		this.residual_maturity = residual_maturity;
	}
	public String getResidual_maturity_rounded() {
		return residual_maturity_rounded;
	}
	public void setResidual_maturity_rounded(String residual_maturity_rounded) {
		this.residual_maturity_rounded = residual_maturity_rounded;
	}
	public String getMaturity_period() {
		return maturity_period;
	}
	public void setMaturity_period(String maturity_period) {
		this.maturity_period = maturity_period;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getPrincipal() {
		return principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	public String getPrincipal_aed() {
		return principal_aed;
	}
	public void setPrincipal_aed(String principal_aed) {
		this.principal_aed = principal_aed;
	}
	public String getInterest_profit_rate() {
		return interest_profit_rate;
	}
	public void setInterest_profit_rate(String interest_profit_rate) {
		this.interest_profit_rate = interest_profit_rate;
	}
	public Date getReport_submit_date() {
		return report_submit_date;
	}
	public void setReport_submit_date(Date report_submit_date) {
		this.report_submit_date = report_submit_date;
	}
	public Date getReport_from_date() {
		return report_from_date;
	}
	public void setReport_from_date(Date report_from_date) {
		this.report_from_date = report_from_date;
	}
	public Date getReport_to_date() {
		return report_to_date;
	}
	public void setReport_to_date(Date report_to_date) {
		this.report_to_date = report_to_date;
	}
	public Date getReport_date() {
		return report_date;
	}
	public void setReport_date(Date report_date) {
		this.report_date = report_date;
	}
	public String getEntity_flg() {
		return entity_flg;
	}
	public void setEntity_flg(String entity_flg) {
		this.entity_flg = entity_flg;
	}
	public String getModify_flg() {
		return modify_flg;
	}
	public void setModify_flg(String modify_flg) {
		this.modify_flg = modify_flg;
	}
	public String getDel_flg() {
		return del_flg;
	}
	public void setDel_flg(String del_flg) {
		this.del_flg = del_flg;
	}
	public RT_MmData(String sI_NO, Date bank_date, String bank_name, String head_office_subsidiary, String subsidiary,
			String bank_symbol, String conventional_islamic, String local_foreign, String cbuae_tiering,
			BigDecimal deal_no, String customer_id, String counterparty_name, String final_rating_banks,
			String final_rating_cbuae, String country_of_risk, String cbuae_geographical_zone, String deal_type,
			Date value_date, Date maturity_date, String initial_maturity, String initial_maturity_rounded,
			String initial_maturity_period, String residual_maturity, String residual_maturity_rounded,
			String maturity_period, String currency, String principal, String principal_aed,
			String interest_profit_rate, Date report_submit_date, Date report_from_date, Date report_to_date,
			Date report_date, String entity_flg, String modify_flg, String del_flg) {
		super();
		SI_NO = sI_NO;
		this.bank_date = bank_date;
		this.bank_name = bank_name;
		this.head_office_subsidiary = head_office_subsidiary;
		this.subsidiary = subsidiary;
		this.bank_symbol = bank_symbol;
		this.conventional_islamic = conventional_islamic;
		this.local_foreign = local_foreign;
		this.cbuae_tiering = cbuae_tiering;
		this.deal_no = deal_no;
		this.customer_id = customer_id;
		this.counterparty_name = counterparty_name;
		this.final_rating_banks = final_rating_banks;
		this.final_rating_cbuae = final_rating_cbuae;
		this.country_of_risk = country_of_risk;
		this.cbuae_geographical_zone = cbuae_geographical_zone;
		this.deal_type = deal_type;
		this.value_date = value_date;
		this.maturity_date = maturity_date;
		this.initial_maturity = initial_maturity;
		this.initial_maturity_rounded = initial_maturity_rounded;
		this.initial_maturity_period = initial_maturity_period;
		this.residual_maturity = residual_maturity;
		this.residual_maturity_rounded = residual_maturity_rounded;
		this.maturity_period = maturity_period;
		this.currency = currency;
		this.principal = principal;
		this.principal_aed = principal_aed;
		this.interest_profit_rate = interest_profit_rate;
		this.report_submit_date = report_submit_date;
		this.report_from_date = report_from_date;
		this.report_to_date = report_to_date;
		this.report_date = report_date;
		this.entity_flg = entity_flg;
		this.modify_flg = modify_flg;
		this.del_flg = del_flg;
	}
	public RT_MmData() {
		super();
		// TODO Auto-generated constructor stub
	}
	

    
     
}
